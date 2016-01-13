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

/**
 * @author johnnyFiftyFive
 */

@Component
public class DbListener {
    private final static Logger logger = LoggerFactory.getLogger(DbListener.class);
    private final static LoteriaClient client = new LoteriaClient();

    @Autowired
    private ReceiptRepository repo;

    @Scheduled(fixedRate = 5000)
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
                logger.info("Receipt succesfully pushed");
                receipt.setStatus(Receipt.STATUS_DONE);
            } catch (ClientException e) {
                logger.error("Error while sending receipt", e);
                receipt.setStatus(Receipt.STATUS_ERROR);
            } finally {
                repo.update(receipt);
            }
        }

    }

}
