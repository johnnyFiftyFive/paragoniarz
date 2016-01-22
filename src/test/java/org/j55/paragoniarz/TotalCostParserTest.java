package org.j55.paragoniarz;

import org.j55.paragoniarz.processing.Receipt;
import org.j55.paragoniarz.processing.parser.Parser;
import org.j55.paragoniarz.processing.parser.TotalCostParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author johnnyFiftyFive
 */
public class TotalCostParserTest {
    private static final List<String> CORRECT = Arrays.asList("~ SUMA PLN 47,47", "~ SUMA  PLN 47.47", "SUMQ PLN 90.30", "Suma 203,12",
            "SUMA 43,23", "DSAD DSS 3,22", ": Karta kredytowa 279,99", "Płatność Karta płatnicza 4,35", "Płatność 47,47");
    private static final List<String> INCORRECT = Arrays.asList(",02", "32.3", "dsadsada as 3.21");

    @Test
    public void valid() {
        Parser parser = new TotalCostParser();
        Receipt receipt = new Receipt();
        for (String total : CORRECT) {
            assertTrue(parser.parse(total, receipt));
            assertNotNull(receipt.getTotal());
        }
    }

    @Test
    public void invalidTaxNumberTest() {
        Parser parser = new TotalCostParser();
        Receipt receipt = new Receipt();
        for (String total : INCORRECT) {
            assertFalse(parser.parse(total, receipt));
            assertNull(receipt.getTotal());
        }
    }
}
