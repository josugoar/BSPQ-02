package es.deusto.spq.pojo;

import es.deusto.spq.server.jdo.User;

public class UserData {

    private String login;
    private String password;
    private String name;
    private String surname;
    private Role role;

    public UserData(){
    }

    public UserData(User user) {
        this.setLogin(user.getLogin());
        this.setName(user.getName());
        this.setSurname(user.getSurname());
        this.setPassword(user.getPassword());
        this.setRole(user.getRole());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ExcludeFromJacocoGeneratedReport
    public String toString() {
        return "[login=" + login + ", password=" + password + "]";
    }

}
