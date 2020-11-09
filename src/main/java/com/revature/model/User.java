package com.revature.model;

import java.util.ArrayList;

public class User {
	// The User model keeps track of users information.
	private int userId; // primary key
	private String username; // not null, unique
	private String password; // not null
	private String firstName; // not null
	private String lastName; // not null
	private String email; // not null
	private Role role;
	ArrayList<Account> AccountList = new ArrayList<Account>(); // Create and store a list of accounts

	public User(String username, String password, String firstName, String lastName, String email) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public User(int userId, String username, String password, String firstName, String lastName, String email,
			Role role) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
	}

	public User(int userId, String username, String password, String firstName, String lastName, String email,
			Role role, ArrayList<Account> accountList) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
		AccountList = accountList;
	}

	public User(int userId, String username, String password, String firstName, String lastName, String email) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public ArrayList<Account> getAccountList() {
		return AccountList;
	}

	public void setAccountList(ArrayList<Account> accountList) {
		AccountList = accountList;
	}

	public String toString() {
		String valid = "User ID: " + userId + "\t\tName: " + firstName + " " + lastName + "\nUser name: "
				+ username + "\tEmail: " + email + "\n" + role + "\n" + AccountList.toString() + "\n";
		return valid;
	}

	public void addAccount(Account a) {
		AccountList.add(a);
	}

	public void removeAccount(Account a) {
		AccountList.remove(a);
	}

	public int getUserId() {// Create an id number
		return userId;

	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
