package org.j55.paragoniarz.processing.parser;

import org.j55.paragoniarz.processing.Receipt;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author johnnyFiftyFive
 */
public abstract class Parser {

    protected List<Pattern> patterns;

    public abstract boolean parse(String text, Receipt receipt);

    protected Optional<String> match(String text) {
        for (Pattern pattern : patterns) {
            Optional<String> match = match(pattern, text);
            if (match.isPresent()) {
                return match;
            }
        }

        return Optional.empty();
    }

    protected Optional<String> match(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group());
        }
        return Optional.empty();
    }
}
