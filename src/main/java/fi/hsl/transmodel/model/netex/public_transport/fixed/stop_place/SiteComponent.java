package fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place;

import fi.hsl.transmodel.model.netex.common.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.netex.common.mixin.IHasName;
import fi.hsl.transmodel.model.netex.generic.VersionedEntity;

public interface SiteComponent
        extends VersionedEntity,
                IHasCoordinates,
                IHasName {
}
