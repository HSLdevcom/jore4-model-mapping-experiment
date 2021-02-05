package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.TerminalId;
import fi.hsl.transmodel.model.jore.key.JrTerminalAreaPk;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.jore.mixin.IHasName;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasTerminalId;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_LIJ_TERMINAALIALUE)
public interface JrTerminalArea
        extends IHasPrimaryKey<JrTerminalAreaPk>,
                IHasTerminalId,
                IHasTransitType,
                IHasName,
                IHasCoordinates {

    @Value.Derived
    default JrTerminalAreaPk pk() {
        return JrTerminalAreaPk.of(terminalId());
    }

    static JrTerminalArea of(final TerminalId terminalId,
                             final TransitType transitType,
                             final String name,
                             final String latitude,
                             final String longitude) {
        return ImmutableJrTerminalArea.builder()
                                      .terminalId(terminalId)
                                      .transitType(transitType)
                                      .name(name)
                                      .latitude(IHasCoordinates.fromString(latitude))
                                      .longitude(IHasCoordinates.fromString(longitude))
                                      .build();
    }
}
