package org.j55.paragoniarz.db;

import org.j55.paragoniarz.processing.RawReceipt;

/**
 * @author johnnyFiftyFive
 */
public interface RawReceiptRepository {

    void save(RawReceipt receipt);
}
