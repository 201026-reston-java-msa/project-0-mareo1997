package com.revature.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revature.model.User;
import com.revature.service.UserServiceImpl;

public class UserTesting {
	private static final UserServiceImpl user = new UserServiceImpl();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/************************************** Test new user *********************************/

	@Ignore
	@Test
	public void testINSERT() {// Must change username or email after each use
		User u = new User("mareo", "password", "Mareo", "Yapp", "mareo@gmail.com");
		assertEquals("Mareo", user.newUser(u).getFirstName());
	}

	@Test
	public void testINSERTDUPLICATEUSERNAME() {
		expectedException.expect(NullPointerException.class);
		user.newUser(new User("mareo1997", "password", "Mareo", "Yapp", "mareo@hotmail.com"));
	}

	@Test
	public void testINSERTDUPLICATEEMAIL() {
		expectedException.expect(NullPointerException.class);
		user.newUser(new User("mareo", "password", "Mareo", "Yapp", "mareo1997@gmail.com"));
	}

	/*************************************** Test Login *********************************/

	@Test
	public void testLOGIN() {
		assertEquals(3, user.userlogin("king", "george").getUserId());
	}

	@Test
	public void testFAKELOGIN() {
		expectedException.expect(NullPointerException.class);
		user.userlogin("fake", "user");
	}

	/*************************************** Test User id *********************************/

	@Test
	public void testUSERID() {
		assertEquals("mareo1997", user.getUser(1).getUsername());
	}

	@Test
	public void testFAKEID() {
		expectedException.expect(NullPointerException.class);
		user.getUser(0);// assertEquals(null, user.getUser(0));
	}

	/**********************************************************************************/

	@Ignore
	public void testUSERS() {
		User u1 = user.getUser(1);
		User u2 = user.getUser(2);
		User u3 = user.getUser(3);
		ArrayList<User> mine = new ArrayList<>();
		mine.add(u1);
		mine.add(u2);
		mine.add(u3);
		// Compareto
		// System.out.println(mine.equals(user.getAllUser()));
		mine.removeAll(user.getAllUser());
		System.out.println(mine);
		assertTrue(mine.equals(user.getAllUser()));
	}
}
