package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.Link;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.RouteLinkRefStructure;

import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class RouteLink
        implements Link {

    public abstract RouteLinkEndpoint from();

    public abstract RouteLinkEndpoint to();

    public static RouteLink of(final BigDecimal distance,
                               final RouteLinkEndpoint from,
                               final RouteLinkEndpoint to) {
        return ImmutableRouteLink.builder()
                                 .id(String.format("rl.%s.%s", from.id(), to.id()))
                                 .distance(distance)
                                 .from(from)
                                 .to(to)
                                 .build();
    }

    public org.rutebanken.netex.model.RouteLink xml() {
        return new ObjectFactory()
                .createRouteLink()
                .withId(id())
                .withVersion(version())
                .withDistance(distance())
                .withFromPointRef(from().routePointRef())
                .withToPointRef(to().routePointRef());
    }

    public RouteLinkRefStructure ref() {
        return new ObjectFactory()
                .createRouteLinkRefStructure()
                .withRef(id())
                .withVersion(version());
    }
}
