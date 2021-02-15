package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.RouteDirection;
import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.mixin.IHasDuration;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteDirection;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteId;
import org.immutables.value.Value;

import java.time.LocalDateTime;

@Value.Immutable
public interface JrRoutePathPk extends IHasRouteId,
                                       IHasDuration,
                                       IHasRouteDirection {
    static JrRoutePathPk of(final RouteId routeId,
                            final RouteDirection direction,
                            final LocalDateTime validFrom) {
        return ImmutableJrRoutePathPk.builder()
                                     .routeId(routeId)
                                     .direction(direction)
                                     .validFrom(validFrom)
                                     .build();
    }
}
