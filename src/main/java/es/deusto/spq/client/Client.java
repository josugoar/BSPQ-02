package es.deusto.spq.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    protected static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Use: java Client.Client [host] [port]");
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        LoginClient loginClient = new LoginClient(hostname, port);
        loginClient.setVisible(true);
    }
}
