package fi.hsl.transmodel;

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
import fi.hsl.transmodel.model.jore.field.NodeType;
import fi.hsl.transmodel.model.jore.field.RouteDirection;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.field.generated.RouteLinkId;
import fi.hsl.transmodel.model.jore.field.generated.StopAreaId;
import fi.hsl.transmodel.model.jore.field.generated.TerminalId;
import io.vavr.collection.List;

import java.time.Duration;
import java.time.Instant;

/**
 * This class provides some pseudo-random mock data
 */
public final class MockJoreContextBuilder {

    private static final TransitType TRANSIT_TYPE = TransitType.BUS;
    private static final LineId LINE_1001 = LineId.of("1001");
    private static final RouteId ROUTE_1 = RouteId.of("1");
    private static final TerminalId TERMINAL = TerminalId.of("000003");
    private static final StopAreaId STOP_AREA = StopAreaId.of("10004");

    private static final NodeId NODE_1 = NodeId.of("SOLMU 1");
    private static final NodeId NODE_2 = NodeId.of("SOLMU 2");
    private static final NodeId NODE_3 = NodeId.of("SOLMU 3");
    private static final NodeId NODE_4 = NodeId.of("SOLMU 4");

    private MockJoreContextBuilder() {
    }

    public static JoreImportContext ctx() {
        final Instant validFrom = Instant.now();
        final List<JrLine> lines = List.of(
                JrLine.of(LINE_1001,
                          TRANSIT_TYPE,
                          "Helsinki")
        );
        final List<JrLineHeader> lineHeaders = List.of(
                JrLineHeader.of(LINE_1001,
                                "Eira - Töölö - Sörnäinen (M) - Käpylä",
                                "Eira",
                                "Käpylä",
                                validFrom,
                                validFrom.plus(Duration.ofDays(100))),
                JrLineHeader.of(LINE_1001,
                                "Eira - Töölö - Sörnäinen (M) - Käpylä",
                                "Eira",
                                "Käpylä",
                                validFrom.plus(Duration.ofDays(100)),
                                validFrom.plus(Duration.ofDays(200)))
        );
        final List<JrRoute> routes = List.of(
                JrRoute.of(LINE_1001,
                           ROUTE_1,
                           "Reitti 1")
        );
        final List<JrRoutePath> routePaths = List.of(
                JrRoutePath.of(ROUTE_1,
                               RouteDirection.DIRECTION_1,
                               "Lasipalatsi - Itä-Pasila",
                               "Postitalo",
                               "Ratamestarinkatu",
                               validFrom),
                JrRoutePath.of(ROUTE_1,
                               RouteDirection.DIRECTION_2,
                               "Itä-Pasila - Lasipalatsi",
                               "Ratamestarinkatu",
                               "Postitalo",
                               validFrom)
        );
        final List<JrRoutePathLink> routePathLinks = List.of(
                JrRoutePathLink.of(ROUTE_1,
                                   TRANSIT_TYPE,
                                   RouteDirection.DIRECTION_1,
                                   RouteLinkId.of(10),
                                   1,
                                   NODE_1,
                                   NODE_2,
                                   validFrom),
                JrRoutePathLink.of(ROUTE_1,
                                   TRANSIT_TYPE,
                                   RouteDirection.DIRECTION_1,
                                   RouteLinkId.of(11),
                                   2,
                                   NODE_2,
                                   NODE_3,
                                   validFrom),
                JrRoutePathLink.of(ROUTE_1,
                                   TRANSIT_TYPE,
                                   RouteDirection.DIRECTION_1,
                                   RouteLinkId.of(12),
                                   3,
                                   NODE_3,
                                   NODE_4,
                                   validFrom)
        );
        final List<JrTerminalArea> terminalAreas = List.of(
                JrTerminalArea.of(TERMINAL,
                                  TRANSIT_TYPE,
                                  "Rautatientori",
                                  "60.17128",
                                  "24.94257")
        );
        final List<JrStopArea> stopAreas = List.of(
                JrStopArea.of(STOP_AREA,
                              TERMINAL,
                              TRANSIT_TYPE,
                              "Kirkkokatu",
                              "60.17127",
                              "24.95657")
        );
        final List<JrStop> stops = List.of(
                JrStop.of(STOP_AREA,
                          NODE_1,
                          "Rautatientori",
                          "118A",
                          "Rautatientori",
                          "123",
                          "60.17107",
                          "24.94289"),
                JrStop.of(STOP_AREA,
                          NODE_4,
                          "Rautatientori",
                          "120A",
                          "Rautatientori",
                          "123",
                          "60.17076",
                          "24.94271")
        );
        final List<JrNode> nodes = List.of(
                JrNode.of(NODE_1, NodeType.BUS_STOP, "60.17107", "24.94289"),
                JrNode.of(NODE_2, NodeType.CROSSROADS, "60.27522", "25.03550"),
                JrNode.of(NODE_3, NodeType.CROSSROADS, "60.16900", "24.93166"),
                JrNode.of(NODE_4, NodeType.BUS_STOP, "60.17076", "24.94271")
        );
        final List<JrLink> links = List.of(
                JrLink.of(TRANSIT_TYPE,
                          NODE_1,
                          NODE_2,
                          5),
                JrLink.of(TRANSIT_TYPE,
                          NODE_2,
                          NODE_3,
                          7),
                JrLink.of(TRANSIT_TYPE,
                          NODE_3,
                          NODE_4,
                          11)
        );
        final List<JrPoint> points = List.of(
                JrPoint.of(TRANSIT_TYPE,
                           NODE_1,
                           NODE_2,
                           1,
                           1,
                           "60.17200",
                           "24.97166"),
                JrPoint.of(TRANSIT_TYPE,
                           NODE_1,
                           NODE_2,
                           2,
                           2,
                           "60.19200",
                           "26.01166")
        );
        return JoreImportContext.of(lines,
                                    lineHeaders,
                                    routes,
                                    routePaths,
                                    routePathLinks,
                                    terminalAreas,
                                    stopAreas,
                                    stops,
                                    links,
                                    points,
                                    nodes);
    }
}
