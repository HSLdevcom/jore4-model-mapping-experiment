package fi.hsl.transmodel.model.netex.common.mixin;

import org.immutables.value.Value;

public interface IHasVersion {
    @Value.Default
    default String version() {
        return "any";
    }
}
