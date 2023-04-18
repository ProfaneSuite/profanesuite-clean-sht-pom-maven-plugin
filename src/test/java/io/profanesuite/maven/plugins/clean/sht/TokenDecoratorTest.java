package io.profanesuite.maven.plugins.clean.sht;

import io.profanesuite.maven.plugins.clean.sht.io.LocalFiles;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

class TokenDecoratorTest {

    private final Path template = LocalFiles.templateXsltFromClasspath();

    @Test
    void will_replace_template() throws IOException {
        final Decorator<String> sut = new TokenDecorator("<!-- replace here -->", "<xsl:template />");

        final String actual = sut.apply(Files.readString(template, StandardCharsets.UTF_8));

        Approvals.verify(actual, new Options().forFile().withExtension(".xml"));
    }
}