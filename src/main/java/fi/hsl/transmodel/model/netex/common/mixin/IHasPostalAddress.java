package fi.hsl.transmodel.model.netex.common.mixin;


import fi.hsl.transmodel.model.netex.generic.PostalAddress;

import java.util.Optional;

public interface IHasPostalAddress {
    Optional<PostalAddress> postalAddress();
}
