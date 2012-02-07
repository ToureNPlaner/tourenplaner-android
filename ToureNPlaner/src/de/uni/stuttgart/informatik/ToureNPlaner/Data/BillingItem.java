package de.uni.stuttgart.informatik.ToureNPlaner.Data;


import org.codehaus.jackson.JsonNode;

import java.io.Serializable;
import java.util.ArrayList;


public class BillingItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String algorithm;
	private int requestid;
	private int userid;
	private String request;
	private String response;
	private int cost;
	private String requestdate;
	private String finishdate;
	private int duration;
	private String status;
	
	public String getAlgorithm() {
		return algorithm;
	}

	public int getRequestid() {
		return requestid;
	}

	public int getUserid() {
		return userid;
	}

	public String getRequest() {
		return request;
	}

	public String getResponse() {
		return response;
	}

	public int getCost() {
		return cost;
	}

	public String getRequestdate() {
		return requestdate;
	}

	public String getFinishdate() {
		return finishdate;
	}

	public int getDuration() {
		return duration;
	}

	public String getStatus() {
		return status;
	}

	public static ArrayList<BillingItem> parse(JsonNode object) {
		
		ArrayList<BillingItem> billingItemlist = new ArrayList<BillingItem>();
		//TODO:: reimplement when /requestlist is available
		//JsonNode requests = object.get("requests");
		//Integer numbers = object.get("numbers").asInt();
		Integer numbers = 1;
		for(int i = 0;i<numbers;i++){
			
			BillingItem billing = new BillingItem();
			//TODO:: implement rest of BillingItem when available
			//TODO:: replace static BillingItem
			//billing.algorithm = requests.get("algorithm").asText();
			//...
			billing.algorithm = "tsp";
			billing.requestid = 1;
			billing.userid = 42;
			billing.request ="{...}";
			billing.response = "{...}";
			billing.cost= 512;
			billing.requestdate = "2012-01-01 13:37";
			billing.finishdate ="2012-01-01 13:42";
			billing.duration = 300;
			billing.status = "OK";
			
			
			billingItemlist.add(billing);
		}
		return billingItemlist;
	}
}
