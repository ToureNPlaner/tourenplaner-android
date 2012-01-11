package de.uni.stuttgart.informatik.ToureNPlaner.Data;

public class Billing {
	private int requestid;
	private int userid;
	private String algorithm;
	private String request;
	private String response;
	private int cost;
	private String requestDate;
	private String finishedDate;
	private int duration;
	private String status;
	
	public Billing(int requestid, int userid, String algorithm, String request,
			String response, int cost, String requestDate, String finishedDate,
			int duration, String status) {
		this.requestid = requestid;
		this.userid = userid;
		this.algorithm = algorithm;
		this.request = request;
		this.response = response;
		this.cost = cost;
		this.requestDate = requestDate;
		this.finishedDate = finishedDate;
		this.duration = duration;
		this.status = status;
	}

	public int getRequestid() {
		return requestid;
	}

	public void setRequestid(int requestid) {
		this.requestid = requestid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getFinishedDate() {
		return finishedDate;
	}

	public void setFinishedDate(String finishedDate) {
		this.finishedDate = finishedDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	


}
