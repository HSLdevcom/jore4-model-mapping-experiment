package fi.hsl.transmodel.model.netex.common.mixin;

import org.rutebanken.netex.model.MultilingualString;
import org.rutebanken.netex.model.ObjectFactory;

import java.util.Optional;

public interface IHasName {
    Optional<String> name();

    default MultilingualString nameXml() {
        return new ObjectFactory()
                .createMultilingualString()
                .withValue(name().orElseThrow());
    }
}
