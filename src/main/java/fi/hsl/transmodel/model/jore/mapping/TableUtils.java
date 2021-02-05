package fi.hsl.transmodel.model.jore.mapping;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.util.Arrays;

public final class TableUtils {

    private static final Reflections REFLECTIONS = new Reflections("fi.hsl.transmodel.model.jore",
                                                                   new SubTypesScanner(false),
                                                                   new TypeAnnotationsScanner(),
                                                                   new FieldAnnotationsScanner());
    
    private TableUtils() {
    }

    public static Map<JoreTable, List<JoreColumn>> findTables() {
        return REFLECTIONS.getTypesAnnotatedWith(JoreTable.class)
                          .stream()
                          .filter(clazz -> !clazz.isInterface())
                          .map(clazz -> {
                              final JoreTable table = clazz.getAnnotation(JoreTable.class);
                              return Tuple.of(table, Arrays.stream(clazz.getDeclaredMethods())
                                                           .filter(method -> method.isAnnotationPresent(JoreColumn.class))
                                                           .map(method -> method.getAnnotation(JoreColumn.class))
                                                           .collect(List.collector()));
                          })
                          .collect(HashMap.collector());
    }
}
