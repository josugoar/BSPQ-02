package es.deusto.spq.pojo;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

public class SubjectDataTest {

    SubjectData subjectData;
    UserData professor;
    Date startD;
    
    @Before
    public void setUp() {
        subjectData = new SubjectData();
        subjectData.setId(0000);
        subjectData.setName("name");
        subjectData.setProfessor(professor);
        subjectData.setStartDate(startD);
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
    public void testProfessor() {
        assertEquals(professor, subjectData.getProfessor());
    }

    @Test
    public void testGetStartDate() {
        assertEquals(startD, subjectData.getStartDate());
    }
}
