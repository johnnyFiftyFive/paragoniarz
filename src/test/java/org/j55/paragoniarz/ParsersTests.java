package org.j55.paragoniarz;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author johnnyFiftyFive
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CashIdParserTest.class,
        DateAndPrintNumberParserTest.class,
        ParserSuiteTest.class,
        TaxNumberTest.class,
        TotalCostParserTest.class
})
public class ParsersTests {
}
