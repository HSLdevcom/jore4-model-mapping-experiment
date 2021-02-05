package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasTransitType {
    @JoreColumn(name = "lnkverkko")
    TransitType transitType();
}
