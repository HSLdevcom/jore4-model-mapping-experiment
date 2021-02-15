package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.PointRefStructure;
import org.rutebanken.netex.model.RoutePointRefStructure;

import javax.xml.bind.JAXBElement;

/**
 * Route Links connect two points, which can be either Route Points or Scheduled Stop Points.
 */
public interface RouteLinkEndpoint extends VersionedEntity {
    default RoutePointRefStructure routePointRef() {
        return new ObjectFactory()
                .createRoutePointRefStructure()
                .withVersion(version())
                .withRef(id());
    }

    default JAXBElement<? extends PointRefStructure> pointRef() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createPointRef(factory.createPointRefStructure()
                                             .withRef(id())
                                             .withVersion(version()));
    }
}
