package fi.hsl.transmodel.xform;

import fi.hsl.transmodel.model.jore.JoreImportContext;
import fi.hsl.transmodel.model.jore.entity.JrLine;
import fi.hsl.transmodel.model.jore.entity.JrLineHeader;
import fi.hsl.transmodel.model.jore.entity.JrLink;
import fi.hsl.transmodel.model.jore.entity.JrNode;
import fi.hsl.transmodel.model.jore.entity.JrPoint;
import fi.hsl.transmodel.model.jore.entity.JrRoute;
import fi.hsl.transmodel.model.jore.entity.JrRoutePath;
import fi.hsl.transmodel.model.jore.entity.JrRoutePathLink;
import fi.hsl.transmodel.model.jore.entity.JrStop;
import fi.hsl.transmodel.model.jore.entity.JrStopArea;
import fi.hsl.transmodel.model.jore.entity.JrTerminalArea;
import fi.hsl.transmodel.model.jore.key.JrLineHeaderPk;
import fi.hsl.transmodel.model.jore.key.JrLinkPk;
import fi.hsl.transmodel.model.jore.key.JrNodePk;
import fi.hsl.transmodel.model.jore.key.JrStopAreaPk;
import fi.hsl.transmodel.model.jore.key.JrTerminalAreaPk;
import fi.hsl.transmodel.model.netex.PublicationDelivery;
import fi.hsl.transmodel.model.netex.generic.PointOnLink;
import fi.hsl.transmodel.model.netex.generic.RootFrame;
import fi.hsl.transmodel.model.netex.public_transport.fixed.site.SiteFrame;
import fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place.Quay;
import fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place.StopPlace;
import fi.hsl.transmodel.model.netex.public_transport.network.infrastructure.InfrastructureFrame;
import fi.hsl.transmodel.model.netex.public_transport.network.infrastructure.RoadElement;
import fi.hsl.transmodel.model.netex.public_transport.network.infrastructure.RoadJunction;
import fi.hsl.transmodel.model.netex.public_transport.network.route.Line;
import fi.hsl.transmodel.model.netex.public_transport.network.route.PointOnRoute;
import fi.hsl.transmodel.model.netex.public_transport.network.route.Route;
import fi.hsl.transmodel.model.netex.public_transport.network.route.RouteLink;
import fi.hsl.transmodel.model.netex.public_transport.network.route.RouteLinkEndpoint;
import fi.hsl.transmodel.model.netex.public_transport.network.route.RoutePoint;
import fi.hsl.transmodel.model.netex.public_transport.tactical.service.ScheduledStopPoint;
import fi.hsl.transmodel.model.netex.public_transport.tactical.service.Service;
import fi.hsl.transmodel.model.netex.public_transport.tactical.service.ServiceLink;
import fi.hsl.transmodel.model.netex.public_transport.tactical.stop.PassengerStopAssignment;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import static fi.hsl.transmodel.xform.Util.direction;
import static fi.hsl.transmodel.xform.Util.toStopType;
import static fi.hsl.transmodel.xform.Util.toVehicleMode;

/**
 * This functionality is modelled after https://github.com/NeTEx-CEN/NeTEx/blob/master/examples/functions/simpleNetwork/Netex_SimpleNetwork_1.xml
 */
public class JoreImportContextConverter {

