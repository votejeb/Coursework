package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("linkedfilters/")

public class LinkedFiltersController {

    public static void CreateTable(String filterID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS LinkedFilters" + filterID + " (\n"
                    + "Words String ,\n"
                    + ");");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }

    //SQL SELECTALL//
    @GET
    @Path("readfilter{filterID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String SelectTable(@PathParam("filterID")Integer filterID) throws Exception {
        if(filterID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        System.out.println("linkedfilters/readfilter");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Words FROM LinkedFilters" + filterID + "");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("Words", results.getString(1));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //insert//
    @POST
    @Path("newfilter{filterID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertToTable(
            @PathParam("filterID")Integer filterID,
            @FormDataParam("newFilter") String filter){
        try {
            if (filter == null||filterID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("linkedfilter/newfilter ="+ filter);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO LinkedFilters" + filterID + " (Words)VALUES(?)");
            ps.setString(1, filter);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    //delete
    @POST
    @Path("deletefilter{filterID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteFromTable (
            @PathParam("filterID")Integer filterID,
            @FormDataParam("Words")String Words){
        try {
            if(Words==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/deleteuser id=" + Words);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM LinkedFilters" + filterID + " WHERE Words ?");
            ps.setString(1, Words);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    //delete whole table
    public static void DeleteTable(String filterID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE LinkedFilters" + filterID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }
}


