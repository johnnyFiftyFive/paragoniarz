package org.j55.paragoniarz.processing;

import java.time.LocalDate;

/**
 * Encja paragonu
 *
 * @author johnnyFiftyFive
 */
public class Receipt {
    private String taxNumber;
    private LocalDate date;
    private String receiptNumber;
    private String cashId;
    private String sum;

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

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return String.format("{ 'taxNumber': '%s',\t 'date': '%s',\t 'receiptNumber': '%s',\t 'cashId': '%s',\t 'sum': '%s' }", taxNumber, date, receiptNumber, cashId, sum);
    }
}
