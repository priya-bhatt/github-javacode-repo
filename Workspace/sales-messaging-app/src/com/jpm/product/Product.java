package com.jpm.product;

public class Product {
    private String type; // Message Type
    private long inStock; //In stock at any given time
    private long soldOut; //Out stock at any given time 
    private Double unitPrice; //Unit Price of the Item

    public Product() {
    }

    public Product(String type, Double unitPrice, long inStock, long soldOut) {
        this.type = type;
        this.inStock = inStock;
        this.soldOut = soldOut;
        this.unitPrice = unitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getInStock() {
        return inStock;
    }

    public void setInStock(long inStock) {
        this.inStock = inStock;
    }

    public long getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(long soldOut) {
        this.soldOut = soldOut;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
