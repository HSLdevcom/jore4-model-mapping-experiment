package fi.hsl.transmodel.model.jore.entity;

import fi.hsl.transmodel.model.jore.Tables;
import fi.hsl.transmodel.model.jore.field.RouteDirection;
import fi.hsl.transmodel.model.jore.field.TransitType;
import fi.hsl.transmodel.model.jore.field.generated.NodeId;
import fi.hsl.transmodel.model.jore.field.generated.RouteId;
import fi.hsl.transmodel.model.jore.field.generated.RouteLinkId;
import fi.hsl.transmodel.model.jore.key.JrLinkPk;
import fi.hsl.transmodel.model.jore.key.JrNodePk;
import fi.hsl.transmodel.model.jore.key.JrRoutePathLinkPk;
import fi.hsl.transmodel.model.jore.key.JrRoutePathPk;
import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import fi.hsl.transmodel.model.jore.mixin.IHasNodes;
import fi.hsl.transmodel.model.jore.mixin.IHasOrderNumber;
import fi.hsl.transmodel.model.jore.mixin.IHasPrimaryKey;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteDirection;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteId;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteLinkId;
import fi.hsl.transmodel.model.jore.mixin.IHasTransitType;
import fi.hsl.transmodel.model.jore.style.JoreDtoStyle;
import org.immutables.value.Value;

import java.time.LocalDateTime;

@Value.Immutable
@JoreDtoStyle
@JoreTable(name = Tables.JR_REITINLINKKI)
public interface JrRoutePathLink
        extends IHasPrimaryKey<JrRoutePathLinkPk>,
                IHasRouteId,
                IHasTransitType,
                IHasRouteDirection,
                IHasRouteLinkId,
                IHasOrderNumber,
                IHasNodes {

    @JoreColumn(name = "reljarjnro")
    int orderNumber();

    @JoreColumn(name = "suuvoimast")
    LocalDateTime validFrom();

    @Value.Derived
    default JrRoutePathLinkPk pk() {
        return JrRoutePathLinkPk.of(routeLinkId());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_REITINSUUNTA)
    default JrRoutePathPk fkRoutePath() {
        return JrRoutePathPk.of(routeId(),
                                direction(),
                                validFrom());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_LINKKI)
    default JrLinkPk fkLink() {
        return JrLinkPk.of(transitType(),
                           startNode(),
                           endNode());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_SOLMU)
    default JrNodePk fkStartNode() {
        return JrNodePk.of(startNode());
    }

    @Value.Derived
    @JoreForeignKey(targetTable = Tables.JR_SOLMU)
    default JrNodePk fkEndNode() {
        return JrNodePk.of(endNode());
    }

    static JrRoutePathLink of(final RouteId routeId,
                              final TransitType transitType,
                              final RouteDirection direction,
                              final RouteLinkId linkId,
                              final int order,
                              final NodeId startNode,
                              final NodeId endNode,
                              final LocalDateTime validFrom) {
        return ImmutableJrRoutePathLink.builder()
                                       .routeId(routeId)
                                       .transitType(transitType)
                                       .direction(direction)
                                       .routeLinkId(linkId)
                                       .startNode(startNode)
                                       .endNode(endNode)
                                       .validFrom(validFrom)
                                       .orderNumber(order)
                                       .build();
    }
}
