/*package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.Message;
import com.example.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LogoutController {

	public static String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		User u = (User) session.getAttribute("login");

		response.setContentType("application/json");
		PrintWriter ps = response.getWriter();

		if (u != null) {
			session.invalidate();
			ps.write(new ObjectMapper().writeValueAsString(new Message("You have successfully logged out " + u.getUsername())));
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			ps.write(new ObjectMapper().writeValueAsString(new Message("There was no user logged into the session")));
		}
		return null;
	}

}
*/