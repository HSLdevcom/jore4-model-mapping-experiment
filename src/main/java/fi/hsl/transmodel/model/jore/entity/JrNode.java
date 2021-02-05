package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.NodeType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.key.JrNodePk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasCoordinates;
import fi.hsl.transmodel.model.jore.mixin.IHasNodeId;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_SOLMU)
public interface JrNode
        extends IHasPrimaryKey<JrNodePk>,
                IHasNodeId,
                IHasCoordinates {

    @Value.Derived
    default JrNodePk pk() {
        return JrNodePk.of(nodeId());
    }

    @JoreColumn(name = "soltunnus")
    NodeType nodeType();

    static JrNode of(final NodeId nodeId,
                     final NodeType type,
                     final String latitude,
                     final String longitude) {
        return ImmutableJrNode.builder()
                              .nodeId(nodeId)
                              .nodeType(type)
                              .latitude(IHasCoordinates.fromString(latitude))
                              .longitude(IHasCoordinates.fromString(longitude))
                              .build();
    }
}
