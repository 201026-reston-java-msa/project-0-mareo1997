/*package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.postgresql.util.PSQLException;

import com.example.model.Message;
import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegisterController {

	public static void register(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException, PSQLException {
		UserService userserv = new UserServiceImpl();
		User user1 = null, user2 = null;

		HttpSession session = request.getSession();
		String r = (String) session.getAttribute("role");
		response.setContentType("application/json");
		PrintWriter ps = response.getWriter();

		if (!("ADMIN".equalsIgnoreCase(r))) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted")));
		} else {
//			int id = Integer.parseInt(request.getParameter("id"));
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String email = request.getParameter("email");
			int roleid = Integer.parseInt(request.getParameter("roleid"));

			Role role = new Role(roleid, "Customer");
			user1 = new User(username, password, firstname, lastname, email, role);
			user2 = userserv.newUser(user1);

			if (user2 != null) {
				response.setStatus(HttpServletResponse.SC_CREATED);
				ps.write(new ObjectMapper().writeValueAsString(user2));
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				ps.write(new ObjectMapper().writeValueAsString(new Message("Invalid fields")));
			}
		}
	}

}
*/