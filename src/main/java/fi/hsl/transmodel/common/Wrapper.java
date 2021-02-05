package fi.hsl.transmodel.common;

import org.immutables.value.Value;

public abstract class Wrapper<T> {
    @Value.Parameter
    public abstract T value();

    @Override
    public String toString() {
        return value().toString();
    }
}
