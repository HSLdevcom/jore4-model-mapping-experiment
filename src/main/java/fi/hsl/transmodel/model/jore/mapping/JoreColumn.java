package fi.hsl.transmodel.model.jore.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface JoreColumn {
    /**
     * @return Name of the column
     */
    String name() default "";

    /**
     * @return Whether or not this column may be nullable
     */
    boolean nullable() default false;

    /**
     * @return An example value for this column
     */
    String example() default "";
}
