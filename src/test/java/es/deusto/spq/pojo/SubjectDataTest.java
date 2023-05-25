package es.deusto.spq.pojo;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

public class SubjectDataTest {

    SubjectData subjectData;
    UserData proffessor;
    Date startD;
    
    @Before
    public void setUp() {
        subjectData = new SubjectData();
        subjectData.setId(0000);
        subjectData.setName("name");
        subjectData.setProffessor(proffessor);
        subjectData.setStartDate(startD);
        subjectData.setFaculty(Faculty.ENGINEERING);
    }

    @Test
    public void testGetID() {
        assertEquals(0000, subjectData.getId().intValue());
    }

    @Test
    public void testGetName() {
        assertEquals("name", subjectData.getName());
    }

    @Test
    public void testProffessor() {
        assertEquals(proffessor, subjectData.getProffessor());
    }

    @Test
    public void testGetStartDate() {
        assertEquals(startD, subjectData.getStartDate());
    }
}
