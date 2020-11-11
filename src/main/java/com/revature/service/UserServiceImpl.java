package com.revature.service;

import java.util.ArrayList;

import com.revature.dao.UserDao;
import com.revature.dao.UserDaoImpl;
import com.revature.model.User;

public class UserServiceImpl implements UserService {

	private UserDao dao = new UserDaoImpl();

	@Override
	public User newUser(User u) {
		return dao.insertUser(u);

	}

	@Override
	public ArrayList<User> getAllUser() { //NEED TO TEST
		// our post-DB call business logic would go here
		ArrayList<User> myList = dao.selectAllUser();
		return myList;
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return dao.selectUserById(id);
	}

	@Override
	public User updateUser(int id, String username, String password, String firstname, String lastname, String email) {
		return null; //NOT USING
		//return dao.updateUser(id, username, password, firstname, lastname, email);
	}

	@Override
	public User userlogin(String username, String password) {
		return dao.userlogin(username, password);
	}
}
