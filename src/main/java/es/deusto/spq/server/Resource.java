package es.deusto.spq.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import es.deusto.spq.server.jdo.User;
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

	@PUT
	@Path("/{login}/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("login") String login, UserData userData) {
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
	@Path("/{login}/delete")
	public Response deleteUser(@PathParam("login") String login) {
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

	@GET
	@Path("/login")
	public Response login(@QueryParam("login") String login, @QueryParam("password") String password) {
		User user = null;
		try {
			tx.begin();
			logger.info("Creating query ...");

			try (Query<?> q = pm.newQuery("SELECT FROM " + User.class.getName() + " WHERE login == \""
					+ login + "\" &&  password == \""
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
			logger.info(" * Client login: {}", login);
			UserData userData = new UserData();
			userData.setLogin(user.getLogin());
			userData.setPassword(user.getPassword());
			userData.setName(user.getName());
			userData.setSurname(user.getPassword());
			userData.setRole(user.getRole());
			return Response.ok(userData).build();
		} else {
			return Response.status(Status.BAD_REQUEST)
					.entity("Login details supplied for message delivery are not correct").build();
		}
	}

	@GET
	@Path("/{login}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("login") String login) {
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
				tx.commit();
				return Response.status(Status.OK).entity(user).build();
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
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		List<User> users = null;
		List<UserData> usersdat = null;
		try {
			tx.begin();
			logger.info("Creating query ...");

			try (Query<User> q = pm.newQuery(User.class)) {
				users = q.executeList();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (users != null) {
				for (User user : users) {
					UserData usdat = null;
					usdat.setLogin(user.getLogin());
					usdat.setName(user.getName());
					usdat.setSurname(user.getSurname());
					usdat.setRole(user.getRole());
					usersdat.add(usdat);

				}
				tx.commit();
				return Response.status(Status.OK).entity(usersdat).build();
			} else {
				logger.info("Users not found");

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
	@Path("/register")
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
}
