package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.exceptions.DepositException;
import com.revature.exceptions.OverDraftException;
import com.revature.exceptions.UnOpenException;
import com.revature.model.Account;
import com.revature.model.AccountType;
import com.revature.model.User;
import com.revature.service.AccountServiceImpl;
import com.revature.service.UserServiceImpl;

public class AccountTesting {

	private static final UserServiceImpl user = new UserServiceImpl();
	private static final AccountServiceImpl acct = new AccountServiceImpl();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Ignore
	// @Test
	public void testOPENACCOUNT() {
		User u = user.getUser(1);
		AccountType at = new AccountType("Savings");
		Account a = new Account(8000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(1);
		at = new AccountType("Savings");
		a = new Account(7000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(1);
		at = new AccountType("Checkings");
		a = new Account(6000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		u = user.getUser(1);
		at = new AccountType("Checkings");
		a = new Account(5000, at);
		assertEquals(u.getUserId(), acct.newAccount(a, u).getOwnerid());

		at = new AccountType("Savings");
		a = new Account(4000, at);
		u = new User("THIS", "IS", "A", "FAKE", "USER");
		assertEquals(null, acct.newAccount(a, u));
	}

	@Test
	public void testOWNERSHIP() {
		assertTrue(acct.isOwner(1, 1));

		assertFalse(acct.isOwner(1, 0));
	}

	@Test
	public void testACCTID() {
		assertEquals(1, acct.getAccount(1).getAccountId());

		expectedException.expect(NullPointerException.class);
		acct.getAccount(0);
	}

	@Test
	public void testSTATUS() {
		assertEquals("Open", acct.change(1, "Open").getStatus().getStatus());
		assertEquals("Open", acct.change(4, "Open").getStatus().getStatus());

		assertEquals(null, acct.change(0, "Open"));

		// assertEquals("Close", acct.change(2, "Close").getStatus().getStatus());

		expectedException.expect(NullPointerException.class);
		acct.getAccount(2);
	}

	@Test
	public void testTRANSACTIONS() {
		Account a = acct.getAccount(1);
		double d = a.getBalance() + 3000;
		assertEquals(d, acct.deposit(1, 3000), 2);

		double w = d - 2000;
		assertEquals(w, acct.withdraw(1, 2000), 2);

		assertEquals(1, acct.transfer(1, 2, 1000), 2); // return 1 means transfer went through
		assertEquals(1, acct.transfer(2, 1, 1000), 2);

		Account a2 = acct.getAccount(2);

		double t = a.getBalance() + a2.getBalance();
		expectedException.expect(OverDraftException.class);
		acct.transfer(1, 2, t); // t is more than amt in acct 1
		expectedException.expect(OverDraftException.class);
		acct.transfer(2, 1, t); // t is more than amt in acct 2
	}

	@Test
	public void testDEPOSITEXCEPTION() {
		//Account a = acct.getAccount(1);
		expectedException.expect(DepositException.class);
		acct.deposit(1, 0); // Cannot deposit $0
	}

	@Test
	public void testOVERDRAFTEXCEPTION() {
		Account a = acct.getAccount(1);
		double w1 = a.getBalance() + 1;
		expectedException.expect(OverDraftException.class);
		acct.withdraw(1, w1); // w is more than amt in acct 1

	}

	@Test
	public void testTRANSFEREXCEPTION() {
		Account a = acct.getAccount(1);
		Account a2 = acct.getAccount(2);

		double t = a.getBalance() + a2.getBalance();
		expectedException.expect(OverDraftException.class);
		acct.transfer(1, 2, t); // t is more than amt in acct 1
		expectedException.expect(OverDraftException.class);
		acct.transfer(2, 1, t); // t is more than amt in acct 2

	}

	@Test
	public void testUNAPPROVEDACCOUNTS() { // Return -1 cause unopen
		expectedException.expect(UnOpenException.class);
		acct.deposit(2, 5000);

		expectedException.expect(UnOpenException.class);
		acct.withdraw(2, 5000);

		expectedException.expect(UnOpenException.class);
		acct.transfer(1, 2, 5000);
		acct.transfer(2, 1, 5000);
	}

	@Ignore
	// @Test
	public void testACCT() {
		//User u = user.getUser(1);
		Account a = acct.getAccount(1);
		Account c = acct.getAccount(3);
		Account d = acct.getAccount(4);
		ArrayList<Account> mine = new ArrayList<>();
		mine.add(a);
		mine.add(c);
		mine.add(d);
	}
}
