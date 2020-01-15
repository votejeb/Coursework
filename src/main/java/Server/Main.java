package Server;


import Controllers.RawDatasController;
import Controllers.SearchandSort;
import Controllers.Twitter4jController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;

import static Controllers.ProcessedDatasController.CreateTable;
import static Controllers.SearchandSort.SortData;
import static java.sql.DriverManager.getConnection;

public class Main {
    public static Connection db = null;
/*
    public static void main(String[] args) {
        openDatabase("Database1.db");
        SortData("1",10);
        closeDatabase();
    }
*/
/*
    public static void main(String[] args) throws InterruptedException {
        openDatabase("Database1.db");
        //Twitter4jController.runStream("aNH2A2u6c1Hu4Q9VLo8tZhcdP", "MR5HLztZOE8X5DP6Voouh5z2nAFtHWEheg47TIFhMaPnv839by", "942163284245049350-LZASvUsl8Pvs66sxagrBxY2tPr1WxeG", "U36APPf6w23HdrJJPtugEMsKiGTOETfrBqOy13bdZNbHs","trump","en","1",300);

        closeDatabase();
    }
*/

    public static void main(String[] args) {
        openDatabase("Database1.db");
        ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        //Configuring server settings//

        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");
        //opens http listener at port 8081//

        try {
            server.start();
            //starts server//
            System.out.println("Server successfully started.");
            server.join();
            //joins server//
        } catch (Exception e) {
            //catches exceptions and prints stack trace errors//
            e.printStackTrace();
        }
    }



//This subroutine opens the database connection, outputs any errors with try catch statement
    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

//This subroutine closes the database connection, outputs any errors with try catch statement
    public static void closeDatabase(){
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }
}
