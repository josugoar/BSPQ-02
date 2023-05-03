package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.Role;

public class SubjectTest {

    private Subject subject;
    private User professor = new User("prof-login", "prof-pass", "prof-name", "prof-surname", Role.PROFESSOR);
    private Date date = new Date(System.currentTimeMillis());

    @Before
    public void setUp() {
        subject = new Subject(date, "test-name", professor, 1);
    }


    @Test
    public void testGetId() {
        assertEquals(1, subject.getId().intValue());
    }

    @Test
    public void testGetName() {
        assertEquals("test-name", subject.getName());
    }

    @Test
    public void testGetProfessor() {
        assertEquals(professor, subject.getProfessor());
    }

    @Test
    public void testGetStartDate() {
        assertEquals(date, subject.getStartDate());
    }

    @Test
    public void testSetId() {
        subject.setId(2);
        assertEquals(2, subject.getId().intValue());
    }

    @Test
    public void testSetName() {
        subject.setName("newname");
        assertEquals("newname", subject.getName());
    }

    @Test
    public void testSetProfessor() {
        User newProf = new User("newlogin", "newpass", "newname", "newsurname", Role.PROFESSOR);
        subject.setProfessor(newProf);
        assertEquals(newProf, subject.getProfessor());
    }

    @Test
    public void testSetStartDate() {
        Date newDate = new Date(System.currentTimeMillis());
        subject.setStartDate(newDate);
        assertEquals(newDate, subject.getStartDate());
    }
    
}