    public static PublicationDelivery convert(final JoreImportContext ctx) {

        /*
         * Infrastructure
         */

        // Note how we take _all_ the nodes, not just crossroads!
        // Road elements in Jore can go [junction]--[bus stop]--[junction]--[municipal border]
        // while in transmodel they are [junction]--------------[junction]-------...
        final Map<JrNodePk, RoadJunction> roadJunctions = ctx.nodes()
                                                             .values()
                                                             .toMap(node -> Tuple.of(node.pk(), roadJunction(node)));

        final Map<JrLinkPk, List<PointOnLink>> pointsOnLink = ctx.pointsPerLink()
                                                                 .mapValues(points -> points.map(JoreImportContextConverter::pointOnLink));

        final Map<JrLinkPk, RoadElement> roadElements = ctx.links()
                                                           .values()
                                                           .toMap(link -> Tuple.of(link.pk(),
                                                                                   roadElement(link,
                                                                                               pointsOnLink.getOrElse(link.pk(), List.empty()),
                                                                                               roadJunctions)));

        /*
         * Sites
         */

        final Map<JrStop, Quay> quays = ctx.stops()
                                           .toMap(stop -> Tuple.of(stop,
                                                                   jrStopToQuay(stop)));

        final Map<JrNodePk, Quay> quaysPerNode = quays.mapKeys(JrStop::fkNode);

        final Map<JrStopAreaPk, List<Quay>> quaysPerStopArea = quays.groupBy(stopAndQuay -> stopAndQuay._1().fkStopArea())
                                                                    .mapValues(maps -> maps.values().toList());

        final Set<StopPlace> stopPlaces = HashSet
                .<StopPlace>empty()
                .addAll(ctx.terminalAreas()
                           .values()
                           .map(terminal -> jrTerminalAreaToStopPlace(terminal, ctx.stopAreasPerTerminal(), quaysPerStopArea)))
                .addAll(ctx.stopAreasNotInTerminals()
                           .map(stopArea -> jrStopAreaToStopPlace(stopArea, quaysPerStopArea)));

        final Map<Quay, StopPlace> stopPlacesPerQuay = stopPlaces.flatMap(stopPlace -> stopPlace.quays()
                                                                                                .map(quay -> Tuple.of(quay, stopPlace)))
                                                                 .toMap(Function.identity());

        /*
         * Routes and services
         */

        final Map<JrLineHeaderPk, Line> lines = ctx.lineHeaders()
                                                   .values()
                                                   .toMap(header -> Tuple.of(header.pk(),
                                                                             jrLineHeaderToLine(header, ctx.lines().get(header.fkLine()).get())));

        final Map<JrNodePk, RoutePoint> routePoints = roadJunctions.map((id, junction) -> Tuple.of(id, RoutePoint.of(junction)));

        final Map<JrNodePk, ScheduledStopPoint> stopPoints = ctx.busStopRoutePoints()
                                                                .toMap(node -> Tuple.of(node.pk(), jrNodeToStopPoint(node)));

        final Map<Tuple2<JrNodePk, JrNodePk>, RouteLink> routeLinks = ctx.links()
                                                                         .values()
                                                                         .toMap(link -> Tuple.of(Tuple.of(link.fkStartNode(), link.fkEndNode()),
                                                                                                 jrLinkToRouteLink(link, routePoints, stopPoints)));

        final Set<ServiceLink> serviceLinks = ctx.busStopLinks()
                                                 .map(nodeTuple -> serviceLink(nodeTuple._1(), nodeTuple._2(), stopPoints));

        final Set<PassengerStopAssignment> stopAssignments = stopPoints
                .map(nodeAndStopPoint -> stopAssignment(nodeAndStopPoint._2(),
                                                        quaysPerNode.get(nodeAndStopPoint._1()).get(),
                                                        stopPlacesPerQuay))
                .toSet();

        final Set<Route> routes = ctx.routePaths()
                                     .values()
                                     .map(routePath -> {
                                         final JrRoute route = ctx.routes().get(routePath.fkRoute()).get();
                                         final JrLineHeader lineHeader = ctx.routeToLineHeader().get(route.pk()).get();
                                         final Line line = lines.get(lineHeader.pk()).get();
                                         final Route r = route(routePath, line);
                                         final List<PointOnRoute> points = pointsOnRoute(r,
                                                                                         routePoints,
                                                                                         routeLinks,
                                                                                         ctx.linksPerRoutePath()
                                                                                            .getOrElse(routePath.pk(), List.empty()));
                                         return r.withPoints(points);
                                     })
                                     .toSet();

        /*
         * Top level frames:
         */

        final InfrastructureFrame infrastructure = InfrastructureFrame.of(roadJunctions.values(),
                                                                          roadElements.values());

        final SiteFrame site = SiteFrame.of(stopPlaces);

        final Service routeFrame = Service.of("routes")
                                          .withLines(lines.values().toSet())
                                          .withRoutePoints(routePoints.values().toSet())
                                          .withRouteLinks(routeLinks.values().toSet())
                                          .withRoutes(routes);

        final Service services = Service.of("services")
                                        .withStopPoints(stopPoints.values().toSet())
                                        .withServiceLinks(serviceLinks)
                                        .withStopAssignments(stopAssignments);

        final List<RootFrame> frames = List.<RootFrame>empty()
                .append(infrastructure)
                .append(site)
                .append(routeFrame)
                .append(services);

        return PublicationDelivery.of(
                "publication",
                "JORE",
                "Example NeTEx export from JORE",
                frames);
    }

    private static List<PointOnRoute> pointsOnRoute(final Route route,
                                                    final Map<JrNodePk, RoutePoint> routePoints,
                                                    final Map<Tuple2<JrNodePk, JrNodePk>, RouteLink> routeLinks,
                                                    final List<JrRoutePathLink> routePathLinks) {
        final JrRoutePathLink finalLink = routePathLinks.last();
        return routePathLinks
                .map(routePathLink -> {
                    final RoutePoint rp = routePoints.get(routePathLink.fkStartNode()).get();
                    final RouteLink link = routeLinks.get(Tuple.of(routePathLink.fkStartNode(),
                                                                   routePathLink.fkEndNode()))
                                                     .get();
                    return PointOnRoute.of(route.id(),
                                           rp,
                                           routePathLink.orderNumber(),
                                           link);
                })
                // Add an extra point to indicate the end of the route with no onwards link
                .append(PointOnRoute.of(route.id(),
                                        routePoints.get(finalLink.fkEndNode()).get(),
                                        finalLink.orderNumber() + 1));
    }

