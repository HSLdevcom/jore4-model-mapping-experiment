package fi.hsl.transmodel.model.netex.common.mixin;

import com.google.common.base.Preconditions;
import org.immutables.value.Value;
import org.rutebanken.netex.model.LocationStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.SimplePoint_VersionStructure;

import java.math.BigDecimal;

public interface IHasCoordinates {

    BigDecimal latitude();

    BigDecimal longitude();

    @Value.Check
    default void checkCoordinates() {
        Preconditions.checkState(latitude().abs().doubleValue() <= 90.0D);
        Preconditions.checkState(longitude().abs().doubleValue() <= 180.0D);
    }

    default SimplePoint_VersionStructure simplePointXml() {
        return new ObjectFactory()
                .createSimplePoint_VersionStructure()
                .withLocation(locationXml());
    }

    default LocationStructure locationXml() {
        return new ObjectFactory()
                .createLocationStructure()
                .withLatitude(latitude())
                .withLongitude(longitude());
    }
}
