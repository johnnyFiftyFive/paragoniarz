package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.RawReceipt;
import org.springframework.stereotype.Repository;

/**
 * @author johnnyFiftyFive
 */
@Repository
public class RawReceiptRepositorySql2o extends CommonRepository<RawReceipt> implements RawReceiptRepository {

    private static String INSERT = "INSERT INTO RAW_RECEIPT(text) values(:text)";

    @Override public void save(RawReceipt receipt) {
        int id = save(receipt, INSERT);
        receipt.setId(id);
    }
}
