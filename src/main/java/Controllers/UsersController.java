package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("users/")
public class UsersController {

    //SQL SELECTALL//
    @GET
    @Path("listusers")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowUserInfo() {
        System.out.println("users/listall");
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
    //SQL SELECT//
    @GET
    @Path("listone/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowOneUsersInfo(@PathParam("UserID")Integer UserID) throws Exception {
        if(UserID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        System.out.println("users/listone");
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Server.Main.db.prepareStatement("SELECT UserName, Password, ConsumerKey, ConsumerSecret, AccessToken, AccessSecret, ProcessRestrictionStream, ProcessRestrictionQuery FROM Users WHERE USERID=?");
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
                item.put("ProcessRestrictionStream",results.getString(7));
                item.put("ProcessRestrictionQuery",results.getString(8));
            }
            return item.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";

        }
    }

//insert//

    @POST
    @Path("newuser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String insertUser(
            @FormDataParam("UserID") Integer UserID,
            @FormDataParam("UserName")String UserName,
            @FormDataParam("Password")String Password){
        try {
            if (UserID == null || UserName == null ||Password==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/newuser id="+ UserID);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(UserID, UserName, Password, ProcessRestrictionStream, ProcessRestrictionQuery)VALUES(?,?,?,?,?)");
            ps.setInt(1, UserID);
            ps.setString(2, UserName);
            ps.setString(3, Password);
            ps.setInt(4,450);
            ps.setInt(5,180);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //Update//

    @POST
    @Path("updateuser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUser (
            @FormDataParam("UserID")Integer UserID,
            @FormDataParam("UserName")String UserName,
            @FormDataParam("Password")String Password,
            @FormDataParam("ConsumerKey")String ConsumerKey,
            @FormDataParam("ConsumerSecret")String ConsumerSecret,
            @FormDataParam("AccessToken")String AccessToken,
            @FormDataParam("AccessSecret")String AccessSecret,
            @FormDataParam("ProcessRestrictionStream")Integer ProcessRestrictionStream,
            @FormDataParam("ProcessRestrictionQuery")Integer ProcessRestrictionQuery){
        try {
            if (UserID == null || UserName == null || Password == null|| ProcessRestrictionStream == null || ProcessRestrictionQuery == null){
                throw new Exception("One or more data parameters are missing in the HTTP request");
            }
            System.out.println("users/updateuser id="+ UserID);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Users SET UserName = ?, Password = ?,  ConsumerKey = ?, ConsumerSecret = ?, AccessToken = ?, AccessSecret = ?, ProcessRestrictionStream = ?, ProcessRestrictionQuery = ? WHERE UserID = ?");
            ps.setString(1, UserName);
            ps.setString(2, Password);
            ps.setString(3, ConsumerKey);
            ps.setString(4, ConsumerSecret);
            ps.setString(5, AccessToken);
            ps.setString(6, AccessSecret);
            ps.setInt(7, ProcessRestrictionStream);
            ps.setInt(8, ProcessRestrictionQuery);
            ps.setInt(9, UserID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("deleteuser")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUser (@FormDataParam("UserID")Integer UserID){
        try {
            if(UserID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/deleteuser id=" + UserID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Users WHERE UserID= ?");
            ps.setInt(1, UserID);
            ps.execute();

            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }

    }
}
