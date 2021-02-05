package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.key.JrLinkPk;
import fi.hsl.transmodel.model.jore.key.JrNodePk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasNodes;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

/**
 * A link identifies a segment of a infrastructure network between two junctions.
 * <p>
 * For example, in a road network a link identifies the road section between two road
 * intersections.
 * <p>
 * Links only specify the start and end nodes of the segment, the actual precise
 * link path is described with a list of points.
 */
@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_LINKKI)
public interface JrLink
        extends IHasPrimaryKey<JrLinkPk>,
                IHasTransitType,
                IHasNodes {

    @JoreColumn(name = "lnkmitpituus")
    Integer measuredLength();

    @Value.Derived
    default JrLinkPk pk() {
        return JrLinkPk.of(transitType(),
                           startNode(),
                           endNode());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_SOLMU)
    default JrNodePk fkStartNode() {
        return JrNodePk.of(startNode());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_SOLMU)
    default JrNodePk fkEndNode() {
        return JrNodePk.of(endNode());
    }

    static JrLink of(final TransitType transitType,
                     final NodeId startNode,
                     final NodeId endNode,
                     final int measuredLength) {
        return ImmutableJrLink.builder()
                              .transitType(transitType)
                              .startNode(startNode)
                              .endNode(endNode)
                              .measuredLength(measuredLength)
                              .build();
    }
}
