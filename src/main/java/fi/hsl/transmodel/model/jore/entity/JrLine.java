package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.key.JrLinePk;
import fi.hsl.transmodel.model.jore.key.JrRoutePk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasLineId;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.util.Optional;


@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_LINJA)
public interface JrLine
        extends IHasPrimaryKey<JrLinePk>,
                IHasLineId,
                IHasTransitType {

    @Value.Derived
    default JrLinePk pk() {
        return JrLinePk.of(lineId());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_REITTI)
    default Optional<JrRoutePk> fkBasicRoute() {
        return basicRoute()
                .map(JrRoutePk::of);
    }

    @JoreColumn(name = "lintilorg",
                example = "Helsinki")
    String clientOrganization();

    @JoreColumn(name = "vaihtoaika")
    @Value.Default
    default int exchangeTime() {
        return 0;
    }

    @JoreColumn(name = "linperusreitti")
    Optional<RouteId> basicRoute();

    static JrLine of(final LineId lineId,
                     final TransitType transitType,
                     final String clientOrganization) {
        return ImmutableJrLine.builder()
                              .lineId(lineId)
                              .transitType(transitType)
                              .clientOrganization(clientOrganization)
                              .build();
    }
}
