package io.profanesuite.maven.plugins.clean.sht.model;

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

import io.profanesuite.maven.plugins.clean.sht.Builder;
import io.profanesuite.maven.plugins.clean.sht.model.LocationContext.LocationContextBuilder;
import io.profanesuite.maven.plugins.clean.sht.pom.Element;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

/**
 * Context which describes what to use and what to produce.
 */
public class CleanShtPomContext
{
    private LocationContext locationContext;

    private Charset encoding;

    private final Set<Element> inclusions = new HashSet<>();

    private final Set<Element> exclusions = new HashSet<>();

    private CleanShtPomContext() {
    }

    /**
     * Productionised pom location.
     *
     * @return pom location represented as a path
     */
    public Path getProductionPomLocation()
    {
        return locationContext.getProductionPomLocation();
    }

    /**
     * Effective pom location.
     *
     * @return pom location represented as a path
     */
    public Path getEffectivePomLocation()
    {
        return locationContext.getEffectivePomLocation();
    }

    public Charset getEncoding() {
        return encoding;
    }

    private void setEncoding(final Charset encoding) {
        this.encoding = encoding;
    }

    public List<Element> getInclusions() {
        return List.copyOf(inclusions);
    }

    public List<Element> getExclusions() {
        return List.copyOf(exclusions);
    }

    private void setLocationContext(final LocationContext locationContext) {
        this.locationContext = locationContext;
    }

    private void addExclusions(final String[] exclusions) {
        addExclusions(Stream.of(exclusions)
                .map(this::findElement)
                .toArray(Element[]::new));
    }

    private void addExclusions(final Element[] exclusions) {
        this.exclusions.addAll(List.of(exclusions));
    }

    private Element findElement(String elementName) {
        return Stream.of(Element.values())
                .filter(element -> element.getElementName().equalsIgnoreCase(elementName))
                .findAny().orElseThrow(() -> new RuntimeException("Could not find element"));
    }


    private void addInclusions(final String[] inclusions) {
        addInclusions(Stream.of(inclusions)
                .map(this::findElement)
                .toArray(Element[]::new));
    }

    private void addInclusions(final Element[] inclusions) {
        this.inclusions.addAll(List.of(inclusions));
    }

    /**
     * Builder used to create and instance of the context.
     */
    public static class CleanShtPomContextBuilder implements Builder<CleanShtPomContext>
    {

        private CleanShtPomContext instance = new CleanShtPomContext();
        private final LocationContextBuilder locationContextBuilder = new LocationContextBuilder();

        @Override
        public CleanShtPomContext build() {
            final CleanShtPomContext result = instance;
            instance = null;

            final Element[] exclusions = defaultExclusions(result)
                    .orElseGet(() -> calculatedExclusions(result));

            final Element[] inclusions =
            Stream.of(Element.values())
                    .filter(element -> !ArrayUtils.contains(exclusions, element))
                            .toArray(Element[]::new);

            result.setEncoding(Optional.ofNullable(result.getEncoding())
                    .or(() -> Optional.of(toCharset(System.getProperty( "file.encoding" ))))
                    .orElse(StandardCharsets.UTF_8));

            result.addExclusions(exclusions);
            result.addInclusions(inclusions);

            result.setLocationContext(locationContextBuilder.build());
            return result;
        }

        private Element[] calculatedExclusions(CleanShtPomContext result) {
            final Element[] inclusions = result.getInclusions().toArray(Element[]::new);
            final Stream<Element> defaultExclusionMinusUserInclusions = of(Element.DEFAULT_EXCLUDE)
                    .filter(element -> !ArrayUtils.contains(inclusions, element));

            return Stream.concat(defaultExclusionMinusUserInclusions, result.getExclusions().stream())
                    .toArray(Element[]::new);
        }

        private Optional<Element[]> defaultExclusions(CleanShtPomContext context) {
            return Optional.of(context)
                    .filter(c -> c.getExclusions().isEmpty())
                    .filter(c -> c.getInclusions().isEmpty())
                    .map(c -> Element.DEFAULT_EXCLUDE);
        }

        /**
         *
         * @param effectivePomLocation Where the production pom will be located.
         * @return The context builder
         */
        public CleanShtPomContextBuilder withEffectivePomLocation(final String effectivePomLocation) {
            locationContextBuilder.withEffectivePomLocation(effectivePomLocation);
            return this;
        }

        public CleanShtPomContextBuilder withEffectivePomLocation(final Path effectivePomLocation) {
            locationContextBuilder.withEffectivePomLocation(effectivePomLocation);
            return this;
        }

        /**
         *
         * @param productionPomLocation Where the production pom will be located.
         * @return The context builder
         */
        public CleanShtPomContextBuilder withProductionPomLocation(final String productionPomLocation) {
            locationContextBuilder.withProductionPomLocation(productionPomLocation);
            return this;
        }

        public CleanShtPomContextBuilder withProductionPomLocation(final Path productionPomLocation) {
            locationContextBuilder.withProductionPomLocation(productionPomLocation);
            return this;
        }

        public CleanShtPomContextBuilder withExclusions(String... exclusions) {
            instance.addExclusions(exclusions);
            return this;
        }

        public CleanShtPomContextBuilder withInclusions(String... inclusions) {
            instance.addInclusions(inclusions);
            return this;
        }

        public CleanShtPomContextBuilder withEncoding(final String encoding) {
            instance.setEncoding(toCharset(encoding));
            return this;
        }

        private Charset toCharset(final String encoding) {
            return Optional.ofNullable(encoding)
                    .filter(Charset::isSupported)
                    .map(Charset::forName)
                    .orElseThrow(
                            () -> new UnsupportedCharsetException(encoding));
        }

    }
}