    private static Route route(final JrRoutePath routePath,
                               final Line line) {
        return Route.of(routePath.routeId().value(),
                        routePath.name(),
                        line,
                        direction(routePath.direction()),
                        routePath.validFrom(),
                        routePath.validTo());
    }

    private static PassengerStopAssignment stopAssignment(final ScheduledStopPoint stopPoint,
                                                          final Quay quay,
                                                          final Map<Quay, StopPlace> stopPlacesPerQuay) {
        final StopPlace stopPlace = stopPlacesPerQuay.get(quay).get();
        return PassengerStopAssignment.of(stopPoint,
                                          stopPlace,
                                          quay);
    }

    private static RoadJunction roadJunction(final JrNode node) {
        return RoadJunction.from(node.nodeId().value(),
                                 node.latitude(),
                                 node.longitude());
    }

    private static RoadElement roadElement(final JrLink link,
                                           final List<PointOnLink> pointsInLink,
                                           final Map<JrNodePk, RoadJunction> roadJunctions) {
        final RoadJunction from = roadJunctions.get(link.fkStartNode()).get();
        final RoadJunction to = roadJunctions.get(link.fkEndNode()).get();
        return RoadElement.of(from,
                              to,
                              BigDecimal.valueOf(link.measuredLength()),
                              pointsInLink);
    }

    private static PointOnLink pointOnLink(final JrPoint point) {
        return PointOnLink.of(String.valueOf(point.pointId()),
                              point.orderNumber(),
                              point.latitude(),
                              point.longitude());
    }

    private static Quay jrStopToQuay(final JrStop stop) {
        return Quay.of(stop.nodeId().value(),
                       stop.name(),
                       stop.platform(),
                       stop.latitude(),
                       stop.longitude());
    }

    private static StopPlace jrTerminalAreaToStopPlace(final JrTerminalArea terminal,
                                                       final Map<JrTerminalAreaPk, Set<JrStopArea>> areas,
                                                       final Map<JrStopAreaPk, List<Quay>> quays) {
        final Set<JrStopArea> areasInTerminal = areas.getOrElse(terminal.pk(), HashSet.empty());
        final List<Quay> quaysInTerminal = areasInTerminal.flatMap(area -> quays.getOrElse(area.pk(), List.empty()))
                                                          .toList();
        // Note that the data from JrStopAreas are effectively ignored
        return StopPlace.of(String.format("terminal.%s", terminal.terminalId().value()),
                            terminal.name(),
                            terminal.latitude(),
                            terminal.longitude(),
                            toVehicleMode(terminal),
                            toStopType(terminal),
                            quaysInTerminal);
    }

    private static StopPlace jrStopAreaToStopPlace(final JrStopArea area,
                                                   final Map<JrStopAreaPk, List<Quay>> quays) {
        return StopPlace.of(String.format("stoparea.%s", area.stopAreaId().value()),
                            area.name(),
                            area.latitude(),
                            area.longitude(),
                            toVehicleMode(area),
                            toStopType(area),
                            quays.getOrElse(area.pk(), List.empty()));
    }

    private static Line jrLineHeaderToLine(final JrLineHeader header,
                                           final JrLine line) {
        return Line.of(line.lineId().value(),
                       line.lineId().value(),
                       header.name(),
                       header.validFrom().toString(),
                       Optional.of(header.validFrom()),
                       header.validTo());
    }

    private static ScheduledStopPoint jrNodeToStopPoint(final JrNode node) {
        return ScheduledStopPoint.of(node.nodeId().value(),
                                     node.latitude(),
                                     node.longitude());
    }

    private static RouteLink jrLinkToRouteLink(final JrLink link,
                                               final Map<JrNodePk, RoutePoint> routePoints,
                                               final Map<JrNodePk, ScheduledStopPoint> stopPoints) {

        return RouteLink.of(BigDecimal.valueOf(link.measuredLength()),
                            resolve(link.fkStartNode(), routePoints, stopPoints),
                            resolve(link.fkEndNode(), routePoints, stopPoints));
    }

    private static RouteLinkEndpoint resolve(final JrNodePk nodePk,
                                             final Map<JrNodePk, RoutePoint> routePoints,
                                             final Map<JrNodePk, ScheduledStopPoint> stopPoints) {
        if (routePoints.containsKey(nodePk)) {
            return routePoints.get(nodePk).get();
        }
        if (stopPoints.containsKey(nodePk)) {
            return stopPoints.get(nodePk).get();
        }
        throw new IllegalStateException("No route link endpoint for node " + nodePk);
    }

    private static ServiceLink serviceLink(final JrNode from, final JrNode to, final Map<JrNodePk, ScheduledStopPoint> stopPoints) {
        // TODO: It would be great if we could calculate the service link distance
        return ServiceLink.of(stopPoints.get(from.pk()).get(),
                              stopPoints.get(to.pk()).get(),
                              BigDecimal.ZERO);
    }
}
