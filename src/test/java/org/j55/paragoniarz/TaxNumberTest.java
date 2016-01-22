package org.j55.paragoniarz;

import org.j55.paragoniarz.processing.Receipt;
import org.j55.paragoniarz.processing.parser.Parser;
import org.j55.paragoniarz.processing.parser.TaxNumberParser;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author johnnyFiftyFive
 */
public class TaxNumberTest {
    private static final List<String> WRONG = Arrays.asList("NIP 622-131-82-27", "AAD 428-131-22-27", "653-934-963-14", "NIP7378514832", "NIP 2378513832", "7137293301", "7922199299");
    private static final List<String> CORRECT = Arrays.asList("RESERVED 1912122\n" +
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
            "[ Nr transakcji 1912122*4*15*1*1087", "648—233—16-77", "NIP 628-131-82-27", "AAD 628-131-82-27", "654-934-93-14", "NIP2378514832", "NIP 2378514832", "7137599901", "7922199999");


    @Test
    public void validTaxNumberTest() {
        Parser parser = new TaxNumberParser();
        Receipt receipt = new Receipt();
        for (String taxNo : CORRECT) {
            assertTrue(parser.parse(taxNo, receipt));
            assertNotNull(receipt.getTaxNumber());
        }
    }

    @Test
    public void invalidTaxNumberTest() {
        Parser parser = new TaxNumberParser();
        Receipt receipt = new Receipt();
        for (String taxNo : WRONG) {
            assertFalse(parser.parse(taxNo, receipt));
            assertNull(receipt.getTaxNumber());

        }
    }
}
