package fi.hsl.transmodel.model.netex.generic;

import fi.hsl.transmodel.model.netex.common.mixin.IHasValidBetween;
import fi.hsl.transmodel.model.netex.common.mixin.IHasVersion;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VersionedEntity
        extends Entity,
                IHasVersion,
                IHasValidBetween {

    Optional<LocalDateTime> createdAt();
}
