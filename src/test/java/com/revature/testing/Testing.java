package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.dao.AccountDaoImpl;
import com.revature.dao.UserDaoImpl;
import com.revature.exceptions.DepositException;
import com.revature.exceptions.OverDraftException;
import com.revature.exceptions.UnOpenException;
import com.revature.model.Account;
import com.revature.model.AccountType;
import com.revature.model.User;

public class Testing {
	private static final UserDaoImpl user = new UserDaoImpl();
	private static final AccountDaoImpl acct = new AccountDaoImpl();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testlogin() {
		assertEquals(3, user.userlogin("king", "george").getUserId());
	}

	@Test
	public void faillogin() {
		expectedException.expect(NullPointerException.class);
		user.userlogin("fake", "user");
	}

	@Test
	public void testuserid() {
		assertEquals("Mareo", user.selectUserById(1).getFirstName());
	}

	@Test
	public void failuserid() {
		expectedException.expect(NullPointerException.class);
		user.selectUserById(0);
	}

	//@Ignore
	 @Test
	public void testinsert() {// Must change username or email after each use
		User u = new User("mareo", "password", "Mareo", "Yapp", "mareo1997@yahoo.com");
		assertEquals("Mareo", user.insertUser(u).getFirstName());
	}

	//@Ignore
	@Test
	public void failinsert() {
		expectedException.expect(NullPointerException.class);
		User u = new User("mareo1997", "password", "Mareo", "Yapp", "mareo1997@gmail.com");
		user.insertUser(u);
	}

//	@Ignore
	@Test
	public void testopenaccount() {
		User u = user.selectUserById(1);
		AccountType at = new AccountType("Savings");
		Account a = new Account(8000, at);
		assertEquals(u.getUserId(), acct.insertAccount(a, u).getOwnerid());
	}

	//@Ignore
	@Test
	public void failopenaccount() {
		User u = user.selectUserById(0);
		AccountType at = new AccountType("Savings");
		Account a = new Account(8000, at);
		assertEquals(null, acct.insertAccount(a, u));
	}

	@Test
	public void testowner() {
		assertTrue(acct.isOwner(1, 1));
	}

	@Test
	public void failowner() {
		assertFalse(acct.isOwner(1, 0));
	}

	@Test
	public void testaccountid() {
		assertEquals(1, acct.selectAccountById(1).getAccountId());
	}

	@Test
	public void failaccountid() {
		assertEquals(null, acct.selectAccountById(0));
	}

	@Test
	public void teststatus() {
		assertEquals("Open", acct.status(4, "Open").getStatus().getStatus());
	}

	@Test
	public void failstatus() {
		assertEquals(null, acct.status(0, "Open"));
	}
	
	@Test
	public void testtransaction() {
		Account a = acct.selectAccountById(1);
		double d = a.getBalance() + 3000;
		assertEquals(d, acct.deposit(1, 3000), 2);

		double w = d - 2000;
		assertEquals(w, acct.withdraw(1, 2000), 2);

		assertEquals(1, acct.transfer(1, 4, 1000), 2); // return 1 means transfer went through
		assertEquals(1, acct.transfer(4, 1, 1000), 2);
	}

	@Test
	public void failtransaction() {
		Account a = acct.selectAccountById(1);
		Account a2 = acct.selectAccountById(4);

		expectedException.expect(DepositException.class);
		acct.deposit(1, 0); // Cannot deposit $0

		double w = a.getBalance() + 1;
		expectedException.expect(OverDraftException.class);
		acct.withdraw(1, w); // w is more than amt in acct 1

		double t = a.getBalance() + a2.getBalance();
		expectedException.expect(OverDraftException.class);
		acct.transfer(1, 4, t); // t is more than amt in acct 1
		expectedException.expect(OverDraftException.class);
		acct.transfer(4, 1, t); // t is more than amt in acct 2
	}

	@Test
	public void testunapproved() { // Return -1 cause unopen
		expectedException.expect(UnOpenException.class);
		acct.deposit(3, 5000);
		
		expectedException.expect(UnOpenException.class);
		acct.withdraw(3, 5000);
		
		expectedException.expect(UnOpenException.class);
		acct.transfer(1, 3, 5000);
		acct.transfer(3, 1, 5000);
	}
}
