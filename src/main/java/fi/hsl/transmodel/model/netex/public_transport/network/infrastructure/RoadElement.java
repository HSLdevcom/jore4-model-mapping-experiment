package fi.hsl.transmodel.model.netex.public_transport.network.infrastructure;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.Link;
import fi.hsl.transmodel.model.netex.generic.PointOnLink;
import io.vavr.collection.List;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PointsOnLink_RelStructure;

import javax.annotation.Nullable;
import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class RoadElement implements Link {

    public abstract RoadJunction from();

    public abstract RoadJunction to();

    public static RoadElement of(final RoadJunction from,
                                 final RoadJunction to,
                                 final BigDecimal distance,
                                 final List<PointOnLink> points) {
        return ImmutableRoadElement.builder()
                                   .id(String.format("re.%s.%s", from.id(), to.id()))
                                   .distance(distance)
                                   .from(from)
                                   .to(to)
                                   .passingThrough(points)
                                   .build();
    }

    public org.rutebanken.netex.model.RoadElement xml() {
        return new ObjectFactory()
                .createRoadElement()
                .withVersion(version())
                .withId(id())
                .withDistance(distance())
                .withPassingThrough(pointsOnLink())
                .withFromPointRef(from().roadPointRef())
                .withToPointRef(to().roadPointRef());
    }

    @Nullable
    public PointsOnLink_RelStructure pointsOnLink() {
        return passingThrough().isEmpty() ?
                null :
                new ObjectFactory()
                        .createPointsOnLink_RelStructure()
                        .withId(id())
                        .withPointOnLink(passingThrough()
                                                 .map(PointOnLink::xml)
                                                 .toJavaList());
    }
}
