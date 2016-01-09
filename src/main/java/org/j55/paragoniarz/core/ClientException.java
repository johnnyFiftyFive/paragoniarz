package org.j55.paragoniarz.core;

/**
 * @author johnnyFiftyFive
 */
public class ClientException extends Exception {
    public ClientException(String msg) {
        super(msg);
    }

    public ClientException(String msg, Exception e) {
        super(msg, e);
    }
}
