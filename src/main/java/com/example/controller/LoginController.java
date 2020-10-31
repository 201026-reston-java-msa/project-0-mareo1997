/*package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.Message;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginController {

	public static void login(HttpServletRequest request, HttpServletResponse resp) throws JsonProcessingException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		UserService userserv = new UserServiceImpl();
		User user = userserv.userlogin(username, password);

		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();
		if (user!=null) {
			HttpSession session = request.getSession();
			session.setAttribute("login",user);
			session.setAttribute("id", user.getUserId());
			session.setAttribute("role", user.getRole().getRole());
			ps.write(new ObjectMapper().writeValueAsString(user));
		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			ps.write(new ObjectMapper().writeValueAsString(new Message("Invalid credentials")));
		}
	}

}
*/