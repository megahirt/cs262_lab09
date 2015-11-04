package example;
import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

/**
 * Created by chris on 11/4/2015.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/hello/thailand")
public class HelloWorld {
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces("text/plain")
    public String getClichedMessage() {
        // Return some cliched textual content
        return "Hello Thailand";
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:9998/helloworld");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }

    private static String DB_URI = "jdbc:postgresql://localhost:5432/monopoly";
    private static String DB_LOGINID = "postgres";
    private static String DB_PASSWORD = "password";

    @GET
    @Path("/player/{id}")
    @Produces("text/plain")
    public String getPlayer(@PathParam("id") int id) {
        String result = "";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URI, DB_LOGINID, DB_PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Player WHERE id = " + id);
            if (resultSet.next()) {
                result = resultSet.getInt(1) + " " + resultSet.getString(3) + " " + resultSet.getString(2);
            } else {
                result = "nothing found...";
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            result = e.getMessage().toString();
        }
        return result;
    }
}
