package com.maa.v2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTableResult;

public class DynamoDBClient {
	private String tableName = "Stores";
	private AmazonDynamoDBClient client = null;

	public DynamoDBClient() throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(
				DynamoDBClient.class
						.getResourceAsStream("AwsCredentials.properties"));
		client = new AmazonDynamoDBClient(credentials);
	}

	public void createTable() {
		logMessage("Creating table " + tableName);
		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName(
				"Id").withAttributeType("N"));

		ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
		ks.add(new KeySchemaElement().withAttributeName("Id").withKeyType(
				KeyType.HASH));
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
				.withReadCapacityUnits(10L).withWriteCapacityUnits(10L);

		CreateTableRequest request = new CreateTableRequest()
				.withTableName(tableName)
				.withAttributeDefinitions(attributeDefinitions)
				.withKeySchema(ks)
				.withProvisionedThroughput(provisionedThroughput);

		CreateTableResult result = client.createTable(request);
		logMessage("Created table "
				+ result.getTableDescription().getTableName());
	}

	public static void logMessage(String msg) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()) + " ==> " + msg);
	}

	public String getTableStatus() {
		TableDescription tableDescription = client.describeTable(
				new DescribeTableRequest().withTableName(tableName)).getTable();
		return tableDescription.getTableStatus();
	}

	public void describeTable() {
		logMessage("Describing table " + tableName);
		TableDescription tableDescription = client.describeTable(
				new DescribeTableRequest().withTableName(tableName)).getTable();
		String desc = String.format(
				"%s: %s \t ReadCapacityUnits: %d \t WriteCapacityUnits: %d",
				tableDescription.getTableStatus(), tableDescription
						.getTableName(), tableDescription
						.getProvisionedThroughput().getReadCapacityUnits(),
				tableDescription.getProvisionedThroughput()
						.getWriteCapacityUnits());
		logMessage(desc);
	}

	public void updateTable() {
		logMessage("Updating table " + tableName);
		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
				.withReadCapacityUnits(5L).withWriteCapacityUnits(10L);

		UpdateTableRequest updateTableRequest = new UpdateTableRequest()
				.withTableName(tableName).withProvisionedThroughput(
						provisionedThroughput);

		UpdateTableResult result = client.updateTable(updateTableRequest);
		logMessage("Updated table "
				+ result.getTableDescription().getTableName());
	}

	public void listTables() {
		logMessage("Listing tables");
		String lastEvaluatedTableName = null;
		do {

			ListTablesRequest listTablesRequest = new ListTablesRequest()
					.withLimit(10).withExclusiveStartTableName(
							lastEvaluatedTableName);

			ListTablesResult result = client.listTables(listTablesRequest);
			lastEvaluatedTableName = result.getLastEvaluatedTableName();

			for (String name : result.getTableNames()) {
				logMessage(name);
			}

		} while (lastEvaluatedTableName != null);
	}

	public void deleteTable() {
		logMessage("Deleting table " + tableName);
		DeleteTableRequest deleteTableRequest = new DeleteTableRequest()
				.withTableName(tableName);
		DeleteTableResult result = client.deleteTable(deleteTableRequest);
		logMessage("Deleted table "
				+ result.getTableDescription().getTableName());
	}

	public void putItems() {
		logMessage("Putting items into table " + tableName);
		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();

		item.put("Id", new AttributeValue().withN("1"));
		item.put("pincode", new AttributeValue().withS("395009"));
//		String pc=item.get("pincode").getS();
//		Coords API=new Coords();
		item.put("address", new AttributeValue().withS("croma,adajan,surat,gujarat"));
		item.put("type", new AttributeValue().withS("electronics"));
//		item.put("Lat", new AttributeValue().withS(API.Lat(pc)));
//		item.put("Lon", new AttributeValue().withS(API.Long(pc)));
		item.put("Lat", new AttributeValue().withS(""));
		item.put("Lon", new AttributeValue().withS(""));
		PutItemRequest itemRequest = new PutItemRequest().withTableName(
				tableName).withItem(item);
		client.putItem(itemRequest);
		item.clear();

		item.put("Id", new AttributeValue().withN("2"));
		item.put("pincode", new AttributeValue().withS("110001"));
		//pc=item.get("pincode").getS();
		item.put("address", new AttributeValue().withS("Om kariyana store,delhi"));
		item.put("type", new AttributeValue().withS("grocery"));
		item.put("Lat", new AttributeValue().withS(""));
		item.put("Lon", new AttributeValue().withS(""));
		itemRequest = new PutItemRequest().withTableName(tableName).withItem(
				item);
		client.putItem(itemRequest);
		item.clear();

		item.put("Id", new AttributeValue().withN("3"));
		item.put("pincode", new AttributeValue().withS("384265"));
		//pc=item.get("pincode").getS();
		item.put("address", new AttributeValue().withS("om krupa clothes,patan,gujarat"));
		item.put("type", new AttributeValue().withS("garments"));
		item.put("Lat", new AttributeValue().withS(""));
		item.put("Lon", new AttributeValue().withS(""));
		itemRequest = new PutItemRequest().withTableName(tableName).withItem(
				item);
		client.putItem(itemRequest);
		item.clear();

		
	}

	

	public void deleteItem() {
		Map<String, ExpectedAttributeValue> expectedValues = new HashMap<String, ExpectedAttributeValue>();
		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("Id", new AttributeValue().withN("2"));

		ReturnValue returnValues = ReturnValue.ALL_OLD;

		DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
				.withTableName(tableName).withKey(key)
				.withExpected(expectedValues).withReturnValues(returnValues);

		DeleteItemResult result = client.deleteItem(deleteItemRequest);
		logMessage("Printing item that was deleted...");
		printItem(result.getAttributes());
		
	}

	public void listItems() {
		logMessage("List all items");
		ScanRequest scanRequest = new ScanRequest().withTableName(tableName);

		ScanResult result = client.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			printItem(item);
		}
	}
	public void UI()
	{
		ScanRequest scanRequest = new ScanRequest().withTableName(tableName);

		ScanResult result = client.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			String k=String.valueOf(item.get("Id").getN());
			String pc=item.get("pincode").getS();
			Coords API=new Coords();
			String lat=API.Lat(pc);
			String lon=API.Long(pc);
			updateItem(k, lat, lon);
		}
	}
	public void updateItem(String k,String lat,String lon) {
		Map<String, AttributeValueUpdate> updateItems = new HashMap<String, AttributeValueUpdate>();

		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("Id", new AttributeValue().withN(k));
		updateItems
				.put("Lat",
						new AttributeValueUpdate().withValue(
								new AttributeValue().withS(lat)));
		updateItems
		.put("Lon",
				new AttributeValueUpdate().withValue(
						new AttributeValue().withS(lon)));
		ReturnValue returnValues = ReturnValue.ALL_NEW;

		UpdateItemRequest updateItemRequest = new UpdateItemRequest()
				.withTableName(tableName).withKey(key)
				.withAttributeUpdates(updateItems)
				.withReturnValues(returnValues);

		UpdateItemResult result = client.updateItem(updateItemRequest);

		// Check the response.
//		logMessage("Printing item after attribute update...");
//		printItem(result.getAttributes());
	}
	public void findQuery(Double radius,String pincode)
	{
		
		ScanRequest scanRequest = new ScanRequest().withTableName(tableName);
		Coords API=new Coords();
		Double lat1=Double.valueOf(API.Lat(pincode));
		Double lon1=Double.valueOf(API.Long(pincode));
		ScanResult result = client.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			Double lat2=Double.valueOf(item.get("Lat").getS());
			Double lon2=Double.valueOf(item.get("Lon").getS());
			Double ans=distance(lat1,lat2,lon1,lon2);
			if(ans<=radius)
			{
				printItem(item);
			}
		}
	}
	public Double distance(Double lat1,Double lat2,Double lon1,Double lon2)
	{
		lon1 = Math.toRadians(lon1); 
		lon2 = Math.toRadians(lon2); 
		lat1 = Math.toRadians(lat1); 
		lat2 = Math.toRadians(lat2); 
		double dlon = lon2 - lon1; 
		double dlat = lat2 - lat1; 
		double a = Math.pow(Math.sin(dlat / 2), 2) 
				+ Math.cos(lat1) * Math.cos(lat2) 
				* Math.pow(Math.sin(dlon / 2),2); 
			
		double c = 2 * Math.asin(Math.sqrt(a)); 
		double r = 6371;
		return(c * r); 
	}
	private void printItem(Map<String, AttributeValue> attributeList) {
		String itemString = new String();
		for (Map.Entry<String, AttributeValue> item : attributeList.entrySet()) {
			if (!itemString.equals(""))
				itemString += ", ";
			String attributeName = item.getKey();
			AttributeValue value = item.getValue();
				
			itemString += attributeName
					+ ""
					+ (value.getS() == null ? "" : "=\"" + value.getS() + "\"")
					+ (value.getN() == null ? "" : "=\"" + value.getN() + "\"")
					+ (value.getB() == null ? "" : "=\"" + value.getB() + "\"")
					+ (value.getSS() == null ? "" : "=\"" + value.getSS()
							+ "\"")
					+ (value.getNS() == null ? "" : "=\"" + value.getNS()
							+ "\"")
					+ (value.getBS() == null ? "" : "=\"" + value.getBS()
							+ "\" \n");
					}
		logMessage(itemString);
	}

	public static void main(String[] args) {
		try {
			DynamoDBClient dbClient = new DynamoDBClient();

//			dbClient.createTable();
//			while (!"ACTIVE".equalsIgnoreCase(dbClient.getTableStatus())) {
//				logMessage("Waiting for table being created. Sleeping 10 seconds");
//				Thread.sleep(10000);
//			}
			//dbClient.describeTable();

			//dbClient.putItems();
			//dbClient.listItems();
		//	dbClient.UI();
		//	dbClient.listItems();
			//dbClient.deleteItem();
			Scanner in =new Scanner(System.in);
			System.out.println("Put in the radius : ");
			Double radius;
			radius=in.nextDouble();
			System.out.println("Enter your pincode : ");
			String pincode;
			pincode=in.next();
			System.out.println("Ready to go!!");
			dbClient.findQuery(radius, pincode);
		//	dbClient.updateTable();
//			while ("UPDATING".equalsIgnoreCase(dbClient.getTableStatus())) {
//				logMessage("Waiting for table being updated. Sleeping 10 seconds");
//				Thread.sleep(10000);
//			}
			//dbClient.describeTable();
			//dbClient.listTables();
//			dbClient.deleteTable();
//			try {
//				while ("DELETING".equalsIgnoreCase(dbClient.getTableStatus())) {
//					logMessage("Waiting for table being deleted. Sleeping 10 seconds");
//					Thread.sleep(10000);
//				}
//			} catch (ResourceNotFoundException e) {
//			}
			//dbClient.listTables();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
