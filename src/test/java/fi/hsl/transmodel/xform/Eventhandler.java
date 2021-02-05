package fi.hsl.transmodel.xform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class Eventhandler implements ValidationEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(Eventhandler.class);

    @Override
    public boolean handleEvent(final ValidationEvent event) {

        LOG.info("{}: {}", event.getSeverity(),
                 event.getMessage());

        return false;
    }
}
