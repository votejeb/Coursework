import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UsersController {

    //SQL SELECT//
    public static void ShowUserInfo() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users");
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
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(UserID, UserName, Password)VALUES(?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.executeUpdate();
            System.out.println("User Added");

        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }

    //Update//
    public static void updateUser (int UserID, String UserName, String Password){
        try {

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?, Password = ? WHERE UserID = ?");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.executeUpdate();
            System.out.println("User Updated");

        } catch (Exception exception) {

            System.out.println("Database Error "+exception.getMessage());

        }

    }

    public static void deleteUser (int UserID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID= ?");
            ps.setInt(1, UserID);
            ps.executeUpdate();
            System.out.println("User Deleted");

        } catch (Exception e) {
            System.out.println("Database error "+e.getMessage());

        }

    }
}
