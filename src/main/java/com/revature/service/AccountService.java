package com.revature.service;

import java.util.ArrayList;
import java.util.List;

import com.revature.model.Account;
import com.revature.model.User;

public interface AccountService {

	public Account newAccount(Account a, User u);

	public List<Account> getAllAccount();

	public ArrayList<Account> getAllPersonalAccount(User u);

	public Account getAccount(int id);

	public Account change(int sID, String status);

	public double deposit(int id, double b);

	public double withdraw(int id, double b);

	public double transfer(int from, int to, double b);

	public List<Account> getAllStatus(String status);

	public Account updateAccount(int id, double balance, int sID, String status, int tID, String type, int oID);

	public boolean isOwner(int i, int id);

}
