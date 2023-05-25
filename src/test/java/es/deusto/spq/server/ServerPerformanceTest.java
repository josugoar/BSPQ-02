package es.deusto.spq.server;

import junit.framework.JUnit4TestAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.databene.contiperf.Required;
import org.databene.contiperf.PerfTest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import categories.PerformanceTest;

@PerfTest(invocations = 5)
@Required(max = 1200, average = 250)
@Category(PerformanceTest.class)
public class ServerPerformanceTest {
	final Logger logger = LogManager.getLogger(ServerPerformanceTest.class);
	static int iteration = 0;

	ResourceTest resource = new ResourceTest();

	// @Rule public ContiPerfRule rule = new ContiPerfRule();

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(ServerPerformanceTest.class);
	}

	@Before
	public void setUp() {
		logger.info("Entering setUp: {}", iteration++);

		resource.setUp();

		logger.info("Leaving setUp");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testAuthenticate() throws Exception {
		logger.info("Starting testAuthenticate");
		resource.testAuthenticate();
		logger.debug("Finishing testAuthenticate");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testLogin() throws Exception {
		logger.info("Starting testLogin");
		resource.testLogin();
		logger.debug("Finishing testLogin");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testGetUser() throws Exception {
		logger.info("Starting testGetUser");
		resource.testGetUser();
		logger.debug("Finishing testGetUser");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testRegisterUser() throws Exception {
		logger.info("Starting testRegisterUser");
		resource.testRegisterUser();
		logger.debug("Finishing testRegisterUser");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testRegisterUserNotFound() throws Exception {
		logger.info("Starting testRegisterUserNotFound");
		resource.testRegisterUserNotFound();
		logger.debug("Finishing testRegisterUserNotFound");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testGetUsers() throws Exception {
		logger.info("Starting testGetUsers");
		resource.testGetUsers();
		logger.debug("Finishing testGetUsers");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testUpdateUsers() throws Exception {
		logger.info("Starting testUpdateUsers");
		resource.testUpdateUsers();
		logger.debug("Finishing testUpdateUsers");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testDeleteUsers() throws Exception {
		logger.info("Starting testDeleteUsers");
		resource.testDeleteUsers();
		logger.debug("Finishing testDeleteUsers");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testGetSubjects() throws Exception {
		logger.info("Starting testGetSubjects");
		resource.testGetSubjects();
		logger.debug("Finishing testGetSubjects");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testRegisterSubject() throws Exception {
		logger.info("Starting testRegisterSubject");
		resource.testRegisterSubject();
		logger.debug("Finishing testRegisterSubject");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void updateSubjectTest() throws Exception {
		logger.info("Starting updateSubjectTest");
		resource.updateSubjectTest();
		logger.debug("Finishing updateSubjectTest");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void deleteSubjectTest() throws Exception {
		logger.info("Starting deleteSubjectTest");
		resource.deleteSubjectTest();
		logger.debug("Finishing deleteSubjectTest");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testGetScore() throws Exception {
		logger.info("Starting testGetScore");
		resource.testGetScore();
		logger.debug("Finishing testGetScore");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testRegisterScore() throws Exception {
		logger.info("Starting testRegisterScore");
		resource.testRegisterScore();
		logger.debug("Finishing testRegisterScore");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testUpdateScore() throws Exception {
		logger.info("Starting testUpdateScore");
		resource.testUpdateScore();
		logger.debug("Finishing testUpdateScore");
	}

	@Test
	@PerfTest(invocations = 1000, threads = 20)
	@Required(max = 1200, average = 300)
	public void testDeleteScore() throws Exception {
		logger.info("Starting testDeleteScore");
		resource.testDeleteScore();
		logger.debug("Finishing testDeleteScore");
	}

}
