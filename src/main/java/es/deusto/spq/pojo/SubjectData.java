package es.deusto.spq.pojo;

import java.sql.Date;

import es.deusto.spq.server.jdo.Subject;

public class SubjectData {
    
    private Date startDate;
    private String name;
    private UserData professor;
    private Integer id;


    
    public SubjectData(Subject subject) {
        // required by serialization
        this.startDate = subject.getStartDate();
        this.name = subject.getName();
        this.professor = new UserData(subject.getProfessor());
        this.id = subject.getId();
    }

    public SubjectData(){
        
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
    public UserData getProfessor() {
        return professor;
    }
    public void setProfessor(UserData professor) {
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