package fi.hsl.transmodel.model.netex.common.mixin;

import org.rutebanken.netex.model.MultilingualString;
import org.rutebanken.netex.model.ObjectFactory;

public interface IHasDescription {
    String description();

    default MultilingualString descriptionXml() {
        return new ObjectFactory()
                .createMultilingualString()
                .withValue(description());
    }
}
