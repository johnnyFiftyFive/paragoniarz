package org.j55.paragoniarz;

import org.j55.paragoniarz.processing.Receipt;
import org.j55.paragoniarz.processing.parser.ParserSuite;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * @author johnnyFiftyFive
 */
public class ParserSuiteTest {
    private static final String RECEIPT = "x\n" +
            "RESERVED 1912122\n" +
            "' LPP S.A. 80 769 Gdańsk, u]. Łąkowa 39/44\n" +
            "43-302 Katowice; A1. Piotra Skargi 6\n" +
            "NIP 583-10-14-898\n" +
            "2015-10—27 nr wydr.001177\n" +
            "PARAGON FISKALNY\n" +
            "L8510—99X—XL KURTKA MĘSKA _\n" +
            "1 * 279,99 279,99 A\n" +
            "» Sprzed. opod. PTU A 279,99\n" +
            "Kwota A 23,00% 52,36\n" +
            "Podatek PTU 52,36\n" +
            "000057 #4 229481995 50:34\n" +
            "* 5AGDL-L61DK-AJUWO-ISFIZ—IMBQA\n" +
            "ZE? BGI 14149559\n" +
            "1 Karta kredytowa 279,99\n" +
            ";RAZEH PLN 279,99\n" +
            "[ Nr transakcji 1912122*4*15*1*1087";

    @Test
    public void parseTest() {
        ParserSuite parsers = new ParserSuite();
        Receipt receipt = parsers.parse(RECEIPT);
        assertEquals(receipt.getTransactionDate(), LocalDate.parse("2015-10-27"));
        assertEquals(receipt.getReceiptNumber(), "001177");
        assertEquals(receipt.getTaxNumber(), "5831014898");
        assertEquals(receipt.getTotal(), "279,99");
        assertEquals(receipt.getCashId(), "BGI14149559");
    }
}
