package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.mixin.IHasNodes;
import fi.hsl.transmodel.model.jore.mixin.IHasOrderNumber;
import fi.hsl.transmodel.model.jore.mixin.IHasPointId;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import org.immutables.value.Value;

@Value.Immutable
public interface JrPointPk extends IHasTransitType,
                                   IHasNodes,
                                   IHasOrderNumber,
                                   IHasPointId {
    static JrPointPk of(final TransitType transitType,
                        final NodeId start,
                        final NodeId end,
                        final int orderNumber,
                        final int pointId) {
        return ImmutableJrPointPk.builder()
                                 .transitType(transitType)
                                 .startNode(start)
                                 .endNode(end)
                                 .orderNumber(orderNumber)
                                 .pointId(pointId)
                                 .build();
    }
}
