package com.revature.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;

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

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Static block has failed");
		}
	}

	@Override
	public Account insertAccount(Account a, User u) {
		Account acct = null;
		// System.out.println(a.getType().getType());
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "insert into accountstatus(statusid,status) values (?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getStatus().getStatusId());
			ps.setString(2, a.getStatus().getStatus());
			ps.executeUpdate();

			sql = "insert into accounttype(typeid,accttype) values (?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getType().getTypeId());
			ps.setString(2, a.getType().getType());
			ps.executeUpdate();

			sql = "insert into account(balance, statusid, typeid, ownerid) values (?,?,?,?)";// Adds to user table

			ps = conn.prepareStatement(sql);
			ps.setDouble(1, a.getBalance());
			ps.setInt(2, a.getStatus().getStatusId());
			ps.setInt(3, a.getType().getTypeId());
			ps.setInt(4, u.getUserId());
			ps.executeUpdate();

			sql = "select * from account a full join accountstatus a2 on a.statusid = a2.statusid full join accounttype a3 on a.typeid =a3.typeid "
					+ "where a.ownerid=" + u.getUserId() + " and a2.statusid =" + a.getStatus().getStatusId()
					+ " and a3.typeid =" + a.getType().getTypeId();

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				AccountStatus as = new AccountStatus(rs.getInt(6), rs.getString(7));
				AccountType at = new AccountType(rs.getInt(8), rs.getString(9));
				acct = new Account(rs.getInt(1), rs.getDouble(2), as, at, rs.getInt(5));
				u.addAccount(acct);
			}
		} catch (PSQLException e) {
			acct = null;
		} catch (SQLException e) {
			e.printStackTrace();
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
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		double balance = 0;
		Account a = selectAccountById(id);
		balance = a.getBalance();

		if (a.getStatus().getStatus().equalsIgnoreCase("open")) {
			if (b > 0) {
				balance += b;
				a.setBalance(balance);
				try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
					sql = "UPDATE account set balance = " + a.getBalance() + " where accountid=" + a.getAccountId();
					ps = conn.prepareStatement(sql);
					ps.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				balance = 0;
			}
		} else {
			balance = -1;
		}
		return balance;
	}

	@Override
	public double withdraw(int id, double b) {
		double balance = 0;
		Account a;

		a = selectAccountById(id);
		balance = a.getBalance();// money in the account

		if (a.getStatus().getStatus().equalsIgnoreCase("open")) {
			if (b > 0 && balance >= b) {
				balance -= b;
				a.setBalance(balance);
				try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
					sql = "UPDATE account set balance = " + a.getBalance() + " where accountid=" + a.getAccountId();

					ps = conn.prepareStatement(sql);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				balance = 0;
			}
		} else {
			balance = -1;
		}
		return balance;
	}

	@Override
	public double transfer(int from, int to, double b) {
		// TODO Auto-generated method stub
		Account a1 = selectAccountById(from);
		Account a2 = selectAccountById(to);
		double balance1 = a1.getBalance();
		double balance2 = a2.getBalance();
		double bal = 1;

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
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				bal = 0;
				System.out.println("Transaction fail.");
			}
		} else {
			bal = -1;
			System.out.println("Account is not open.");
		}
		return bal;
	}

	@Override
	public Account status(int sID, String status) {
		Account a = null;

		if (status.equalsIgnoreCase("Close")) {
			a = selectAccountById(sID);
			cancelAccount(a);
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
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return a;
	}

	@Override
	public void cancelAccount(Account a) { // TODO Auto-generated
		UserService userserv = new UserServiceImpl();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "delete from accountstatus where statusid=" + a.getStatus().getStatusId();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "delete from accounttype where typeid=" + a.getType().getTypeId();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			User u = userserv.getUser(a.getOwnerid());
			System.out.println(u);
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
