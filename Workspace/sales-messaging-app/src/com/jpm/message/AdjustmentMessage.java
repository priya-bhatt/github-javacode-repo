package com.jpm.message;

public class AdjustmentMessage extends Message {
    String operationType;

    public AdjustmentMessage() {}

    public AdjustmentMessage(String operationType) {
        this.operationType = operationType;
    }

    public AdjustmentMessage(String type, Double sellingPrice, String operationType) {
        super(type, sellingPrice);
        this.operationType = operationType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
