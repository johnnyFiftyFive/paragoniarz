package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.Receipt;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;

import java.util.List;

/**
 * @author johnnyFiftyFive
 */
@Repository
public class ReceiptRepositorySql2O extends CommonRepository<Receipt> implements ReceiptRepository {

    private static final String INSERT = "INSERT INTO RECEIPT(taxNumber, transactionDate, receiptNumber, cashId, total, sourceId) VALUES(" +
            ":taxNumber, :transactionDate, :receiptNumber, :cashId, :total, :sourceId);";

    private static final String SELECT_TO_PROCESS = "SELECT id, taxNumber, transactionDate, receiptNumber, cashId, total, status, sourceId FROM RECEIPT " +
            "WHERE status=" + Receipt.STATUS_READY;

    private static final String UPDATE_INPROGRESS = "UPDATE RECEIPT SET status = " + Receipt.STATUS_INPROGRESS + " WHERE id=:id AND status=" + Receipt.STATUS_READY;

    private static final String UPDATE = "UPDATE RECEIPT SET status=:status WHERE id=:id";

    @Override public void save(Receipt receipt) {
        Integer id = save(receipt, INSERT);
        receipt.setId(id);
    }

    @Override public List<Receipt> getUnprocessed() {
        try (Connection conn = db.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE)) {
            List<Receipt> unprocessed = conn.createQuery(SELECT_TO_PROCESS)
                    .executeAndFetch(Receipt.class);
            if (!unprocessed.isEmpty()) {
                Query update = conn.createQuery(UPDATE_INPROGRESS);
                for (Receipt receipt : unprocessed) {
                    receipt.setStatus(Receipt.STATUS_INPROGRESS);
                    update.addParameter("id", receipt.getId())
                            .addToBatch();
                }

                update.executeBatch();
                conn.commit();
            }
            return unprocessed;
        }

    }
}
