package fi.hsl.transmodel.model.jore.field;

import java.util.Arrays;
import java.util.Optional;

public enum NodeType {
    BUS_STOP('P'),
    CROSSROADS('X'),
    MUNICIPAL_BORDER('-'),
    ;

    private final char jrValue;

    NodeType(final char val) {
        this.jrValue = val;
    }

    static Optional<NodeType> of(final char i) {
        return Arrays.stream(values())
                     .filter(transitType -> transitType.jrValue == i)
                     .findFirst();
    }

    static Optional<NodeType> of(final String s) {
        try {
            return of(s.charAt(0));
        } catch (final StringIndexOutOfBoundsException ignored) {
            return Optional.empty();
        }
    }
}
