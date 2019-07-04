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
    public static void ShowUserInfo() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT UserID, UserName, TimeTo FROM Users");

            ResultSet results  = ps.executeQuery();
            while (results.next()) {
                int UserID = results.getInt(1);
                String UserName = results.getString(2);
                System.out.println(UserID + " " + UserName);
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }

}
