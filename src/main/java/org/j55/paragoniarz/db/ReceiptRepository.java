package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.Receipt;

/**
 * @author johnnyFiftyFive
 */
public interface ReceiptRepository {

    void save(Receipt receipt);
}
