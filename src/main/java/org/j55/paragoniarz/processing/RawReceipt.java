package org.j55.paragoniarz.processing;

/**
 * @author johnnyFiftyFive
 */
public class RawReceipt {
    private long id;
    private String text;

    public RawReceipt() {
    }

    public RawReceipt(String text) {
        this.text = text;
    }

    public RawReceipt(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
