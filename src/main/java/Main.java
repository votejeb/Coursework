import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static Connection db = null;

    public static void main(String[] args) {
        openDatabase("Database1.db");
// code to get data from, write to the database etc goes here!
        ShowUserInfo();
        InsertUser(2,"test2","test2");
        updateUser(2,"test3","test3");
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

    //SQL SELECT//
    public static void ShowUserInfo() {
        try {
            PreparedStatement ps = db.prepareStatement("SELECT UserID, UserName, Password FROM Users");

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
//insert//

    public static void InsertUser(int UserID, String UserName, String Password){
        try {
            PreparedStatement ps = db.prepareStatement("INSERT INTO Users(UserID, UserName, Password)VALUES(?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.executeUpdate();
            System.out.println("User Added");

        } catch (Exception exception) {
            System.out.println("Database error"+exception.getMessage());
        }
    }

    //Update//
    public static void updateUser (int UserID, String UserName, String Password){
        try {

            PreparedStatement ps = db.prepareStatement("UPDATE Users SET UserName = ?, String Password = ? WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.executeUpdate();

        } catch (Exception exception) {

            System.out.println("Database Error"+exception.getMessage());

        }

    }


}
