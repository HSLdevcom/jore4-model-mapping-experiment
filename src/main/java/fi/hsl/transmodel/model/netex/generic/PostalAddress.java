package fi.hsl.transmodel.model.netex.generic;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;

import java.util.Optional;

@Value.Immutable
@NeTExDtoStyle
public abstract class PostalAddress
        implements VersionedEntity {
    public abstract String address1();

    public abstract Optional<String> address2();

    public abstract Optional<String> postCode();

    public abstract PostalAddress withAddress2(String address2);

    public abstract PostalAddress withPostCode(String postCode);

    public static PostalAddress of(final String id,
                                   final String address1) {
        return ImmutablePostalAddress.builder()
                                     .id(String.format("pa.%s", id))
                                     .address1(address1)
                                     .build();
    }

    public org.rutebanken.netex.model.PostalAddress xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createPostalAddress()
                      .withId(id())
                      .withVersion(version())
                      .withAddressLine1(factory.createMultilingualString().withValue(address1()))
                      .withAddressLine2(address2().map(name -> factory.createMultilingualString().withValue(name)).orElse(null))
                      .withPostCode(postCode().orElse(null));
    }
}
