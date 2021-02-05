package fi.hsl.transmodel.model.jore.key;

import fi.hsl.transmodel.model.jore.field.generated.RouteLinkId;
import fi.hsl.transmodel.model.jore.mixin.IHasRouteLinkId;
import org.immutables.value.Value;

@Value.Immutable
public interface JrRoutePathLinkPk extends IHasRouteLinkId {
    static JrRoutePathLinkPk of(final RouteLinkId routeLinkId) {
        return ImmutableJrRoutePathLinkPk.builder()
                                         .routeLinkId(routeLinkId)
                                         .build();
    }
}
