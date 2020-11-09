package com.revature.model;

public class Account {
	// The Account model is used to represent a single account for a user
	private int accountId; // primary key
	private double balance; // not null
	private AccountStatus status;
	private AccountType type;
	private int ownerid;

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public Account(int accountId, double balance, int ownerid) {
		super();
		this.accountId = accountId;
		this.balance = balance;
		this.ownerid = ownerid;
	}

	public Account(int accountId, double balance, AccountStatus status, AccountType type, int ownerid) {
		super();
		this.accountId = accountId;
		this.ownerid = ownerid;
		this.balance = balance;
		this.status = status;
		this.type = type;
	}

	public Account(double balance, AccountType type) {
		super();
		this.balance = balance;
		this.type = type;
	}

	public Account(double balance, AccountStatus status, AccountType type) {
		super();
		this.balance = balance;
		this.status = status;
		this.type = type;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String toString() {
		String open = "\nAccount ID: " + accountId + "\t\tBalance: $" + balance + "\n" + status + "\n" + type + "\n";
		return open;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public void setOwnerid(int ownerid) {
		this.ownerid = ownerid;
	}
}
