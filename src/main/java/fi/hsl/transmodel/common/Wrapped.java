package fi.hsl.transmodel.common;

import org.immutables.value.Value;

// Immutables wrapper type, check https://immutables.github.io/immutable.html#wrapper-types
@Value.Style(
        // Detect names starting with underscore
        typeAbstract = "_*",
        // Generate without any suffix, just raw detected name
        typeImmutable = "*",
        // Put generated code under a specific package for easy filtering
        packageGenerated = "*.generated",
        // Make generated public, leave underscored as package private
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        // Seems unnecessary to have builder or superfluous copy method
        defaults = @Value.Immutable(builder = false,
                                    copy = false))
public @interface Wrapped {
}
