package org.j55.paragoniarz.processing;

import java.time.LocalDate;

/**
 * Encja paragonu
 *
 * @author johnnyFiftyFive
 */
public class Receipt {
    private Integer id;
    private String taxNumber;
    private LocalDate date;
    private String receiptNumber;
    private String cashId;
    private String total;
    private Integer sourceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber.replaceAll("-|\\s", "");
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return String.format("{ 'taxNumber': '%s',\t 'date': '%s',\t 'receiptNumber': '%s',\t 'cashId': '%s',\t 'total': '%s' }", taxNumber, date, receiptNumber, cashId, total);
    }
}
