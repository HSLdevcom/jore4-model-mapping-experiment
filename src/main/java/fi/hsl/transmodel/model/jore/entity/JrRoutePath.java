package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.RouteDirection;
import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.key.JrRoutePathPk;
import fi.hsl.transmodel.model.jore.key.JrRoutePk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasDuration;
import fi.hsl.transmodel.model.jore.mixin.IHasName;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteDirection;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteId;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.Optional;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_REITINSUUNTA)
public interface JrRoutePath
        extends IHasPrimaryKey<JrRoutePathPk>,
                IHasRouteId,
                IHasName,
                IHasRouteDirection,
                IHasDuration {

    @JoreColumn(name = "suulahpaik")
    String origin();

    @JoreColumn(name = "suupaapaik")
    String destination();

    @JoreColumn(name = "suuvoimast")
    LocalDateTime validFrom();

    @JoreColumn(name = "suuvoimviimpvm")
    Optional<LocalDateTime> validTo();

    @Value.Derived
    default JrRoutePathPk pk() {
        return JrRoutePathPk.of(routeId(),
                                direction(),
                                validFrom());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_REITTI)
    default JrRoutePk fkRoute() {
        return JrRoutePk.of(routeId());
    }

    static JrRoutePath of(final RouteId routeId,
                          final RouteDirection direction,
                          final String name,
                          final String origin,
                          final String destination,
                          final LocalDateTime validFrom) {
        return ImmutableJrRoutePath.builder()
                                   .routeId(routeId)
                                   .direction(direction)
                                   .name(name)
                                   .origin(origin)
                                   .destination(destination)
                                   .validFrom(validFrom)
                                   .build();
    }
}
