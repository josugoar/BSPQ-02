package es.deusto.spq.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import es.deusto.spq.pojo.Role;

@PersistenceCapable
public class User {
	@PrimaryKey
	String login = null;
	String password = null;
	String name = null;
	String surname = null;
	Role role = null;

	public User(String login, String password, String name, String surname, Role role) {
		this.login = login;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.role = role;
	}

	public String getName() {
		return this.name;
	}

	public String getSurname() {
		return this.surname;
	}

	public String getLogin() {
		return this.login;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String toString() {
		return "User: name --> " + this.name + ", surname --> " + this.surname + ", login -->" + this.login
				+ ", password -->  " + this.password + "]";
	}
}
