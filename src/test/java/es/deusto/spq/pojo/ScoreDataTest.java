package es.deusto.spq.pojo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ScoreDataTest {
    ScoreData scoreData;
    UserData student;
    SubjectData subject;
    
    @Before
    public void setUp() {
        scoreData = new ScoreData();
        scoreData.setId(0000);
        scoreData.setScore(5.0f);
        scoreData.setStudent(student);
        scoreData.setSubject(subject);
    }

    @Test
    public void testGetID() {
        assertEquals(0000, scoreData.getId().intValue());
    }

    @Test
    public void testGetScore() {
        assertEquals(5.0f, scoreData.getScore().floatValue(), 0f);
    }

    @Test
    public void testGetStudent() {
        assertEquals(student, scoreData.getStudent());
    }

    @Test
    public void testGetSubject() {
        assertEquals(subject, scoreData.getSubject());
    }
}
