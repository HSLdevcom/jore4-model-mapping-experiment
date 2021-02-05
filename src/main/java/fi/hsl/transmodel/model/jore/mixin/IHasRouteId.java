package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasRouteId {
    @JoreColumn(name = "reitunnus")
    RouteId routeId();
}
