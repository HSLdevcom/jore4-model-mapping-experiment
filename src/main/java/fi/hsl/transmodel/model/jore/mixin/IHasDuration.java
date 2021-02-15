package fi.hsl.transmodel.model.jore.mixin;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IHasDuration {
    LocalDateTime validFrom();

    Optional<LocalDateTime> validTo();
}
