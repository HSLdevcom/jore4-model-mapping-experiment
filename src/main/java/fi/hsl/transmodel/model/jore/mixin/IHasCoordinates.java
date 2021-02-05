package fi.hsl.transmodel.model.jore.mixin;

import com.google.common.base.Preconditions;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import org.immutables.value.Value;

import java.math.BigDecimal;

public interface IHasCoordinates {

    @JoreColumn(name = "solx")
    BigDecimal latitude();

    @JoreColumn(name = "soly")
    BigDecimal longitude();

    @Value.Check
    default void checkCoordinates() {
        Preconditions.checkState(latitude().abs().doubleValue() <= 90.0D);
        Preconditions.checkState(longitude().abs().doubleValue() <= 180.0D);
    }

    static BigDecimal fromString(final String val) {
        return new BigDecimal(val);
    }
}
