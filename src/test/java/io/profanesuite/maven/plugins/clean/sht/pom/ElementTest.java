package io.profanesuite.maven.plugins.clean.sht.pom;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class ElementTest {

    @Test
    void all_elements_are_included_or_excluded() {
        final Element[] configuredElements = Stream.concat(
                Stream.of(Element.DEFAULT_INCLUDE),
                Stream.of(Element.DEFAULT_EXCLUDE))
                .toArray(Element[]::new);

        assertThat(configuredElements).containsExactlyInAnyOrder(Element.values());
    }
}