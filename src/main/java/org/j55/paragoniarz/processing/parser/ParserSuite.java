package org.j55.paragoniarz.processing.parser;

import org.j55.paragoniarz.processing.Receipt;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author johnnyFiftyFive
 */
@Component
public class ParserSuite {
    private static final Set<Parser> parsers;

    static {
        parsers = new HashSet<>();
        parsers.add(new DateAndReceiptNumberParser());
        parsers.add(new TotalCostParser());
        parsers.add(new TaxNumberParser());
        parsers.add(new CashIdParser());
    }

    public Receipt parse(String text) {
        Receipt receipt = new Receipt();
        final String input = text.replaceAll("\u2014", "-");
        parsers.stream().forEach(p -> p.parse(input, receipt));

        return receipt;
    }
}
