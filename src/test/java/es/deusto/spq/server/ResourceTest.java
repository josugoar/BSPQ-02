package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.Role;
import es.deusto.spq.pojo.ScoreData;
import es.deusto.spq.pojo.SubjectData;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.server.jdo.Score;
import es.deusto.spq.server.jdo.Subject;
import es.deusto.spq.server.jdo.User;


public class ResourceTest {

    private Resource resource;

    @Mock
    private PersistenceManager persistenceManager;

    @Mock
    private Transaction transaction;

    @Mock
    private Query<User> queryUser;

    @Mock
    private Query<Subject> querySubject;

    @Mock
    private Query<Score> queryScore;

    User adminUser;
    User deanUser;
    User studentUser;
    User proffessorUser;
    User fakeProffessor;

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

        adminUser = spy(User.class);
        when(adminUser.getLogin()).thenReturn("test-admin");
        when(adminUser.getPassword()).thenReturn("test-admin");
        when(adminUser.getRole()).thenReturn(Role.ADMIN);
        when(persistenceManager.getObjectById(User.class, "test-admin")).thenReturn(adminUser);

        deanUser = spy(User.class);
        when(deanUser.getLogin()).thenReturn("test-dean");
        when(deanUser.getPassword()).thenReturn("test-dean");
        when(deanUser.getRole()).thenReturn(Role.DEAN);
        when(persistenceManager.getObjectById(User.class, "test-dean")).thenReturn(deanUser);

        studentUser = spy(User.class);
        when(studentUser.getLogin()).thenReturn("test-student");
        when(studentUser.getPassword()).thenReturn("test-student");
        when(studentUser.getRole()).thenReturn(Role.STUDENT);
        when(persistenceManager.getObjectById(User.class, "test-student")).thenReturn(studentUser);

        proffessorUser = spy(User.class);
        when(proffessorUser.getLogin()).thenReturn("test-proffessor");
        when(proffessorUser.getPassword()).thenReturn("test-proffessor");
        when(proffessorUser.getRole()).thenReturn(Role.PROFFESSOR);
        when(persistenceManager.getObjectById(User.class, "test-proffessor")).thenReturn(proffessorUser);

