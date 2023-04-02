package es.deusto.spq.server.jdo;

import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import java.util.HashSet;

@PersistenceCapable
public class User {
	@PrimaryKey
	String login = null;
	String password = null;
	String name = null;
	String surname = null;

	@Persistent(mappedBy = "user", dependentElement = "true")
	@Join
	Set<Message> messages = new HashSet<>();

	public User(String login, String password, String name, String surname) {
		this.login = login;
		this.password = password;
		this.name = name;
		this.surname = surname;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void removeMessage(Message message) {
		messages.remove(message);
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

	public Set<Message> getMessages() {
		return this.messages;
	}

	public String toString() {
		StringBuilder messagesStr = new StringBuilder();
		for (Message message : this.messages) {
			messagesStr.append(message.toString() + " - ");
		}
		return "User: name --> " + this.name + ", surname --> " + this.surname + ", login -->" + this.login + ", password -->  " + this.password
				+ ", messages --> [" + messagesStr + "]";
	}
}
