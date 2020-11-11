package com.revature.service;

import java.util.ArrayList;
import java.util.List;

import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoImpl;
import com.revature.model.Account;
import com.revature.model.User;

public class AccountServiceImpl implements AccountService {

	private AccountDao dao = new AccountDaoImpl();

	@Override
	public Account newAccount(Account a, User u) {
		return dao.insertAccount(a, u);
	}

	@Override
	public List<Account> getAllAccount() { //NEED TO TEST
		List<Account> myList = dao.selectAllAccount();
		return myList;
	}

	@Override
	public Account getAccount(int id) {
		return dao.selectAccountById(id);
	}

	@Override
	public Account change(int sID, String status) {
		return dao.status(sID, status);
	}

	@Override
	public ArrayList<Account> getAllPersonalAccount(User u) { //NEED TO TEST
		ArrayList<Account> myList = dao.PersonalAccount(u);
		return myList;
	}

	public double deposit(int i, double b) {
		return dao.deposit(i, b);
	}

	@Override
	public double withdraw(int i, double b) {
		return dao.withdraw(i, b);
	}

	@Override
	public double transfer(int a, int b, double c) {
		return dao.transfer(a, b, c);
	}

	@Override
	public List<Account> getAllStatus(String status) {
		return null; //NOT USING
		//List<Account> list = dao.selectAllStatus(status);
		//return list;
	}

	@Override
	public Account updateAccount(int id, double balance, int sID, String status, int tID, String type, int oID) {
		return null; //NOT USING
		//return dao.updateAccount(id, balance, sID, status, tID, type, oID);
	}

	@Override
	public boolean isOwner(int i, int id) {
		boolean owner = false;
		owner = dao.isOwner(i, id);
		return owner;
	}

}
