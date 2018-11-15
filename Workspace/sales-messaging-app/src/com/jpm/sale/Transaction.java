package com.jpm.sale;

import java.util.Date;

public class Transaction {
    private double value;
    private boolean txnStatusAdjusted = Boolean.FALSE;
    private Date transactionDate;

    public Transaction() {
        this.txnStatusAdjusted = Boolean.FALSE;
        this.transactionDate = new Date();
    }

    public Transaction(double value) {
        this.value = value;
        this.txnStatusAdjusted = Boolean.FALSE;
        this.transactionDate = new Date();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Boolean getTransactionStatus() {
        return txnStatusAdjusted;
    }

    public void setTransactionStatus(Boolean txnStatusAdjusted) {
        this.txnStatusAdjusted = txnStatusAdjusted;
    }
}
