package fi.hsl.transmodel.xform;

import com.google.common.collect.Lists;
import fi.hsl.transmodel.model.jore.JoreImportContext;
import fi.hsl.transmodel.model.jore.entity.JrLine;
import fi.hsl.transmodel.model.jore.entity.JrLineHeader;
import fi.hsl.transmodel.model.jore.entity.JrLink;
import fi.hsl.transmodel.model.jore.entity.JrNode;
import fi.hsl.transmodel.model.jore.entity.JrRoute;
import fi.hsl.transmodel.model.jore.entity.JrRoutePath;
import fi.hsl.transmodel.model.jore.entity.JrRoutePathLink;
import fi.hsl.transmodel.model.jore.field.NodeType;
import fi.hsl.transmodel.model.jore.field.RouteDirection;
import io.vavr.collection.List;
import org.rutebanken.netex.model.Common_VersionFrameStructure;
import org.rutebanken.netex.model.DataManagedObjectStructure;
import org.rutebanken.netex.model.DirectionTypeEnumeration;
import org.rutebanken.netex.model.Line;
import org.rutebanken.netex.model.LineRefStructure;
import org.rutebanken.netex.model.LinesInFrame_RelStructure;
import org.rutebanken.netex.model.LinkSequence_VersionStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PointOnRoute;
import org.rutebanken.netex.model.PointRefStructure;
import org.rutebanken.netex.model.PointsOnRoute_RelStructure;
import org.rutebanken.netex.model.RouteLink;
import org.rutebanken.netex.model.RouteLinksInFrame_RelStructure;
import org.rutebanken.netex.model.RoutePoint;
import org.rutebanken.netex.model.RoutePointRefStructure;
import org.rutebanken.netex.model.RoutePointsInFrame_RelStructure;
import org.rutebanken.netex.model.RoutesInFrame_RelStructure;
import org.rutebanken.netex.model.ServiceFrame;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import static fi.hsl.transmodel.xform.Util.sanitizeId;
import static fi.hsl.transmodel.xform.Util.validBetween;

public final class RouteConverter {

    private static final ObjectFactory FACTORY = new ObjectFactory();

    private RouteConverter() {
    }

    public static Collection<JAXBElement<? extends Common_VersionFrameStructure>> routesForServices(final JoreImportContext ctx) {
        return Lists.newArrayList(serviceFrame(ctx));
    }

    private static JAXBElement<ServiceFrame> serviceFrame(final JoreImportContext ctx) {
        return FACTORY.createServiceFrame(
                FACTORY.createServiceFrame()
                       .withVersion("any")
                       .withId(sanitizeId("routes-for-services"))
                       // route points == road junctions etc.
                       .withRoutePoints(routePointsFrame(ctx))
                       // links between route points and/or stop points
                       .withRouteLinks(routeLinksFrame(ctx))
                       // the parent lines
                       .withLines(linesFrame(ctx))
                       // .. which consist of routes
                       .withRoutes(routesFrame(ctx))
        );
    }

    /*
     * ROUTE POINTS
     */

    private static RoutePointsInFrame_RelStructure routePointsFrame(final JoreImportContext ctx) {
        return FACTORY.createRoutePointsInFrame_RelStructure()
                      .withRoutePoint(routepoints(ctx));
    }

    private static Collection<RoutePoint> routepoints(final JoreImportContext ctx) {
        return ctx.plainRoutePoints()
                  .map(RouteConverter::routePoint)
                  .toJavaList();
    }

    private static RoutePoint routePoint(final JrNode node) {
        final String id = sanitizeId("rp", node.nodeId());
        return FACTORY.createRoutePoint()
                      .withId(id)
                      .withVersion("any")
                      .withProjections(
                              FACTORY.createProjections_RelStructure()
                                     .withProjectionRefOrProjection(
                                             FACTORY.createPointProjection(
                                                     FACTORY.createPointProjection()
                                                            .withVersion("any")
                                                            .withId(sanitizeId("proj", id))
                                                            .withProjectToPointRef(FACTORY.createPointRefStructure()
                                                                                          .withVersion("any")
                                                                                          .withNameOfRefClass("RoadJunction")
                                                                                          .withRef(sanitizeId("junc", node.nodeId().value()))))));
    }

    /*
     * ROUTE LINKS
     */

    private static RouteLinksInFrame_RelStructure routeLinksFrame(final JoreImportContext ctx) {
        return FACTORY.createRouteLinksInFrame_RelStructure()
                      .withRouteLink(routeLinks(ctx));
    }

    private static Collection<RouteLink> routeLinks(final JoreImportContext ctx) {
        return ctx.links()
                  .values()
                  .map(link -> routeLink(link, ctx))
                  .toJavaList();
    }


    private static RouteLink routeLink(final JrLink link,
                                       final JoreImportContext ctx) {
        final String id = sanitizeId("rl",
                                     link.startNode().value(),
                                     link.endNode().value());
        final JrNode startNode = ctx.nodes().get(link.fkStartNode()).get();
        final JrNode endNode = ctx.nodes().get(link.fkEndNode()).get();
        return FACTORY.createRouteLink()
                      .withId(id)
                      .withVersion("any")
                      .withDistance(BigDecimal.valueOf(link.measuredLength()))
                      .withFromPointRef(pointRef(startNode))
                      .withToPointRef(pointRef(endNode));
    }

    private static RoutePointRefStructure pointRef(final JrNode node) {
        // We refer either to a scheduled stop point or a route point
        final String prefix = node.nodeType() == NodeType.BUS_STOP ?
                "ssp" : "rp";
        final String ref = sanitizeId(prefix, node.nodeId().value());
        return FACTORY.createRoutePointRefStructure()
                      .withVersion("any")
                      .withRef(ref);
    }

