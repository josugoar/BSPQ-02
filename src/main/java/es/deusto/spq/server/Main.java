package es.deusto.spq.server;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Main {
 
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/rest/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in es.deusto.spq package
        final ResourceConfig rc = new ResourceConfig().packages("es.deusto.spq");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        startServer();
        System.out.println(String.format("Jersey app started at "
                + "%sresource", BASE_URI));
    }
}
