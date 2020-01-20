package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import static Util.customUtil.listToString;

@Path("processeddatas/")

public class ProcessedDatasController {

    public static void CreateTable(String TableID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS ProcessedDatas_" + TableID + " (\n"
                    + "Words String UNIQUE)");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }

    //Alter Table
    public static void AlterTable(String TableID, String TimeID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("ALTER TABLE ProcessedDatas_" + TableID + " ADD COLUMN WordCount_"+TimeID+" Integer");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }

    //SQL SELECTALL//
    @GET
    @Path("readkeywords/{SetID}/{RawSets}")
    @Produces(MediaType.APPLICATION_JSON)
    public String SelectTable(@PathParam("SetID")String TableID, @PathParam("RawSets")String RawSets) throws Exception {
        if(TableID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT * FROM ProcessedDatas_" + TableID);
            ResultSet results = ps.executeQuery();
            String[] TimeID = RawSets.split("-");
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("Words", results.getString(1));
                for (int i = 0; i < TimeID.length; ++i) {
                    int[] temp= new int[TimeID.length];
                    for (int j = 0; j < TimeID.length; j++) {
                        temp[j]=results.getInt(j+2);
                    }
                    item.put("WordCount", Arrays.toString(temp));
                }
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //insert words
    public static void InsertWordsToTable(String Words, String TableID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT OR IGNORE INTO ProcessedDatas_"+TableID+" (Words) VALUES(?)");
            ps.setString(1, Words);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }

    //Update//
    public static void UpdateTable (int WordCount, String Words, String TableID, String TimeID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE ProcessedDatas_"+TableID+" SET WordCount_"+TimeID+" = ? WHERE Words = ?");
            ps.setInt(1, WordCount);
            ps.setString(2, Words);
            ps.execute();
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
