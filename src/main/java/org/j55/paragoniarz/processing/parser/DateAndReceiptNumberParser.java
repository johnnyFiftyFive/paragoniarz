package org.j55.paragoniarz.processing.parser;

import org.j55.paragoniarz.processing.Receipt;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.DOTALL;

/**
 * @author johnnyFiftyFive
 */
public class DateAndReceiptNumberParser extends Parser {

    private static final Pattern DATE = Pattern.compile("(\\d{4}[-\u2014\\s]\\d{2}[-\u2014\\s]\\d{2})");
    private static final Pattern RECEIPT_NUMBER = Pattern.compile("\\d{4,}");

    public DateAndReceiptNumberParser() {
        patterns = Collections.singletonList(Pattern.compile("(\\d{4}[-\u2014\\s]\\d{2}[-\u2014\\s]\\d{2})\\s*(nr\\s[uw]ydr.|[wW])?\\d+", DOTALL));
    }

    @Override
    public boolean parse(String text, Receipt receipt) {
        Optional<String> line = match(text);
        if (line.isPresent()) {
            Optional<String> date = match(DATE, line.get());
            Optional<String> number = line
                    .map(l -> l.replaceFirst(DATE.pattern(), ""))
                    .flatMap(n -> match(RECEIPT_NUMBER, n));
            date.map(s -> s.replaceAll("[\\s]", "-"))
                    .map(LocalDate::parse)
                    .ifPresent(receipt::setTransactionDate);
            number.ifPresent(receipt::setReceiptNumber);

            return date.isPresent() && number.isPresent();
        }
        return false;
    }
}
