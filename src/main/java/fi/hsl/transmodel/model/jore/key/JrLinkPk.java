package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.mixin.IHasNodes;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import org.immutables.value.Value;

@Value.Immutable
public interface JrLinkPk extends IHasTransitType,
                                  IHasNodes {
    static JrLinkPk of(final TransitType transitType,
                       final NodeId startNode,
                       final NodeId endNode) {
        return ImmutableJrLinkPk.builder()
                                .transitType(transitType)
                                .startNode(startNode)
                                .endNode(endNode)
                                .build();
    }
}
