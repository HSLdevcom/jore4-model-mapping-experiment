package fi.hsl.transmodel.model.jore.field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

public enum TransitType {
    BUS(1),
    SUBWAY(2),
    TRAM(3),
    TRAIN(4),
    FERRY(7),
    ;

    private static final Logger LOG = LoggerFactory.getLogger(TransitType.class);

    private final int jrValue;

    TransitType(final int val) {
        this.jrValue = val;
    }

    public static Optional<TransitType> of(final int i) {
        return Arrays.stream(values())
                     .filter(transitType -> transitType.jrValue == i)
                     .findFirst();
    }

    public static Optional<TransitType> of(final String s) {
        try {
            return of(Integer.parseInt(s));
        } catch (final NumberFormatException ignored) {
            LOG.warn("Could not parse TransitType from '{}'", s);
            return Optional.empty();
        }
    }
}
