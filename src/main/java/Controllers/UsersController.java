package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")
public class UsersController {

    //SQL SELECT//
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowUserInfo() {
        System.out.println("thing/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID, UserName, Password FROM Users");
            ResultSet results  = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("UserID", results.getInt(1));
                item.put("UserName", results.getString(2));
                item.put("Password", results.getString(3));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";

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
