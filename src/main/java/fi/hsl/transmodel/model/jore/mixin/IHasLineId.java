package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.LineId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasLineId {
    @JoreColumn(name = "lintunnus")
    LineId lineId();
}
