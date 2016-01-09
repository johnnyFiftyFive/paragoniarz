package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.Receipt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author johnnyFiftyFive
 */
@Repository
public class ReceiptRepositorySql2O extends CommonRepository<Receipt> implements ReceiptRepository {

    private static final String INSERT = "INSERT INTO RECEIPT(taxNumber, transactionDate, receiptNumber, cashId, total, sourceId) VALUES(" +
            ":taxNumber, :transactionDate, :receiptNumber, :cashId, :total, :sourceId);";

    private static String SELECT_TO_PROCESS = "SELECT id, taxNumber, transactionDate, receiptNumber, cashId, total, status, sourceId FROM RECEIPT " +
            "WHERE status=10";

    @Override public void save(Receipt receipt) {
        Integer id = save(receipt, INSERT);
        receipt.setId(id);
    }

    @Override public List<Receipt> getUnprocessed() {
        return select(SELECT_TO_PROCESS, Receipt.class);
    }


}
