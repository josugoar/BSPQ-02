package es.deusto.spq.pojo;

import java.sql.Date;

import es.deusto.spq.server.jdo.Subject;

public class SubjectData {
    
    private Date startDate;
    private String name;
    private UserData proffessor;
    private Integer id;

    public SubjectData(){
    }

    public SubjectData(Subject subject) {
        this.startDate = subject.getStartDate();
        this.name = subject.getName();
        this.proffessor = new UserData(subject.getProffessor());
        this.id = subject.getId();
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
    public UserData getProffessor() {
        return proffessor;
    }
    public void setProffessor(UserData proffessor) {
        this.proffessor = proffessor;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
		return "Subject: name --> " + this.name + ", start date --> " + this.startDate + ", proffessor name -->" + this.proffessor.getName()
				+ ", proffessor surname -->" + this.proffessor.getSurname() +", id -->  " + this.id + "]";
	}

}
