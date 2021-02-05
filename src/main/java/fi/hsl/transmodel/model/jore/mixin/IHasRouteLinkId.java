package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.RouteLinkId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasRouteLinkId {
    @JoreColumn(name = "relid")
    RouteLinkId routeLinkId();
}
