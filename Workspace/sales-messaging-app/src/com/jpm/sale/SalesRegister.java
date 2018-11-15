package com.jpm.sale;

import com.jpm.message.AdjustmentMessage;
import com.jpm.message.DetailedMessage;
import com.jpm.message.Message;
import com.jpm.product.Product;

import java.util.*;

public class SalesRegister {
    Map<Product, List<Transaction>> records;

    public SalesRegister() {
        this.records = new HashMap<>();
    }

    public SalesRegister(Map<Product, List<Transaction>> records) {
        this.records = records;
    }

    public Map<Product, List<Transaction>> getRecords() {
        return records;
    }

    public boolean addProduct(Product product) {
        if(product == null || records.containsKey(product)) {
            return false;
        }

        records.put(product, new ArrayList<>());
        return true;
    }

    public boolean updateRecords(Message message) {
        if(message == null) {
            System.out.println("Invalid sales record. Please review the data.");
            return false;
        }

        Product product = findProduct(message.getType());
        if(product == null) {
            System.out.println("No Sales record found initialize file. Product was not in stock to begin with.");
            return false;
        }

        List<Transaction> transactions = records.get(product);

        if(message instanceof AdjustmentMessage) {
            transactions = adjustTransactions(transactions, message);
        } else if(message instanceof DetailedMessage) {
            transactions = addNewTransactions(transactions, message);
        } else {
            examineNotification(message);
            transactions.add(new Transaction(message.getSellingPrice()));
        }

        if(transactions.size() == 0) {
            System.out.println("\nThere are no product (" + message.getType() + ") related sales to adjust. Doing nothing.");
            return false; 
        }

        records.put(product, transactions);
        return true;
    }

    private Product findProduct(String productType) {
        Set<Product> products = records.keySet();

        for(Product product : products) {
            if(productType.equals(product.getType())) {
                return product;
            }
        }

        return null;
    }

    private List<Transaction> adjustTransactions(List<Transaction> transactions, Message message) {
        String operationType = ((AdjustmentMessage) message).getOperationType();

        switch(operationType) {
            case "ADD":
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue() + message.getSellingPrice());
                    transaction.setTransactionStatus(Boolean.TRUE);
                }
                break;
            case "MULTIPLY":
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue() * message.getSellingPrice());
                    transaction.setTransactionStatus(Boolean.TRUE);
                }
                break;
            case "SUBTRACT":
                for(Transaction transaction : transactions) {
                    if(transaction.getValue() < message.getSellingPrice()) {
                       System.out.println("Potential loss detected @ [Product type: " + message.getType() + 
                    		   ", Existing value: " + transaction.getValue() +
                               ", Selling price: " + message.getSellingPrice() +
                               ", Adjustment operation: SUBTRACT] during processing.");
                    }

                    transaction.setValue(transaction.getValue() - message.getSellingPrice());
                    transaction.setTransactionStatus(Boolean.TRUE);
                }
                break;
            default:
                System.out.println("Unsupported operation.");
                break;
        }

        return transactions;
    }

    private List<Transaction> addNewTransactions(List<Transaction> transactions, Message message) {
        double price = message.getSellingPrice();
        long transactionsCount = ((DetailedMessage) message).getInstanceCount();

        examineNotification(message);

        if(transactionsCount <= 0) {
            System.out.println("Null or negative sales instance(s) found. Doing nothing.");
            return transactions;
        }

        for(long i=0; i<transactionsCount; i++) {
            transactions.add(new Transaction(price));
        }

        return transactions;
    }

    private void examineNotification(Message message) {
        if(message.getSellingPrice() <= 0) {
            System.out.println("\nNegative sales notification(s) for Product : " + message.getType() + ", Selling price: " + message.getSellingPrice() + ".");
        }
    }

    public void printSalesReport() {
        for(Map.Entry<Product, List<Transaction>> record : records.entrySet()) {
            System.out.println("Product : " + record.getKey().getType() +
                    "\nTotal Selling units : " + record.getValue().size() +
                    "\nRevenue Collected: " + getRevenueForProduct(record.getValue())+"\n");
        }
    }

    private double getRevenueForProduct(List<Transaction> transactions) {
        double revenueGenerated = 0;

        for(Transaction transaction : transactions) {
            revenueGenerated += transaction.getValue();
        }

        return revenueGenerated;
    }
}
