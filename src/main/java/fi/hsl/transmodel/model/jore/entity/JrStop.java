package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.field.generated.StopAreaId;
import fi.hsl.transmodel.model.jore.key.JrStopAreaPk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.jore.mixin.IHasName;
import fi.hsl.transmodel.model.jore.mixin.IHasNodeId;
import fi.hsl.transmodel.model.jore.mixin.IHasStopAreaId;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_PYSAKKI)
public interface JrStop
        extends IHasNodeId,
                IHasStopAreaId,
                IHasName,
                IHasCoordinates {

    @JoreColumn(name = "pyslaituri",
                example = "113A")
    String platform();

    @JoreColumn(name = "pysosoite")
    String address();

    @JoreColumn(name = "postinro",
                example = "33720")
    String postalNumber();

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_LIJ_PYSAKKIALUE)
    default JrStopAreaPk fkStopArea() {
        return JrStopAreaPk.of(stopAreaId());
    }

    static JrStop of(final StopAreaId stopAreaId,
                     final NodeId nodeId,
                     final String name,
                     final String platform,
                     final String address,
                     final String postalNumber,
                     final String latitude,
                     final String longitude) {
        return ImmutableJrStop.builder()
                              .stopAreaId(stopAreaId)
                              .nodeId(nodeId)
                              .name(name)
                              .platform(platform)
                              .address(address)
                              .postalNumber(postalNumber)
                              .latitude(IHasCoordinates.fromString(latitude))
                              .longitude(IHasCoordinates.fromString(longitude))
                              .build();
    }
}
