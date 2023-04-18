package io.profanesuite.maven.plugins.clean.sht.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class RuntimeFiles {

    private RuntimeFiles() {}

    public static String readString(final Path resource, final Charset encoding) {
        try {
            return Files.readString(resource, encoding);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to load content", exception);
        }
    }

    public static Path writeString(final Path resource, final String content, final Charset encoding, final OpenOption... options) {
        try {
            Path parent = createDirectories(resource.getParent());

            return Files.writeString(parent.resolve(resource.getFileName()), content, encoding, options);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to write content", exception);
        }
    }

    public static Path createDirectories(final Path directory) {
        try {
            return Files.createDirectories(directory);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to create directories", exception);
        }
    }
}
