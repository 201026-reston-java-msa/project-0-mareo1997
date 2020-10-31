/*package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.Account;
import com.example.model.AccountStatus;
import com.example.model.AccountType;
import com.example.model.Message;
import com.example.model.User;
import com.example.service.AccountService;
import com.example.service.AccountServiceImpl;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountController {

	public static void collector(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();
		
		try {
			HttpSession session = req.getSession();
			String r = (String) session.getAttribute("role");

			if (r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("EMPLOYEE")) {
				List<Account> AccountList = acctserv.getAllAccount();
				for (Account a : AccountList) {
					ps.write(new ObjectMapper().writeValueAsString(a));
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

	public static void deposit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		int aID = Integer.parseInt(req.getParameter("accountid"));
		double b = Double.parseDouble(req.getParameter("amount"));

		try {
			HttpSession session = req.getSession();
			int oID = (int) session.getAttribute("id");
			String r = (String) session.getAttribute("role");

			boolean owner = acctserv.isOwner(oID, aID);

			if ("ADMIN".equalsIgnoreCase(r) || owner == true) {
				double i = acctserv.deposit(aID, b);
				if(!(i==0)) {
					ps.write(new ObjectMapper().writeValueAsString(new Message("$" + b + " has been deposited to Account #" + aID)));
				}else {
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action could not be permitted.")));
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

	public static void withdraw(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		int aID = Integer.parseInt(req.getParameter("accountid"));
		double b = Double.parseDouble(req.getParameter("amount"));

		try {
			HttpSession session = req.getSession();
			int oID = (int) session.getAttribute("id");
			String r = (String) session.getAttribute("role");

			boolean owner = acctserv.isOwner(oID, aID);

			if ("ADMIN".equalsIgnoreCase(r) || owner == true) {
				double i = acctserv.withdraw(aID, b);
				if(!(i==0)) {
					ps.write(new ObjectMapper().writeValueAsString(new Message("$" + b + " has been withdrawn from Account #" + aID)));
				}else {
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action could not be permitted.")));
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

	public static void trasfer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		int a = Integer.parseInt(req.getParameter("sourceAccountId"));
		int c = Integer.parseInt(req.getParameter("targetAccountId"));
		double b = Double.parseDouble(req.getParameter("amount"));

		try {
			HttpSession session = req.getSession();
			int i = (int) session.getAttribute("id");
			String r = (String) session.getAttribute("role");

			boolean owner = acctserv.isOwner(i, a);

			if (r.equalsIgnoreCase("ADMIN") || owner == true) {
				double h = acctserv.transfer(a, c, b);
				if(!(h==0)) {
					ps.write(new ObjectMapper().writeValueAsString(new Message("$" + b + " has been transferred from Account #" + a + " to Account #" + c)));
				}else {
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
					ps.write(new ObjectMapper().writeValueAsString(new Message("The requested action could not be permitted.")));
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

	public static void finder(HttpServletRequest req, HttpServletResponse resp) throws JsonProcessingException, IOException {
		AccountService acctserv = new AccountServiceImpl();
		int id = Integer.parseInt(req.getParameter("id"));// account id
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		try {
			HttpSession session = req.getSession();
			int i = (int) session.getAttribute("id");// userid
			String r = (String) session.getAttribute("role");

			boolean owner = acctserv.isOwner(i, id);// Checks if user owns account

			if (r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("EMPLOYEE") || owner == true) {
				Account a = acctserv.getAccount(id);
				if(a!=null) {
					ps.write(new ObjectMapper().writeValueAsString(a));
				}else {
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

	public static void submit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		UserService userserv = new UserServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();
		
		double b = Double.parseDouble(req.getParameter("amount"));
		String type = req.getParameter("type");
		int sID = Integer.parseInt(req.getParameter("statusid"));
		int tID = Integer.parseInt(req.getParameter("typeid"));
		int oID = Integer.parseInt(req.getParameter("ownerid"));

		try {
			HttpSession session = req.getSession();
			int i = (int) session.getAttribute("id");// userid
			String r = (String) session.getAttribute("role");

			User owner = userserv.getUser(i);

			if (r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("EMPLOYEE") || (owner != null && i == oID)) {
				User u = userserv.getUser(oID);
				AccountStatus as = new AccountStatus(sID, "Pending");
				AccountType at = new AccountType(tID, type);
				Account a = new Account(b, as, at);
				Account account = acctserv.newAccount(a, u);
				if (account == null) {
					resp.setStatus(HttpServletResponse.SC_CONFLICT);
					ps.write(new ObjectMapper().writeValueAsString(new Message("Violates constraint.")));
				} else {
					resp.setStatus(HttpServletResponse.SC_CREATED);
					ps.write(new ObjectMapper().writeValueAsString(account));
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

	public static void statusfinder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		String status = req.getParameter("status");
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		try {
			HttpSession session = req.getSession();
			String r = (String) session.getAttribute("role");

			if (r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("EMPLOYEE")) {
				List<Account> AccountList = acctserv.getAllStatus(status);
				for (Account a : AccountList) {
					ps.write(new ObjectMapper().writeValueAsString(a));
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

	public static void ownerfinder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		AccountService acctserv = new AccountServiceImpl();
		UserService userserv = new UserServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		int oID = Integer.parseInt(req.getParameter("ownerid"));

		try {
			HttpSession session = req.getSession();
			int i = (int) session.getAttribute("id");
			String r = (String) session.getAttribute("role");
			User owner = userserv.getUser(i);

			if (r.equalsIgnoreCase("ADMIN") || r.equalsIgnoreCase("EMPLOYEE") || (owner != null && i == oID)) {
				User result = userserv.getUser(oID);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(result);

				for (Account a : AccountList) {
					ps.write(new ObjectMapper().writeValueAsString(a));
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
		AccountService acctserv = new AccountServiceImpl();
		resp.setContentType("application/json");
		PrintWriter ps = resp.getWriter();

		int aID = Integer.parseInt(req.getParameter("id"));
		double balance = Double.parseDouble(req.getParameter("balance"));
		int sID = Integer.parseInt(req.getParameter("statusid"));
		String status = req.getParameter("status");
		int tID = Integer.parseInt(req.getParameter("typeid"));
		String type = req.getParameter("type");
		int oID = Integer.parseInt(req.getParameter("ownerid"));

		try {
			HttpSession session = req.getSession();
			String r = (String) session.getAttribute("role");

			if (r.equalsIgnoreCase("ADMIN")) {
				Account a = acctserv.updateAccount(aID, balance, sID, status, tID, type, oID);
				if (a != null) {
					ps.write(new ObjectMapper().writeValueAsString(a));
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
