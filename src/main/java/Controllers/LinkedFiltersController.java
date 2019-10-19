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

    public static void CreateTable(Integer TableID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS LinkedFilters_"+TableID+" (\n"
                    + "Words String\n"
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
    public String SelectTable(@PathParam("filterID")Integer TableID) throws Exception {
        if(TableID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Words FROM LinkedFilters_" + TableID + "");
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
    @Path("newfilter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertToTable(
            @FormDataParam("filterID")Integer TableID,
            @FormDataParam("newFilter") String filter){
        try {
            if (filter == null||TableID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO LinkedFilters_" + TableID + " (Words)VALUES(?)");
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
    @Path("deletefilter")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteFromTable (
            @FormDataParam("filterID")Integer TableID,
            @FormDataParam("Words")String Words){
        try {
            if(Words==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM LinkedFilters_" + TableID + " WHERE Words ?");
            ps.setString(1, Words);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    //delete whole table
    public static void DeleteTable(Integer TableID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE LinkedFilters_" + TableID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }
}


