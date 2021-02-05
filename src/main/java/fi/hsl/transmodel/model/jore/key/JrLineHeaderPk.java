package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.mixin.IHasDuration;
import fi.hsl.transmodel.model.jore.mixin.IHasLineId;
import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public interface JrLineHeaderPk extends IHasLineId,
                                        IHasDuration {
    static JrLineHeaderPk of(final LineId lineId,
                             final Instant validFrom) {
        return ImmutableJrLineHeaderPk.builder()
                                      .lineId(lineId)
                                      .validFrom(validFrom)
                                      .build();
    }
}
