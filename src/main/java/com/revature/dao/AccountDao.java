package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import com.revature.model.Account;
import com.revature.model.User;

public interface AccountDao {
	public Account insertAccount(Account a, User u);

	public Account selectAccountById(int id);

	public List<Account> selectAllAccount();

	public ArrayList<Account> PersonalAccount(User u);

	// Update
	public double deposit(int id, double b);

	public double withdraw(int id, double b);

	public double transfer(int from, int to, double b);

	public Account status(int sID, String status);

	// Delete
	public Account cancelAccount(Account a);

	//public List<Account> selectAllStatus(String status);

	//public Account updateAccount(int id, double balance, int sID, String status, int tID, String type, int oID);

	public boolean isOwner(int i, int id);

}
