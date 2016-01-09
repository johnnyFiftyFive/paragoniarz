package org.j55.paragoniarz.processing;

/**
 * @author johnnyFiftyFive
 */
public class RawReceipt {
    private Integer id;
    private String text;

    public RawReceipt() {
    }

    public RawReceipt(String text) {
        this.text = text;
    }

    public RawReceipt(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
