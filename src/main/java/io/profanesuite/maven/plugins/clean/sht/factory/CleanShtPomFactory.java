package io.profanesuite.maven.plugins.clean.sht.factory;

import io.profanesuite.maven.plugins.clean.sht.CleanShtPom;
import io.profanesuite.maven.plugins.clean.sht.Factory;
import io.profanesuite.maven.plugins.clean.sht.TokenDecorator;
import io.profanesuite.maven.plugins.clean.sht.io.ClasspathFiles;
import io.profanesuite.maven.plugins.clean.sht.model.CleanShtPomContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CleanShtPomFactory implements Factory<CleanShtPom> {

    private final CleanShtPomContext context;

    public CleanShtPomFactory(final CleanShtPomContext context) {
        this.context = context;
    }

    @Override
    public CleanShtPom getInstance() {
//        Path xsltTemplate = ClasspathFiles.resourceFromClasspath();
//        final String generatedString = "";
//        new TokenDecorator(null , generatedString);
        return null;
    }

    private String pathToString(final Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read content", exception);
        }
    }
}
