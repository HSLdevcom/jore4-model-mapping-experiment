package fi.hsl.transmodel.model.jore.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface JoreForeignKey {
    /**
     * @return Name of the referred table
     */
    String targetTable() default "";
}
