package org.j55.paragoniarz;

import org.j55.paragoniarz.core.ClientException;
import org.j55.paragoniarz.processing.Receipt;

/**
 * Created by b0640423 on 2016-01-28.
 */
public interface LoteriaClient {
    void pushReceipt(Receipt receipt) throws ClientException;
}