    /*
     * LINES
     */

    private static LinesInFrame_RelStructure linesFrame(final JoreImportContext ctx) {
        return FACTORY.createLinesInFrame_RelStructure()
                      .withLine_(lines(ctx));
    }

    private static Collection<JAXBElement<? extends DataManagedObjectStructure>> lines(final JoreImportContext ctx) {
        return Lists.newLinkedList(
                ctx.lineHeaders()
                   .values()
                   .map(lineHeader -> line(lineHeader, ctx))
                   .toJavaList()
        );
    }

    private static JAXBElement<Line> line(final JrLineHeader lineHeader, final JoreImportContext ctx) {
        // Each line header creates a new version of the parent lane
        final JrLine line = ctx.lines().get(lineHeader.fkLine()).get();
        final String id = sanitizeId("line", line.lineId());
        final String version = lineHeader.validFrom().toString();
        return FACTORY.createLine(
                FACTORY.createLine()
                       .withVersion(version)
                       .withValidBetween(validBetween(lineHeader))
                       .withId(id)
                       .withPublicCode(line.lineId().value())
                       .withName(FACTORY.createMultilingualString()
                                        .withValue(lineHeader.name()))
        );
    }

    /*
     * ROUTES
     */

    private static RoutesInFrame_RelStructure routesFrame(final JoreImportContext ctx) {
        return FACTORY.createRoutesInFrame_RelStructure()
                      .withRoute_(routes(ctx));
    }

    private static Collection<JAXBElement<? extends LinkSequence_VersionStructure>> routes(final JoreImportContext ctx) {
        return Lists.newLinkedList(
                // Note how we iterate the jr route paths and not the jr routes!
                ctx.routePaths()
                   .values()
                   .map(routePath -> route(routePath, ctx))
        );
    }

    private static JAXBElement<? extends LinkSequence_VersionStructure> route(final JrRoutePath routePath,
                                                                              final JoreImportContext ctx) {
        // we need some info from the parent route
        final JrRoute route = ctx.routes().get(routePath.fkRoute()).get();
        final JrLineHeader header = ctx.routeToLineHeader().get(route.pk())
                                       .get();
        final String id = sanitizeId("r", route.routeId().value(), routePath.direction() == RouteDirection.DIRECTION_1 ? "1" : "2");
        return FACTORY.createRoute(
                FACTORY.createRoute()
                       .withId(id)
                       .withName(FACTORY.createMultilingualString().withValue(route.name()))
                       .withVersion("any")
                       .withDescription(FACTORY.createMultilingualString().withValue(routePath.name()))
                       .withValidBetween(validBetween(routePath))
                       .withLineRef(lineRef(header))
                       .withDirectionType(direction(routePath.direction()))
                       .withPointsInSequence(pointsOnRoute(routePath, ctx))
        );
    }

    private static DirectionTypeEnumeration direction(final RouteDirection jrDirection) {
        // This is an arbitrary guess as RouteDirection doesn't actually specify the direction
        switch (jrDirection) {
            case DIRECTION_1:
                return DirectionTypeEnumeration.OUTBOUND;
            case DIRECTION_2:
                return DirectionTypeEnumeration.INBOUND;
        }
        return DirectionTypeEnumeration.INBOUND;
    }

    private static JAXBElement<? extends LineRefStructure> lineRef(final JrLineHeader lineHeader) {
        final String lineId = sanitizeId("line", lineHeader.lineId());
        final String version = lineHeader.validFrom().toString();
        return FACTORY.createLineRef(
                FACTORY.createLineRefStructure()
                       .withVersion(version)
                       .withRef(lineId)
        );
    }

    @Nullable
    private static PointsOnRoute_RelStructure pointsOnRoute(final JrRoutePath routePath, final JoreImportContext ctx) {
        final Collection<PointOnRoute> points = points(routePath, ctx);
        return points.isEmpty() ?
                null : FACTORY.createPointsOnRoute_RelStructure()
                              .withPointOnRoute(points);
    }

    private static Collection<PointOnRoute> points(final JrRoutePath routePath, final JoreImportContext ctx) {
        return ctx.linksPerRoutePath()
                  .getOrElse(routePath.pk(), List.empty())
                  .map(routePathLink -> point(routePathLink, ctx))
                  .toJavaList();
    }

    private static PointOnRoute point(final JrRoutePathLink link, final JoreImportContext ctx) {
        final String id = sanitizeId("p",
                                     link.startNode().value(),
                                     link.endNode().value(),
                                     link.orderNumber());
        final String linkId = sanitizeId("rl",
                                         link.startNode().value(),
                                         link.endNode().value());
        return FACTORY.createPointOnRoute()
                      .withVersion("any")
                      .withId(id)
                      .withOrder(BigInteger.valueOf(link.orderNumber()))
                      .withPointRef(routePointRef(ctx.nodes().get(link.fkStartNode()).get()))
                      .withOnwardRouteLinkRef(
                              FACTORY.createRouteLinkRefStructure()
                                     .withVersion("any")
                                     .withRef(linkId)
                      );
    }

    private static JAXBElement<? extends PointRefStructure> routePointRef(final JrNode node) {
        if (node.nodeType() == NodeType.BUS_STOP) {
            final String ref = sanitizeId("ssp", node.nodeId().value());
            return FACTORY.createScheduledStopPointRef(
                    FACTORY.createScheduledStopPointRefStructure()
                           .withVersion("any")
                           .withRef(ref)
            );
        } else {
            final String ref = sanitizeId("rp", node.nodeId().value());
            return FACTORY.createRoutePointRef(
                    FACTORY.createRoutePointRefStructure()
                           .withVersion("any")
                           .withRef(ref)
            );
        }
    }
}
