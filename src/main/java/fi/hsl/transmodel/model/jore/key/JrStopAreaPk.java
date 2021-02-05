package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.StopAreaId;
import fi.hsl.transmodel.model.jore.mixin.IHasStopAreaId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrStopAreaPk extends IHasStopAreaId {
    static JrStopAreaPk of(final StopAreaId stopAreaId) {
        return ImmutableJrStopAreaPk.builder()
                                    .stopAreaId(stopAreaId)
                                    .build();
    }
}
