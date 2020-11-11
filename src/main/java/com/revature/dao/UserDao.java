package com.revature.dao;

import java.util.ArrayList;

import com.revature.model.User;

public interface UserDao {

	// Create
	public User insertUser(User u);

	// Read
	public ArrayList<User> selectAllUser();

	public User selectUserById(int id);

	public User userlogin(String username, String password);

	// Update
	//public User updateUser(int id, String username, String password, String firstname, String lastname, String email);

}
