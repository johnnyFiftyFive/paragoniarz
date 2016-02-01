package org.j55.paragoniarz;

import org.j55.paragoniarz.core.ClientException;
import org.j55.paragoniarz.db.ReceiptRepository;
import org.j55.paragoniarz.processing.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @author johnnyFiftyFive
 */

@Component
public class DbListener {
    private final static Logger logger = LoggerFactory.getLogger(DbListener.class);

    @Autowired
    private ReceiptRepository repo;

    @Autowired
    private LoteriaClient client;

    @Scheduled(fixedRate = 10000)
    public void run() {
        List<Receipt> unprocessed = repo.getUnprocessed();
        if (unprocessed.isEmpty()) {
            logger.info("[listener] NO TASKS TO PROCESS");
            return;
        }
        logger.info("[listener] got " + unprocessed.size() + " to process.");

        for (Receipt receipt : unprocessed) {
            try {
                client.pushReceipt(receipt);
                receipt.setStatus(Receipt.STATUS_DONE);
                Random r = new Random();
                int sleepTime = r.nextInt(30) * 1000 + 40000;
                logger.info("Receipt succesfully pushed. Sleeping for " + sleepTime + " seconds");
                Thread.sleep(sleepTime); // X seconds + 40 seconds
            } catch (ClientException | InterruptedException e) {
                logger.error("Error while sending receipt=" + receipt, e);
                receipt.setStatus(Receipt.STATUS_ERROR);
            } finally {
                repo.update(receipt);
            }
        }

    }

}
