package es.deusto.spq.pojo;

import es.deusto.spq.server.jdo.Score;

public class ScoreData {
    private SubjectData subject;
    private UserData student;
    private Float score;
    private Integer id;

    public ScoreData(){
    }

    public ScoreData(Score score) {
        this.subject = new SubjectData(score.getSubject());
        this.student = new UserData(score.getStudent());
        this.score = score.getScore();
        this.id = score.getId();
    }

    public SubjectData getSubject() {
        return subject;
    }

    public void setSubject(SubjectData subject) {
        this.subject = subject;
    }

    public UserData getStudent() {
        return student;
    }

    public void setStudent(UserData student) {
        this.student = student;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
		return "Score: user name --> " + this.student.getName() + ", user surname --> " + this.student.getSurname() + ", subject -->" + this.subject
				+ ", score -->  " + this.score + "]";
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((student == null) ? 0 : student.hashCode());
        result = prime * result + ((score == null) ? 0 : score.hashCode());
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
        ScoreData other = (ScoreData) obj;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (student == null) {
            if (other.student != null)
                return false;
        } else if (!student.equals(other.student))
            return false;
        if (score == null) {
            if (other.score != null)
                return false;
        } else if (!score.equals(other.score))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
