package com.revature;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.exceptions.DepositException;
import com.revature.exceptions.MinimumDepositException;
import com.revature.exceptions.OverDraftException;
import com.revature.exceptions.UnOpenException;
import com.revature.model.Account;
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
		String username, password, fname, lname, email, repassword, confirm;
		boolean registering = true;
		Scanner s = new Scanner(System.in);
		User user2;

		log.info("Attempting to register a profile.\n");

		do {
			System.out.println("\nRegister a profile");

			System.out.println("Enter first and last name");
			fname = s.next();
			lname = s.next();

			System.out.println("Enter Email");
			email = s.next();

			System.out.println("Enter User Name");
			username = s.next();

			System.out.println("Enter Password");
			password = s.next();

			System.out.println("Reenter Password");
			repassword = s.next();

			if (password.equals(repassword)) {
				System.out.println("\nName: " + fname + " " + lname + "\nUser Name: " + username + "\nEmail: " + email);

				System.out.println("Is the above information correct?");
				confirm = s.next();

				if (confirm.startsWith("y") || confirm.startsWith("Y")) {

					try {
						user2 = userserv.newUser(new User(username, password, fname, lname, email));

						CustomerMenu(user2);
						registering = false;

					} catch (NullPointerException e) {
						System.out.println("Could not register user.\n");
						log.warn("Could not register user.\n");
					}
				} else if (confirm.startsWith("n") || confirm.startsWith("N")) {
					registering = false;
				}
			} else {
				System.out.println("Password error.\n");
			}

		} while (registering);
	}

	private static void Login() {
		System.out.println("Login");
		Scanner s = new Scanner(System.in);
		String username, password;

		System.out.println("Enter User Name");
		username = s.next();
		System.out.println("Enter Password");
		password = s.next();

		System.out.println();
		log.info("Login attempt.\n");

		try {
			UserService userserv = new UserServiceImpl();
			User user = userserv.userlogin(username, password);
			String role = user.getRole().getRole();

			if (user.getRole().getRole().equalsIgnoreCase("Customer")) {

				System.out.println(username + " successfully logged in.");
				CustomerMenu(user);

			} else if (role.equalsIgnoreCase("Employee") || role.equalsIgnoreCase("Admin")) {

				System.out.println(role + ": " + username + " successful logged in.");
				EmployeeMenu(user);

			}
		} catch (NullPointerException e) {
			System.out.println(e + "\n");
		}
	}

	private static void CustomerMenu(User u) {
		boolean login = true;
		Scanner s = new Scanner(System.in);

		log.info(u.getUsername() + " is in Customer Menu.\n");

		do {
			int custselect = -1;

			try {
				System.out.println("Welcome " + u.getUsername() + " would you like to:");
				System.out.println("1. View profile and accounts");
				System.out.println("2. Open an Account?");
				System.out.println("3. Make a Deposit?");
				System.out.println("4. Make a Withdrawl?");
				System.out.println("5. Make a Transfer?");
				System.out.println("0. Logout?");
				custselect = s.nextInt();

			} catch (InputMismatchException e) {
				System.out.println("Not a valid number\n");
				log.warn(e + "\n");
			}
			s.nextLine();

			switch (custselect) {
			case 1:
				log.info("Chose option to view profile.");
				userfinder(u);
				break;
			case 2:
				log.info("Chose option to open account.");
				openAccount(u);
				break;
			case 3:
				log.info("Chose option to deposit.\n");
				deposit(u);
				break;
			case 4:
				log.info("Chose option to withdraw.\n");
				withdraw(u);
				break;
			case 5:
				log.info("Chose option to transfer.\n");
				transfer(u);
				break;
			default:
				System.out.println("Option not available. Try again.\n");
				break;
			case 0:
				System.out.println("Logged out\n");
				log.info(u.getUsername() + " logged out\n");
				login = false;
				break;
			}
		} while (login);

	}

	private static void userfinder(User u) {
		UserService userserv = new UserServiceImpl();
		User result = userserv.getUser(u.getUserId());
		System.out.println(result);
	}

	private static void openAccount(User u) {
		AccountService acctserv = new AccountServiceImpl();
		UserService userserv = new UserServiceImpl();
		String type = null, confirm;
		Scanner s = new Scanner(System.in);

		log.info(u.getUsername() + " attempting to open account.\n");

		try {
			System.out.println("\nWhat type of account do you want? Checkings or Savings");
			String select = s.next();

			if (select.startsWith("c") || select.startsWith("C")) {
				type = "Checkings";

			} else if (select.startsWith("s") || select.startsWith("S")) {
				type = "Savings";

			} else {
				System.out.println("Chose non-exisiting account type.");
				log.warn("Chose non-exisiting account type.\n");
				throw new NullPointerException();
			}

			System.out.println("\nEnter a minimum deposit of $500.00");
			double deposit = s.nextDouble();

			System.out.println();
			log.info("Attempting to identify user to open an account.\n");
			User user = userserv.getUser(u.getUserId());// System.out.println(user);

			AccountType at = new AccountType(type);// System.out.println(at);

			Account a = new Account(deposit, at);// System.out.println(a);

			System.out.println("User ID: " + u.getUserId() + "\t\tName: " + u.getFirstName() + " " + u.getLastName()
					+ "\nUser Name: " + u.getUsername() + "\tEmail: " + u.getEmail() + "\nType: " + type
					+ "\t\tInitial Deposit: $" + deposit);
			System.out.println("Is the above information correct?");
			confirm = s.next();

			if (confirm.startsWith("y") || confirm.startsWith("Y")) {

				System.out.println();
				log.info("Attempting to open account.\n");
				acctserv.newAccount(a, user);// System.out.println(account);

			}
		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e + "\n");

		} catch (NullPointerException e) {
			System.out.println("Could not open account.\n");
			log.warn(e + "\n");

		} catch (MinimumDepositException e) {
			System.out.println(e + "\n");
		}
	}

	private static void deposit(User u) {
		Scanner s = new Scanner(System.in);
		AccountService acctserv = new AccountServiceImpl();

		try {
			System.out.println("\nBanking deposits");
			System.out.println("Enter account id");
			int aID = s.nextInt();

			boolean owner = acctserv.isOwner(u.getUserId(), aID);

			if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println("How much are you depositing?");
				double b = s.nextDouble();

				log.info(u.getUsername() + " is attempting to deposit $" + b + " in acctID: " + aID + ".\n");

				acctserv.deposit(aID, b);

				log.info(u.getUsername() + " deposited $" + b + " into acctID: " + aID + ".\n");

			} else {
				System.out.println(u.getUsername() + " did not have permission to access acctID: " + aID + ".\n");
				log.warn(u.getUsername() + " did not have permission to access acctID: " + aID + ".\n");
			}

		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e);

		} catch (NullPointerException e) {
			System.out.println("Account does not exist\n");
			log.warn("Account does not exist.\n");

		} catch (UnOpenException e) {
			System.out.println(e + "\n");
			log.warn("Tried to access unopen account.\n");

		} catch (DepositException e) {
			System.out.println(e + "\n");
			log.warn("Tried to deposit $0 into account.\n");
		}
	}

	private static void withdraw(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		try {
			System.out.println("\nBanking withdrawls");
			System.out.println("Enter account id");
			int aID = s.nextInt();

			boolean owner = acctserv.isOwner(u.getUserId(), aID);

			if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println("How much are you withdrawing?");
				double b = s.nextDouble();

				log.info(u.getUsername() + " is attempting to withdraw $" + b + " from acctID: " + aID + ".\n");

				acctserv.withdraw(aID, b);

				log.info(u.getUsername() + " withdrew $" + b + " from acctID: " + aID + ".\n");// }

			} else {
				System.out.println(u.getUsername() + " did not have permission to access acctID: " + aID + ".\n");
				log.warn(u.getUsername() + " did not have permission to access acctID: " + aID + ".\n");
			}

		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e);

		} catch (NullPointerException e) {
			System.out.println("Account does not exist\n");
			log.warn("Account does not exist.\n");

		} catch (UnOpenException e) {
			System.out.println(e + "\n");
			log.warn("Tried to access unopen account.\n");

		} catch (OverDraftException e) {
			System.out.println(e + "\n");
			log.warn("Tried to overdraw from account.\n");
		}

	}

	private static void transfer(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		try {

			System.out.println("\nBanking transfers");
			System.out.println("Enter source account id");
			int source = s.nextInt();

			System.out.println("Enter target account id");
			int target = s.nextInt();

			boolean owner = acctserv.isOwner(u.getUserId(), source);

			if (owner == true || u.getRole().getRole().equalsIgnoreCase("Admin")) {
				System.out.println("How much are you transfering?");
				double b = s.nextDouble();

				log.info(u.getUsername() + " is attempting to transfer $" + b + " from acctID: " + source
						+ " to acctID: " + target + ".\n");

				acctserv.transfer(source, target, b);

				log.info(u.getUsername() + " transfered $" + b + " from acctID: " + source + " to acctID: " + target
						+ ".\n");

			} else {
				System.out.println(u.getUsername() + " did not have permission to access accounts.\n");
				log.warn(u.getUsername() + " did not have permission to access accounts.\n");
			}

		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e);

		} catch (NullPointerException e) {
			System.out.println("Account does not exist\n");
			log.warn("Account does not exist.\n");

		} catch (UnOpenException e) {
			System.out.println(e + "\n");
			log.warn("Tried to access unopen account.\n");

		} catch (OverDraftException e) {
			System.out.println(e + "\n");
			log.warn("Tried to overdraw from account.\n");
		}
	}

	private static void EmployeeMenu(User u) {
		Scanner s = new Scanner(System.in);
		Boolean login = true;

		System.out.println();
		log.info(u.getRole().getRole() + ": " + u.getUsername() + " is in Employee Menu.\n");

		do {
			int emplselect = -1;

			try {

				System.out.println("\nWelcome " + u.getUsername() + ", here are a range of options");
				System.out.println("1. View all Customers");
				System.out.println("2. Find User");
				System.out.println("3. Find Accounts");
				System.out.println("4. Approve/Deny account");
				System.out.println("5. Update accounts");
				System.out.println("0. Logout");
				emplselect = s.nextInt();

			} catch (InputMismatchException e) {
				System.out.println("Not a valid number\n");
				log.warn(e + "\n");
			}
			s.nextLine();

			switch (emplselect) {
			case 1:
				log.info("Chose option to view customers.\n");
				collector();
				break;

			case 2:
				log.info("Chose option to find customer.\n");
				emplfinder();
				break;

			case 3:
				log.info("Chose option to find customer.\n");
				acctfinder();
				break;

			case 4:
				log.info("Chose option to approve or deny accounts.\n");
				status(u);
				break;

			case 5:
				log.info("Chose option to perform transactions on accounts.\n");
				transaction(u);
				break;

			case 0:
				System.out.println("Logged out\n");
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

	private static void emplfinder() {
		UserService userserv = new UserServiceImpl();
		Scanner s = new Scanner(System.in);

		try {
			System.out.println("Enter userid");
			int id = s.nextInt();
			System.out.println(userserv.getUser(id));

		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e + "\n");

		} catch (NullPointerException e) {
			System.out.println(e + "\n");
			log.warn("Could not find userid.\n");
		}
	}

	private static void acctfinder() {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);

		try {
			System.out.println("Enter acctid");
			int id = s.nextInt();
			acctserv.getAccount(id);

		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e + "\n");

		} catch (NullPointerException e) {
			System.out.println("Could not find acctid.\n");
			log.warn("Could not find acctid.\n");
		}
	}

	private static void status(User u) {
		AccountService acctserv = new AccountServiceImpl();
		Scanner s = new Scanner(System.in);
		String newstatus = null;

		log.info(u.getRole().getRole() + ": " + u.getUsername() + " attempting to change account status.\n");

		try {
			System.out.println("Enter account id");
			int sID = s.nextInt();

			Account a = acctserv.getAccount(sID);

			System.out.println("Pick a status? Open, Pending, Denied, Close.");
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
				System.out.println("Status does not exist.");
				log.warn("Chose a non-existing status.\n");
				throw new NullPointerException("Could not change status");
			}

			if (newstatus.equalsIgnoreCase("Close") && !(u.getRole().getRole().equalsIgnoreCase("Admin"))) {
				System.out.println(u.getRole().getRole() + ": " + u.getUsername()
						+ " does not have authorization to close acctID: " + sID + ".\n");
				log.warn(u.getRole().getRole() + ": " + u.getUsername()
						+ " does not have authorization to close acctID: " + sID + ".\n");

			} else {
				acctserv.change(a.getStatus().getStatusId(), newstatus);

				log.info(u.getUsername() + " changed acctID: " + sID + " status to " + newstatus + ".\n");
			}

		} catch (InputMismatchException e) {
			System.out.println("Not a valid number\n");
			log.warn(e + "\n");

		} catch (NullPointerException e) {
			System.out.println(e + "\n");
			log.warn("Could not change status.\n");
		}

	}

	private static void transaction(User u) {
		boolean login = true;
		Scanner s = new Scanner(System.in);

		log.info(u.getRole().getRole() + ": " + u.getUsername() + " is attempting to update accounts.\n");

		do {
			int select = -1;

			try {

				System.out.println("\nWould you like to:");
				System.out.println("1. Make a Deposit?");
				System.out.println("2. Make a Withdrawl?");
				System.out.println("3. Make a Transfer?");
				System.out.println("0. Exit?");
				select = s.nextInt();

			} catch (InputMismatchException e) {
				System.out.println("Not a valid number\n");
				log.warn(e + "\n");
			}
			s.nextLine();

			switch (select) {
			case 1:
				log.info("Chose option to deposit.\n");
				deposit(u);
				break;

			case 2:
				log.info("Chose option to withdraw.\n");
				withdraw(u);
				break;

			case 3:
				log.info("Chose option to transfer.\n");
				transfer(u);
				break;

			case 0:
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