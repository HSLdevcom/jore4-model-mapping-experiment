package fi.hsl.transmodel.model.netex.public_transport.tactical.service;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.Link;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;

import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class ServiceLink
        implements Link {

    public abstract ScheduledStopPoint from();

    public abstract ScheduledStopPoint to();

    public static ServiceLink of(final ScheduledStopPoint from,
                                 final ScheduledStopPoint to,
                                 final BigDecimal distance) {
        return ImmutableServiceLink.builder()
                                   .id(String.format("sl.%s.%s", from.id(), to.id()))
                                   .name(String.format("%s - %s", from.id(), to.id()))
                                   .distance(distance)
                                   .from(from)
                                   .to(to)
                                   .build();
    }

    public org.rutebanken.netex.model.ServiceLink xml() {
        return new ObjectFactory()
                .createServiceLink()
                .withVersion(version())
                .withId(id())
                .withName(nameXml())
                .withFromPointRef(from().stopPointRef())
                .withToPointRef(to().stopPointRef());
    }
}
