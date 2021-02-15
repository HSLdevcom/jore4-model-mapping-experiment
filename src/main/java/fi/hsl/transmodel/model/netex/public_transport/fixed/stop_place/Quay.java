package fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.PostalAddress;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.QuayRefStructure;

import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class Quay
        implements SiteComponent {

    public abstract String publicCode();

    public static Quay of(final String id,
                          final String name,
                          final String publicCode,
                          final BigDecimal latitude,
                          final BigDecimal longitude,
                          final PostalAddress postalAddress) {
        return ImmutableQuay.builder()
                            .id(String.format("q.%s", id))
                            .name(name)
                            .publicCode(publicCode)
                            .latitude(latitude)
                            .longitude(longitude)
                            .postalAddress(postalAddress)
                            .build();
    }

    public org.rutebanken.netex.model.Quay xml() {
        return new ObjectFactory()
                .createQuay()
                .withId(id())
                .withVersion(version())
                .withName(nameXml())
                .withPublicCode(publicCode())
                .withPostalAddress(postalAddress().map(postalAddress -> postalAddress.xml()).orElse(null))
                .withCentroid(simplePointXml());
    }

    public QuayRefStructure ref() {
        return new ObjectFactory()
                .createQuayRefStructure()
                .withRef(id())
                .withVersion(version());
    }
}
