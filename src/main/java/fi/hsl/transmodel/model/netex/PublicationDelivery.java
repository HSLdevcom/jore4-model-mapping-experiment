package fi.hsl.transmodel.model.netex;

import com.google.common.collect.Lists;
import fi.hsl.transmodel.model.netex.common.mixin.IHasDescription;
import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.RootFrame;
import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.rutebanken.netex.model.Common_VersionFrameStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PublicationDeliveryStructure;

import javax.xml.bind.JAXBElement;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Value.Immutable
@NeTExDtoStyle
public abstract class PublicationDelivery
        implements VersionedEntity,
                   IHasDescription {

    public abstract String participant();

    public abstract Set<RootFrame> frames();

    public static PublicationDelivery of(final String id,
                                         final String participant,
                                         final String description,
                                         final Iterable<RootFrame> frames) {
        return ImmutablePublicationDelivery.builder()
                                           .id(id)
                                           .participant(participant)
                                           .description(description)
                                           .frames(HashSet.ofAll(frames))
                                           .build();
    }

    public PublicationDeliveryStructure xml() {
        final ObjectFactory factory = new ObjectFactory();
        final Collection<JAXBElement<? extends Common_VersionFrameStructure>> nested =
                Lists.newLinkedList(frames().map(RootFrame::xml));
        final Collection<JAXBElement<? extends Common_VersionFrameStructure>> compositeFrame =
                List.of(factory.createCompositeFrame(
                        factory.createCompositeFrame()
                               .withId(id())
                               .withVersion(version())
                               .withFrames(factory.createFrames_RelStructure()
                                                  .withCommonFrame(nested))));
        return factory.createPublicationDeliveryStructure()
                      .withVersion(version())
                      .withPublicationTimestamp(LocalDateTime.now())
                      .withParticipantRef(participant())
                      .withDescription(descriptionXml())
                      .withDataObjects(factory.createPublicationDeliveryStructureDataObjects()
                                              .withCompositeFrameOrCommonFrame(compositeFrame));
    }
}
