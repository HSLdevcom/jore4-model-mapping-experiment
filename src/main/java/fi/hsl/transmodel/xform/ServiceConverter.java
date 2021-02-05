package fi.hsl.transmodel.xform;

import com.google.common.collect.Lists;
import fi.hsl.transmodel.model.jore.JoreImportContext;
import fi.hsl.transmodel.model.jore.entity.JrNode;
import fi.hsl.transmodel.model.jore.entity.JrRoutePath;
import fi.hsl.transmodel.model.jore.field.RouteDirection;
import org.rutebanken.netex.model.Common_VersionFrameStructure;
import org.rutebanken.netex.model.JourneyPattern;
import org.rutebanken.netex.model.JourneyPatternsInFrame_RelStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.ScheduledStopPoint;
import org.rutebanken.netex.model.ScheduledStopPointsInFrame_RelStructure;
import org.rutebanken.netex.model.ServiceFrame;

import javax.xml.bind.JAXBElement;
import java.util.Collection;

import static fi.hsl.transmodel.xform.Util.sanitizeId;
import static fi.hsl.transmodel.xform.Util.toLocation;

public final class ServiceConverter {

    private static final ObjectFactory FACTORY = new ObjectFactory();

    private ServiceConverter() {
    }

    public static Collection<JAXBElement<? extends Common_VersionFrameStructure>> services(final JoreImportContext ctx) {
        return Lists.newArrayList(serviceFrame(ctx));
    }

    private static JAXBElement<ServiceFrame> serviceFrame(final JoreImportContext ctx) {
        return FACTORY.createServiceFrame(
                FACTORY.createServiceFrame()
                       .withVersion("any")
                       .withId(sanitizeId("services"))
                       // scheduled stop points == bus stops etc.
                       .withScheduledStopPoints(stopPointsFrame(ctx))
                       .withJourneyPatterns(journeyPatternsFrame(ctx))
        );
    }

    /*
     * SCHEDULED STOP POINTS
     */

    private static ScheduledStopPointsInFrame_RelStructure stopPointsFrame(final JoreImportContext ctx) {
        return FACTORY.createScheduledStopPointsInFrame_RelStructure()
                      .withScheduledStopPoint(stopPoints(ctx));
    }

    private static Collection<ScheduledStopPoint> stopPoints(final JoreImportContext ctx) {
        return ctx.busStopRoutePoints()
                  .map(ServiceConverter::stopPoint)
                  .toJavaList();
    }

    private static ScheduledStopPoint stopPoint(final JrNode node) {
        final String id = sanitizeId("ssp", node.nodeId());
        return FACTORY.createScheduledStopPoint()
                      .withId(id)
                      .withVersion("any")
                      .withLocation(toLocation(node));
    }

    /*
     * JOURNEY PATTERNS
     */

    private static JourneyPatternsInFrame_RelStructure journeyPatternsFrame(final JoreImportContext ctx) {
        return FACTORY.createJourneyPatternsInFrame_RelStructure()
                      .withId("journey-patterns")
                      .withJourneyPattern_OrJourneyPatternView(journeyPatterns(ctx));
    }

    private static Collection<JAXBElement<?>> journeyPatterns(final JoreImportContext ctx) {
        return Lists.newLinkedList(
                ctx.routePaths()
                   .values()
                   .map(routePath -> journeyPattern(routePath, ctx))
        );
    }

    private static JAXBElement<JourneyPattern> journeyPattern(final JrRoutePath routePath, final JoreImportContext ctx) {
        final String id = sanitizeId("journey", routePath.routeId(), routePath.direction());
        final String routeId = sanitizeId("r", routePath.routeId().value(), routePath.direction() == RouteDirection.DIRECTION_1 ? "1" : "2");
        return FACTORY.createJourneyPattern(
                FACTORY.createJourneyPattern()
                       .withVersion("any")
                       .withId(id)
                       .withRouteRef(FACTORY.createRouteRefStructure()
                                            .withVersion("any")
                                            .withRef(routeId))
        );
    }
}
