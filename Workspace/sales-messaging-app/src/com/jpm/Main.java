package com.jpm;

import com.jpm.message.Message;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        
    	if(args.length != 2) {
            System.out.println("Incorrect number of commandline arguments. Please enter the stock file and the notification file to proceed.");
            System.exit(1);
        }

        if(isInvalidFilePath(args[0]) || isInvalidFilePath(args[1])) {
            System.out.println("Either the files doesnot exist or not accessible.");
            System.exit(1);
        }

        String stockDataFile = args[0];
        String notificationsFile = args[1];

        SalesMessageProcessor salesMessageProcessor = SalesMessageProcessor.getSalesMessageProcessor();

        boolean initialized = salesMessageProcessor.initialize(stockDataFile);
        if(!initialized) {
            System.out.println("Error in stock initialization. Please check the console logs for more details.");
            System.exit(1);
        }

        List<Message> messages = salesMessageProcessor.parse(notificationsFile);
        if(messages == null) {
            System.out.println("Error in parsing notification File. Please check the console logs for more details.");
            System.exit(1);
        }

        boolean processed = salesMessageProcessor.process(messages);
        if(!processed) {
            System.out.println("Error in processing the notifications file. Please check the console logs for the more details.");
            System.exit(1);
        }
    }

    private static boolean isInvalidFilePath(String filePath) {
        try {
        	
            Path path = Paths.get(filePath);
            
            if(!Files.exists(path) || Files.notExists(path)) { 
            	return true;
            }

            if(!Files.isRegularFile(path)) {
                return true;
            }

            if(!Files.isReadable(path)) { 
                return true;
            }
        } catch (InvalidPathException | NullPointerException exception) {
            return true; 
        
        }

        return false;
    }
}
