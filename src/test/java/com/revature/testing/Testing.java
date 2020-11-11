package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.revature.dao.AccountDaoImpl;
import com.revature.exceptions.DepositException;
import com.revature.exceptions.MinimumDepositException;
import com.revature.exceptions.OverDraftException;
import com.revature.exceptions.UnOpenException;
import com.revature.model.Account;
import com.revature.model.AccountType;
import com.revature.model.User;
import com.revature.service.AccountServiceImpl;
import com.revature.service.UserServiceImpl;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Testing {

	private static final UserServiceImpl user = new UserServiceImpl();
	private static final AccountServiceImpl acct = new AccountServiceImpl();
	private static final AccountDaoImpl acctdao = new AccountDaoImpl();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/*
	 * 1 is owned by me 2 is pending 3 is deleted 4 is owned by other 5 is owned by
	 * me
	 */
	
	/************************************** Test new user *********************************/

	//@Ignore
	@Test
	public void test1_INSERT() {// Must change username or email after each use
		User u = new User("mare", "password", "Mareo", "Yapp", "mareo@gmail.com");
		assertEquals("mare", user.newUser(u).getUsername());
		
	}

	@Test
	public void test1_INSERTDUPLICATEUSERNAME() {
		expectedException.expect(NullPointerException.class);
		user.newUser(new User("mareo1997", "password", "Mareo", "Yapp", "mareo@hotmail.com"));
	}

	@Test
	public void test1_INSERTDUPLICATEEMAIL() {
		expectedException.expect(NullPointerException.class);
		user.newUser(new User("mareo", "password", "Mareo", "Yapp", "mareo1997@gmail.com"));
	}

	/*************************************** Test Login *********************************/

	@Test
	public void test2_LOGIN() {
		assertEquals("king", user.userlogin("king", "george").getUsername());
	}

	@Test
	public void test2_FAKELOGIN() {
		expectedException.expect(NullPointerException.class);
		user.userlogin("fake", "user");
	}

	/*************************************** Test User id *********************************/

	@Test
	public void test3_USERID() {
		//System.out.println(user.getUser(1));
		assertEquals("mareo1997", user.getUser(1).getUsername());
	}

	@Test
	public void test3_FAKEID() {
		expectedException.expect(NullPointerException.class);
		user.getUser(0);// assertEquals(null, user.getUser(0));
	}
	
	/***************************************** Test new accounts*********************************/

	//@Ignore
	@Test
	public void test4_OPENACCOUNT() {
		User u = user.getUser(1);
		AccountType at = new AccountType("Savings");
		Account a = new Account(8000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(4);
		at = new AccountType("Savings");
		a = new Account(7000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(1);
		at = new AccountType("Checkings");
		a = new Account(6000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(4);
		at = new AccountType("Checkings");
		a = new Account(5000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(1);
		at = new AccountType("Checkings");
		a = new Account(4000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

	}

	@Test
	public void test4_OPENACCTFAIL() {
		AccountType at = new AccountType("Savings");
		Account a = new Account(4000, at);
		User u = new User("THIS", "IS", "A", "FAKE", "USER");

		expectedException.expect(NullPointerException.class);
		acct.newAccount(a, u);
	}

	@Test
	public void test4_DEPOSITFAIL() {
		User u = user.getUser(1);
		AccountType at = new AccountType("Savings");
		Account a = new Account(499, at);

		expectedException.expect(MinimumDepositException.class);
		acct.newAccount(a, u);
	}

	/***************************************** Test account id*********************************/

	@Test
	public void test5_ACCTID() {
		assertEquals(1, acct.getAccount(1).getAccountId());
	}

	@Test
	public void test5_FAKEACCTID() {
		expectedException.expect(NullPointerException.class);
		acct.getAccount(0);
	}

	@Test
	public void test6_TRUEOWNER() {
		assertTrue(acct.isOwner(1, 1));
	}

	@Test
	public void test6_FAKEOWNER() {
		assertFalse(acct.isOwner(1, 0));
	}

	@Test
	public void test6_FALSEOWNER() {
		assertFalse(acct.isOwner(1, 2));
	}

	/***************************************** Test Status*********************************/

	@Test
	public void test7_STATUSOPEN() {
		assertEquals("Open", acct.change(1, "Open").getStatus().getStatus());
		assertEquals("Open", acct.change(3, "Open").getStatus().getStatus());
		assertEquals("Denied", acct.change(2, "Denied").getStatus().getStatus());
	}

	@Test
	public void test7_STATUSFAIL() {
		expectedException.expect(NullPointerException.class);
		acct.change(0, "Open");
	}

	//@Ignore
	@Test
	public void test7_CANCELACCT() {
		Account a = acct.getAccount(4);
		assertEquals("Close", acctdao.cancelAccount(a).getStatus().getStatus());
		expectedException.expect(NullPointerException.class);
		acct.getAccount(4);
	}

	//@Ignore
	@Test
	public void test7_CLOSEACCT() {//
		assertEquals(null, acct.change(5, "Close"));
		expectedException.expect(NullPointerException.class);
		acct.getAccount(5);
	}

	@Test
	public void test7_CLOSEFAIL() {
		expectedException.expect(NullPointerException.class);
		acct.change(0, "Close");

		Account a = null;
		expectedException.expect(NullPointerException.class);
		acctdao.cancelAccount(a);
	}

	/***************************************** Test Transactions*********************************/

	@Test
	public void test8_DEPOSIT() {
		Account a = acct.getAccount(1);
		assertEquals(a.getBalance() + 3000, acct.deposit(1, 3000), 2);
	}

	@Test
	public void test9_DEPOSITEXCEPTION() {
		expectedException.expect(DepositException.class);
		acct.deposit(1, 0); // Cannot deposit $0
	}

	@Test
	public void test8_WITHDRAW() {
		Account a = acct.getAccount(1);
		assertEquals(a.getBalance() - 2000, acct.withdraw(1, 2000), 2);
	}

	@Test
	public void test9_OVERDRAFTEXCEPTION() {
		Account a = acct.getAccount(1);
		expectedException.expect(OverDraftException.class);
		acct.withdraw(1, a.getBalance() + 1); // w is more than amt in acct 1
	}

	@Test
	public void test8_TRANSFER() {
		assertEquals(1, acct.transfer(1, 3, 1000), 2); // return 1 means transfer went through
	}

	@Test
	public void test9_TRANSFEREXCEPTION() {
		Account a = acct.getAccount(1);

		expectedException.expect(OverDraftException.class);
		acct.transfer(1, 3, a.getBalance() + 1); // t is more than amt in acct 1
	}

	//@Ignore
	@Test
	public void test9_UNAPPROVEDACCOUNTS() { // Return -1 cause unopen
		expectedException.expect(UnOpenException.class);
		acct.deposit(2, 5000);

		expectedException.expect(UnOpenException.class);
		acct.withdraw(2, 5000);

		expectedException.expect(UnOpenException.class);
		acct.transfer(1, 2, 5000);
		acct.transfer(2, 1, 5000);
	}

	/*************************************************************************************/

}
