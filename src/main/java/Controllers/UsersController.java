package Controllers;

import Server.Main;
import org.eclipse.jetty.server.Authentication;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

@Path("users/")
//The api path to call//
public class UsersController {

    //SQL SELECTALL//
    @GET
    //api path to call//
    @Path("listusers")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowUserInfo() {
        //outputs results to a JSON array//
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
            //returns exceptions//
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //SQL SELECT//
    @GET
    @Path("listone/{UserID}")
    //path takes the UserID as a parameter to modify the correct table, path params are used because it is a get request//
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowOneUsersInfo(@PathParam("UserID")Integer UserID,
                                   @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        if(UserID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONObject item = new JSONObject();
        //Creates JSON object and outputs results to it//
        try {
            PreparedStatement ps = Server.Main.db.prepareStatement("SELECT UserName, Password, ConsumerKey, ConsumerSecret, AccessToken, AccessSecret FROM Users WHERE USERID=?");
            ps.setInt(1,UserID);
            ResultSet results  = ps.executeQuery();
            if (results.next()) {
                item.put("UserID",UserID);
                item.put("UserName",results.getString(1));
                item.put("Password",results.getString(2));
                item.put("ConsumerKey",results.getString(3));
                item.put("ConsumerSecret",results.getString(4));
                item.put("AccessToken",results.getString(5));
                item.put("AccessSecret",results.getString(6));
            }
            return item.toString();
        } catch (Exception exception) {
            //returns exceptions//
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

//insert//

    @POST
    @Path("newuser")
    //api path to call//
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertUser(
            //takes input as form data paramater//
            @FormDataParam("UserID") Integer UserID,
            @FormDataParam("UserName")String UserName,
            @FormDataParam("Password")String Password,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            //data field not null verification//
            if (UserID == null || UserName == null ||Password==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            //executes prepared statement//
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(UserID, UserName, Password)VALUES(?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            //returns exceptions//
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //Update//

    @POST
    @Path("updateuser")
    //api path to call//
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateUser (
            @FormDataParam("UserID")Integer UserID,
            @FormDataParam("UserName")String UserName,
            @FormDataParam("Password")String Password,
            @FormDataParam("ConsumerKey")String ConsumerKey,
            @FormDataParam("ConsumerSecret")String ConsumerSecret,
            @FormDataParam("AccessToken")String AccessToken,
            @FormDataParam("AccessSecret")String AccessSecret,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            //verifies parameter existence//
            if (UserID == null || UserName == null || Password == null){
                throw new Exception("One or more data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?, Password = ?,  ConsumerKey = ?, ConsumerSecret = ?, AccessToken = ?, AccessSecret = ? WHERE UserID = ?");
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setString(3, ConsumerKey);
            ps.setString(4, ConsumerSecret);
            ps.setString(5, AccessToken);
            ps.setString(6, AccessSecret);
            ps.setInt(7, UserID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            //returns exceptions//
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("deleteuser")
    //api path to call//
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    //takes data as a form data parameter//
    public String DeleteUser (@FormDataParam("UserID")Integer UserID,
                              @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            //verifies parameter existence//
            if(UserID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID= ?");
            ps.setInt(1, UserID);
            ps.execute();

            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            //returns exceptions//
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String loginUser(@FormDataParam("username") String username, @FormDataParam("password") String password) {

        try {

            System.out.println("user/login");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT Password FROM Users WHERE UserName = ?");
            ps1.setString(1, username);
            ResultSet loginResults = ps1.executeQuery();
            if (loginResults.next()) {

                String correctPassword = loginResults.getString(1);
                if (password.equals(correctPassword)) {

                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = ? WHERE UserName = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, username);
                    ps2.executeUpdate();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", username);
                    userDetails.put("token", token);
                    return userDetails.toString();

                } else {
                    return "{\"error\": \"Incorrect password!\"}";
                }

            } else {
                return "{\"error\": \"Unknown user!\"}";
            }

        } catch (Exception exception) {
            System.out.println("Database error during /user/login: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutUser(@CookieParam("token") String token) {

        try {

            System.out.println("user/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Users SET Token = NULL WHERE UserID = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";

            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }
    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT UserID FROM Users WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }
}







