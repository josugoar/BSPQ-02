package es.deusto.spq.server;

import java.util.List;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import es.deusto.spq.server.jdo.Score;
import es.deusto.spq.server.jdo.Subject;
import es.deusto.spq.server.jdo.User;
import es.deusto.spq.pojo.Role;
import es.deusto.spq.pojo.ScoreData;
import es.deusto.spq.pojo.SubjectData;
import es.deusto.spq.pojo.UserData;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Path("/resource")
@Produces(MediaType.APPLICATION_JSON)
public class Resource {

	protected static final Logger logger = LogManager.getLogger();

	private PersistenceManager pm = null;
	private Transaction tx = null;

	public Resource() {
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
		this.pm = pmf.getPersistenceManager();
		this.tx = pm.currentTransaction();
	}

	private boolean authenticate(String login, String password){
		User user = null;
		try {
			tx.begin();
			logger.info("Checking whether the user exists or not: '{}'", login);
			try {
				user = pm.getObjectById(User.class, login);
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
			}
			logger.info("User: {}", user);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		if(user!=null){
			if (password.equals(user.getPassword())) {
				return true;
			} else {
				logger.info("Password is not correct");
				return false;
			}
		} else {
			logger.info("The user does not exist");
			return false;
		}
		
	}

	private boolean authorize(String login, Role[] roles){
		User user = null;
		try {
			tx.begin();
			logger.info("Checking whether the user exists or not: '{}'", login);
			try {
				user = pm.getObjectById(User.class, login);
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
			}
			logger.info("User: {}", user);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
		if(user!=null){
			for (int i = 0; i < roles.length; i++) {
				if (roles[i].equals(user.getRole()) ) {
					return true;
				}
			}
			logger.info("Role no authorizated");
			return false;
		} else {
			logger.info("The user does not exist");
			return false;
		}
	}

	@GET
	@Path("/login")
	public Response login(@QueryParam("login") String logIn, @QueryParam("password") String password) {
		User user = null;
		try {
			tx.begin();
			logger.info("Creating query ...");

			try {
				user = pm.getObjectById(User.class, logIn);
				if (user != null) {
					if (!password.equals(user.getPassword())) {
						user = null;
					} else {
						logger.info("User retrieved: {}", user);
					}
				}
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}

			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}

		if (user != null) {
			logger.info(" * Client login: {}", logIn);
			UserData userData = new UserData(user);
			return Response.ok(userData).build();
		} else {
			return Response.status(Status.BAD_REQUEST)
					.entity("Login details supplied for message delivery are not correct").build();
		}
	}

	@GET
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers(@QueryParam("login") String logIn, @QueryParam("password") String password) {
		List<User> users = null;
		List<UserData> usersdat = new ArrayList<>();

		Role[] roles = {Role.ADMIN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Creating query ...");

			Query<User> q = pm.newQuery(User.class);
			users = q.executeList();
			
			for (User user : users) {
				UserData usdat = new UserData(user);
				usersdat.add(usdat);
			}
			try {
				q.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			tx.commit();
			return Response.status(Status.OK).entity(usersdat.toArray(new UserData[0])).build();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/users")
	public Response registerUser(@QueryParam("login") String logIn, @QueryParam("password") String password, UserData userData) {
		Role[] roles = {Role.ADMIN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the user already exists or not: '{}'", userData.getLogin());
			User user = null;
			try {
				user = pm.getObjectById(User.class, userData.getLogin());
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}
			logger.info("User: {}", user);
			if (user != null) {
				logger.info("The user already exists");

				tx.commit();
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				logger.info("Creating user: {}", user);
				user = new User(userData.getLogin(), userData.getPassword(), userData.getName(), userData.getSurname(),
						userData.getRole());
				pm.makePersistent(user);
				logger.info("User created: {}", user);

				tx.commit();
				return Response.status(Status.OK).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}

		}
	}

	@GET
	@Path("/users/{login}/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("login") String login) {

		Role[] roles = {Role.ADMIN, Role.DEAN, Role.PROFFESSOR};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the user already exists or not: '{}'", login);
			User user = null;
			try {
				user = pm.getObjectById(User.class, login);
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
			}
			logger.info("User: {}", user);
			if (user != null) {
				UserData userData = new UserData(user);
				tx.commit();
				return Response.status(Status.OK).entity(userData).build();
			} else {
				logger.info("The user does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@PUT
	@Path("/users/{login}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("login") String login, UserData userData) {
		
		Role[] roles = {Role.ADMIN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the user already exists or not: '{}'", login);
			User user = null;
			try {
				user = pm.getObjectById(User.class, login);
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
			}
			logger.info("User: {}", user);
			if (user != null) {
				logger.info("Setting password user: {}", user);
				user.setPassword(userData.getPassword());
				logger.info("Password set user: {}", user);

				logger.info("Setting name user: {}", user);
				user.setName(userData.getName());
				logger.info("Password set user: {}", user);

				logger.info("Setting surname user: {}", user);
				user.setSurname(userData.getSurname());
				logger.info("Surname set user: {}", user);

				logger.info("Setting role user: {}", user);
				user.setRole(userData.getRole());
				logger.info("Role set user: {}", user);

				logger.info("User updated: {}", user);

				tx.commit();
				return Response.status(Status.OK).build();
			} else {
				logger.info("The user does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@DELETE
	@Path("/users/{login}/delete")
	public Response deleteUser(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("login") String login) {
		
		Role[] roles = {Role.ADMIN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the user already exists or not: '{}'", login);
			User user = null;
			try {
				user = pm.getObjectById(User.class, login);
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
			}
			logger.info("User: {}", user);
			if (user != null) {
				logger.info("Deleting user: {}", user);
				pm.deletePersistent(user);

				tx.commit();
				return Response.status(Status.OK).build();
			} else {
				logger.info("The user does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/subjects")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSubjects(@QueryParam("login") String logIn, @QueryParam("password") String password) {
		List<Subject> subjects = null;
		List<SubjectData> subjectsdat = new ArrayList<>();

		Role[] roles = {Role.DEAN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Creating query ...");

			Query<Subject> q = pm.newQuery(Subject.class);
			subjects = q.executeList();
			
			for (Subject subject : subjects) {
				SubjectData subjectdat = new SubjectData(subject);
				subjectsdat.add(subjectdat);
			}
			try {
				q.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			tx.commit();
			return Response.status(Status.OK).entity(subjectsdat.toArray(new SubjectData[0])).build();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}

	}

	@POST
	@Path("/subjects")
	public Response registerSubject(@QueryParam("login") String logIn, @QueryParam("password") String password, SubjectData subjectData) {
		Role[] roles = {Role.DEAN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the subject already exists or not: '{}'", subjectData.getId());
			Subject subject = null;
			try {
				subject = pm.getObjectById(Subject.class, subjectData.getId());
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}
			logger.info("Subject: {}", subject);
			if (subject != null) {
				logger.info("The user already exists");

				tx.commit();
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				logger.info("Creating subject: {}", subject);

				UserData proffessor = subjectData.getProffessor();
				subject = new Subject(subjectData.getStartDate(), subjectData.getName(), 
				new User(proffessor.getLogin(), proffessor.getPassword(), proffessor.getName(), proffessor.getSurname(), proffessor.getRole()),
				subjectData.getId(), subjectData.getFaculty());

				pm.makePersistent(subject);
				logger.info("Subject created: {}", subject);

				tx.commit();
				return Response.status(Status.OK).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@PUT
	@Path("/subjects/{id}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSubject(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("id") Integer id, SubjectData subjectData) {
		Role[] roles = {Role.DEAN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the Subject already exists or not: '{}'", subjectData.getId());
			Subject subject = null;
			try {
				subject = pm.getObjectById(Subject.class, subjectData.getId());
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
			}
			logger.info("User: {}", subject);
			if (subject != null) {

				logger.info("Setting name subject: {}", subject);
				subject.setName(subjectData.getName());
				logger.info("Name set subject: {}", subject);

				logger.info("Setting starting date subject: {}", subject);
				subject.setStartDate(subjectData.getStartDate());
				logger.info("Starting date set subject: {}", subject);

				logger.info("Setting proffessor subject: {}", subject);
				UserData proffessor = subjectData.getProffessor();
				subject.setProffessor(new User(proffessor.getLogin(), proffessor.getPassword(), proffessor.getName(), proffessor.getSurname(), proffessor.getRole()));
				logger.info("Proffessor set subject: {}", subject);

				logger.info("Subject updated: {}", subject);

				tx.commit();
				return Response.status(Status.OK).build();
			} else {
				logger.info("The subject does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@DELETE
	@Path("/subjects/{id}/delete")
	public Response deleteSubject(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("id") Integer id) {
		Role[] roles = {Role.DEAN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the subject already exists or not: '{}'", id);
			Subject subject = null;
			try {
				subject = pm.getObjectById(Subject.class, id);
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}
			logger.info("Subject: {}", subject);
			if (subject != null) {
				logger.info("Deleting subject: {}", subject);
				pm.deletePersistent(subject);
				logger.info("Deleted subject: {}", subject);

				tx.commit();
				return Response.status(Status.OK).build();
			} else {
				logger.info("The subject does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@GET
	@Path("/scores")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScore(@QueryParam("login") String logIn, @QueryParam("password") String password) {
		List<Score> scores = null;
		List<ScoreData> scoresdata = new ArrayList<>();

		Role[] roles = {Role.DEAN, Role.PROFFESSOR, Role.STUDENT};

		User user = null;
			try {
				user = pm.getObjectById(User.class, logIn);
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Creating query ...");

			Query<Score> q = pm.newQuery(Score.class);
			scores = q.executeList();
			
			for (Score score : scores) {
				ScoreData scoredat = new ScoreData(score);
				switch (user.getRole()) {
					case STUDENT:
						if (score.getStudent().getLogin().equals(logIn)) {
							scoresdata.add(scoredat);
						}
						break;
					case PROFFESSOR:
						if (score.getSubject().getProffessor().getLogin().equals(logIn)) {
							scoresdata.add(scoredat);
						}
						break;
					case DEAN:
					case ADMIN:
						scoresdata.add(scoredat);
						break;
					default:
						break;
				}
			}
			try {
				q.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			tx.commit();
			return Response.status(Status.OK).entity(scoresdata.toArray(new ScoreData[0])).build();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/scores")
	public Response registerScore(@QueryParam("login") String logIn, @QueryParam("password") String password, ScoreData scoreData) {

		Role[] roles = {Role.DEAN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the score already exists or not: '{}'", scoreData.getId());
			Score score = null;
			try {
				score = pm.getObjectById(Score.class, scoreData.getId());
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}
			logger.info("Score: {}", score);
			if (score != null) {
				logger.info("The score already exists");

				tx.commit();
				return Response.status(Status.BAD_REQUEST).build();
			} else {
				logger.info("Creating score: {}", score);
				SubjectData subject = scoreData.getSubject();
				UserData proffessor = subject.getProffessor();
				UserData student = scoreData.getStudent();

				score = new Score(
					new Subject(subject.getStartDate(), subject.getName(), new User(proffessor.getLogin(), proffessor.getPassword(), proffessor.getName(), proffessor.getSurname(), proffessor.getRole()), subject.getId(), subject.getFaculty())	, 
					new User(student.getLogin(), student.getPassword(), student.getName(), student.getSurname(), student.getRole()), 
					scoreData.getScore(), 
					scoreData.getId());
				pm.makePersistent(score);
				logger.info("Score created: {}", score);

				tx.commit();
				return Response.status(Status.OK).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}


	@PUT
	@Path("/scores/{id}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateScore(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("id") Integer id, ScoreData scoreData) {
		Role[] roles = {Role.PROFFESSOR};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}
		logger.info("Aqui no llega");
		try {
			tx.begin();
			logger.info("Checking whether the score already exists or not: '{}'", id);
			Score score = null;
			try {
				score = pm.getObjectById(Score.class, id);
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}
			logger.info("Score: {}", score);
			if (score != null) {
				logger.info("Setting student score: {}", score);
				UserData user = scoreData.getStudent();
				score.setStudent(new User(user.getLogin(), user.getPassword(), user.getName(), user.getSurname(), user.getRole()));
				logger.info("Student set score: {}", score);

				// TODO: get users by id
				logger.info("Setting Subject score: {}", score);
				SubjectData subject = scoreData.getSubject();
				score.setSubject(new Subject(subject.getStartDate(), subject.getName(), 
				new User(subject.getProffessor().getLogin(), subject.getProffessor().getPassword(), subject.getProffessor().getName(), subject.getProffessor().getSurname(), subject.getProffessor().getRole()),
				subject.getId(), subject.getFaculty()));
				logger.info("Subject set score: {}", score);

				logger.info("Setting Score score: {}", score);
				score.setScore(scoreData.getScore());
				logger.info("Score set score: {}", user);

				logger.info("Score updated: {}", score);

				tx.commit();
				return Response.status(Status.OK).build();
			} else {
				logger.info("The user does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@DELETE
	@Path("/scores/{id}/delete")
	public Response deleteScore(@QueryParam("login") String logIn, @QueryParam("password") String password, @PathParam("id") Integer id) {
		
		Role[] roles = {Role.DEAN};

		if(authenticate(logIn, password)){
			logger.info("User authenticated");
		} else {
			logger.info("Authentication failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		if(authorize(logIn, roles)){
			logger.info("User authorized");
		} else {
			logger.info("Authorization failed");
			return Response.status(Status.BAD_REQUEST).build();
		}

		try {
			tx.begin();
			logger.info("Checking whether the score already exists or not: '{}'", id);
			Score score = null;
			try {
				score = pm.getObjectById(Score.class, id);
			} catch (javax.jdo.JDOObjectNotFoundException e) {
				logger.info("Exception launched: {}", e.getMessage());
			}
			logger.info("Score: {}", score);
			if (score != null) {
				logger.info("Deleting score: {}", score);
				pm.deletePersistent(score);
				logger.info("Deleted score: {}", score);

				tx.commit();
				return Response.status(Status.OK).build();
			} else {
				logger.info("The score does not exist");

				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

}
