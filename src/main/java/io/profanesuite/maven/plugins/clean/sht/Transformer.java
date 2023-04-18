package io.profanesuite.maven.plugins.clean.sht;

public interface Transformer<SourceT, TargetT> {

    TargetT apply(SourceT source);
}
