package es.deusto.spq.server;

import java.util.List;
import java.util.ArrayList;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import es.deusto.spq.server.jdo.User;
import es.deusto.spq.pojo.Role;
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

			try (Query<?> q = pm.newQuery("SELECT FROM " + User.class.getName() + " WHERE login == \""
					+ logIn + "\" &&  password == \""
					+ password + "\"")) {
				q.setUnique(true);
				user = (User) q.execute();

				logger.info("User retrieved: {}", user);
			} catch (Exception e) {
				e.printStackTrace();
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
			
			if (users != null) {
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
			} else {
				logger.info("Users not found");
				try {
					q.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				tx.commit();
				return Response.status(Status.NOT_FOUND).build();
			}
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	@POST
	@Path("/users")
	public Response registerUser(UserData userData) {
		try {
			tx.begin();
			logger.info("Checking whether the user already exists or not: '{}'", userData.getLogin());
			User user = null;
			try {
				user = pm.getObjectById(User.class, userData.getLogin());
			} catch (javax.jdo.JDOObjectNotFoundException jonfe) {
				logger.info("Exception launched: {}", jonfe.getMessage());
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

		Role[] roles = {Role.ADMIN, Role.DEAN, Role.PROFESSOR};

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
				logger.info("Deleted user: {}", user);

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
}
