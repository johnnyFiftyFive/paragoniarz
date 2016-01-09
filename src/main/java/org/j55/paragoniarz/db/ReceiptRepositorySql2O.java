package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.Receipt;
import org.springframework.stereotype.Repository;

/**
 * @author johnnyFiftyFive
 */
@Repository
public class ReceiptRepositorySql2O extends CommonRepository implements ReceiptRepository {

    private static final String INSERT = "INSERT INTO RECEIPT(taxNumber, transactionDate, receiptNumber, cashId, total, sourceId) VALUES(" +
            ":taxNumber, :date, :receiptNumber, :cashId, :total, :sourceId);";

    @Override public void save(Receipt receipt) {
        Integer id = save(receipt, INSERT);
        receipt.setId(id);
    }
}
