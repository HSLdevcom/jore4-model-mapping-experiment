package fi.hsl.transmodel.model.netex.common.mixin;

import com.google.common.base.Preconditions;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.ValidBetween;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface IHasValidBetween {
    Optional<LocalDateTime> validFrom();

    Optional<LocalDateTime> validTo();

    @Value.Check
    default void checkValidBetween() {
        Preconditions.checkState(validTo().isEmpty() || validFrom().isPresent());
    }

    @Nullable
    default ValidBetween validBetweenXml() {
        return validFrom().isEmpty() ?
                null :
                new ObjectFactory()
                        .createValidBetween()
                        .withFromDate(validFrom().orElseThrow())
                        .withToDate(validTo().orElse(null));
    }
}