        fakeProffessor = spy(User.class);
        when(fakeProffessor.getLogin()).thenReturn("fake-proffessor");
        when(fakeProffessor.getPassword()).thenReturn("fake-proffessor");
        when(fakeProffessor.getRole()).thenReturn(Role.PROFFESSOR);
        when(persistenceManager.getObjectById(User.class, "fake-proffessor")).thenReturn(fakeProffessor);        
    }

    @Test
    public void testAuthenticate() {
        Response response4 = resource.getUser("no-admin", "no-admin", "");

        assertEquals(Response.Status.BAD_REQUEST, response4.getStatusInfo());

        Response response3 = resource.getUser("test-admin", "no-admin", "");

        assertEquals(Response.Status.BAD_REQUEST, response3.getStatusInfo());
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
    public void testGetUser() {
        Response response1 = resource.getUser("test-admin", "test-admin", "nonexistent");

        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        UserData userData = new UserData();
        userData.setLogin("test-user");
        userData.setPassword("password");
        userData.setRole(Role.ADMIN);

        User user2 = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user2);

        Response response2 = resource.getUser("test-admin", "test-admin", userData.getLogin());

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

    @Test
    public void testRegisterUser() {
        UserData userData = new UserData();
        userData.setLogin("test-register");
        userData.setPassword("passwd");

        Response response1 = resource.registerUser("test-admin", "test-admin", userData);

        assertEquals(Response.Status.OK, response1.getStatusInfo());

        User user = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);

        Response response2 = resource.registerUser("test-admin", "test-admin", userData);

        assertEquals(Response.Status.BAD_REQUEST, response2.getStatusInfo());
    }

    @Test
    public void testRegisterUserNotFound() {
        UserData userData = new UserData();
        userData.setLogin("test-register");
        userData.setPassword("passwd");

        when(transaction.isActive()).thenReturn(true);

        Response response = resource.registerUser("test-admin", "test-admin", userData);

        assertEquals(Response.Status.OK, response.getStatusInfo());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(persistenceManager).makePersistent(userCaptor.capture());
        assertEquals("test-register", userCaptor.getValue().getLogin());
        assertEquals("passwd", userCaptor.getValue().getPassword());
    }

    @Test
    public void testGetUsers() {
        UserData userData = new UserData();
        userData.setLogin("test-user");
        userData.setPassword("password");
        userData.setRole(Role.ADMIN);

        User user = new User(userData.getLogin(), userData.getPassword(), userData.getName(), userData.getSurname(), userData.getRole());
        List<User> userlist = new ArrayList<>();
        userlist.add(user);

        when(persistenceManager.newQuery(User.class)).thenReturn(queryUser);
        when(queryUser.executeList()).thenReturn(userlist);

        Response response2 = resource.getUsers("test-admin", "test-admin");

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

    @Test
    public void testUpdateUsers() {
        UserData userData = new UserData();
        userData.setLogin("test-user");
        userData.setPassword("password");
        userData.setRole(Role.ADMIN);

        Response response1 = resource.updateUser("test-admin", "test-admin", userData.getLogin(), userData);

        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        User user = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);

        Response response2 = resource.updateUser("test-admin", "test-admin", userData.getLogin(), userData);

        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(user).setPassword(passwordCaptor.capture());
        assertEquals("password", passwordCaptor.getValue());

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

    @Test
    public void testDeleteUsers() {
        UserData userData = new UserData();
        userData.setLogin("test-user");
        userData.setPassword("password");
        userData.setRole(Role.ADMIN);

        Response response1 = resource.updateUser("test-admin", "test-admin", userData.getLogin(), userData);

        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        User user = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);

        Response response2 = resource.deleteUser("test-admin", "test-admin", userData.getLogin());

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }


    @Test
    public void testGetSubjects() {
        User proffessor = new User("prof-login", "prof-pass", "prof-name", "prof-surname", Role.PROFFESSOR);
        UserData proffessordat = new UserData(proffessor);
        SubjectData subjectData = new SubjectData();
        subjectData.setName("test-subject");
        subjectData.setProffessor(proffessordat);
        subjectData.setId(1);

        Subject subject = new Subject(subjectData.getStartDate(), subjectData.getName(), proffessor, subjectData.getId());
        List<Subject> subjectlist = new ArrayList<>();
        subjectlist.add(subject);

        when(persistenceManager.newQuery(Subject.class)).thenReturn(querySubject);
        when(querySubject.executeList()).thenReturn(subjectlist);

        Response response2 = resource.getSubjects("test-dean", "test-dean");

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

    @Test
    public void testRegisterSubject() {
        UserData proffessor= new UserData();
        SubjectData subjectData = new SubjectData();
        subjectData.setName("test-subject");
        subjectData.setProffessor(proffessor);
        subjectData.setId(1);
        Response response1 = resource.registerSubject("test-dean", "test-dean", subjectData);

        assertEquals(Response.Status.OK, response1.getStatusInfo());

        Subject subject = spy(Subject.class);
        when(persistenceManager.getObjectById(Subject.class, subjectData.getId())).thenReturn(subject);

        Response response2 = resource.registerSubject("test-dean", "test-dean", subjectData);

        assertEquals(Response.Status.BAD_REQUEST, response2.getStatusInfo());
    }

    @Test
    public void updateSubjectTest() {
        UserData proffessor= new UserData();
        SubjectData subjectData = new SubjectData();
        subjectData.setName("test-subject");
        subjectData.setProffessor(proffessor);
        subjectData.setId(1);

        Response response1 = resource.updateSubject("test-dean", "test-dean", subjectData.getId(), subjectData);

        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        Subject subject = spy(Subject.class);
        when(persistenceManager.getObjectById(Subject.class, subjectData.getId())).thenReturn(subject);

        Response response2 = resource.updateSubject("test-dean", "test-dean", subjectData.getId(), subjectData);

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

    @Test
    public void deleteSubjectTest() {
        UserData proffessor= new UserData();
        SubjectData subjectData = new SubjectData();
        subjectData.setName("test-subject");
        subjectData.setProffessor(proffessor);
        subjectData.setId(1);

        Response response1 = resource.deleteSubject("test-dean", "test-dean", subjectData.getId());

        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        Subject subject = spy(Subject.class);
        when(persistenceManager.getObjectById(Subject.class, subjectData.getId())).thenReturn(subject);

        Response response2 = resource.deleteSubject("test-dean", "test-dean", subjectData.getId());

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }
    
        @Test
    public void testGetScore() {
        Subject subject1 = new Subject();
        subject1.setId(1);
        subject1.setProffessor(proffessorUser);

        Subject subject2 = new Subject();
        subject2.setId(2);
        subject2.setProffessor(proffessorUser);

        Subject subject3 = new Subject();
        subject3.setId(3);
        subject3.setProffessor(fakeProffessor);

        Score score1 = new Score(
            subject1,
            studentUser,
            8.3f, 
            1);

        Score score2 = new Score(
            subject2,
            studentUser,
            8.3f, 
            2);
        
        Score score3 = new Score(
            subject3,
            studentUser,
            8.3f, 
            3);
        List<Score> scorelist = new ArrayList<>();
        scorelist.add(score1);
        scorelist.add(score2);
        scorelist.add(score3);

        when(persistenceManager.newQuery(Score.class)).thenReturn(queryScore);
        when(queryScore.executeList()).thenReturn(scorelist);

        Response response1 = resource.getScore("test-student", "test-student");

        assertEquals(Response.Status.OK, response1.getStatusInfo());
        assertEquals(3, ((ScoreData[])response1.getEntity()).length);

        Response response2 = resource.getScore("test-proffessor", "test-proffessor");

        assertEquals(Response.Status.OK, response2.getStatusInfo());
        assertEquals(2, ((ScoreData[])response2.getEntity()).length);
    }

    @Test
    public void testRegisterScore() {
        SubjectData subjectData = new SubjectData();
        subjectData.setId(1);
        subjectData.setProffessor(new UserData(proffessorUser));

        ScoreData scoreData = new ScoreData();
        scoreData.setId(1);
        scoreData.setScore(8.3f);
        scoreData.setStudent(new UserData(studentUser));
        scoreData.setSubject(subjectData);

        Response response1 = resource.registerScore("test-admin", "test-admin", new ScoreData());

        assertEquals(Response.Status.BAD_REQUEST, response1.getStatusInfo());

        Response response2 = resource.registerScore("test-dean", "test-dean", scoreData);

        assertEquals(Response.Status.OK, response2.getStatusInfo());

        Score score = spy(Score.class);
        when(persistenceManager.getObjectById(Score.class, scoreData.getId())).thenReturn(score);

        Response response3 = resource.registerScore("test-admin", "test-admin", scoreData);

        assertEquals(Response.Status.BAD_REQUEST, response3.getStatusInfo());
    }

    @Test
    public void testUpdateScore() {
        SubjectData subjectData = new SubjectData();
        subjectData.setId(1);
        subjectData.setProffessor(new UserData(proffessorUser));

        ScoreData scoreData = new ScoreData();
        scoreData.setId(1);
        scoreData.setScore(8.3f);
        scoreData.setStudent(new UserData(studentUser));
        scoreData.setSubject(subjectData);
        
        Response response1 = resource.updateScore("test-proffessor", "test-proffessor", 1, scoreData);
        
        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        Score score = spy(Score.class);
        when(score.getId()).thenReturn(scoreData.getId());
        when(score.getScore()).thenReturn(scoreData.getScore());
        when(score.getStudent()).thenReturn(null);
        when(persistenceManager.getObjectById(Score.class, scoreData.getId())).thenReturn(score);

        when(persistenceManager.getObjectById(Score.class, scoreData.getId())).thenReturn(score);

        Response response2 = resource.updateScore("test-proffessor", "test-proffessor", 1, scoreData);

        ArgumentCaptor<Float> scoreCaptor = ArgumentCaptor.forClass(Float.class);
        verify(score).setScore(scoreCaptor.capture());
        assertEquals(scoreData.getScore(), scoreCaptor.getValue());

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

    @Test
    public void testDeleteScore() {
        SubjectData subjectData = new SubjectData();
        subjectData.setId(1);
        subjectData.setProffessor(new UserData(proffessorUser));

        ScoreData scoreData = new ScoreData();
        scoreData.setId(1);
        scoreData.setScore(8.3f);
        scoreData.setStudent(new UserData(studentUser));
        scoreData.setSubject(subjectData);

        Response response1 = resource.deleteScore("test-dean", "test-dean", scoreData.getId());

        assertEquals(Response.Status.NOT_FOUND, response1.getStatusInfo());

        Subject subject = spy(Subject.class);
        when(persistenceManager.getObjectById(Subject.class, scoreData.getId())).thenReturn(subject);

        Response response2 = resource.deleteSubject("test-dean", "test-dean", scoreData.getId());

        assertEquals(Response.Status.OK, response2.getStatusInfo());
    }

}
