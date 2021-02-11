package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.public_transport.network.infrastructure.RoadJunction;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;

@Value.Immutable
@NeTExDtoStyle
public abstract class RoutePoint
        implements RouteLinkEndpoint {

    public abstract RoadJunction junction();

    public static RoutePoint of(final RoadJunction junction) {
        return ImmutableRoutePoint.builder()
                                  .id(String.format("rp.%s", junction.id()))
                                  .junction(junction)
                                  .build();
    }

    public org.rutebanken.netex.model.RoutePoint xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory
                .createRoutePoint()
                .withId(id())
                .withVersion(version())
                .withProjections(
                        factory.createProjections_RelStructure()
                               .withProjectionRefOrProjection(
                                       factory.createPointProjection(
                                               factory.createPointProjection()
                                                      .withVersion(version())
                                                      .withId(id())
                                                      .withProjectToPointRef(junction().pointRef()))));
    }
}
