package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.Role;

public class ScoreTest {

    private User professor;
    private Subject subject;
    private User student;
    private Score score;

    @Before
    public void setUp() {
        professor = new User("test-login", "test-password", "test-name", "test-surnmame", Role.PROFESSOR);
        subject = new Subject(new Date(0), "test-name", professor, 0);
        student = new User("test-login", "test-password", "test-name", "test-surnmame", Role.STUDENT);
        score = new Score(subject, student, 10.0f, 0);
    }    

    @Test
    public void testGetId() {
        assertEquals(0, score.getId().intValue());
    }

    @Test
    public void testGetScore() {
        assertEquals(10.0f, score.getScore().floatValue(), 0.001);
    }

    @Test
    public void testGetStudent() {
        assertEquals(student, score.getStudent());
    }

    @Test
    public void testGetSubject() {
        assertEquals(subject, score.getSubject());
    }

    @Test
    public void testSetId() {
        score.setId(10);
        assertEquals(10, score.getId().intValue());
    }

    @Test
    public void testSetScore() {
        score.setScore(0.0f);
        assertEquals(0.0f, score.getScore().floatValue(), 0.001);

        assertThrows(IllegalArgumentException.class, () -> score.setScore(-1.0f));

        assertThrows(IllegalArgumentException.class, () -> score.setScore(11.0f));
    }

    @Test
    public void testSetStudent() {
        User newStudent = new User("test-login2", "test-password2", "test-name2", "test-surnmame2", Role.STUDENT);
        score.setStudent(newStudent);
        assertEquals(newStudent, score.getStudent());
    }

    @Test
    public void testSetSubject() {
        Subject newSubject = new Subject(new Date(1000), "test-name2", professor, 10);
        score.setId(10);
        assertEquals(newSubject, score.getSubject());
    }
    
}
