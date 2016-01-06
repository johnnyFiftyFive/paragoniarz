package org.j55.paragoniarz.processing.parser;

import org.j55.paragoniarz.processing.Receipt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author johnnyFiftyFive
 */
public class CashIdParser extends Parser {

    public CashIdParser() {
        patterns = Collections.singletonList(Pattern.compile("(?!NIP)[A-Z]{3}\\s?\\d{8,10}(?!\\d)"));
    }

    @Override
    public boolean parse(String text, Receipt receipt) {
        Optional<String> id = match(text);
        id.ifPresent(receipt::setCashId);
        return id.isPresent();
    }
}
