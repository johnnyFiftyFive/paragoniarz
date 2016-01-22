package org.j55.paragoniarz;

import org.j55.paragoniarz.processing.Receipt;
import org.j55.paragoniarz.processing.parser.CashIdParser;
import org.j55.paragoniarz.processing.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author johnnyFiftyFive
 */
public class CashIdParserTest {

    private static final List<String> CORRECT = Arrays.asList("BGI 13254321", "HYW 12345678", "BAS1234567890");
    private static final List<String> INCORRECT = Arrays.asList("881 13254321", "HYW 123456782323", "HUJ 4545");

    @Test
    public void valid() {
        Parser parser = new CashIdParser();
        Receipt receipt = new Receipt();
        for (String id : CORRECT) {
            assertTrue(parser.parse(id, receipt));
            assertNotNull(receipt.getCashId());
        }
    }

    @Test
    public void invalidTaxNumberTest() {
        Parser parser = new CashIdParser();
        Receipt receipt = new Receipt();
        for (String id : INCORRECT) {
            assertFalse(parser.parse(id, receipt));
            assertNull(receipt.getCashId());
        }
    }
}
