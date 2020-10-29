package com.revature.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import com.revature.model.Account;
import com.revature.model.Role;
import com.revature.model.User;
import com.revature.service.AccountService;
import com.revature.service.AccountServiceImpl;

public class UserDaoImpl implements UserDao {

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
	public User insertUser(User u) { // Done
		// TODO Auto-generated method stub
		ArrayList<Role> roles = new ArrayList<>();
		User user = null;
		Role r;

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "INSERT INTO Roles(bankrole) VALUES (?)";

			ps = conn.prepareStatement(sql);
			ps.setString(1, "Customer");
			ps.executeUpdate();

			sql = "select * from Roles where bankrole='Customer'";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				roles.add(new Role(rs.getInt(1), rs.getString(2)));
			}
			r = roles.get(roles.size() - 1);
			System.out.println(r);

			sql = "INSERT INTO bankuser(username, bankpassword, firstname, lastname, email,roleid) VALUES (?,?,?,?,?,?)";
			// Adds to user table

			ps = conn.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getFirstName());
			ps.setString(4, u.getLastName());
			ps.setString(5, u.getEmail());
			ps.setInt(6, r.getRoleId());
			ps.executeUpdate();

			sql = "select * from bankuser b full join roles r on b.roleid =r.roleid where b.roleid = " + r.getRoleId();// u.getRole().getRoleId();

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				r = new Role(rs.getInt(8), rs.getString(9));
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
			}
		} catch (PSQLException e) {
			user = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User selectUserById(int id) {// DONE
		// TODO Auto-generated method stub
		User u = null;
		AccountService acctserv = new AccountServiceImpl();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from bankuser b " + "full join roles r on b.roleid =r.roleid " + "where b.userid =" + id;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				Role r = new Role(rs.getInt(8), rs.getString(9));
				User i = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(i);
//				System.out.println(AccountList+"\n");
				u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r, AccountList);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public User updateUser(int id, String username, String password, String firstname, String lastname, String email) {
		User u = null;
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "update bankuser set username='" + username + "', bankpassword='" + password + "', " + "firstname='"
					+ firstname + "', lastname='" + lastname + "', " + "email='" + email + "' where userid=" + id;

			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

			sql = "select * from bankuser b full join roles r on b.roleid =r.roleid where userid=" + id;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				Role r = new Role(rs.getInt(8), rs.getString(9));
				u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

	@Override
	public ArrayList<User> selectAllUser() { // Done
		ArrayList<User> user = new ArrayList<User>();
		AccountService acctserv = new AccountServiceImpl();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
			sql = "select * from bankuser b full outer join roles r on b.roleid =r.roleid where r.bankrole ='Customer'";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Role r = new Role(rs.getInt(8), rs.getString(9));
				User i = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(i);
				// System.out.println(AccountList+"\n");
				user.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r, AccountList));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User userlogin(String username, String password) {
		User u = null;
		AccountService acctserv = new AccountServiceImpl();
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from bankuser b inner join roles r on r.roleid = b.roleid where username= '" + username
					+ "' AND bankpassword= '" + password + "'";

			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				Role r = new Role(rs.getInt(8), rs.getString(9));
				User i = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(i);
//				System.out.println(AccountList+"\n");
				u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r, AccountList);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}

}