package com.revature;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.exceptions.DepositException;
import com.revature.exceptions.OverDraftException;
import com.revature.exceptions.UnOpenException;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.AccountServiceImpl;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		boolean run = true;

		while (run) {
			System.out.println("Do you have an account? Yes/No");
			String a = s.next();
			if (a.startsWith("y") || a.startsWith("Y")) {
				Login();
			} else if (a.startsWith("n") || a.startsWith("N")) {
				Register();
			} else {
				System.out.println();
				log.info("Ended\n");
				run = false;
			}
		}
	}

	private static void Register() {
		UserService userserv = new UserServiceImpl();
		String username, password, fname, lname, email, repassword;
		boolean registering = true;
		Scanner s = new Scanner(System.in);

		System.out.println();
		log.info("Attempting to register a profile.\n");

		do {
			System.out.println("Register a profile");
			System.out.println("Enter Name");
			fname = s.next();
			lname = s.next();
			System.out.println("Enter Email");
			email = s.next();
			System.out.println("Enter Username");
			username = s.next();
			System.out.println("Enter Password");
			password = s.next();//
			System.out.println("Reenter Password");//
			repassword = s.next();//
			if (password.equals(repassword)) {//
				registering = false;//
			} else {//
				System.out.println("Password error.");// }
			}

		} while (registering);

		try {
			User user1 = new User(username, password, fname, lname, email);// System.out.println(user1);
			User user2 = userserv.newUser(user1);// System.out.println(user2); //if (user2 != null) { //
													// System.out.println("Registration sucessful"); //
													// System.out.println(user2);
			CustomerMenu(user2);// } else { // System.out.println("Failure to register account\n");
		} catch (NullPointerException e) {
		}
	}

	private static void Login() {
		System.out.println("Login");
		Scanner s = new Scanner(System.in);
		String username, password;

		System.out.println("Enter Username");
		username = s.next();
		System.out.println("Enter Password");
		password = s.next();

		System.out.println();
		log.info("Login attempt.\n");

		try {
			UserService userserv = new UserServiceImpl();
			User user = userserv.userlogin(username, password);// if (user != null) { //user.getUserId();

			if (user.getRole().getRole().equalsIgnoreCase("Customer")) {
				System.out.println(username + " successfully logged in.");
				CustomerMenu(user);
			} else if (user.getRole().getRole().equalsIgnoreCase("Employee")
					|| user.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println(user.getRole().getRole() + ": " + username + " successful logged in.");
				EmployeeMenu(user);
			} // } else { // System.out.println("Invalid username or password.\n"); //}
		} catch (NullPointerException e) {
		}
	}

	private static void CustomerMenu(User u) {
		boolean login = true;
		Scanner s = new Scanner(System.in);
		String cust = null;
		int custselect = 0;

		System.out.println();
		log.info(u.getUsername() + " is in Customer Menu.\n");

		do {

			System.out.println("Welcome " + u.getUsername() + " would you like to:");
			System.out.println("1. Open an Account?");
			System.out.println("2. Make a Deposit?");
			System.out.println("3. Make a Withdrawl?");
			System.out.println("4. Make a Transfer?");
			System.out.println("5. Logout?");
			cust = s.next();

			if (cust.startsWith("o") || cust.startsWith("O") || cust.startsWith("A") || cust.startsWith("a")) {
				custselect = 1;
			} else if (cust.startsWith("d") || cust.startsWith("D")) {
				custselect = 2;
			} else if (cust.startsWith("w") || cust.startsWith("W")) {
				custselect = 3;
			} else if (cust.startsWith("t") || cust.startsWith("T")) {
				custselect = 4;
			} else if (cust.startsWith("l") || cust.startsWith("L")) {
				custselect = -1;
			} else {
				custselect = 0;
			}

			System.out.println();
			switch (custselect) {
			case 1:
				log.info("Choose option to open account.");
				openAccount(u);
				break;
			case 2:
				log.info("Choose option to deposit.\n");
				deposit(u);
				break;
			case 3:
				log.info("Choose option to withdraw.\n");
				withdraw(u);
				break;
			case 4:
				log.info("Choose option to transfer.\n");
				transfer(u);
				break;
			case -1:
				log.info(u.getUsername() + " logged out\n");
				login = false;
				break;
			default:
				System.out.println("Option not available. Try again.\n");
				break;
			}
		} while (login);
	}

	private static void openAccount(User u) {
		AccountService acctserv = new AccountServiceImpl();
		UserService userserv = new UserServiceImpl();
		String type = null;
		Scanner s = new Scanner(System.in);

		System.out.println();
		log.info(u.getUsername() + " attempting to open account.\n");

		System.out.println("What type of account do you want? Checkings or Savings");
		String select = s.next();
		if (select.startsWith("c") || select.startsWith("C")) {
			type = "Checkings";
		} else if (select.startsWith("s") || select.startsWith("S")) {
			type = "Savings";
		} else {
			System.out.println();
			log.warn("Choose non-exisiting account type.\n");
			CustomerMenu(u);
		}

		System.out.println("\nEnter a minimum deposit of $500.00");
		double deposit = s.nextDouble();
		if (deposit < 500.00) {
			System.out.println();
			log.warn("Initial deposit did not meet minimum deposit.\n");
			System.out.println("Initial deposit did not meet minimum deposit.");
			CustomerMenu(u);
		} else {

			System.out.println();
			log.info("Attempting to identify user to open an account.\n");
			User user = userserv.getUser(u.getUserId());// System.out.println(user);

			AccountStatus as = new AccountStatus("Pending");
			AccountType at = new AccountType(type);// System.out.println(at);

			Account a = new Account(deposit, as, at);// System.out.println(a);

			System.out.println();
			log.info("Attempting to open account.\n");
			Account account = acctserv.newAccount(a, user);// System.out.println(account);

			if (account != null) {
				System.out.println("Successfully opened account. Approval Pending.");
			} else {
				System.out.println("Failure to open account.");
				log.warn("Failure to open account.\n");
			}
		}
	}

	private static void deposit(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		System.out.println("Banking deposits");
		System.out.println("Enter account id");
		int aID = s.nextInt();

		try {
			System.out.println();
			log.info("Attempting to validate if " + u.getUserId() + " owns acctID: " + aID);
			boolean owner = acctserv.isOwner(u.getUserId(), aID);

			if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println("How much are you depositing?");
				double b = s.nextDouble();

				System.out.println();
				log.info(u.getUsername() + " is attempting to deposit $" + b + " in acctID: " + aID + ".\n");

				double i = acctserv.deposit(aID, b);

				System.out.println();
				log.info(u.getUsername() + " deposited $" + b + " into acctID: " + aID + ".\n");// }
			} else {
				System.out.println();
				log.warn(u.getUsername() + " did not have permission to access acctID: " + aID + ".\n");
			}
		} catch (NullPointerException e) {
			System.out.println();
			log.warn("acctID: " + aID + " does not exist.\n");
		} catch (UnOpenException e) {
			System.out.println();
			log.warn("Tried to access unopen acctID: " + aID + ".\n");
		} catch (DepositException e) {
			System.out.println();
			log.warn("Tried to deposit $0 int acctID: " + aID + ".\n");
		}
	}

	private static void withdraw(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		System.out.println("Banking withdrawls");
		System.out.println("Enter account id");
		int aID = s.nextInt();

		try {
			System.out.println();
			log.info("Attempting to validate if " + u.getUserId() + " owns acctID: " + aID);
			boolean owner = acctserv.isOwner(u.getUserId(), aID);

			if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println("How much are you withdrawing?");
				double b = s.nextDouble();

				System.out.println();
				log.info(u.getUsername() + " is attempting to withdraw $" + b + " from acctID: " + aID + ".\n");

				double i = acctserv.withdraw(aID, b);

				System.out.println();
				log.info(u.getUsername() + " withdrew $" + b + " from acctID: " + aID + ".\n");// }
			} else {
				System.out.println();
				log.warn(u.getUsername() + " did not have permission to access acctID: " + aID + ".\n");
			}
		} catch (NullPointerException e) {
			System.out.println();
			log.warn("acctID: " + aID + " does not exist.\n");
		} catch (UnOpenException e) {
			System.out.println();
			log.warn("Tried to access unopen acctID: " + aID + ".\n");
		} catch (OverDraftException e) {
			System.out.println();
			log.warn("Tried to overdraw from acctID: " + aID + ".\n");
		}
	}

	private static void transfer(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		System.out.println("Banking transfers");
		System.out.println("Enter source account id");
		int source = s.nextInt();
		System.out.println("Enter target account id");
		int target = s.nextInt();

		try {
			System.out.println();
			log.info("Attempting to validate if " + u.getUserId() + " owns acctID: " + source);
			boolean owner = acctserv.isOwner(u.getUserId(), source);

			if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println("How much are you transfering?");
				double b = s.nextDouble();

				System.out.println();
				log.info(u.getUsername() + " is attempting to transfer $" + b + " from acctID: " + source
						+ " to acctID: " + target + ".\n");

				double i = acctserv.transfer(source, target, b);

				System.out.println();
				log.info(u.getUsername() + " transfered $" + b + " from acctID: " + source + " to acctID: " + target
						+ ".\n");
			} else {
				System.out.println();
				log.warn(u.getUsername() + " did not have permission to access acctID: " + source + ".\n");
			}
		} catch (NullPointerException e) {
			System.out.println();
			log.warn("acctID: " + target + " does not exist.\n");
		} catch (UnOpenException e) {
			System.out.println();
			log.warn("Either acctID: " + source + " or acctID: " + target + " were unopen.\n");
		} catch (OverDraftException e) {
			System.out.println();
			log.warn("Tried to overdraw from acctID: " + source + ".\n");
		}
	}

	private static void EmployeeMenu(User u) {
		Scanner s = new Scanner(System.in);
		Boolean login = true;
		String empl = null;
		int emplselect = 0;

		System.out.println();
		log.info(u.getRole().getRole() + ": " + u.getUsername() + " is in Employee Menu.\n");

		do {

			System.out.println("Welcome " + u.getUsername() + ", here are a range of options");
			System.out.println("1. View all Customers");
			System.out.println("2. Approve/Deny account");
			System.out.println("3. Update accounts");
			System.out.println("4. Logout");
			empl = s.next();

			if (empl.startsWith("v") || empl.startsWith("V") || empl.startsWith("c") || empl.startsWith("C")) {
				emplselect = 1;
			} else if (empl.startsWith("a") || empl.startsWith("A") || empl.startsWith("d") || empl.startsWith("D")) {
				emplselect = 2;
			} else if (empl.startsWith("U") || empl.startsWith("u")) {
				emplselect = 3;
			} else if (empl.startsWith("l") || empl.startsWith("L")) {
				emplselect = -1;
			} else {
				emplselect = 0;
			}

			System.out.println();
			switch (emplselect) {
			case 1:
				log.info("Choose option to view customers.\n");
				collector();
				break;
			case 2:
				log.info("Choose option to approve or deny accounts.\n");
				status(u);
				break;
			case 3:
				log.info("Choose option to perform transactions on accounts.\n");
				transaction(u);
				break;
			case -1:
				log.info(u.getUsername() + " logged out\n");
				login = false;
				break;
			default:
				System.out.println("Option not available. Try again\n");
				break;
			}
		} while (login);
	}

	private static void collector() {
		UserService userserv = new UserServiceImpl();
		ArrayList<User> Customer = userserv.getAllUser();

		for (User u : Customer) {
			System.out.println(u);
		}
	}

	private static void status(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);
		String newstatus = null;

		System.out.println();
		log.info(u.getRole().getRole() + ": " + u.getUsername() + " attempting to change account status.\n");

		System.out.println("Enter account id");
		int sID = s.nextInt();

		try {
			System.out.println();
			log.info("Attempting to identify account.\n");
			Account a = acctserv.getAccount(sID);

			System.out.println("Pick a status?\nOpen, Pending, Denied, Close.");
			String status = s.next();
			if (status.startsWith("o") || status.startsWith("O")) {
				newstatus = "Open";
			} else if (status.startsWith("d") || status.startsWith("D")) {
				newstatus = "Denied";
			} else if (status.startsWith("p") || status.startsWith("P")) {
				newstatus = "Pending";
			} else if (status.startsWith("c") || status.startsWith("C")) {
				newstatus = "Close";
			} else {
				System.out.println();
				log.warn("Choose a non-existing status.\n");
				EmployeeMenu(u);
			}

			System.out.println();
			log.info("Attempting to [" + newstatus + "] acctID: " + a.getAccountId() + ".\n");

			if (newstatus.equalsIgnoreCase("Close") && !(u.getRole().getRole().equalsIgnoreCase("Admin"))) {
				System.out.println();
				log.warn(u.getRole().getRole() + ": " + u.getUsername()
						+ " does not have authorization to close acctID: " + sID + ".\n");
			} else {
				Account a1 = acctserv.change(a.getStatus().getStatusId(), newstatus);

				System.out.println();
				log.info(u.getUsername() + " changed acctID: " + sID + " status to " + newstatus + ".\n");
			}
		} catch (NullPointerException e) {
		}

	}

	private static void transaction(User u) {
		boolean login = true;
		Scanner s = new Scanner(System.in);
		int select = 0;
		String empl;

		System.out.println();
		log.info(u.getRole().getRole() + ": " + u.getUsername() + " is attempting to update accounts.\n");

		do {
			System.out.println("Would you like to:");
			System.out.println("1. Make a Deposit?");
			System.out.println("2. Make a Withdrawl?");
			System.out.println("3. Make a Transfer?");
			System.out.println("4. Exit?");
			empl = s.next();

			if (empl.startsWith("d") || empl.startsWith("D")) {
				select = 1;
			} else if (empl.startsWith("w") || empl.startsWith("W")) {
				select = 2;
			} else if (empl.startsWith("t") || empl.startsWith("T")) {
				select = 3;
			} else if (empl.startsWith("e") || empl.startsWith("E")) {
				select = -1;
			} else {
				select = 0;
			}

			System.out.println();
			switch (select) {
			case 1:
				log.info("Choose option to deposit.\n");
				deposit(u);
				break;
			case 2:
				log.info("Choose option to withdraw.\n");
				withdraw(u);
				break;
			case 3:
				log.info("Choose option to transfer.\n");
				transfer(u);
				break;
			case -1:
				log.info(u.getUsername() + " exited.\n");
				login = false;
				break;
			default:
				System.out.println("Option not available. Try again.\n");
				break;
			}
		} while (login);
	}
}