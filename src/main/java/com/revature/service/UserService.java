package com.revature.service;

import java.util.ArrayList;

import com.revature.model.User;

public interface UserService {

	public ArrayList<User> getAllUser();

	public User getUser(int id);

	public User userlogin(String username, String password);

	public User newUser(User u);

	public User updateUser(int id, String username, String password, String firstname, String lastname, String email);

}
