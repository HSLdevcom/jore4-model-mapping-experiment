package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.StopAreaId;
import fi.hsl.transmodel.model.jore.field.generated.TerminalId;
import fi.hsl.transmodel.model.jore.key.JrStopAreaPk;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.jore.mixin.IHasName;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasStopAreaId;
import fi.hsl.transmodel.model.jore.mixin.IHasTerminalIdFk;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_LIJ_PYSAKKIALUE)
public interface JrStopArea
        extends IHasPrimaryKey<JrStopAreaPk>,
                IHasStopAreaId,
                IHasTerminalIdFk,
                IHasTransitType,
                IHasName,
                IHasCoordinates {

    @Value.Derived
    default JrStopAreaPk pk() {
        return JrStopAreaPk.of(stopAreaId());
    }

    static JrStopArea of(final StopAreaId stopAreaId,
                         final Optional<TerminalId> terminalId,
                         final TransitType transitType,
                         final String name,
                         final String latitude,
                         final String longitude) {
        return ImmutableJrStopArea.builder()
                                  .stopAreaId(stopAreaId)
                                  .terminalId(terminalId)
                                  .transitType(transitType)
                                  .name(name)
                                  .latitude(IHasCoordinates.fromString(latitude))
                                  .longitude(IHasCoordinates.fromString(longitude))
                                  .build();
    }
}
