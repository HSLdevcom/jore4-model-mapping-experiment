package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.StopAreaId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasStopAreaId {
    @JoreColumn(name = "pysalueid")
    StopAreaId stopAreaId();
}
