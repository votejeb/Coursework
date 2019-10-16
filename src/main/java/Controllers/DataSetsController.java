package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("datasets/")
public class DataSetsController {
    //SQL SELECT//
    @GET
    @Path("listone/{UserID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String ShowDataSets(@PathParam("UserID")Integer OriginUser) throws Exception {
        if(OriginUser==null){
            throw new Exception("One or more form data parameters are missing in the HTTP request.");
        }
        JSONObject item = new JSONObject();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT SetID, Keyword, StartTime, Runtime, PublicPrivate FROM DataSets Where PublicPrivate=true OR OriginUser=?");
            ps.setInt(1,OriginUser);
            ResultSet results  = ps.executeQuery();
            if (results.next()) {
                item.put("SetID",results.getInt(1));
                item.put("KeyWord",results.getString(2));
                item.put("StartTime",results.getString(3));
                item.put("RunTime",results.getInt(4));
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
    @Path("newset")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String InsertDataSet(
            @FormDataParam("KeywordID") Integer SetID,
            @FormDataParam("Keyword")String Keyword,
            @FormDataParam("StartTime")Integer StartTime,
            @FormDataParam("RunTime")Integer RunTime){
        try {
            if (SetID == null || Keyword == null || StartTime==null || RunTime==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO DataSets(SetID, Keyword, StartTime, RunTime,PublicPrivate)VALUES(?,?,?,?,?)");
            ps.setInt(1, SetID);
            ps.setString(2, Keyword);
            ps.setInt(3, StartTime);
            ps.setInt(4,RunTime);
            ps.setBoolean(5,true);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

///delete//
    @POST
    @Path("deletedataset")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteDataSet (@FormDataParam("KeywordID")Integer KeywordID){
        try {
            if(KeywordID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM DataSets WHERE SetID= ?");
            ps.setInt(1, KeywordID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
