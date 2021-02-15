package fi.hsl.transmodel.xform;

import fi.hsl.transmodel.model.jore.entity.JrStopArea;
import fi.hsl.transmodel.model.jore.entity.JrTerminalArea;
import fi.hsl.transmodel.model.jore.field.RouteDirection;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import org.rutebanken.netex.model.DirectionTypeEnumeration;
import org.rutebanken.netex.model.StopTypeEnumeration;
import org.rutebanken.netex.model.VehicleModeEnumeration;

public final class Util {

    private Util() {
    }

    public static VehicleModeEnumeration toVehicleMode(final IHasTransitType withType) {
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

    public static StopTypeEnumeration toStopType(final JrStopArea stopArea) {
        switch (stopArea.transitType()) {
            case BUS:
                return StopTypeEnumeration.ONSTREET_BUS;
            case SUBWAY:
                return StopTypeEnumeration.METRO_STATION;
            case TRAM:
                return StopTypeEnumeration.ONSTREET_TRAM;
            case TRAIN:
                return StopTypeEnumeration.RAIL_STATION;
            case FERRY:
                return StopTypeEnumeration.FERRY_STOP;
        }
        return StopTypeEnumeration.OTHER;
    }

    public static StopTypeEnumeration toStopType(final JrTerminalArea terminal) {
        switch (terminal.transitType()) {
            case BUS:
                return StopTypeEnumeration.BUS_STATION;
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

    public static DirectionTypeEnumeration direction(final RouteDirection jrDirection) {
        // This is an arbitrary guess as RouteDirection doesn't actually specify the direction
        switch (jrDirection) {
            case DIRECTION_1:
                return DirectionTypeEnumeration.OUTBOUND;
            case DIRECTION_2:
                return DirectionTypeEnumeration.INBOUND;
        }
        return DirectionTypeEnumeration.INBOUND;
    }
}
