package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasNodes {
    @JoreColumn(name = "lnkalkuSolmu")
    NodeId startNode();

    @JoreColumn(name = "lnkloppuSolmu")
    NodeId endNode();
}
