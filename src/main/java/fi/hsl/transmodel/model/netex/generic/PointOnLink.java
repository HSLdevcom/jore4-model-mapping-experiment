package fi.hsl.transmodel.model.netex.generic;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;

import java.math.BigDecimal;

@Value.Immutable
@NeTExDtoStyle
public abstract class PointOnLink
        implements Point {

    public static PointOnLink of(final String parentId,
                                 final int pointNumber,
                                 final BigDecimal latitude,
                                 final BigDecimal longitude) {
        return ImmutablePointOnLink
                .builder()
                .id(String.format("pol.%s", parentId))
                .pointNumber(pointNumber)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    public org.rutebanken.netex.model.PointOnLink xml() {
        final ObjectFactory factory = new ObjectFactory();
        final int pointNumber = pointNumber().orElseThrow();
        return factory.createPointOnLink()
                      .withId(id())
                      .withVersion(version())
                      .withPoint(factory.createPoint(factory.createPoint_VersionStructure()
                                                            .withVersion(version())
                                                            .withPointNumber(Integer.toString(pointNumber))
                                                            .withId(String.format("%s.%s", id(), pointNumber))
                                                            .withLocation(locationXml())));
    }
}
