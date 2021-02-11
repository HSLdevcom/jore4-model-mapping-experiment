package fi.hsl.transmodel.model.netex.public_transport.network.infrastructure;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.Point;
import org.immutables.value.Value;
import org.rutebanken.netex.model.InfrastructurePoint_VersionStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PointRefStructure;
import org.rutebanken.netex.model.RoadPointRefStructure;

import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class RoadJunction implements Point {

    public static RoadJunction from(final String id,
                                    final BigDecimal latitude,
                                    final BigDecimal longitude) {
        return ImmutableRoadJunction.builder()
                                    .id(String.format("rj.%s", id.replaceAll("\\s", "_")))
                                    .latitude(latitude)
                                    .longitude(longitude)
                                    .build();
    }

    public RoadPointRefStructure roadPointRef() {
        return new ObjectFactory()
                .createRoadPointRefStructure()
                .withRef(id());
    }

    public PointRefStructure pointRef() {
        return new ObjectFactory()
                .createPointRefStructure()
                .withVersion(version())
                .withNameOfRefClass("RoadJunction")
                .withRef(id());
    }

    public InfrastructurePoint_VersionStructure xml() {
        return new ObjectFactory()
                .createRoadJunction()
                .withVersion(version())
                .withId(id())
                .withLocation(locationXml());
    }
}
