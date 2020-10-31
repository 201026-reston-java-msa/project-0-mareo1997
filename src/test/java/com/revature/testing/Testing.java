package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
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

@FixMethodOrder
public class Testing {
	private static final UserDaoImpl user = new UserDaoImpl();
	private static final AccountDaoImpl acct = new AccountDaoImpl();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	// @Ignore
	@Test
	public void testinsert() {// Must change username or email after each use

		User u = new User("mareo1997", "password", "Mareo", "Yapp", "mareo1997@gmail.com");
		assertEquals("Mareo", user.insertUser(u).getFirstName());

		u = new User("marwil", "william", "Marcia", "Williamson", "mareo199@gmail.com");
		assertEquals("Marcia", user.insertUser(u).getFirstName());

		u = new User("king", "george", "Kingsley", "Yapp", "mareo19@gmail.com");
		assertEquals("Kingsley", user.insertUser(u).getFirstName());

		// expectedException.expect(NullPointerException.class);
		User u2 = new User("mareo1997", "password", "Mareo", "Yapp", "mareo1997@gmail.com");
		assertEquals(null, user.insertUser(u2));

	}

//	@Ignore
	@Test
	public void testOPENACCOUNT() {
		User u = user.selectUserById(1);
		AccountType at = new AccountType("Savings");
		Account a = new Account(8000, at);
		assertEquals(u.getUserId(), acct.insertAccount(a, u).getOwnerid());

		u = user.selectUserById(1);
		at = new AccountType("Savings");
		a = new Account(7000, at);
		assertEquals(u.getUserId(), acct.insertAccount(a, u).getOwnerid());

		u = user.selectUserById(1);
		at = new AccountType("Checkings");
		a = new Account(6000, at);
		assertEquals(u.getUserId(), acct.insertAccount(a, u).getOwnerid());

		u = user.selectUserById(1);
		at = new AccountType("Checkings");
		a = new Account(5000, at);
		assertEquals(u.getUserId(), acct.insertAccount(a, u).getOwnerid());

		at = new AccountType("Savings");
		a = new Account(4000, at);
		u = new User("THIS", "IS", "A", "FAKE", "USER");
		assertEquals(null, acct.insertAccount(a, u));
	}

	@Test
	public void testlogin() {
		assertEquals(3, user.userlogin("king", "george").getUserId());

		expectedException.expect(NullPointerException.class);
		user.userlogin("fake", "user");
	}

	@Test
	public void testuserid() {
		assertEquals("Mareo", user.selectUserById(1).getFirstName());

		expectedException.expect(NullPointerException.class);
		user.selectUserById(0);// assertEquals(null, user.selectUserById(0));
	}

	@Test
	public void testOWNERSHIP() {
		assertTrue(acct.isOwner(1, 1));

		assertFalse(acct.isOwner(1, 0));
	}

	@Test
	public void testACCTID() {
		assertEquals(1, acct.selectAccountById(1).getAccountId());

		expectedException.expect(NullPointerException.class);
		acct.selectAccountById(0);
	} 

	@Test
	public void testSTATUS() {
		assertEquals("Open", acct.status(1, "Open").getStatus().getStatus());
		assertEquals("Open", acct.status(4, "Open").getStatus().getStatus());

		assertEquals(null, acct.status(0, "Open"));

		assertEquals("Close", acct.status(2, "Close").getStatus().getStatus());

		expectedException.expect(NullPointerException.class);
		acct.selectAccountById(2);
	}

	@Test
	public void testTRANSACTIONS() {
		Account a = acct.selectAccountById(1);
		double d = a.getBalance() + 3000;
		assertEquals(d, acct.deposit(1, 3000), 2);

		double w = d - 2000;
		assertEquals(w, acct.withdraw(1, 2000), 2);

		assertEquals(1, acct.transfer(1, 4, 1000), 2); // return 1 means transfer went through
		assertEquals(1, acct.transfer(4, 1, 1000), 2);

		Account a2 = acct.selectAccountById(4);

		expectedException.expect(DepositException.class);
		acct.deposit(1, 0); // Cannot deposit $0

		double w1 = a.getBalance() + 1;
		expectedException.expect(OverDraftException.class);
		acct.withdraw(1, w1); // w is more than amt in acct 1

		double t = a.getBalance() + a2.getBalance();
		expectedException.expect(OverDraftException.class);
		acct.transfer(1, 4, t); // t is more than amt in acct 1
		expectedException.expect(OverDraftException.class);
		acct.transfer(4, 1, t); // t is more than amt in acct 2
	}

	@Test
	public void testUNAPPROVEDACCOUNTS() { // Return -1 cause unopen
		expectedException.expect(UnOpenException.class);
		acct.deposit(1, 5000);

		expectedException.expect(UnOpenException.class);
		acct.withdraw(1, 5000);

		expectedException.expect(UnOpenException.class);
		acct.transfer(1, 3, 5000);
		acct.transfer(3, 1, 5000);
	}
}
