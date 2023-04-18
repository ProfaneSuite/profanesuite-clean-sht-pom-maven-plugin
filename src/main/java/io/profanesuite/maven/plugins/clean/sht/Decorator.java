package io.profanesuite.maven.plugins.clean.sht;

public interface Decorator<T> {

    T apply(T target);
}
