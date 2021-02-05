package fi.hsl.transmodel.xform;

import com.google.common.base.Joiner;
import fi.hsl.transmodel.model.jore.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.jore.mixin.IHasDuration;
import io.vavr.collection.List;
import org.rutebanken.netex.model.LocationStructure;
import org.rutebanken.netex.model.ObjectFactory;
import org.rutebanken.netex.model.SimplePoint_VersionStructure;
import org.rutebanken.netex.model.ValidBetween;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

public final class Util {

    private static final ObjectFactory FACTORY = new ObjectFactory();
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");

    private Util() {
    }

    public static SimplePoint_VersionStructure toSimplePoint(final IHasCoordinates entity) {
        return FACTORY.createSimplePoint_VersionStructure()
                      .withLocation(toLocation(entity));
    }

    public static LocationStructure toLocation(final IHasCoordinates entity) {
        return FACTORY.createLocationStructure()
                      .withLatitude(entity.latitude())
                      .withLongitude(entity.longitude());
    }

    public static ValidBetween validBetween(final IHasDuration withDuration) {
        return FACTORY.createValidBetween()
                      .withFromDate(LocalDateTime.ofInstant(withDuration.validFrom(), ZoneId.of("Europe/Helsinki")))
                      .withToDate(withDuration.validTo()
                                              .map(to -> LocalDateTime.ofInstant(to, ZoneId.of("Europe/Helsinki")))
                                              .orElse(null));
    }

    public static String sanitizeId(final List<Object> fields) {
        final String combined = Joiner.on(".").join(fields);
        return WHITESPACE_PATTERN
                .matcher(combined)
                .replaceAll("_");
    }

    public static String sanitizeId(final Object... fields) {
        return sanitizeId(List.of(fields));
    }
}
