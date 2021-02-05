package fi.hsl.transmodel.xform;

import com.google.common.collect.Lists;
import fi.hsl.transmodel.model.jore.JoreImportContext;
import fi.hsl.transmodel.model.jore.entity.JrStop;
import fi.hsl.transmodel.model.jore.entity.JrStopArea;
import fi.hsl.transmodel.model.jore.entity.JrTerminalArea;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import io.vavr.collection.HashSet;
import org.rutebanken.netex.model.Common_VersionFrameStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.Quay;
import org.rutebanken.netex.model.Quays_RelStructure;
import org.rutebanken.netex.model.SiteFrame;
import org.rutebanken.netex.model.StopPlace;
import org.rutebanken.netex.model.StopPlacesInFrame_RelStructure;
import org.rutebanken.netex.model.StopTypeEnumeration;
import org.rutebanken.netex.model.VehicleModeEnumeration;

import javax.xml.bind.JAXBElement;
import java.util.Collection;

import static fi.hsl.transmodel.xform.Util.toSimplePoint;

public final class SiteConverter {

    private static final ObjectFactory FACTORY = new ObjectFactory();

    private SiteConverter() {
    }

    public static Collection<JAXBElement<? extends Common_VersionFrameStructure>> sites(final JoreImportContext ctx) {
        return Lists.newLinkedList(
                ctx.terminalAreas()
                   .values()
                   .map(terminal -> siteFrame(terminal, ctx))
        );
    }

    private static JAXBElement<SiteFrame> siteFrame(final JrTerminalArea terminal,
                                                    final JoreImportContext ctx) {
        return FACTORY.createSiteFrame(
                FACTORY.createSiteFrame()
                       .withVersion("any")
                       .withId(terminal.terminalId().value())
                       .withName(FACTORY.createMultilingualString()
                                        .withValue(terminal.name()))
                       .withStopPlaces(stopPlaces(terminal, ctx))
        );
    }

    private static StopPlacesInFrame_RelStructure stopPlaces(final JrTerminalArea terminal,
                                                             final JoreImportContext ctx) {
        return FACTORY.createStopPlacesInFrame_RelStructure()
                      .withStopPlace(ctx.stopAreasPerTerminal()
                                        .getOrElse(terminal.pk(), HashSet.empty())
                                        .map(area -> stopPlace(area, ctx))
                                        .toJavaList());
    }

    private static StopPlace stopPlace(final JrStopArea stopArea,
                                       final JoreImportContext ctx) {
        return FACTORY.createStopPlace()
                      .withVersion("any")
                      .withId(stopArea.name())
                      .withTransportMode(toVehicleMode(stopArea))
                      .withStopPlaceType(toStopType(stopArea))
                      .withCentroid(toSimplePoint(stopArea))
                      .withQuays(quayRel(stopArea, ctx));
    }

    private static VehicleModeEnumeration toVehicleMode(final IHasTransitType withType) {
        switch (withType.transitType()) {
            case BUS:
                return VehicleModeEnumeration.BUS;
            case SUBWAY:
                return VehicleModeEnumeration.METRO;
            case TRAM:
                return VehicleModeEnumeration.TRAM;
            case TRAIN:
                return VehicleModeEnumeration.RAIL;
            case FERRY:
                return VehicleModeEnumeration.FERRY;
        }
        return VehicleModeEnumeration.OTHER;
    }

    private static StopTypeEnumeration toStopType(final IHasTransitType withType) {
        switch (withType.transitType()) {
            case BUS:
                return StopTypeEnumeration.ONSTREET_BUS;
            case SUBWAY:
                return StopTypeEnumeration.METRO_STATION;
            case TRAM:
                return StopTypeEnumeration.TRAM_STATION;
            case TRAIN:
                return StopTypeEnumeration.RAIL_STATION;
            case FERRY:
                return StopTypeEnumeration.FERRY_STOP;
        }
        return StopTypeEnumeration.OTHER;
    }

    private static Quays_RelStructure quayRel(final JrStopArea stopArea,
                                              final JoreImportContext ctx) {
        return FACTORY.createQuays_RelStructure()
                      .withQuayRefOrQuay(quays(stopArea, ctx));
    }

    private static Collection<Object> quays(final JrStopArea stopArea,
                                            final JoreImportContext ctx) {
        return ctx.stopsPerStopArea()
                  .getOrElse(stopArea.pk(), HashSet.empty())
                  .map(stop -> (Object) quay(stop))
                  .toJavaList();
    }

    private static Quay quay(final JrStop stop) {
        return FACTORY.createQuay()
                      .withId(stop.platform())
                      .withVersion("any")
                      .withName(FACTORY.createMultilingualString()
                                       .withValue(stop.name()))
                      .withCentroid(toSimplePoint(stop));
    }
}
