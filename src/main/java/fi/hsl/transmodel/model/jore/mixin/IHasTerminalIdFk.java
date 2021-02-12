package fi.hsl.transmodel.model.jore.mixin;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.generated.TerminalId;
import fi.hsl.transmodel.model.jore.key.JrTerminalAreaPk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import org.immutables.value.Value;

import java.util.Optional;

public interface IHasTerminalIdFk {
    @JoreColumn(name = "termid",
                nullable = true)
    Optional<TerminalId> terminalId();

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_LIJ_TERMINAALIALUE)
    default Optional<JrTerminalAreaPk> fkTerminal() {
        return terminalId().map(JrTerminalAreaPk::of);
    }
}
