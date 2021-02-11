package fi.hsl.transmodel.model.netex.generic;

import fi.hsl.transmodel.model.netex.common.mixin.IHasName;
import io.vavr.collection.List;
import org.immutables.value.Value;

import java.math.BigDecimal;

public interface Link
        extends VersionedEntity,
                IHasName {

    BigDecimal distance();

    @Value.Default
    default List<PointOnLink> passingThrough() {
        return List.empty();
    }
}
