import org.sqlite.SQLiteConfig;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.sql.*;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("Database1.db");
// code to get data from, write to the database etc goes here!
        closeDatabase();
    }
//This subroutine opens the database connection, outputs any errors with try catch statement
    private static void openDatabase(String dbFile) {
        try  {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");
        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }

//This subroutine closes the database connection, outputs any errors with try catch statement
    private static void closeDatabase(){
        try {
            db.close();
            System.out.println("Disconnected from database.");
        } catch (Exception exception) {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }

    //SQL SELECT
    public static void ShowTimeFrames() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT TimeFrameID, TimeFrom, TimeTo FROM TimeFrames");

            ResultSet results  = ps.executeQuery();
            while (results.next()) {
                int TimeFrameID = results.getInt(1);
                Date TimeFrom = results.getDate(2);
                Date TimeTo = results.getDate(3);
                System.out.println(TimeFrameID + " " + TimeFrom + " " + TimeTo);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

}
