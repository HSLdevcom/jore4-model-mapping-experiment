package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasPointId {
    @JoreColumn(name = "pisid")
    int pointId();
}
