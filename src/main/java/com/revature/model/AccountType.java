package com.revature.model;

public class AccountType {
	// The AccountType model is used to track what kind of account is being created.
	// Type possibilities are Checking or Savings
	private int typeId; // primary key
	private String type; // not null, unique

	public AccountType(String type) {
		super();
		this.type = type;
	}

	public AccountType(int typeId, String type) {
		super();
		this.typeId = typeId;
		this.type = type;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		String output = "Type ID: " + typeId + "\t\tType: " + type;
		return output;
	}

}
