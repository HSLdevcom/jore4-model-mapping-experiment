package fi.hsl.transmodel.model.netex.public_transport.network.infrastructure;

import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.RootFrame;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.rutebanken.netex.model.InfrastructureElementsInFrame_RelStructure;
import org.rutebanken.netex.model.InfrastructureJunctionsInFrame_RelStructure;
import org.rutebanken.netex.model.InfrastructureLink_VersionStructure;
import org.rutebanken.netex.model.ObjectFactory;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;

@Value.Immutable
@NeTExDtoStyle
public abstract class InfrastructureFrame
        implements RootFrame {

    public abstract Set<RoadJunction> roadJunctions();

    public abstract Set<RoadElement> roadElements();

    public static InfrastructureFrame of(final Iterable<RoadJunction> junctions,
                                         final Iterable<RoadElement> elements) {
        return ImmutableInfrastructureFrame.builder()
                                           .id("infrastructure")
                                           .roadJunctions(HashSet.ofAll(junctions))
                                           .roadElements(HashSet.ofAll(elements))
                                           .build();
    }

    @Override
    public JAXBElement<org.rutebanken.netex.model.InfrastructureFrame> xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createInfrastructureFrame(
                factory.createInfrastructureFrame()
                       .withVersion(version())
                       .withId(id())
                       .withJunctions(junctionsXml())
                       .withElements(elementsXml())
        );
    }

    @Nullable
    private InfrastructureJunctionsInFrame_RelStructure junctionsXml() {
        return roadJunctions().isEmpty() ?
                null :
                new ObjectFactory()
                        .createInfrastructureJunctionsInFrame_RelStructure()
                        .withId(id())
                        .withRailwayJunctionOrRoadJunctionOrWireJunction(roadJunctions()
                                                                                 .map(RoadJunction::xml)
                                                                                 .toJavaList());
    }

    @Nullable
    private InfrastructureElementsInFrame_RelStructure elementsXml() {
        return roadElements().isEmpty() ?
                null :
                new ObjectFactory()
                        .createInfrastructureElementsInFrame_RelStructure()
                        .withId(id())
                        .withRailwayElementOrRoadElementOrWireElement(roadElements()
                                                                              .map(element -> (InfrastructureLink_VersionStructure) element.xml())
                                                                              .toJavaList());
    }
}
