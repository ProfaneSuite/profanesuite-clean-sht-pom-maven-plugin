package io.profanesuite.maven.plugins.clean.sht;

import org.apache.commons.lang3.StringUtils;

public class TokenDecorator implements Decorator<String> {

    private final String token;
    private final String replacement;

    public TokenDecorator(final String token, final String replacement) {
        this.token = token;
        this.replacement = replacement;
    }

    @Override
    public String apply(String target) {
        return StringUtils.replace(target, token, replacement);
    }
}
