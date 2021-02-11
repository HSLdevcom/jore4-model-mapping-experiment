package fi.hsl.transmodel.xform;

import fi.hsl.transmodel.MockJoreContextBuilder;
import fi.hsl.transmodel.model.jore.JoreImportContext;
import fi.hsl.transmodel.model.netex.PublicationDelivery;
import org.junit.jupiter.api.Test;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PublicationDeliveryStructure;
import org.rutebanken.netex.validation.NeTExValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;

public class JoreImportContextConverterTest {

    private static final Logger LOG = LoggerFactory.getLogger(JoreImportContextConverterTest.class);


    @Test
    public void testConversion() throws JAXBException, IOException, SAXException {
        final JoreImportContext ctx = MockJoreContextBuilder.ctx();

        final PublicationDelivery delivery = JoreImportContextConverter.convert(ctx);

        final PublicationDeliveryStructure publication = delivery.xml();

        final JAXBContext jaxbContext = JAXBContext.newInstance(PublicationDeliveryStructure.class);
        final NeTExValidator neTExValidator = new NeTExValidator();
        final Marshaller marshaller = jaxbContext.createMarshaller();
        final ObjectFactory objectFactory = new ObjectFactory();

        marshaller.setSchema(neTExValidator.getSchema());
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // This isn't strictly needed but we can use this to set the prefixes
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NameSpaceMapper());

        // Enable for debugging if validation fails
        final boolean debug = false;
        if (debug) {
            marshaller.setEventHandler(new Eventhandler());
            marshaller.setListener(new MyListener());
        }

        final StringWriter writer = new StringWriter();

        marshaller.marshal(objectFactory.createPublicationDelivery(publication), writer);

        final String output = writer.toString();

        LOG.info("{}{}", System.lineSeparator(), output);
    }
}
