package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.mixin.IHasLineId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrLinePk extends IHasLineId {
    static JrLinePk of(final LineId lineId) {
        return ImmutableJrLinePk.builder()
                                .lineId(lineId)
                                .build();
    }
}
