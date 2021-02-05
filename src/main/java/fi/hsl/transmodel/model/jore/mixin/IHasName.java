package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import org.immutables.value.Value;

public interface IHasName {
    @JoreColumn(name = "nimi")
    String name();

    @JoreColumn(name = "nimir")
    @Value.Default
    default String nameSwedish() {
        return name();
    }
}
