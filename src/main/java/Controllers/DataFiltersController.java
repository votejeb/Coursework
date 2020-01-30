package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("datafilters/")
public class DataFiltersController {

    @GET
    @Path("listone/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowFilters(@PathParam("UserID")Integer UserID) throws Exception {
        if(UserID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONArray list = new JSONArray();

        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT DataFilterID, DataFilterName, WhitelistBlacklist, PublicPrivate FROM DataFilters Where PublicPrivate=true OR DataFilterID IN (SELECT DataFilterID FROM UserFilterLink Where UserID = ?)");
            ps.setInt(1, UserID);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("DataFilterID", results.getInt(1));
                item.put("DataFilterName", results.getString(2));
                item.put("WhitelistBlacklist", results.getString(3));
                item.put("PublicPrivate", results.getBoolean(4));
                list.add(item);
            }
        return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //insert
    @POST
    @Path("newfilter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertFilter(
            @FormDataParam("DataFilterName")String DataFilterName,
            @FormDataParam("UserID")Integer UserID,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (DataFilterName == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            //created datafilter table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO DataFilters(DataFilterName, WhitelistBlacklist, PublicPrivate)VALUES(?,?,?)");
            ps.setString(1, DataFilterName);
            ps.setBoolean(2,true);
            ps.setBoolean(3,true);
            ps.execute();
            //gets last insertaed autoincrement table from dataset
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT last_insert_rowid() AS rowid FROM DataFilters LIMIT 1");
            ResultSet results  = ps1.executeQuery();
            int results1 = results.getInt(1);
            //inserts composite key into userfilter link
            PreparedStatement ps2 = Main.db.prepareStatement("INSERT INTO UserFilterLink(UserID,FilterID)VALUES(?,?)");
            ps2.setInt(1, UserID);
            ps2.setInt(2,results1);
            ps2.execute();
            //creates linked filter controller
            LinkedFiltersController.CreateTable(results1);
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //Update//
    @POST
    @Path("updatefilter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String UpdateUser (
            @FormDataParam("DataFilterID") Integer SetID,
            @FormDataParam("DataFilterName") String DataFilterName,
            @FormDataParam("WhitelistBlacklist")Boolean WhitelistBlacklist,
            @FormDataParam("PublicPrivate")Boolean PublicPrivate,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (SetID == null || PublicPrivate == null){
                throw new Exception("One or more data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("UPDATE DataFilters SET WhitelistBlacklist = ?, PublicPrivate = ?, DataFilterName=? WHERE DataFilterID = ?");
            ps.setBoolean(1, PublicPrivate);
            ps.setBoolean(2, WhitelistBlacklist);
            ps.setString(3, DataFilterName);
            ps.setInt(4, SetID);
            ps.execute();
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

//delete//
    @POST
    @Path("deletefilter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String DeleteFilter(@FormDataParam("DataFilterID") Integer DataFilterID,
                                      @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if(DataFilterID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }

            LinkedFiltersController.DeleteTable(DataFilterID);

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM DataFilters WHERE DataFilterID= ?");
            ps.setInt(1, DataFilterID);
            ps.execute();
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}

