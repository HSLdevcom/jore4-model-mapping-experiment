package fi.hsl.transmodel.model.netex.public_transport.fixed.site;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.RootFrame;
import fi.hsl.transmodel.model.netex.public_transport.fixed.stop_place.StopPlace;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.rutebanken.netex.model.ObjectFactory;

import javax.xml.bind.JAXBElement;

@Value.Immutable
@NeTExDtoStyle
public abstract class SiteFrame
        implements RootFrame {

    public abstract Set<StopPlace> stopPlaces();

    public static SiteFrame of(final Iterable<StopPlace> stopPlaces) {
        return ImmutableSiteFrame.builder()
                                 .id("site")
                                 .stopPlaces(HashSet.ofAll(stopPlaces))
                                 .build();
    }

    @Override
    public JAXBElement<org.rutebanken.netex.model.SiteFrame> xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createSiteFrame(
                factory.createSiteFrame()
                       .withVersion(version())
                       .withId(id())
                       .withStopPlaces(factory.createStopPlacesInFrame_RelStructure()
                                              .withStopPlace(stopPlaces()
                                                                     .map(StopPlace::xml)
                                                                     .toJavaList())));
    }
}
