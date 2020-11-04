package com.revature.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import com.revature.exceptions.DepositException;
import com.revature.exceptions.OverDraftException;
import com.revature.exceptions.UnOpenException;
import com.revature.model.Account;
import com.revature.model.AccountStatus;
import com.revature.model.AccountType;
import com.revature.model.User;
import com.revature.service.UserService;
import com.revature.service.UserServiceImpl;

public class AccountDaoImpl implements AccountDao {

	private static String url = "jdbc:postgresql://" + System.getenv("Bank_URL") + "/Bank";
	private static String sqlusername = "postgres";
	private static String sqlpassword = System.getenv("DB_PASSWORD");

	public String sql;
	public PreparedStatement ps;
	public ResultSet rs;

	private static Logger log = Logger.getLogger(AccountDaoImpl.class);

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Static block has failed");
		}
	}

	@Override
	public Account insertAccount(Account a, User u) {// System.out.println(a);//System.out.println(u);
		Account acct = null;
		AccountStatus status;
		AccountType type;
		ArrayList<AccountType> typelist = new ArrayList<>();
		ArrayList<AccountStatus> statuslist = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "INSERT INTO accountstatus(status) VALUES (?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, "Pending");
			ps.executeUpdate();// System.out.println(ps);

			sql = "INSERT INTO accounttype(accttype) VALUES (?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, a.getType().getType());
			ps.executeUpdate();

			sql = "select * from accountstatus where status='Pending'";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				statuslist.add(new AccountStatus(rs.getInt(1), rs.getString(2)));
			}
			status = statuslist.get(statuslist.size() - 1);

			sql = "select * from accounttype";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				typelist.add(new AccountType(rs.getInt(1), rs.getString(2)));
			}
			type = typelist.get(typelist.size() - 1);

			sql = "insert into account(balance, statusid, typeid, ownerid) values (?,?,?,?)";// Adds to user table
			ps = conn.prepareStatement(sql);
			ps.setDouble(1, a.getBalance());
			ps.setInt(2, status.getStatusId());
			ps.setInt(3, type.getTypeId());
			ps.setInt(4, u.getUserId());
			ps.executeUpdate();

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid "
					+ "where a.ownerid=" + u.getUserId() + " and a2.statusid =" + status.getStatusId()
					+ " and a3.typeid =" + type.getTypeId();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				AccountStatus s = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType t = new AccountType(rs.getInt(8), rs.getString(9));
				acct = new Account(rs.getInt(1), rs.getDouble(2), s, t, rs.getInt(5));
				u.addAccount(acct);

				System.out.println();
				log.info("Successfully opened AcctID: " + acct.getAccountId() + " Approval Pending.\n");
				System.out.println(acct);
			}
		} catch (PSQLException e) {
			System.out.println();
			log.warn(e);
		} catch (SQLException e) {
			System.out.println();
			log.error("Incorrect SQL syntax.\n");
		}
		return acct;
	}

	@Override
	public Account selectAccountById(int id) {
		Account a = null;
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid where a.accountid="
					+ id;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
				a = new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5));
				System.out.println();
				log.info("Identified acctID: " + a.getAccountId() + ".\n");
			}

		} catch (SQLException e) {
			System.out.println();
			log.error("Incorrect SQL syntax.\n");
		}
		if (a == null) {
			System.out.println();
			log.warn("Account does not exist.\n");
			throw new NullPointerException();
		}
		return a;
	}

	@Override
	public List<Account> selectAllAccount() {
		List<Account> AccountList = new ArrayList<Account>();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from account a " + "full join accountstatus a2 on a.statusid =a2.statusid "
					+ "full join accounttype a4 on a4.typeid =a.typeid " + "where a.accountid is not null";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
				AccountList.add(new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return AccountList;
	}

	@Override
	public ArrayList<Account> PersonalAccount(User u) {
		ArrayList<Account> AccountList = new ArrayList<Account>();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid where a.ownerid="
					+ u.getUserId();

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
				AccountList.add(new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return AccountList;
	}

	@Override
	public double deposit(int id, double b) {
		Account a = selectAccountById(id);
		double balance = a.getBalance(), oldbal = a.getBalance();

		if (a.getStatus().getStatus().equalsIgnoreCase("open")) {
			if (b > 0) {
				balance += b;
				a.setBalance(balance);
				try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
					sql = "UPDATE account set balance = " + a.getBalance() + " where accountid=" + a.getAccountId();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();

					System.out.println();
					log.info("acctID: " + a.getAccountId() + " old balance: $" + oldbal + " new balance: $"
							+ a.getBalance());// }

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println();
				log.warn("Can't deposit $0 into an account.\n");
				throw new DepositException("Can't deposit $0 into an account.");
			}
		} else {
			System.out.println();
			log.warn("Can't perform transaction on unopen accounts.\n");
			throw new UnOpenException("Can't perform transaction on unopen accounts");
		}
		return balance;
	}

	@Override
	public double withdraw(int id, double b) {
		Account a = selectAccountById(id);
		double balance = a.getBalance(), oldbal = a.getBalance();

		if (a.getStatus().getStatus().equalsIgnoreCase("open")) {
			if (b > 0 && balance >= b) {
				balance -= b;
				a.setBalance(balance);
				try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
					sql = "UPDATE account set balance = " + a.getBalance() + " where accountid=" + a.getAccountId();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();

					System.out.println();
					log.info("acctID: " + a.getAccountId() + " old balance: $" + oldbal + " new balance: $"
							+ a.getBalance());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println();
				log.warn("Withdraw cannot be greater than account.\n");
				throw new OverDraftException("Withdraw cannot be greater than account");
			}
		} else {
			System.out.println();
			log.warn("Can't perform transaction on unopen accounts.\n");
			throw new UnOpenException("Can't perform transaction on unopen accounts");
		}
		return balance;
	}

	@Override
	public double transfer(int from, int to, double b) {
		// TODO Auto-generated method stub
		Account a1 = selectAccountById(from);
		Account a2 = selectAccountById(to);
		double balance1 = a1.getBalance(), oldbal1 = a1.getBalance();
		double balance2 = a2.getBalance(), oldbal2 = a2.getBalance();
		double bal = 0;

		if (a1.getStatus().getStatus().equalsIgnoreCase("open")
				&& a2.getStatus().getStatus().equalsIgnoreCase("open")) {
			if (b > 0 && balance1 >= b) {
				balance1 -= b;
				balance2 += b;
				a1.setBalance(balance1);
				a2.setBalance(balance2);

				try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
					sql = "UPDATE account set balance = " + a1.getBalance() + " where accountid=" + a1.getAccountId();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();

					sql = "UPDATE account set balance = " + a2.getBalance() + " where accountid=" + a2.getAccountId();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();

					bal = 1;// Success

					System.out.println();
					log.info("acctID: " + from + " old balance: $" + oldbal1 + " new balance: $" + a1.getBalance());
					log.info("acctID: " + to + " old balance: $" + oldbal2 + " new balance: $" + a2.getBalance());

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println();
				log.warn("Withdraw cannot be greater than account.\n");
				throw new OverDraftException("Withdraw cannot be greater than account");
			}
		} else {
			System.out.println();
			log.warn("Can't perform transaction on unopen accounts.\n");
			throw new UnOpenException("Can't perform transaction on unopen accounts");
		}
		return bal;
	}

	@Override
	public Account status(int sID, String status) {
		Account a = null;

		if (status.equalsIgnoreCase("Close")) {
			a = selectAccountById(sID);
			a = cancelAccount(a);
		} else {
			try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

				sql = "update accountstatus set status ='" + status + "' where statusid =" + sID;
				ps = conn.prepareStatement(sql);
				ps.executeUpdate();

				sql = "select * from account a " + "full join accountstatus a2 on a.statusid = a2.statusid "
						+ "full join accounttype a3 on a.typeid =a3.typeid " + "where a2.statusid=" + sID;
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next()) {
					AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
					AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
					a = new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5));

					System.out.println(
							"Successfully changed acctID: " + a.getAccountId() + " status to [" + as.getStatus() + "]");
					System.out.println(a);
					log.info("Successfully changed acctID: " + a.getAccountId() + " status to [" + as.getStatus()
							+ "].\n");

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return a;
	}

	@Override
	public Account cancelAccount(Account a) { // TODO Auto-generated
		UserService userserv = new UserServiceImpl();

		System.out.println();
		log.info("Attempting to close acctID: " + a.getAccountId());

		a.getStatus().setStatus("Close");
		AccountStatus s = new AccountStatus(a.getStatus().getStatusId(), a.getStatus().getStatus());
		Account ac = new Account(a.getAccountId(), a.getBalance(), s, a.getType(), a.getOwnerid());

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "delete from accountstatus where statusid=" + a.getStatus().getStatusId();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "delete from accounttype where typeid=" + a.getType().getTypeId();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			System.out.println("Successfully closed acctID: " + a.getAccountId());
			log.info("Successfully closed acctID: " + a.getAccountId() + ".\n");

			User u = userserv.getUser(a.getOwnerid());
			System.out.println(u);
		} catch (SQLException e) {
			System.out.println();
			log.error("Incorrect SQL syntax.\n");
		}
		return ac;
	}

	@Override
	public List<Account> selectAllStatus(String status) {
		List<Account> AccountList = new ArrayList<Account>();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid where a2.status ='"
					+ status + "'";

			ps = conn.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
				AccountList.add(new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return AccountList;
	}

	@Override
	public Account updateAccount(int id, double balance, int sID, String status, int tID, String type, int oID) {
		Account a = null;
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "update accountstatus set status ='" + status + "' where statusid=" + sID;
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "update accounttype set accttype ='" + type + "' where typeid =" + tID;
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "update account set balance =" + balance + " where accountid =" + id;
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "update account set ownerid =" + oID + " where accountid =" + id;
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid "
					+ "where a.accountid=" + id;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
				a = new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5));
			}
		} catch (PSQLException e) {
			a = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public boolean isOwner(int i, int id) {
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid "
					+ "where a.ownerid=" + i + " and a.accountid =" + id;

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println();
				log.info("userID: " + i + " is the owner of acctID: " + id);
				return true;
			}
		} catch (SQLException e) {
			System.out.println();
			log.error("Incorrect SQL syntax.\n");
		}

		System.out.println();
		log.info(i + " is not the owner of acctID: " + id);
		return false;
	}
}
