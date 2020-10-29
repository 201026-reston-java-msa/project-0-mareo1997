package com.revature.model;

public class Role {
	// The Role model keeps track of user permissions. Can be expanded for more
	// features later. Could be Admin, Employee, Standard, or Premium
	private int roleId; // primary key
	private String role; // not null, unique

	public Role() {
		// TODO Auto-generated constructor stub
	}
	
	public Role(String role) {
		super();
//		this.roleId = roleId;
		this.role = role;
	}

	public Role(int roleId, String role) {
		super();
		this.roleId = roleId;
		this.role = role;
	}

	public String toString() {
		String output = "Roleid: " + roleId + "\nRole: " + role;
		return output;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
