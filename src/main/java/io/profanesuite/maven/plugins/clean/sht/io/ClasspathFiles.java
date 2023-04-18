package io.profanesuite.maven.plugins.clean.sht.io;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public final class ClasspathFiles
{

    private ClasspathFiles(){};

    public static Path resourceFromClasspath (final String classpathResource) {
        try {
            final URL resource = ClasspathFiles.class.getResource(classpathResource);
            return Path.of(Objects.requireNonNull(resource).toURI());
        }catch (URISyntaxException exception) {
            throw new RuntimeException(String.format("Failed to load resource [%s]", classpathResource), exception);
        }
    }
}
