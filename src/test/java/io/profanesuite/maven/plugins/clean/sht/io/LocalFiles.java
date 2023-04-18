package io.profanesuite.maven.plugins.clean.sht.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static io.profanesuite.maven.plugins.clean.sht.io.ClasspathFiles.resourceFromClasspath;

public final class LocalFiles {

    private LocalFiles (){};

    public static Path getPathToRoot(FileSystem fileSystem) {
        try {
            Path path = fileSystem.getPath("test-root");
            Files.createDirectory(path);
            return path;
        } catch (IOException exception) {
            throw new RuntimeException("Failed to initialise test root on file system", exception);
        }
    }

    public static Path copyFile(Path source, Path target) {
        try {
            Path parent = Files.createDirectories(target.getParent());
            Path targetPath = parent.resolve(target.getFileName());
            return Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Path validEffectivePomFromClasspath() {
        return resourceFromClasspath("/example/effective/valid.xml");
    }

    public static Path templateXsltFromClasspath() {
        return resourceFromClasspath("/template.xslt");
    }

    public static Path removeParentXsltFromClasspath() {
        return resourceFromClasspath("/example/xslt/remove-parent.xslt");
    }


}
