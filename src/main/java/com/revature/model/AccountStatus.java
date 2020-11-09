package com.revature.model;

public class AccountStatus {
	// The AccountStatus model is used to track the status of accounts. Status
	// possibilities are Pending, Open, or Closed, or Denied
	private int statusId; // primary key
	private String status; // not null, unique

	public AccountStatus(int statusId, String status) {
		super();
		this.statusId = statusId;
		this.status = status;
	}

	public AccountStatus(String status) {
		super();
		this.status = status;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String toString() {
		String output = "Status ID: " + statusId + "\t\tStatus: " + status;
		return output;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
