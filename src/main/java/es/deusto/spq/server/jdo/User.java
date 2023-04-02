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

	@Persistent(mappedBy = "user", dependentElement = "true")
	@Join
	Set<Message> messages = new HashSet<>();

	public User(String name, String login, String password) {
		this.login = login;
		this.password = password;
		this.name = name;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void removeMessage(Message message) {
		messages.remove(message);
	}

	public String getName() {
		return this.login;
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
		return "User: name --> " + this.name + ", login -->" + this.login + ", password -->  " + this.password
				+ ", messages --> [" + messagesStr + "]";
	}
}
