package es.deusto.spq.server.jdo;

import java.sql.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import es.deusto.spq.pojo.Faculty;

@PersistenceCapable
public class Subject {
    
    @PrimaryKey
    private Integer id = null;
    private Date startDate = null;
    private String name = null;
    private User proffessor = null;
    private Faculty faculty = null;

    public Subject() {
	}

    public Subject(Date startDate, String name, User proffessor, Integer id, Faculty faculty) {
        this.startDate = startDate;
        this.name = name;
        this.proffessor = proffessor;
        this.id = id;
        this.faculty = faculty;
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
    public User getProffessor() {
        return proffessor;
    }
    public void setProffessor(User proffessor) {
        this.proffessor = proffessor;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Faculty getFaculty() {
        return faculty;
    }
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String toString() {
		return "Subject: name --> " + this.name + ", start date --> " + this.startDate + ", proffessor name -->" + this.proffessor.getName()
				+ ", proffessor surname -->" + this.proffessor.getSurname() +", id -->  " + this.id + ", faculty -->  " + this.faculty + "]";
	}

}
