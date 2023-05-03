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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((proffessor == null) ? 0 : proffessor.hashCode());
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
        if (proffessor == null) {
            if (other.proffessor != null)
                return false;
        } else if (!proffessor.equals(other.proffessor))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
