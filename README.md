Sales Message Processing and Notification Application
A small message processing application that satisfies the below requirements for processing sales notification messages. Assuming that an input file will be provided from external.

Processing requirements

All sales must be recorded
All messages must be processed
After every 10th message received, the application should log a report detailing the number of sales of each product and their total value.
After 50 messages, the application should log that it is pausing, stop accepting new messages and log a report of the adjustments that have been made to each sale type while the application was running.
Sales and Messages

A sale has a product type field and a value.
Any number of different product types can be expected. There is no fixed set.
A message notifying of a sale could be one of the following types o Message Type 1 – contains the details of 1 sale E.g apple at 10p o Message Type 2 – contains the details of a sale and the number of occurrences of that sale. E.g 20 sales of apples at 10p each. o Message Type 3 – contains the details of a sale and an adjustment operation to be applied to all stored sales of this product type. Operations can be add, subtract, or multiply e.g Add 20p apples would instruct the application to add 20p to each sale of apples recorded.
##The application is based on the assumption that sales notification come in in JSON format.

##Execution Guidelines:

The Application starts with a stock initializing file eg. stockRecord.csv (read from CSV file provided as first commandline argument),
Then parses sales notifications which is a JSON file (provided as second commandline argument), and
Updates sales register to reflect 50 sales notifications.
After every 10 message, an intermediate notification log is print on the console to display the sales so far.
##Sample Arguments:

Adjust the filepath of th input files to "/Users/priya-bhatt/SalesMessageProcessingApp/src/resources/"
First argument is stockRecord.csv file
Second argument is notifications.json file
##Sample Data: There are five sample data files:

stockRecord.csv : This file has stock data listed in there. The data is listed in the order of class (Product) members.
notifications.json : This file only has notification messages of Message Type 1.
detailednotifications.json : This file only has notification messages of Message Type 2.
adjustmentnotifications.json : This file only has notification messages of Message Type 3.
mixednotifications.json : This file only has notification messages of all Message Types.
