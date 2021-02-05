package fi.hsl.transmodel.xform;

import com.google.common.collect.Lists;
import fi.hsl.transmodel.model.jore.JoreImportContext;
import fi.hsl.transmodel.model.jore.entity.JrLink;
import fi.hsl.transmodel.model.jore.entity.JrNode;
import fi.hsl.transmodel.model.jore.entity.JrPoint;
import fi.hsl.transmodel.model.jore.field.NodeType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import io.vavr.collection.List;
import org.rutebanken.netex.model.Common_VersionFrameStructure;
import org.rutebanken.netex.model.InfrastructureElementsInFrame_RelStructure;
import org.rutebanken.netex.model.InfrastructureFrame;
import org.rutebanken.netex.model.InfrastructureJunctionsInFrame_RelStructure;
import org.rutebanken.netex.model.InfrastructureLink_VersionStructure;
import org.rutebanken.netex.model.InfrastructurePoint_VersionStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PointOnLink;
import org.rutebanken.netex.model.PointsOnLink_RelStructure;
import org.rutebanken.netex.model.RoadPointRefStructure;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.util.Collection;

import static fi.hsl.transmodel.xform.Util.sanitizeId;
import static fi.hsl.transmodel.xform.Util.toLocation;

public final class InfrastructureConverter {

    private static final ObjectFactory FACTORY = new ObjectFactory();

    private InfrastructureConverter() {
    }

    public static Collection<JAXBElement<? extends Common_VersionFrameStructure>> infrastructure(final JoreImportContext ctx) {
        return Lists.newArrayList(infrastructureFrame(ctx));
    }

    private static JAXBElement<InfrastructureFrame> infrastructureFrame(final JoreImportContext ctx) {
        return FACTORY.createInfrastructureFrame(
                FACTORY.createInfrastructureFrame()
                       .withVersion("any")
                       .withId(sanitizeId("infrastructure"))
                       .withJunctions(junctionFrame(ctx))
                       .withElements(elementFrame(ctx))
        );
    }

    private static InfrastructureJunctionsInFrame_RelStructure junctionFrame(final JoreImportContext ctx) {
        return FACTORY.createInfrastructureJunctionsInFrame_RelStructure()
                      .withId(sanitizeId("junctions"))
                      .withRailwayJunctionOrRoadJunctionOrWireJunction(junctions(ctx));
    }

    private static Collection<InfrastructurePoint_VersionStructure> junctions(final JoreImportContext ctx) {
        return ctx
                .nodes()
                .values()
                .filter(n -> n.nodeType() == NodeType.CROSSROADS)
                .map(InfrastructureConverter::point)
                .toJavaList();
    }

    private static InfrastructurePoint_VersionStructure point(final JrNode node) {
        return FACTORY.createRoadJunction()
                      .withVersion("any")
                      .withId(sanitizeId("junc", node.nodeId().value()))
                      .withLocation(toLocation(node));
    }

    private static InfrastructureElementsInFrame_RelStructure elementFrame(final JoreImportContext ctx) {
        return FACTORY.createInfrastructureElementsInFrame_RelStructure()
                      .withId(sanitizeId("elements"))
                      .withRailwayElementOrRoadElementOrWireElement(elements(ctx));
    }

    private static Collection<InfrastructureLink_VersionStructure> elements(final JoreImportContext ctx) {
        return ctx
                .links()
                .values()
                .map(link -> roadElement(ctx, link))
                .toJavaList();
    }

    private static InfrastructureLink_VersionStructure roadElement(final JoreImportContext ctx,
                                                                   final JrLink link) {
        final String id = sanitizeId(link.transitType().name(),
                                     link.startNode().value(),
                                     link.endNode().value());
        final List<JrPoint> intermediatePoints = ctx.pointsPerLink().getOrElse(link.pk(), List.empty());
        return FACTORY.createRoadElement()
                      .withVersion("any")
                      .withId(sanitizeId("re", id))
                      .withDistance(BigDecimal.valueOf(link.measuredLength()))
                      .withPassingThrough(intermediatePoints.isEmpty() ? null : passThrough(id, intermediatePoints))
                      .withFromPointRef(refNode(link.startNode()))
                      .withToPointRef(refNode(link.endNode()));
    }

    private static RoadPointRefStructure refNode(final NodeId nodeId) {
        return FACTORY.createRoadPointRefStructure()
                      .withRef(sanitizeId("junc", nodeId.value()));
    }

    private static PointsOnLink_RelStructure passThrough(final String id,
                                                         final List<JrPoint> points) {
        return FACTORY.createPointsOnLink_RelStructure()
                      .withId(sanitizeId("points", id))
                      .withPointOnLink(points.map(p -> pointOnLink(id, p))
                                             .toJavaList());
    }

    private static PointOnLink pointOnLink(final String id,
                                           final JrPoint point) {
        return FACTORY.createPointOnLink()
                      .withId(sanitizeId("point-on-link", point.pointId(), id))
                      .withVersion("any")
                      .withPoint(FACTORY.createPoint(FACTORY.createPoint_VersionStructure()
                                                            .withVersion("any")
                                                            .withPointNumber(Integer.toString(point.orderNumber()))
                                                            .withId(sanitizeId("point", point.pointId(), id))
                                                            .withLocation(toLocation(point))));
    }
}
