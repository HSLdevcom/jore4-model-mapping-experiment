package fi.hsl.transmodel.model.jore.field;

import java.util.Arrays;
import java.util.Optional;

public enum RouteDirection {
    DIRECTION_1(1),
    DIRECTION_2(2),
    ;

    private final int jrValue;

    RouteDirection(final int val) {
        jrValue = val;
    }

    static Optional<RouteDirection> of(final int i) {
        return Arrays.stream(values())
                     .filter(dir -> dir.jrValue == i)
                     .findFirst();
    }

    static Optional<RouteDirection> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
