/*package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.Message;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController {

	public static void collector(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		UserService userserv = new UserServiceImpl();
		res.setContentType("application/json");
		PrintWriter ps = res.getWriter();

		try {
			HttpSession session = req.getSession();
			String r = (String) session.getAttribute("role");

			if (!("ADMIN".equalsIgnoreCase(r) || "EMPLOYEE".equalsIgnoreCase(r))) {
				res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted.")));
			} else {
				ArrayList<User> Customer = userserv.getAllUser();

				for (User u : Customer) {
					ps.write(new ObjectMapper().writeValueAsString(u));
				}
			}
		} catch (NullPointerException e) {
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted.")));
		}
	}

	public static void finder(HttpServletRequest req, HttpServletResponse resp) throws JsonProcessingException, IOException {
		UserService userserv = new UserServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		try {
			HttpSession session = req.getSession();
			int i = (int) session.getAttribute("id");// userid
			String r = (String) session.getAttribute("role");

			int id = Integer.parseInt(req.getParameter("id"));
			
			User owner = userserv.getUser(i);

			if (r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("EMPLOYEE") || (owner != null && i == id)) {
				User result = userserv.getUser(id);
				if (result != null) {
					ps.write(new ObjectMapper().writeValueAsString(result));
					// resp.setStatus(HttpServletResponse.SC_ACCEPTED);
				} else {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					ps.write(new ObjectMapper().writeValueAsString(new Message("Does not exist.")));
				}
			} else {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted.")));
			}
		} catch (NullPointerException e) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted.")));
		}
	}

	public static void updater(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService userserv = new UserServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		int id = Integer.parseInt(req.getParameter("id"));
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");
		Integer.parseInt(req.getParameter("roleid"));

		try {
			HttpSession session = req.getSession();
			int i = (int) session.getAttribute("id"); //userid
			String r = (String) session.getAttribute("role");

			User o = userserv.getUser(i);

			if (r.equalsIgnoreCase("ADMIN") || (o != null && i == id)) {
				User u = userserv.updateUser(id, username, password, firstname, lastname, email);
				if (u != null) {
					ps.write(new ObjectMapper().writeValueAsString(u));
					// resp.setStatus(HttpServletResponse.SC_ACCEPTED);
				} else {
					resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
					ps.write(new ObjectMapper().writeValueAsString(new Message("Does not exist.")));
				}
			} else {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted.")));
			}
		} catch (NullPointerException e) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action is not permitted.")));
		}
	}
}*/