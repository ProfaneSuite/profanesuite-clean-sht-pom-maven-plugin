package io.profanesuite.maven.plugins.clean.sht;

import io.profanesuite.maven.plugins.clean.sht.io.LocalFiles;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.approvaltests.Approvals.verify;

class XsltTransformerTest {

    private final Path xslt = LocalFiles.removeParentXsltFromClasspath();
    private final Path xml = LocalFiles.validEffectivePomFromClasspath();

    @Test
    void will_Transform_xml() throws IOException {

        XsltTransformer sut = new XsltTransformer(Files.readString(xslt, UTF_8));

        String xmlTransformed = sut.apply(Files.readString(xml, UTF_8));

        verify(xmlTransformed, new Options().forFile().withExtension(".xml"));
    }
}