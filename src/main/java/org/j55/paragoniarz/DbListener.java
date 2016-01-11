package org.j55.paragoniarz;

import org.j55.paragoniarz.core.ClientException;
import org.j55.paragoniarz.db.ReceiptRepository;
import org.j55.paragoniarz.db.ReceiptRepositorySql2O;
import org.j55.paragoniarz.processing.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.ReflectiveConstructorExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author johnnyFiftyFive
 */

@Service
public class DbListener {
    private final static Logger logger = LoggerFactory.getLogger(DbListener.class);

    public DbListener() {
        Thread t = new Thread(new Listener());
        t.setName(DbListener.class.getSimpleName());
        t.start();
        logger.info("DbListener started");
    }

  /*  public static void main(String[] args) {
        Thread t = new Thread(new Listener());
        t.setName(DbListener.class.getSimpleName());
        t.start();
    }*/

    private static class Listener implements Runnable {
        private final static ReceiptRepository repo = new ReceiptRepositorySql2O();
        private final static LoteriaClient client = new LoteriaClient();

        @Override public void run() {
            while (true) {
                List<Receipt> unprocessed = repo.getUnprocessed();
                if (unprocessed.isEmpty()) {
                    logger.info("[listener] NO TASKS TO PROCESS");
                    try {
                        Thread.sleep(10000);
                        continue;
                    } catch (InterruptedException e) {
                        logger.error("Exception caugth", e);
                    }
                }
                logger.info("[listener] got " + unprocessed.size() + " to process.");
                for (Receipt receipt : unprocessed) {
                    try {
                        client.pushReceipt(receipt);
                        logger.info("Receipt succesfully pushed");
                        receipt.setStatus(Receipt.STATUS_DONE);
                    } catch (ClientException e) {
                        logger.error("Error while sending receipt", e);
                        receipt.setStatus(Receipt.STATUS_ERROR);
                    }finally {
                        repo.update(receipt);
                    }
                }

            }
        }
    }
}
