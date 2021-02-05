package fi.hsl.transmodel.xform;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class NameSpaceMapper extends NamespacePrefixMapper {

    private static final String GML_PREFIX = "gml";
    private static final String GML_URI = "http://www.opengis.net/gml/3.2";

    @Override
    public String getPreferredPrefix(final String namespaceUri,
                                     final String suggestion,
                                     final boolean requirePrefix) {
        if (GML_URI.equals(namespaceUri)) {
            return GML_PREFIX;
        }
        return suggestion;
    }

    @Override
    public String[] getPreDeclaredNamespaceUris() {
        return new String[]{GML_URI};
    }
}
