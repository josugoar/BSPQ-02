package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.Role;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        user = new User("test-login", "test-password", "test-name", "test-surnmame", Role.STUDENT);
    }

    @Test
    public void testGetLogin() {
        assertEquals("test-login", user.getLogin());
    }

    @Test
    public void testGetName() {
        assertEquals("test-name", user.getName());

    }

    @Test
    public void testGetPassword() {
        assertEquals("test-password", user.getPassword());

    }

    @Test
    public void testGetRole() {
        assertEquals(Role.STUDENT, user.getRole());

    }

    @Test
    public void testGetSurname() {
        assertEquals("test-surname", user.getSurname());
    }

    @Test
    public void testSetName() {
        user.setName("newname");    
        assertEquals("newname", user.getName());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpass");    
        assertEquals("newpass", user.getPassword());
    }

    @Test
    public void testSetRole() {
        user.setRole(Role.ADMIN);    
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    public void testSetSurname() {
        user.setSurname("newsurname");    
        assertEquals("newsurname", user.getSurname());
    }

    @Test
    public void testToString() {
        user.setName("name");
        user.setSurname("surname");
        user.setPassword("password");
        user.setRole(Role.STUDENT);
        String result = "User: name --> " + "name" + ", surname --> " + "surname" + ", login -->" + "test-login"
        + ", password -->  " + "password" + "]";

        assertEquals(result,user.toString());
        
        
    }


    
}
