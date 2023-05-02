package es.deusto.spq.server.jdo;

import java.sql.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Subject {
    
    private Date startDate = null;
    private String name = null;
    private User professor = null;
    @PrimaryKey
    private Integer id = null;

    public Subject(Date startDate, String name, User professor, Integer id) {
        this.startDate = startDate;
        this.name = name;
        this.professor = professor;
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public User getProfessor() {
        return professor;
    }
    public void setProfessor(User professor) {
        this.professor = professor;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
		return "Subject: name --> " + this.name + ", start date --> " + this.startDate + ", professor name -->" + this.professor.getName()
				+ ", professor surname -->" + this.professor.getSurname() +", id -->  " + this.id + "]";
	}

}
