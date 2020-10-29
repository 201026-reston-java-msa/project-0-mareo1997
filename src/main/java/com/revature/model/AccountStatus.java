package com.revature.model;

public class AccountStatus {
	// The AccountStatus model is used to track the status of accounts. Status
	// possibilities are Pending, Open, or Closed, or Denied
	private int statusId; // primary key
	private String status; // not null, unique

	public AccountStatus(int statusId, String s) {
		// TODO Auto-generated constructor stub
		this.statusId = statusId;
		this.status = s;
	}

	public AccountStatus(String s) {
		// TODO Auto-generated constructor stub
		this.status = s;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String toString() {
		String output = "Status ID: " + statusId + "\n" + "Status: " + status;
		return output;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
