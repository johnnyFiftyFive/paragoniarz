package org.j55.paragoniarz;


import org.j55.paragoniarz.processing.Receipt;
import org.j55.paragoniarz.processing.parser.DateAndReceiptNumberParser;
import org.j55.paragoniarz.processing.parser.Parser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DateAndPrintNumberParserTest {
    private static final List<String> CORRECT = Arrays.asList("2015-12-10 nr wydr.027608", "2015-12 10 027608", "2015-12 10    W027608", "2015-12 10 w03213127608");
    private static final List<String> WRONG = Arrays.asList("2015-12-10 nr wydr.027", "2015-12-10 nr wydr.gfdsa", "das32cds0f aoda jda lkjda");

    @Test
    public void testValid() {
        Parser parser = new DateAndReceiptNumberParser();
        Receipt receipt = new Receipt();
        for (String taxNo : CORRECT) {
            assertTrue(parser.parse(taxNo, receipt));
            assertNotNull(receipt.getTransactionDate());
            assertNotNull(receipt.getReceiptNumber());
        }
    }

    @Test
    public void testInvalid() {
        Parser parser = new DateAndReceiptNumberParser();
        Receipt receipt = new Receipt();
        for (String taxNo : WRONG) {
            assertFalse(parser.parse(taxNo, receipt));
            assertNull(receipt.getReceiptNumber());
        }
    }
}