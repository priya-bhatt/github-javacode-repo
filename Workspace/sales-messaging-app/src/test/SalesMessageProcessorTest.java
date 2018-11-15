package test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.jpm.SalesMessageProcessor;
import com.jpm.message.Message;
import com.jpm.product.Product;


import junit.framework.Assert;
class SalesMessageProcessorTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}
	

	@Test
	final void testInitialize() {
		String stockfile = "C:\\github-priya-coderepo\\sales-messgaing-app\\Workspace\\sales-messaging-app\\src\\test\\stockRecord.csv";
		SalesMessageProcessor saleMsg = SalesMessageProcessor.getSalesMessageProcessor();
		
		boolean initialized = saleMsg.initialize(stockfile);
		
		assertTrue(initialized);
		
	}

	@Test
	final void testProcess() {
		String notificationsFile = "C:\\github-priya-coderepo\\sales-messgaing-app\\Workspace\\sales-messaging-app\\src\\test\\notifications.json";		
		SalesMessageProcessor saleMsg = SalesMessageProcessor.getSalesMessageProcessor();
		
		List<Message> messages = new ArrayList<Message>();
		
		messages.add(new Message("apple",10.0));
		
		 saleMsg.parse(notificationsFile);
		Assert.assertEquals(messages, saleMsg.parse(notificationsFile));
	}

}
