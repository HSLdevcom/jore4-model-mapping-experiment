package fi.hsl.transmodel.model.netex.generic;


import fi.hsl.transmodel.model.netex.common.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.netex.common.mixin.IHasName;

import java.util.Optional;

public interface Point
        extends VersionedEntity,
                IHasName,
                IHasCoordinates {

    Optional<Integer> pointNumber();
}
