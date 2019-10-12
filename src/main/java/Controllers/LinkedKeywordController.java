package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("linkedkeywords/")

public class LinkedKeywordController {

    public static void CreateTable(String keywordID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS LinkedKeywords"+keywordID+" (\n"
                    + "words String ,\n"
                    + "OccurenceCount int,\n"
                    + "LikeCount int\n"
                    + ");");
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());

        }
    }
    //SQL SELECTALL//
    @GET
    @Path("readkeywords{keywordID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String SelectTable(@PathParam("keywordID")Integer keywordID) throws Exception {
        if(keywordID==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        System.out.println("linkedkeywords/readkeywords");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Words, OccurenceCount, LikeCount FROM LinkedKeywords" + keywordID + "");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("Words", results.getString(1));
                item.put("OccurenceCount", results.getInt(2));
                item.put("LikeCount", results.getInt(3));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    //delete whole table
    public static void DeleteTable(String keywordID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE LinkedKeywords" + keywordID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }
}
