package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.TerminalId;
import fi.hsl.transmodel.model.jore.mixin.IHasTerminalId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrTerminalAreaPk extends IHasTerminalId {
    static JrTerminalAreaPk of(final TerminalId terminalId) {
        return ImmutableJrTerminalAreaPk.builder()
                                        .terminalId(terminalId)
                                        .build();
    }
}
