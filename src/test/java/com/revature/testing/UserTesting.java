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

	@Ignore
	//@Test
	public void testINSERT() {// Must change username or email after each use

		User u = new User("mareo", "password", "Mareo", "Yapp", "mareo@gmail.com");
		assertEquals("Mareo", user.newUser(u).getFirstName());

		//u = new User("marwil", "william", "Marcia", "Williamson", "mareo199@gmail.com");
		//assertEquals("Marcia", user.newUser(u).getFirstName());

		//u = new User("king", "george", "Kingsley", "Yapp", "mareo19@gmail.com");
		//assertEquals("Kingsley", user.newUser(u).getFirstName());

		// expectedException.expect(NullPointerException.class);
		User u2 = new User("mareo", "password", "Mareo", "Yapp", "mareo@gmail.com");
		assertEquals(null, user.newUser(u2));

	}

	@Test
	public void testLOGIN() {
		assertEquals(3, user.userlogin("king", "george").getUserId());

		expectedException.expect(NullPointerException.class);
		user.userlogin("fake", "user");
	}

	@Test
	public void testUSERID() {
		assertEquals("Mareo", user.getUser(1).getFirstName());

		expectedException.expect(NullPointerException.class);
		user.getUser(0);// assertEquals(null, user.getUser(0));
	}

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
