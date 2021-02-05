package fi.hsl.transmodel.model.jore.mixin;

import java.time.Instant;
import java.util.Optional;

public interface IHasDuration {
    Instant validFrom();

    Optional<Instant> validTo();
}
