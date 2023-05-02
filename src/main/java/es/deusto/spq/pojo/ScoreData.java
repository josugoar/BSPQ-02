package es.deusto.spq.pojo;

import es.deusto.spq.server.jdo.Score;

public class ScoreData {
    private SubjectData subject;
    private UserData student;
    private Float score;
    private Integer id;

    public ScoreData(Score score) {
        // required for serialization
        this.subject = new SubjectData(score.getSubject());
        this.student = new UserData(score.getStudent());
        this.score = score.getScore();
        this.id = score.getId();
    }

    public ScoreData(){
        
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
