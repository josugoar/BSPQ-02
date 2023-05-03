package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.UserData;
import es.deusto.spq.server.jdo.User;


public class ResourceTest {

    private Resource resource;

    @Mock
    private PersistenceManager persistenceManager;

    @Mock
    private Transaction transaction;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        try (MockedStatic<JDOHelper> jdoHelper = Mockito.mockStatic(JDOHelper.class)) {
            PersistenceManagerFactory pmf = mock(PersistenceManagerFactory.class);
            jdoHelper.when(() -> JDOHelper.getPersistenceManagerFactory("datanucleus.properties")).thenReturn(pmf);
            
            when(pmf.getPersistenceManager()).thenReturn(persistenceManager);
            when(persistenceManager.currentTransaction()).thenReturn(transaction);

            resource = new Resource();
        }
    }

    @Test
    public void testLogin() {
        UserData userData = new UserData();
        userData.setLogin("test-login");
        userData.setPassword("passwd");

        Response response1 = resource.login(userData.getLogin(), userData.getPassword());

        assertEquals(Response.Status.BAD_REQUEST, response1.getStatusInfo());

        User user = spy(User.class);
        when(user.getPassword()).thenReturn(userData.getPassword());
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);

        Response response = resource.login(userData.getLogin(), userData.getPassword());

        assertEquals(Response.Status.OK, response.getStatusInfo());
    }

    @Test
    public void testRegisterUser() {
        UserData userData = new UserData();
        userData.setLogin("test-register");
        userData.setPassword("passwd");

        Response response1 = resource.registerUser(userData);

        assertEquals(Response.Status.OK, response1.getStatusInfo());

        User user = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);

        Response response2 = resource.registerUser(userData);

        assertEquals(Response.Status.BAD_REQUEST, response2.getStatusInfo());
    }

    @Test
    public void testGetUser() {
        UserData userData1 = new UserData();
        userData1.setLogin("test-login");
        userData1.setPassword("passwd");
        userData1.setRole(Role.ADMIN);

        User user1 = spy(User.class);
        when(user1.getLogin()).thenReturn(userData1.getLogin());
        when(user1.getPassword()).thenReturn(userData1.getPassword());
        when(user1.getRole()).thenReturn(userData1.getRole());
        when(persistenceManager.getObjectById(User.class, userData1.getLogin())).thenReturn(user1);

        Response response1 = resource.getUser(userData1.getLogin(), userData1.getPassword(), "nonexistent");

        assertEquals(Response.Status.BAD_REQUEST, response1.getStatusInfo());

        UserData userData2 = new UserData();
        userData2.setLogin("test-login2");
        userData2.setPassword("password");
        userData1.setRole(Role.STUDENT);

        User user2 = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData2.getLogin())).thenReturn(user2);

        Response response2 = resource.getUser(userData1.getLogin(), userData1.getPassword(), user2.getLogin());

        // check expected response
        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }


}
