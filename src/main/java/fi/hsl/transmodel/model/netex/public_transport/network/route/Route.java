package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.common.mixin.IHasName;
import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import io.vavr.collection.List;
import org.immutables.value.Value;
import org.rutebanken.netex.model.DirectionTypeEnumeration;
import org.rutebanken.netex.model.LinkSequence_VersionStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PointsOnRoute_RelStructure;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import java.time.LocalDateTime;
import java.util.Optional;

@Value.Immutable
@NeTExDtoStyle
public abstract class Route
        implements VersionedEntity,
                   IHasName {

    public abstract Line line();

    public abstract DirectionTypeEnumeration directionType();

    @Value.Default
    public List<PointOnRoute> points() {
        return List.empty();
    }

    public abstract Route withPoints(List<PointOnRoute> points);

    public static Route of(final String id,
                           final String name,
                           final Line line,
                           final DirectionTypeEnumeration directionType,
                           final LocalDateTime validFrom,
                           final Optional<LocalDateTime> validTo) {
        return ImmutableRoute.builder()
                             .id(String.format("r.%s.%s", id, directionType))
                             .name(name)
                             .line(line)
                             .directionType(directionType)
                             .validFrom(validFrom)
                             .validTo(validTo)
                             .build();
    }

    public JAXBElement<? extends LinkSequence_VersionStructure> xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createRoute(
                factory.createRoute()
                       .withId(id())
                       .withName(nameXml())
                       .withVersion(version())
                       .withValidBetween(validBetweenXml())
                       .withLineRef(line().lineRef())
                       .withDirectionType(directionType())
                       .withPointsInSequence(pointsOnRoute())
        );
    }

    @Nullable
    private PointsOnRoute_RelStructure pointsOnRoute() {
        return points().isEmpty() ?
                null : new ObjectFactory().createPointsOnRoute_RelStructure()
                                          .withPointOnRoute(points().sortBy(PointOnRoute::order)
                                                                    .map(PointOnRoute::xml)
                                                                    .toJavaList());
    }
}
