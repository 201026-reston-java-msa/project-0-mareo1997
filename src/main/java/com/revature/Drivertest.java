package com.revature;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.Role;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.AccountServiceImpl;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

public class Drivertest {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		boolean run = true;

		while (run) {
			System.out.println("Do you have an account?");
			int a = s.nextInt();
			if (a == 1) {
				Register();
			} else if (a == 0) {
				Login();
			} else {
				System.out.println("Goodbye");
				run = false;
			}
		}

	}

	private static void Register() {
		UserService userserv = new UserServiceImpl();
		int roleid = 0;
		String username, password, repassword, fname, lname, email;
		boolean registering = true;
		Scanner s = new Scanner(System.in);

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
			password = s.next();
			System.out.println("Reenter Password");
			repassword = s.next();

			try {
				System.out.println("Enter roleid");
				roleid = s.nextInt();
			} catch (InputMismatchException e) {
				System.out.print("Invalid input type.\n");
			}

			if (password.equals(repassword)) {
				registering = false;
			} else {
				System.out.println("Password error\n");
			}
		} while (registering);

		Role role = new Role(roleid, "Customer");
		User user1 = new User(username, password, fname, lname, email, role);
		User user2 = userserv.newUser(user1);

		if (user2 != null) {
			System.out.println("Registration sucessful");
			System.out.println(user2);
			CustomerMenu(user2);
		} else {
			System.out.println("Failure to register account\n");
		}
	}

	private static void Login() {
		System.out.println("Login");
		Scanner s = new Scanner(System.in);
		String username = "king", password = "george";

		System.out.println("Enter Username");
		username = s.next();
		System.out.println("Enter Password");
		password = s.next();

		UserService userserv = new UserServiceImpl();
		User user = userserv.userlogin(username, password);

		if (user != null) {
			if (user.getRole().getRole().equalsIgnoreCase("Customer")) {
				CustomerMenu(user);
			} else if (user.getRole().getRole().equalsIgnoreCase("Employee")
					|| user.getRole().getRole().equalsIgnoreCase("Admin")) {
				EmployeeMenu(user);
			}
		} else {
			System.out.println("Invalid username or password.\n");
		}
	}

	private static void CustomerMenu(User u) {
		boolean login = true;
		Scanner s = new Scanner(System.in);
		int custselect = 0;

		do {
			try {
				System.out.println("Welcome " + u.getUsername() + " would you like to:");
				System.out.println("1 <- Open an Account?");
				System.out.println("2 <- Make a Deposit?");
				System.out.println("3 <- Make a Withdrawl?");
				System.out.println("4 <- Make a Transfer?");
				System.out.println("0 <- Logout");
				custselect = s.nextInt();
			} catch (InputMismatchException e1) {
				System.out.print("Invalid input type.\n");
			}
			s.nextLine();
			switch (custselect) {
			case 1:
				openAccount(u);
				break;
			case 2:
				deposit(u);
				break;
			case 3:
				withdraw(u);
				break;
			case 4:
				transfer(u);
				break;
			case 0:
				System.out.println("Logged out.\n");
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

		System.out.println("What type of account do you want? Checking or Savings");// Change to checkings later
		String select = s.next();
		if (select.startsWith("c") || select.startsWith("C")) {
			type = "Checking";
		} else if (select.startsWith("s") || select.startsWith("S")) {
			type = "Savings";
		} else {
			CustomerMenu(u);
		}

		System.out.println("Enter type id");
		int tID = s.nextInt();

		System.out.println("Enter status id");
		int sID = s.nextInt();

		System.out.println("\nEnter a minimum deposit of $500.00");
		double deposit = s.nextDouble();
		if (deposit < 500.00) {
			System.out.println("Too low. Account rejected.\n");
			CustomerMenu(u);
		} else {
			User u1 = userserv.getUser(u.getUserId());
			AccountStatus as = new AccountStatus(sID, "Pending");
			AccountType at = new AccountType(tID, type);
			Account a = new Account(deposit, as, at);
			Account account = acctserv.newAccount(a, u1);
			if (account != null) {
				System.out.println("Successfully opened account. Approval pending.");
				System.out.println(account);
			} else {
				System.out.println("Failure to open account.\n");
			}
		}
	}

	private static void deposit(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		System.out.println("Banking deposits");
		System.out.println("Enter account id");
		int aID = s.nextInt();

		boolean owner = acctserv.isOwner(u.getUserId(), aID);

		if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
			System.out.println("How much are you depositing?");
			double b = s.nextDouble();
			double i = acctserv.deposit(aID, b);
			if (i == 0) {
				System.out.println("Transaction fail.\n");
			} else if (i == -1) {
				System.out.println("Account is not open.\n");
			} else {
				System.out.println("Deposited $" + b + " from " + aID + ".\n");
			}
		} else {
			System.out.println(
					"This account does not exist, does not belong to you or you do not have permission to edit it.\n");
		}
	}

	private static void withdraw(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		System.out.println("Banking withdrawls");
		System.out.println("Enter account id");
		int aID = s.nextInt();

		boolean owner = acctserv.isOwner(u.getUserId(), aID);

		if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
			System.out.println("How much are you withdrawing?");
			double b = s.nextDouble();
			double i = acctserv.withdraw(aID, b);
			if (i == 0) {
				System.out.println("Transaction fail.\n");
			} else if (i == -1) {
				System.out.println("Account is not open.\n");
			} else {
				System.out.println("Withdrew $" + b + " from " + aID + ".\n");
			}
		} else {
			System.out.println(
					"This account does not exist, does not belong to you or you do not have permission to edit it.\n");
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

		boolean owner = acctserv.isOwner(u.getUserId(), source);
		if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
			System.out.println("How much are you transfering?");
			double b = s.nextDouble();
			double i = acctserv.transfer(source, target, b);
			if (i == 0) {
				System.out.println("Transaction fail.\n");
			} else if (i == -1) {
				System.out.println("Account is not open.\n");
			} else {
				System.out.println("Transfered $" + b + " from " + source + " to " + target + ".\n");
			}
		} else {
			System.out.println("This account does not exist, does not belong to you or you do not have permission to edit it.\n");
		}
	}

	private static void EmployeeMenu(User u) {
		Scanner s = new Scanner(System.in);
		Boolean login = true;
		int emplselect = 0;
		do {
			try {
				System.out.println("Welcome " + u.getUsername() + ", here are a range of options");
				System.out.println("1 <- View all customers");
				System.out.println("2 <- Approve/Deny account");
				System.out.println("3 <- Update accounts");
				System.out.println("0 <- Logout");
				emplselect = s.nextInt();
			} catch (InputMismatchException e) {
				System.out.print("Invalid input type.\n");
			}
			s.nextLine();
			switch (emplselect) {
			case 1:
				collector();
				break;
			case 2:
				status(u);
				break;
			case 3:
				transaction(u);
				break;
			case 0:
				System.out.println("Logged out\n");
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

		System.out.println("Enter account id");
		int sID = s.nextInt();
		Account a = acctserv.getAccount(sID);
		// System.out.println(a);

		if (a != null) {
			System.out.println("Pick a status?\nOpen, Pending, Denied, Close");
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
				EmployeeMenu(u);
			}
			if (newstatus.equalsIgnoreCase("Close") && !(u.getRole().getRole().equalsIgnoreCase("Admin"))) {
				System.out.println("You do not have authorization to close an account.\n");
			} else {
				Account a1 = acctserv.change(a.getStatus().getStatusId(), newstatus);
				System.out.println(a1);
			}
		} else {
			System.out.println("Failure to find account.\n");
		}

	}

	private static void transaction(User u) {
		boolean login = true;
		Scanner s = new Scanner(System.in);
		int select = 0;

		do {
			try {
				System.out.println("Would you like to:");
				System.out.println("1 <- Make a Deposit?");
				System.out.println("2 <- Make a Withdrawl?");
				System.out.println("3 <- Make a Transfer?");
				System.out.println("0 <- Exit");
				select = s.nextInt();
			} catch (InputMismatchException e1) {
				System.out.print("Invalid input type.\n");
			}
			s.nextLine();
			switch (select) {
			case 1:
				deposit(u);
				break;
			case 2:
				withdraw(u);
				break;
			case 3:
				transfer(u);
				break;
			case 0:
				login = false;
				break;
			default:
				System.out.println("Option not available. Try again.\n");
				break;
			}
		} while (login);
	}
}
