package fi.hsl.transmodel.model.jore.mixin;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IHasPrimaryKey<K> {
    K pk();

    static <K, E extends IHasPrimaryKey<K>> Map<K, E> groupByPk(final List<E> vals) {
        return vals.groupBy(IHasPrimaryKey::pk)
                   .map((k, list) -> {
                       if (list.size() > 1) {
                           final Logger log = LoggerFactory.getLogger(IHasPrimaryKey.class);
                           log.warn("Multiple entities for key {}", k);
                       }
                       return Tuple.of(k, list.get(0));
                   });
    }
}
