package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasNodeId {
    @JoreColumn(name = "soltunnus")
    NodeId nodeId();
}
