package fi.hsl.transmodel.model.netex.public_transport.tactical.service;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.Point;
import fi.hsl.transmodel.model.netex.public_transport.network.route.RouteLinkEndpoint;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.ScheduledStopPointRefStructure;

import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class ScheduledStopPoint
        implements Point,
                   RouteLinkEndpoint {

    public static ScheduledStopPoint of(final String id,
                                        final BigDecimal latitude,
                                        final BigDecimal longitude) {
        return ImmutableScheduledStopPoint.builder()
                                          .id(String.format("ssp.%s", id))
                                          .latitude(latitude)
                                          .longitude(longitude)
                                          .build();
    }

    public org.rutebanken.netex.model.ScheduledStopPoint xml() {
        return new ObjectFactory()
                .createScheduledStopPoint()
                .withId(id())
                .withVersion(version())
                .withLocation(locationXml());
    }

    public ScheduledStopPointRefStructure stopPointRef() {
        return new ObjectFactory()
                .createScheduledStopPointRefStructure()
                .withVersion(version())
                .withRef(id());
    }

    public JAXBElement<? extends ScheduledStopPointRefStructure> stopPointRefElement() {
        return new ObjectFactory()
                .createScheduledStopPointRef(stopPointRef());
    }
}
