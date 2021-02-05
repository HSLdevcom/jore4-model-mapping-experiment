package fi.hsl.transmodel.xform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.Marshaller;

public class MyListener extends Marshaller.Listener {

    private static final Logger LOG = LoggerFactory.getLogger(MyListener.class);

    @Override
    public void beforeMarshal(final Object source) {
        super.beforeMarshal(source);
        LOG.info("before: {}", source);
    }

    @Override
    public void afterMarshal(final Object source) {
        super.afterMarshal(source);
        LOG.info("after: {}", source);
    }
}
