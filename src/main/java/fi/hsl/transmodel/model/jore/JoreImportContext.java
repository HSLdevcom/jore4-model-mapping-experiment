package fi.hsl.transmodel.model.jore;

import com.google.common.base.Preconditions;
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
import fi.hsl.transmodel.model.jore.field.NodeType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.key.JrLineHeaderPk;
import fi.hsl.transmodel.model.jore.key.JrLinePk;
import fi.hsl.transmodel.model.jore.key.JrLinkPk;
import fi.hsl.transmodel.model.jore.key.JrNodePk;
import fi.hsl.transmodel.model.jore.key.JrRoutePathLinkPk;
import fi.hsl.transmodel.model.jore.key.JrRoutePathPk;
import fi.hsl.transmodel.model.jore.key.JrRoutePk;
import fi.hsl.transmodel.model.jore.key.JrStopAreaPk;
import fi.hsl.transmodel.model.jore.key.JrTerminalAreaPk;
import fi.hsl.transmodel.model.jore.mixin.IHasOrderNumber;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey.groupByPk;


@Value.Immutable
public interface JoreImportContext {

    @Value.Derived
    default LocalDateTime createdAt() {
        return LocalDateTime.now();
    }

    Map<JrLinePk, JrLine> lines();

    Map<JrLineHeaderPk, JrLineHeader> lineHeaders();

    Map<JrRoutePk, JrRoute> routes();

    Map<JrRoutePathPk, JrRoutePath> routePaths();

    Map<JrRoutePathLinkPk, JrRoutePathLink> routePathLinks();

    Map<JrLinkPk, JrLink> links();

    List<JrPoint> points();

    Map<JrTerminalAreaPk, JrTerminalArea> terminalAreas();

    Map<JrStopAreaPk, JrStopArea> stopAreas();

    List<JrStop> stops();

    Map<JrNodePk, JrNode> nodes();

    @Value.Check
    default void checkReferences() {
        lines().values()
               .map(JrLine::fkBasicRoute)
               .filter(Optional::isPresent)
               .map(Optional::orElseThrow)
               .forEach(fk -> Preconditions.checkState(routes().containsKey(fk),
                                                       "Line references unknown Route"));
        lineHeaders().values()
                     .map(JrLineHeader::fkLine)
                     .forEach(fk -> Preconditions.checkState(lines().containsKey(fk),
                                                             "LineHeader references unknown Line"));
        routes().values()
                .map(JrRoute::fkLine)
                .forEach(fk -> Preconditions.checkState(lines().containsKey(fk),
                                                        "Route references unknown Line"));
        routePaths().values()
                    .map(JrRoutePath::fkRoute)
                    .forEach(fk -> Preconditions.checkState(routes().containsKey(fk),
                                                            "RoutePath references unknown Route"));
        routePathLinks().values()
                        .map(JrRoutePathLink::fkRoutePath)
                        .forEach(fk -> Preconditions.checkState(routePaths().containsKey(fk),
                                                                "RoutePathLink references unknown RoutePath"));
        routePathLinks().values()
                        .map(JrRoutePathLink::fkLink)
                        .forEach(fk -> Preconditions.checkState(links().containsKey(fk),
                                                                "RoutePathLink references unknown Link"));
        stops().map(JrStop::fkStopArea)
               .forEach(fk -> Preconditions.checkState(stopAreas().containsKey(fk),
                                                       "Stop references unknown StopArea"));
        stopAreas().values()
                   .map(JrStopArea::fkTerminal)
                   .filter(Optional::isPresent)
                   .forEach(fk -> Preconditions.checkState(terminalAreas().containsKey(fk.orElseThrow()),
                                                           "StopArea references unknown Terminal"));
    }

    @Value.Derived
    default Map<JrTerminalAreaPk, Set<JrStopArea>> stopAreasPerTerminal() {
        return stopAreas()
                .values()
                .filter(area -> area.fkTerminal().isPresent())
                .groupBy(area -> area.fkTerminal().orElseThrow())
                .mapValues(HashSet::ofAll);
    }

    @Value.Derived
    default Set<JrStopArea> stopAreasNotInTerminals() {
        return stopAreas()
                .values()
                .filter(area -> area.fkTerminal().isEmpty())
                .toSet();
    }

    @Value.Derived
    default Map<JrStopAreaPk, Set<JrStop>> stopsPerStopArea() {
        return stops()
                .groupBy(JrStop::fkStopArea)
                .mapValues(HashSet::ofAll);
    }

    @Value.Derived
    default Map<JrNodePk, JrStop> stopsPerNode() {
        return stops()
                .groupBy(JrStop::fkNode)
                .mapValues(Traversable::head);
    }

