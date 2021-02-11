package fi.hsl.transmodel.model.netex.generic;

import org.rutebanken.netex.model.Common_VersionFrameStructure;

import javax.xml.bind.JAXBElement;

public interface RootFrame extends Frame {
    JAXBElement<? extends Common_VersionFrameStructure> xml();
}
