package fi.hsl.transmodel.model.jore.style;

import fi.hsl.transmodel.model.jore.mapping.JoreColumn;
import fi.hsl.transmodel.model.jore.mapping.JoreForeignKey;
import fi.hsl.transmodel.model.jore.mapping.JoreTable;
import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
        // Don't require our abstract classes to start with the "Abstract"-prefix
        typeAbstract = "*",
        // Minimize generated code by:
        // - we don't need from methods and we construct the DTOs in one go
        strictBuilder = true,
        passAnnotations = {
                JoreTable.class,
                JoreColumn.class,
                JoreForeignKey.class
        }
)
public @interface JoreDtoStyle {
}
