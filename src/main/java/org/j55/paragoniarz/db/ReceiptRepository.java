package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.Receipt;

import java.util.List;

/**
 * @author johnnyFiftyFive
 */
public interface ReceiptRepository {

    void save(Receipt receipt);

    List<Receipt> getUnprocessed();
}
