package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.common.mixin.IHasName;
import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import org.immutables.value.Value;
import org.rutebanken.netex.model.LineRefStructure;
import org.rutebanken.netex.model.ObjectFactory;

import javax.xml.bind.JAXBElement;
import java.time.LocalDateTime;
import java.util.Optional;

@Value.Immutable
@NeTExDtoStyle
public abstract class Line
        implements VersionedEntity,
                   IHasName {

    public abstract String publicCode();

    public static Line of(final String id,
                          final String publicCode,
                          final String name,
                          final String version,
                          final Optional<LocalDateTime> validFrom,
                          final Optional<LocalDateTime> validto) {
        return ImmutableLine.builder()
                            .id(String.format("l.%s", id))
                            .version(version)
                            .publicCode(publicCode)
                            .name(name)
                            .validFrom(validFrom)
                            .validTo(validto)
                            .build();
    }

    public org.rutebanken.netex.model.Line xml() {
        return new ObjectFactory()
                .createLine()
                .withId(id())
                .withVersion(version())
                .withValidBetween(validBetweenXml())
                .withPublicCode(publicCode())
                .withName(nameXml());
    }

    public JAXBElement<? extends LineRefStructure> lineRef() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createLineRef(
                factory.createLineRefStructure()
                       .withRef(id())
                       .withVersion(version())
        );
    }
}
