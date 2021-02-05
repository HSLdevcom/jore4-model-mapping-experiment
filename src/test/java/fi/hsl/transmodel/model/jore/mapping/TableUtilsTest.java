package fi.hsl.transmodel.model.jore.mapping;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(TableUtilsTest.class);

    @Test
    public void printTables() {
        final Map<JoreTable, List<JoreColumn>> tables = TableUtils.findTables();
        tables.forEach((table, columns) -> {
            LOG.info("Table: {}", table.name());
            columns.forEach(column -> LOG.info("  Column: {}, example: '{}'", column.name(), column.example()));
        });
    }
}
