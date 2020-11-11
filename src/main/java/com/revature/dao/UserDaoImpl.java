package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
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

	public String sql, call;
	public PreparedStatement ps;
	public ResultSet rs;
	public CallableStatement cs;

	private static Logger log = Logger.getLogger(UserDaoImpl.class);

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Static block has failed");
			log.warn(e + "\n");
		}
	}

	@Override
	public User insertUser(User u) { // Done
		// TODO Auto-generated method stub
		ArrayList<User> users = new ArrayList<>();
		ArrayList<Role> roles = new ArrayList<>();
		User user = null;
		Role r;

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			log.info("Generating insert user sql statement.\n");

			sql = "INSERT INTO bankuser(username, bankpassword, firstname, lastname, email) VALUES (?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword());
			ps.setString(3, u.getFirstName());
			ps.setString(4, u.getLastName());
			ps.setString(5, u.getEmail());// ps.setInt(6, r.getRoleId());
			ps.executeUpdate();

			log.info("Generated new user.\n");
			log.info("Generating new role.\n");

			sql = "select * from bankuser where username= '" + u.getUsername() + "'";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6)));
			}
			user = users.get(users.size() - 1);

			log.info("Calling role stored procedure sql statement.\n");

			call = "CALL insert_role(?)";
			ps = conn.prepareStatement(call);
			ps.setInt(1, user.getUserId());
			ps.executeUpdate();

			sql = "select * from Roles where bankrole='Customer'";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				roles.add(new Role(rs.getInt(1), rs.getString(2)));// System.out.println(roles);
			}
			r = roles.get(roles.size() - 1);

			log.info("Generated new role.\n");

			user = new User(user.getUserId(), user.getUsername(), user.getPassword(), user.getFirstName(),
					user.getLastName(), user.getEmail(), r);
			
			log.info("Successfully registered " +user.getUsername() + ".\n");

		} catch (PSQLException e) {
			System.out.println(e+"\n");
			log.warn(e + "\n");
			throw new NullPointerException();
		} catch (SQLException e) {
			System.out.println(e);
			log.warn(e + "\n");
		}

		if (user != null) {
			System.out.println("\nSuccessfully registered: \n"+user);
		}
		return user;
	}

	@Override
	public User selectUserById(int id) {// DONE
		// TODO Auto-generated method stub
		User u = null;
		AccountService acctserv = new AccountServiceImpl();

		log.info("Attempting to identify user.\n");
		
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
			
			sql = "select * from bankuser b inner join roles r on b.userid =r.userid where b.userid = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Role r = new Role(rs.getInt(7), rs.getString(8));
				User i = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(i);// System.out.println(AccountList+"\n");
				u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r, AccountList);

				log.info("Identified " + r.getRole() + ": " + u.getUsername() + "\n");
			}
		} catch (SQLException e) {
			System.out.println(e);
			log.warn(e + "\n");
		}
		if (u == null) {
			log.warn("User id does not exist.\n");
			throw new NullPointerException("User id does not exist.");
		}
		return u;
	}

/*	@Override
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
			System.out.println(e);
			log.warn(e + "\n");
		}
		return u;
	}*/

	@Override
	public ArrayList<User> selectAllUser() { // Done
		ArrayList<User> user = new ArrayList<User>();
		AccountService acctserv = new AccountServiceImpl();

		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {
			sql = "select * from bankuser b full outer join roles r on b.userid =r.userid where r.bankrole ='Customer' and Username is not null";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Role r = new Role(rs.getInt(7), rs.getString(8));
				User i = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(i);// System.out.println(AccountList+"\n");
				user.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r, AccountList));
			}
		} catch (SQLException e) {
			System.out.println(e);
			log.warn(e + "\n");
		}
		return user;
	}

	@Override
	public User userlogin(String username, String password) {
		User u = null;
		AccountService acctserv = new AccountServiceImpl();
		
		log.info("Attempting to validate user.\n");
		
		try (Connection conn = DriverManager.getConnection(url, sqlusername, sqlpassword)) {

			sql = "select * from bankuser b inner join roles r on r.userid = b.userid where username=? AND bankpassword=?";

			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();

			if (rs.next()) {
				Role r = new Role(rs.getInt(7), rs.getString(8));
				User i = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r);
				ArrayList<Account> AccountList = acctserv.getAllPersonalAccount(i);
				u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), r, AccountList);

				log.info(rs.getString(8) + ": " + username + " successfully logged in.\n");
			}

		} catch (SQLException e) {
			System.out.println(e);
			log.warn(e + "\n");
		}
		if (u == null) {
			log.warn("Invalid user name or password.\n");
			throw new NullPointerException("Invalid user name or password");
		}
		return u;
	}

}