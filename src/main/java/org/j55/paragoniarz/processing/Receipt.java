package org.j55.paragoniarz.processing;

import java.time.LocalDate;

/**
 * Encja paragonu
 *
 * @author johnnyFiftyFive
 */
public class Receipt {
    public static final int STATUS_NEW = 0;
    public static final int STATUS_READY = 10;
    public static final int STATUS_INPROGRESS = 50;
    public static final int STATUS_ERROR = 65;
    public static final int STATUS_DONE = 99;

    private Integer id;
    private String taxNumber;
    private LocalDate transactionDate;
    private String receiptNumber;
    private String cashId;
    private String total;
    private Integer status;
    private Integer sourceId;
    private String internalNumber;

    public Receipt(){
        this.status = 0;
    }

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

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
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
        this.cashId = cashId.replaceAll("\\s", "");
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    @Override
    public String toString() {
        return String.format("{ 'taxNumber': '%s',\t 'transactionDate': '%s',\t 'receiptNumber': '%s',\t 'cashId': '%s',\t 'total': '%s' }", taxNumber, transactionDate, receiptNumber, cashId, total);
    }
}
