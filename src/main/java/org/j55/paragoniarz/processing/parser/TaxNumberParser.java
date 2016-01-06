package org.j55.paragoniarz.processing.parser;

import org.j55.paragoniarz.processing.Receipt;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author johnnyFiftyFive
 */
public class TaxNumberParser extends Parser {
    private static final int[] wages = {6, 5, 7, 2, 3, 4, 5, 6, 7};

    public TaxNumberParser() {
        patterns = Collections.singletonList(Pattern.compile(("(\\d{10}|\\d{2,3}[\u2014-]\\d{2,3}[\u2014-]\\d{2,3}[\u2014-]\\d{2,3})"), Pattern.DOTALL));
    }

    @Override
    public boolean parse(String text, Receipt receipt) {
        text = text.replaceAll("—", "-");
        Optional<String> nip = match(text).filter(this::validate);
        nip.ifPresent(receipt::setTaxNumber);
        return nip.isPresent();
    }

    private boolean validate(String nip) {
        nip = nip.replaceAll("-", "");
        if (nip.length() > 10) {
            return false;
        }

        int sum = 0;
        String[] tokens = nip.split("");
        for (int i = 0; i < wages.length; ++i) {
            sum += wages[i] * Integer.parseInt(tokens[i]);
        }

        return sum % 11 == Integer.parseInt(tokens[9]);
    }
}
