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

import com.google.common.jimfs.Jimfs;
import io.profanesuite.maven.plugins.clean.sht.model.CleanShtPomContext;
import io.profanesuite.maven.plugins.clean.sht.model.CleanShtPomContext.CleanShtPomContextBuilder;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.profanesuite.maven.plugins.clean.sht.io.LocalFiles.*;

class CleanShtPomTest {

    private final FileSystem fileSystem = Jimfs.newFileSystem();
    private final Path inMemoryRoot = getPathToRoot(fileSystem);
    private final Path validEffectivePom = copyFile(validEffectivePomFromClasspath(), inMemoryRoot.resolve("example").resolve("pom.xml"));

    private final Path expectedProductionPom = inMemoryRoot.resolve("expected").resolve("pom.xml");

    private final CleanShtPomContextBuilder contextBuilder = new CleanShtPomContextBuilder()
            .withEffectivePomLocation(validEffectivePom)
                .withProductionPomLocation(expectedProductionPom);

    @Test
    void will_generate_production_pom() throws IOException {
        final CleanShtPomContext context = contextBuilder
                .build();

        verifyPomWithContext(context);
    }

    @Test
    void will_remove_properties_from_production_pom() throws IOException {
        final CleanShtPomContext context = contextBuilder
                .withExclusions("PropertieS")
                .build();

        verifyPomWithContext(context);
     }

    @Test
    void will_add_parent_to_production_pom() throws IOException {
        final CleanShtPomContext context = contextBuilder
                .withInclusions("paRent")
                .build();

        verifyPomWithContext(context);
    }


    private void verifyPomWithContext(final CleanShtPomContext context) throws IOException {
        new CleanShtPom(context).run();

        final Path productionPom = context.getProductionPomLocation();
        Approvals.verify(Files.readString(productionPom, StandardCharsets.UTF_8), new Options().forFile().withExtension(".xml"));
    }

}