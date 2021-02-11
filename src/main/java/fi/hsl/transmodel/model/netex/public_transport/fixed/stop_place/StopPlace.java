package fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.StopTypeEnumeration;
import org.rutebanken.netex.model.VehicleModeEnumeration;

import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class StopPlace
        implements SiteComponent {

    public abstract Set<Quay> quays();

    public abstract VehicleModeEnumeration transportMode();

    public abstract StopTypeEnumeration stopPlaceType();

    public static StopPlace of(final String id,
                               final String name,
                               final BigDecimal latitude,
                               final BigDecimal longitude,
                               final VehicleModeEnumeration transportMode,
                               final StopTypeEnumeration stopPlaceType,
                               final Iterable<Quay> quays) {
        return ImmutableStopPlace.builder()
                                 .id(String.format("sp.%s", id))
                                 .name(name)
                                 .latitude(latitude)
                                 .longitude(longitude)
                                 .transportMode(transportMode)
                                 .stopPlaceType(stopPlaceType)
                                 .quays(HashSet.ofAll(quays))
                                 .build();
    }

    public org.rutebanken.netex.model.StopPlace xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory
                .createStopPlace()
                .withVersion(version())
                .withId(id())
                .withTransportMode(transportMode())
                .withStopPlaceType(stopPlaceType())
                .withCentroid(simplePointXml())
                .withQuays(factory.createQuays_RelStructure()
                                  .withQuayRefOrQuay(quays().map(quay -> (Object) quay.xml())
                                                            .toJavaList()));
    }
}
