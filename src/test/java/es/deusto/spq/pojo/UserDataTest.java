package es.deusto.spq.pojo;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class UserDataTest {
    UserData userData;
    Role role;
    
    @Before
    public void setUp() {
        userData = new UserData();
        userData.setName("name");
        userData.setSurname("surname");
        userData.setLogin("login");
        userData.setPassword("password");
        userData.setRole(role);
    }

    @Test
    public void testGetName() {
        assertEquals("name", userData.getName());
    }

    @Test
    public void testGetSurname() {
        assertEquals("surname", userData.getSurname());
    }

    @Test
    public void testGetLogin() {
        assertEquals("long", userData.getLogin());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", userData.getPassword());
    }

    @Test
    public void testGetRole() {
        assertEquals(role, userData.getRole());
    }

    @Test
    public void testToString() {
        assertEquals("[login=login, password=password]", userData.toString());
    }

}
