package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.key.JrLinePk;
import fi.hsl.transmodel.model.jore.key.JrRoutePk;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasLineId;
import fi.hsl.transmodel.model.jore.mixin.IHasName;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteId;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_REITTI)
public interface JrRoute
        extends IHasPrimaryKey<JrRoutePk>,
                IHasLineId,
                IHasRouteId,
                IHasName {

    @Value.Derived
    default JrRoutePk pk() {
        return JrRoutePk.of(routeId());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_LINJA)
    default JrLinePk fkLine() {
        return JrLinePk.of(lineId());
    }

    static JrRoute of(final LineId lineId,
                      final RouteId routeId,
                      final String name) {
        return ImmutableJrRoute.builder()
                               .lineId(lineId)
                               .routeId(routeId)
                               .name(name)
                               .build();
    }
}
