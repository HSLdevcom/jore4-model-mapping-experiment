package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.key.JrLineHeaderPk;
import fi.hsl.transmodel.model.jore.key.JrLinePk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasDuration;
import fi.hsl.transmodel.model.jore.mixin.IHasLineId;
import fi.hsl.transmodel.model.jore.mixin.IHasName;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.Optional;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_LINJANNIMET)
public interface JrLineHeader
        extends IHasPrimaryKey<JrLineHeaderPk>,
                IHasLineId,
                IHasName,
                IHasDuration {

    @Value.Derived
    default JrLineHeaderPk pk() {
        return JrLineHeaderPk.of(lineId(), validFrom());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_LINJA)
    default JrLinePk fkLine() {
        return JrLinePk.of(lineId());
    }

    @JoreColumn(name = "linalkupvm")
    Instant validFrom();

    @JoreColumn(name = "linloppupvm")
    Optional<Instant> validTo();

    @JoreColumn(name = "lahtoPaikka1")
    String startPlace1();

    @JoreColumn(name = "lahtoPaikka2")
    String startPlace2();

    static JrLineHeader of(final LineId lineId,
                           final String name,
                           final String startPlace1,
                           final String startPlace2,
                           final Instant validFrom,
                           final Instant validTo) {
        return ImmutableJrLineHeader.builder()
                                    .lineId(lineId)
                                    .name(name)
                                    .startPlace1(startPlace1)
                                    .startPlace2(startPlace2)
                                    .validFrom(validFrom)
                                    .validTo(validTo)
                                    .build();
    }
}
