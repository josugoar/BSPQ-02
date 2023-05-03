package es.deusto.spq.pojo;

import java.sql.Date;

import es.deusto.spq.server.jdo.Subject;

public class SubjectData {
    
    private Date startDate;
    private String name;
    private UserData professor;
    private Integer id;

    public SubjectData(){
    }

    public SubjectData(Subject subject) {
        this.startDate = subject.getStartDate();
        this.name = subject.getName();
        this.professor = new UserData(subject.getProfessor());
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((professor == null) ? 0 : professor.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubjectData other = (SubjectData) obj;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (professor == null) {
            if (other.professor != null)
                return false;
        } else if (!professor.equals(other.professor))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