    @Value.Derived
    default Map<JrLinkPk, List<JrPoint>> pointsPerLink() {
        return points()
                .groupBy(JrPoint::fkLink)
                .mapValues(points -> points.sortBy(IHasOrderNumber::orderNumber));
    }

    @Value.Derived
    default Map<JrRoutePathPk, List<JrRoutePathLink>> linksPerRoutePath() {
        return routePathLinks()
                .values()
                .toList()
                .groupBy(JrRoutePathLink::fkRoutePath)
                .mapValues(links -> links.sortBy(JrRoutePathLink::orderNumber));
    }

    @Value.Derived
    default Map<JrLinePk, List<JrLineHeader>> lineHeadersPerLine() {
        return lineHeaders()
                .values()
                .toList()
                .groupBy(JrLineHeader::fkLine)
                .mapValues(headers -> headers.sortBy(JrLineHeader::validFrom));
    }

    @Value.Derived
    default Map<JrRoutePk, JrLineHeader> routeToLineHeader() {
        // There is no direct link between route->line_header
        // Instead, we must traverse route->line and then find all the line headers
        // and check which line header to-from dates match the route
        return routes()
                .mapValues(route ->
                                   lineHeadersPerLine()
                                           // TODO: check to & from dates
                                           .getOrElse(route.fkLine(), List.empty())
                                           .peekOption()
                                           .getOrNull())
                .filterValues(Objects::nonNull);
    }

    // gather all nodes referred to in route links
    @Value.Derived
    default Set<NodeId> allRoutePoints() {
        return links()
                .values()
                .flatMap(rp -> HashSet.of(rp.startNode(), rp.endNode()))
                .toSet();
    }

    // Non bus stop route points (e.g. intersections or municipal borders)
    @Value.Derived
    default Set<JrNode> plainRoutePoints() {
        return allRoutePoints()
                .map(n -> nodes().get(JrNodePk.of(n)).get())
                .filter(n -> n.nodeType() != NodeType.BUS_STOP);
    }

    @Value.Derived
    default Set<JrNode> busStopRoutePoints() {
        return allRoutePoints()
                .map(n -> nodes().get(JrNodePk.of(n)).get())
                .filter(n -> n.nodeType() == NodeType.BUS_STOP);
    }

    @Value.Derived
    default Map<JrRoutePathPk, List<JrNode>> nodesOnRoute() {
        return routePaths()
                .mapValues(rp -> linksPerRoutePath().getOrElse(rp.pk(), List.empty())
                                                    // Construct a list of nodes along the route path:
                                                    // [node a, node b, node b, node c, node c, node d ..]
                                                    .flatMap(link -> List.of(nodes().get(link.fkStartNode()).get(),
                                                                             nodes().get(link.fkEndNode()).get()))
                                                    // deduplicate -> [node a, node b, node c ..]
                                                    .foldLeft(List.empty(),
                                                              (nodes, node) -> nodes.isEmpty() || !nodes.last().equals(node) ? nodes.push(node) : nodes));
    }

    // We need a list of links between each bus stop
    @Value.Derived
    default Set<Tuple2<JrNode, JrNode>> busStopLinks() {
        return nodesOnRoute()
                .values()
                .flatMap(nodes -> nodes.filter(node -> node.nodeType() == NodeType.BUS_STOP)
                                       .grouped(2)
                                       .map(pairs -> Tuple.of(pairs.get(0), pairs.get(1)))
                                       .toSet())
                .toSet();
    }

    static JoreImportContext of(final List<JrLine> lines,
                                final List<JrLineHeader> lineHeaders,
                                final List<JrRoute> routes,
                                final List<JrRoutePath> routePaths,
                                final List<JrRoutePathLink> routePathLinks,
                                final List<JrTerminalArea> terminalAreas,
                                final List<JrStopArea> stopAreas,
                                final List<JrStop> stops,
                                final List<JrLink> links,
                                final List<JrPoint> points,
                                final List<JrNode> nodes) {
        return ImmutableJoreImportContext.builder()
                                         .lines(groupByPk(lines))
                                         .lineHeaders(groupByPk(lineHeaders))
                                         .routes(groupByPk(routes))
                                         .routePaths(groupByPk(routePaths))
                                         .routePathLinks(groupByPk(routePathLinks))
                                         .terminalAreas(groupByPk(terminalAreas))
                                         .stopAreas(groupByPk(stopAreas))
                                         .stops(stops)
                                         .links(groupByPk(links))
                                         .points(points)
                                         .nodes(groupByPk(nodes))
                                         .build();
    }
}
