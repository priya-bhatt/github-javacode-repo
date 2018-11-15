package com.jpm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jpm.message.AdjustmentMessage;
import com.jpm.message.Message;
import com.jpm.product.Product;
import com.jpm.sale.SalesRegister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SalesMessageProcessor {
    private static SalesMessageProcessor salesMsgPrcssr = new SalesMessageProcessor();
    private SalesRegister salesRegister; 

    private static final long MESSAGE_PROCESSING_CAPACITY = 50;

    private SalesMessageProcessor() {
        this.salesRegister = new SalesRegister();
    }

    public static SalesMessageProcessor getSalesMessageProcessor() {
        return salesMsgPrcssr;
    }

    public boolean initialize(String stockFile) {
        BufferedReader stockBuffer = null;

        try {
            String stockEntry;
            stockBuffer = new BufferedReader(new FileReader(stockFile));

            while((stockEntry = stockBuffer.readLine()) != null) {
                boolean productAdded = salesRegister.addProduct(parseLineEntry(stockEntry));

                if(!productAdded) {
                    System.out.println("Stock update failed. Please confirm stock data.");
                }
            }
        } catch(IOException exception) {
            exception.printStackTrace();
        } finally {
            if(stockBuffer != null) {
                try {
                    stockBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private Product parseLineEntry(String stockEntry) {
        if(stockEntry == null) {
            return null;
        }

        String[] productData = stockEntry.split("\\s*,\\s*");

        if(productData.length != 4) {
            System.out.println("Invalid Product data. Please enter Product Type, Unit Price, In-Stock Units, Out-Stock Units.");
            return null;
        }

        Product product = null;

        try {
            product = new Product(productData[0], //product type
            		Double.valueOf(productData[1]),//unit price
            		Long.valueOf(productData[2]), //in-stock units
                    Long.valueOf(productData[3])); //out-stock units
        } catch (NumberFormatException exception) {
            System.out.println("Invalid Product count and/or pricing. Please check the product data (entry).");
        }

        return product;
    }

    public List<Message> parse(String notificationsFile) {
        List<Message> messages = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            messages = mapper.readValue(new File(notificationsFile), new TypeReference<List<Message>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public boolean process(List<Message> messages) {
        int processedMessages = 0;
        StringBuilder adjustmentsLog = new StringBuilder();

        for(Message message : messages) {
            boolean recordsUpdated = salesRegister.updateRecords(message);
            if(!recordsUpdated) {
                return false;
            }

            processedMessages++;

            if(message instanceof AdjustmentMessage) {
                adjustmentsLog.append("Product (");
                adjustmentsLog.append(message.getType());
                adjustmentsLog.append(") was adjusted (operation: ");
                adjustmentsLog.append(((AdjustmentMessage) message).getOperationType());
                adjustmentsLog.append(") by a value of ");
                adjustmentsLog.append(message.getSellingPrice());
                adjustmentsLog.append(" at approximately ");
                adjustmentsLog.append(new Date());
                adjustmentsLog.append(".\n");
            }

            if(processedMessages % 10 == 0) {
                System.out.println("\n---------- Intermediate Processed Sales' Record Per 10 message ----------");
                salesRegister.printSalesReport();
                System.out.println("\n-------------------------------------------------------------------------");
            }

            if(processedMessages == MESSAGE_PROCESSING_CAPACITY) {
                System.out.println("\nA total of "+ MESSAGE_PROCESSING_CAPACITY +" messages can be processed. Processing now stopped and no new message will be accepted.");
                System.out.println("\n-----------------------------------------------------------------------------------------------------------------------------------");
                break;
            }
        }

        System.out.println("\n---------- Final Processed Sales' Record ----------");
        salesRegister.printSalesReport();
        System.out.println("\n---------------------------------------------------");
        
        if(adjustmentsLog.length() != 0) {
            System.out.println("\n---------- Adjustment Log ----------");
            System.out.println(adjustmentsLog.toString());
            System.out.println("\n------------------------------------");
        }

        return true;
    }
}