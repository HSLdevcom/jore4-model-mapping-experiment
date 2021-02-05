package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrRoutePk extends IHasRouteId {
    static JrRoutePk of(final RouteId routeId) {
        return ImmutableJrRoutePk.builder()
                                 .routeId(routeId)
                                 .build();
    }
}
