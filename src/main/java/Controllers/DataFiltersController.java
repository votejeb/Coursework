package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataFiltersController {

    @GET
    @Path("listone/{filterID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowFilters(@PathParam("filterID")Integer DataFilterID) throws Exception {
        if(DataFilterID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT DataFilterID, DataFilterName, WhitelistBlacklist, OriginUser, PublicPrivate FROM DataFilters Where PublicPrivate=true OR OriginUser=?");
            ps.setInt(1,DataFilterID);
            ResultSet results  = ps.executeQuery();
            if (results.next()) {
                item.put("DataFilterID",results.getInt(1));
                item.put("DataFilterName",results.getString(2));
                item.put("WhitelistBlacklist",results.getString(3));
                item.put("OriginUser",results.getInt(4));
                item.put("PublicPrivate",results.getBoolean(5));
            }
            return item.toString();
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
            @FormDataParam("DataFilterID") Integer DataFilterID,
            @FormDataParam("DataFilterName")String DataFilterName,
            @FormDataParam("OriginUser")Integer OriginUser,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (DataFilterID == null || DataFilterName == null || OriginUser==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO DataFilters(DataFilterID, DataFilterName, WhitelistBlacklist, OriginUser, PublicPrivate)VALUES(?,?,?,?,?)");
            ps.setInt(1, DataFilterID);
            ps.setString(2, DataFilterName);
            ps.setBoolean(3,true);
            ps.setInt(4, OriginUser);
            ps.setBoolean(5,true);
            ps.execute();
            LinkedFiltersController.CreateTable(DataFilterID);
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
            @FormDataParam("SetID") Integer SetID,
            @FormDataParam("PublicPrivate")Boolean PublicPrivate,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (SetID == null || PublicPrivate == null){
                throw new Exception("One or more data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("UPDATE DataFitlers SET PublicPrivate = ? WHERE SetID = ?");
            ps.setBoolean(1, PublicPrivate);
            ps.setInt(2, SetID);
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
    public String DeleteFilter (@FormDataParam("datafilterID") Integer DataFilterID,
                                @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if(DataFilterID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM DataFilters WHERE DataFilterID= ?");
            ps.setInt(1, DataFilterID);
            ps.execute();
            LinkedFiltersController.DeleteTable(DataFilterID);
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}

