package io.profanesuite.maven.plugins.clean.sht;


/*
 * MIT License
 *
 * Copyright (c) 2022 ProfaneSuite
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import io.profanesuite.maven.plugins.clean.sht.io.ClasspathFiles;
import io.profanesuite.maven.plugins.clean.sht.io.RuntimeFiles;
import io.profanesuite.maven.plugins.clean.sht.model.CleanShtPomContext;
import io.profanesuite.maven.plugins.clean.sht.pom.Element;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * Runnable pom cleaner.
 * <p>
 * This is the task that provide the majority of the behaviour for the Mojo.
 */
public final class CleanShtPom implements Runnable {

    /**
     * The context to be used for cleaning the pom.
     */
    private final CleanShtPomContext internalContext;

    /**
     * Constructor for the task with the required context.
     *
     * @param context The context to be used for cleaning the pom.
     */
    public CleanShtPom(final CleanShtPomContext context) {
        internalContext = context;
    }

    @Override
    public void run() {
        final String excludeFragment = internalContext.getExclusions()
                .stream()
                .map(Element::getElementName)
                .map(elementName -> String.format("/project/%s", elementName))
                .reduce((left, right) -> String.format("%s | %s", left, right))
                .map(StringUtils::trimToNull)
                .map(excludes -> String.format("<xsl:template match=\"%s\"/>", excludes))
                .orElseThrow(() -> new RuntimeException("Nothing to exclude"));
        final Decorator<String> tokenDecorator = new TokenDecorator("<!-- replace here -->", excludeFragment);
        final Path xsltTemplate = ClasspathFiles.resourceFromClasspath("/template.xslt");
        final Transformer<String, String> transformer = new XsltTransformer(tokenDecorator.apply(RuntimeFiles.readString(xsltTemplate, internalContext.getEncoding())));

        Optional.of(internalContext)
                .map(CleanShtPomContext::getEffectivePomLocation)
                .map(path -> RuntimeFiles.readString(path, internalContext.getEncoding()))
                .map(transformer::apply)
                .ifPresentOrElse(this::writeProductionPom,
                        this::failPlugin);

    }

    private void writeProductionPom(final String productionPom) {
        RuntimeFiles.writeString(internalContext.getProductionPomLocation(), productionPom,
                internalContext.getEncoding(), CREATE, TRUNCATE_EXISTING);
    }

    private void failPlugin() {
        throw new RuntimeException("Failed to generate content to write production pom");
    }
}
