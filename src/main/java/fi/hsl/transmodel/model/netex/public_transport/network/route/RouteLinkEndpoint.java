package fi.hsl.transmodel.model.netex.public_transport.network.route;

import fi.hsl.transmodel.model.netex.generic.VersionedEntity;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.RoutePointRefStructure;

/**
 * Route Links connect two points, which can be either Route Points or Scheduled Stop Points.
 */
public interface RouteLinkEndpoint extends VersionedEntity {
    default RoutePointRefStructure pointRef() {
        return new ObjectFactory()
                .createRoutePointRefStructure()
                .withVersion(version())
                .withRef(id());
    }
}
