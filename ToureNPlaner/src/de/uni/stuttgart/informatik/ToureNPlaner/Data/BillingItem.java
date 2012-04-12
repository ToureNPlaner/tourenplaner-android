/*
 * Copyright 2012 ToureNPlaner
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.uni.stuttgart.informatik.ToureNPlaner.Data;


import com.fasterxml.jackson.databind.JsonNode;

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

	public static BillingItem parseItem(JsonNode object) {
		BillingItem billing = new BillingItem();
		billing.algorithm = object.get("algorithm").asText();
		billing.requestid = object.get("requestid").asInt();
		billing.userid = object.get("userid").asInt();
		billing.request = "null";//object.get("request").asText();
		billing.response = "null";//object.get("response").asText();
		billing.cost = object.get("cost").asInt();
		billing.requestdate = object.get("requestdate").asText();
		billing.finishdate = "null";//object.get("finishdate").asText();
		billing.duration = object.get("duration").asInt();
		billing.status = object.get("status").asText();
		return billing;

	}

	public static ArrayList<BillingItem> parse(JsonNode object) {

		ArrayList<BillingItem> billingItemlist = new ArrayList<BillingItem>();
		JsonNode requests = object.get("requests");
		for (JsonNode listItem : requests) {
			billingItemlist.add(parseItem(listItem));
		}
		return billingItemlist;
	}
}
