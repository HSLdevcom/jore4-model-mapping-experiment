package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;

import java.math.BigInteger;
import java.util.Optional;

@Value.Immutable
@NeTExDtoStyle
public abstract class PointOnRoute
        implements VersionedEntity {

    public abstract BigInteger order();

    public abstract RoutePoint routePoint();

    // The final PointOnRoute under a Route does not contain an onwardLink
    public abstract Optional<RouteLink> onwardLink();

    public static PointOnRoute of(final String id,
                                  final RoutePoint point,
                                  final int order,
                                  final RouteLink link) {
        return ImmutablePointOnRoute.builder()
                                    .id(String.format("por.%s.%s.%d", id, point.id(), order))
                                    .order(BigInteger.valueOf(order))
                                    .routePoint(point)
                                    .onwardLink(link)
                                    .build();
    }

    public static PointOnRoute of(final String id,
                                  final RoutePoint point,
                                  final int order) {
        return ImmutablePointOnRoute.builder()
                                    .id(String.format("por.%s.%s.%d", id, point.id(), order))
                                    .order(BigInteger.valueOf(order))
                                    .routePoint(point)
                                    .build();
    }

    public org.rutebanken.netex.model.PointOnRoute xml() {
        return new ObjectFactory()
                .createPointOnRoute()
                .withId(id())
                .withVersion(version())
                .withOrder(order())
                .withPointRef(routePoint().pointRef())
                .withOnwardRouteLinkRef(onwardLink().map(RouteLink::ref).orElse(null));
    }
}
