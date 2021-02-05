package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.mixin.IHasNodeId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrNodePk extends IHasNodeId {
    static JrNodePk of(final NodeId nodeId) {
        return ImmutableJrNodePk.builder()
                                .nodeId(nodeId)
                                .build();
    }
}
