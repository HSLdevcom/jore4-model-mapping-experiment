package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.RouteDirection;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasRouteDirection {
    @JoreColumn(name = "suusuunta",
                example = "1")
    RouteDirection direction();
}
