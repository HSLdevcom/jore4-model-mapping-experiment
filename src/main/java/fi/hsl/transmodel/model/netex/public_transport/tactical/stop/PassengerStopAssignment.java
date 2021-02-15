package fi.hsl.transmodel.model.netex.public_transport.tactical.stop;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place.Quay;
import fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place.StopPlace;
import fi.hsl.transmodel.model.netex.public_transport.tactical.service.ScheduledStopPoint;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.StopAssignment_VersionStructure;

import javax.xml.bind.JAXBElement;
import java.math.BigInteger;

@Value.Immutable
@NeTExDtoStyle
public abstract class PassengerStopAssignment
        implements VersionedEntity {

    public abstract ScheduledStopPoint stopPoint();

    public abstract StopPlace stopPlace();

    public abstract Quay quay();

    // See https://enturas.atlassian.net/wiki/spaces/PUBLIC/pages/728563886/network#PassengerStopAssignment
    @Value.Default
    public BigInteger order() {
        return BigInteger.ZERO;
    }

    public static PassengerStopAssignment of(final ScheduledStopPoint stopPoint,
                                             final StopPlace stopPlace,
                                             final Quay quay) {
        return ImmutablePassengerStopAssignment.builder()
                                               .id(String.format("psa.%s.%s.%s", stopPoint.id(), stopPlace.id(), quay.id()))
                                               .stopPoint(stopPoint)
                                               .stopPlace(stopPlace)
                                               .quay(quay)
                                               .build();
    }

    public JAXBElement<? extends StopAssignment_VersionStructure> xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory
                .createPassengerStopAssignment(
                        factory.createPassengerStopAssignment()
                               .withId(id())
                               .withVersion(version())
                               .withOrder(order())
                               .withScheduledStopPointRef(stopPoint().stopPointRefElement())
                               .withStopPlaceRef(stopPlace().ref())
                               .withQuayRef(quay().ref())
                );
    }
}
