package fi.hsl.transmodel.xform;

import fi.hsl.transmodel.model.jore.JoreImportContext;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PublicationDeliveryStructure;

/**
 * This functionality is modelled after https://github.com/NeTEx-CEN/NeTEx/blob/master/examples/functions/simpleNetwork/Netex_SimpleNetwork_1.xml
 */
public final class JoreImportContextConverter {

    private static final ObjectFactory FACTORY = new ObjectFactory();

    private JoreImportContextConverter() {
    }

    public static PublicationDeliveryStructure convert(final JoreImportContext ctx) {
        return FACTORY.createPublicationDeliveryStructure()
                      .withVersion("any")
                      .withParticipantRef("JORE")
                      .withPublicationTimestamp(ctx.createdAt())
                      .withDescription(FACTORY.createMultilingualString()
                                              .withValue("Sample mock import from JORE"))
                      .withDataObjects(dataObjects(ctx));
    }

    private static PublicationDeliveryStructure.DataObjects dataObjects(final JoreImportContext ctx) {
        //noinspection unchecked
        return FACTORY.createPublicationDeliveryStructureDataObjects()
                      .withCompositeFrameOrCommonFrame(
                              FACTORY.createCompositeFrame(
                                      FACTORY.createCompositeFrame()
                                             .withId("some id")
                                             .withVersion("any")
                                             .withFrames(FACTORY.createFrames_RelStructure()
                                                                // Infrastructure frame for e.g. road junctions and road elements
                                                                .withCommonFrame(InfrastructureConverter.infrastructure(ctx))
                                                                // Site frame for e.g. terminals, bus stops and quays
                                                                .withCommonFrame(SiteConverter.sites(ctx))
                                                                // Service frame for routes (an abstract layer above the infrastructure and site frames)
                                                                .withCommonFrame(RouteConverter.routesForServices(ctx))
                                                                .withCommonFrame(ServiceConverter.services(ctx))
                                             )
                              )
                      );
    }
}
