package es.deusto.spq.pojo;

public class ScoreData {
    private SubjectData subject;
    private UserData student;
    private Float score;
    private Integer id;

    public ScoreData() {
        // required for serialization
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
}
