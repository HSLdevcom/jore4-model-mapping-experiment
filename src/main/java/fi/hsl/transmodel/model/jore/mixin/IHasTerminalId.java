package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.field.generated.TerminalId;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;

public interface IHasTerminalId {
    @JoreColumn(name = "termid")
    TerminalId terminalId();
}
