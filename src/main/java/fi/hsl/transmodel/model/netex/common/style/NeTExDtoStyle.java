package fi.hsl.transmodel.model.netex.common.style;

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
        strictBuilder = true
)
public @interface NeTExDtoStyle {
}
