package Controllers;

import Server.Main;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("linkedkeywords/")

public class AssignedKeywordController {

    public static void CreateTable(Integer keywordID){
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
            PreparedStatement ps = Main.db.prepareStatement("SELECT Words, OccurenceCount, LikeCount FROM LinkedKeywords" + keywordID);
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
    public static void DeleteTable(Integer keywordID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("DELETE LinkedKeywords" + keywordID);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }
}
