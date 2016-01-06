package org.j55.paragoniarz.processing.parser;

import org.j55.paragoniarz.processing.Receipt;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;

/**
 * @author johnnyFiftyFive
 */
public class TotalCostParser extends Parser {
    private static final Pattern TOTAL = Pattern.compile("\\d*[,\\.]\\d{2}");

    public TotalCostParser() {
        patterns = Arrays.asList(Pattern.compile("SUMA\\s*(PLN)?\\s*\\d*[,\\.]\\d{2}", DOTALL),
                Pattern.compile("[A-Z]{4}\\s*[A-Z]{0,3}?\\s*\\d*[,\\.]\\d{2}", DOTALL),
                Pattern.compile("Suma\\s*(PLN)?\\s*\\d*[,\\.]\\d{2}", DOTALL),
                Pattern.compile("Płatność.*\\s\\d*[,\\.]\\d{2}", DOTALL),
                Pattern.compile("Karta (kredytowa|płatnicza).*\\s\\d*[\\,\\.]\\d{2}", DOTALL));
    }

    @Override
    public boolean parse(String text, Receipt receipt) {
        Optional<String> total = match(text).flatMap(t -> match(TOTAL, t));
        total.ifPresent(receipt::setSum);

        return total.isPresent();
    }
}
