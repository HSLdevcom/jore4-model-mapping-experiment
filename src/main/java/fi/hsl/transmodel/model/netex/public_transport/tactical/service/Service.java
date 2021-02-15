package fi.hsl.transmodel.model.netex.public_transport.tactical.service;

import com.google.common.collect.Lists;
import fi.hsl.transmodel.model.netex.common.style.NeTExDtoStyle;
import fi.hsl.transmodel.model.netex.generic.RootFrame;
import fi.hsl.transmodel.model.netex.public_transport.network.route.Line;
import fi.hsl.transmodel.model.netex.public_transport.network.route.RouteLink;
import fi.hsl.transmodel.model.netex.public_transport.network.route.RoutePoint;
import fi.hsl.transmodel.model.netex.public_transport.tactical.stop.PassengerStopAssignment;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.immutables.value.Value;
import org.rutebanken.netex.model.DataManagedObjectStructure;
import org.rutebanken.netex.model.LinesInFrame_RelStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.RouteLinksInFrame_RelStructure;
import org.rutebanken.netex.model.RoutePointsInFrame_RelStructure;
import org.rutebanken.netex.model.ScheduledStopPointsInFrame_RelStructure;
import org.rutebanken.netex.model.ServiceFrame;
import org.rutebanken.netex.model.ServiceLinksInFrame_RelStructure;
import org.rutebanken.netex.model.StopAssignmentsInFrame_RelStructure;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import java.util.Collection;

@Value.Immutable
@NeTExDtoStyle
public abstract class Service
        implements RootFrame {

    @Value.Default
    public Set<Line> lines() {
        return HashSet.empty();
    }

    @Value.Default
    public Set<RoutePoint> routePoints() {
        return HashSet.empty();
    }

    @Value.Default
    public Set<RouteLink> routeLinks() {
        return HashSet.empty();
    }

    @Value.Default
    public Set<ScheduledStopPoint> stopPoints() {
        return HashSet.empty();
    }

    @Value.Default
    public Set<ServiceLink> serviceLinks() {
        return HashSet.empty();
    }

    @Value.Default
    public Set<PassengerStopAssignment> stopAssignments() {
        return HashSet.empty();
    }

    public abstract Service withLines(Set<Line> lines);

    public abstract Service withRoutePoints(Set<RoutePoint> routePoints);

    public abstract Service withRouteLinks(Set<RouteLink> routeLinks);

    public abstract Service withStopPoints(Set<ScheduledStopPoint> stopPoints);

    public abstract Service withServiceLinks(Set<ServiceLink> serviceLinks);

    public abstract Service withStopAssignments(Set<PassengerStopAssignment> stopAssignments);

    public static Service of(final String id) {
        return ImmutableService.builder()
                               .id(id)
                               .build();
    }

    @Override
    public JAXBElement<ServiceFrame> xml() {
        final ObjectFactory factory = new ObjectFactory();
        return factory.createServiceFrame(
                factory.createServiceFrame()
                       .withId(id())
                       .withVersion(version())
                       .withLines(linesFrame())
                       .withRoutePoints(routePointsFrame())
                       .withRouteLinks(routeLinksFrame())
                       .withScheduledStopPoints(stopPointsFrame())
                       .withServiceLinks(serviceLinksFrame())
                       .withStopAssignments(stopAssignmentsFrame())
        );
    }

    @Nullable
    private LinesInFrame_RelStructure linesFrame() {
        return lines().isEmpty() ?
                null :
                new ObjectFactory().createLinesInFrame_RelStructure().withLine_(linesXml());
    }

    private Collection<JAXBElement<? extends DataManagedObjectStructure>> linesXml() {
        final ObjectFactory factory = new ObjectFactory();
        return Lists.newLinkedList(
                lines().map(line -> factory.createLine(line.xml()))
                       .toJavaList()
        );
    }

    @Nullable
    private RoutePointsInFrame_RelStructure routePointsFrame() {
        return routePoints().isEmpty() ?
                null : new ObjectFactory().createRoutePointsInFrame_RelStructure()
                                          .withRoutePoint(routePoints()
                                                                  .map(RoutePoint::xml)
                                                                  .toJavaList());
    }

    @Nullable
    private RouteLinksInFrame_RelStructure routeLinksFrame() {
        return routeLinks().isEmpty() ?
                null : new ObjectFactory().createRouteLinksInFrame_RelStructure()
                                          .withRouteLink(routeLinks()
                                                                 .map(RouteLink::xml)
                                                                 .toJavaList());
    }

    @Nullable
    private ScheduledStopPointsInFrame_RelStructure stopPointsFrame() {
        return stopPoints().isEmpty() ?
                null : new ObjectFactory().createScheduledStopPointsInFrame_RelStructure()
                                          .withScheduledStopPoint(stopPoints()
                                                                          .map(ScheduledStopPoint::xml)
                                                                          .toJavaList());
    }

    @Nullable
    private ServiceLinksInFrame_RelStructure serviceLinksFrame() {
        return serviceLinks().isEmpty() ?
                null : new ObjectFactory().createServiceLinksInFrame_RelStructure()
                                          .withServiceLink(serviceLinks()
                                                                   .map(ServiceLink::xml)
                                                                   .toJavaList());
    }

    @Nullable
    private StopAssignmentsInFrame_RelStructure stopAssignmentsFrame() {
        return stopAssignments().isEmpty() ?
                null : new ObjectFactory().createStopAssignmentsInFrame_RelStructure()
                                          .withStopAssignment(Lists.newLinkedList(stopAssignments()
                                                                                          .map(PassengerStopAssignment::xml)));
    }
}
