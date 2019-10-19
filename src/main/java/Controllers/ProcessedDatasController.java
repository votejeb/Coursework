package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("processeddatas/")

public class ProcessedDatasController {

    public static void CreateTable(Integer TableID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS ProcessedDatas_"+TableID+" (\n"
                    + "Words String ,\n"
                    + "WordCount int ,\n"
                    + "TimePeriod String,\n"
                    + ");");
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());

        }
    }
    //SQL SELECTALL//
    @GET
    @Path("readkeywords{SetID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String SelectTable(@PathParam("SetID")Integer TableID) throws Exception {
        if(TableID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Words, WordCount, TimePeriod FROM ProcessedDatas_" + TableID);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("Words", results.getString(1));
                item.put("WordCount", results.getInt(2));
                item.put("TimePeriod", results.getString(3));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //insert data
    public static void InsertToTable(String Words, String TimePeriod, int TableID ){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO ProcessedDatas_"+TableID+" (Words, TimePeriod)VALUES(?,?)");
            ps.setString(1, Words);
            ps.setInt(2,1);
            ps.setString(3, TimePeriod);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }

    //Update//
    public static void UpdateTable (int WordCount, String Words, int TableID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE ProcessedDatas_"+TableID+" SET WordCount = ?, WHERE Words = ?");
            ps.setInt(1, WordCount);
            ps.setString(2, Words);
            ps.execute();
            System.out.println("User Updated");
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
        }
    }

    //delete data
    @POST
    @Path("deleteprocesseddatas")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteTable(@FormDataParam("processeddataID") String TableID) {
        try {
            if(TableID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE ProcessedDatas_" + TableID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

}
