package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.key.JrLinkPk;
import fi.hsl.transmodel.model.jore.key.JrPointPk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.jore.mixin.IHasNodes;
import fi.hsl.transmodel.model.jore.mixin.IHasOrderNumber;
import fi.hsl.transmodel.model.jore.mixin.IHasPointId;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

/**
 * A point describes a single waypoint within a link.
 * <p>
 * A list of points is used to draw the exact path of a link.
 */
@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_PISTE)
public interface JrPoint
        extends IHasTransitType,
                IHasNodes,
                IHasOrderNumber,
                IHasPointId,
                IHasCoordinates {

    @JoreColumn(name = "pisjarjnro")
    int orderNumber();

    @Value.Derived
    default JrPointPk pk() {
        return JrPointPk.of(transitType(),
                            startNode(),
                            endNode(),
                            orderNumber(),
                            pointId());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_LINKKI)
    default JrLinkPk fkLink() {
        return JrLinkPk.of(transitType(),
                           startNode(),
                           endNode());
    }

    static JrPoint of(final TransitType transitType,
                      final NodeId startNode,
                      final NodeId endNode,
                      final int pointId,
                      final int order,
                      final String latitude,
                      final String longitude) {
        return ImmutableJrPoint.builder()
                               .pointId(pointId)
                               .orderNumber(order)
                               .transitType(transitType)
                               .startNode(startNode)
                               .endNode(endNode)
                               .latitude(IHasCoordinates.fromString(latitude))
                               .longitude(IHasCoordinates.fromString(longitude))
                               .build();
    }
}
