package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("rawdata/")
public class RawDatasController {

    //create whole table
    public static void CreateTable(String TableID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS RawDatas_" + TableID + " (\n"
                    + "TweetContents String, \n"
                    + "CreatedAt String \n"
                    + ");");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }


    public String ShowTableInfo(String TableID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("TweetContents, CreatedAt FROM RawDatas_" + TableID);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                String TweetContents = results.getString(1);
                String CreatedAt = results.getString(2);
                String RawResults = TweetContents + " " + CreatedAt;
                return RawResults;
            }
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //insert data
    public static void InsertToTable(String TweetContents, String CreatedAt, String TableID ){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO RawDatas_"+TableID+" (TweetContents, CreatedAt)VALUES(?,?)");
            ps.setString(1, TweetContents);
            ps.setString(2, CreatedAt);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }

    //delete whole table
    @POST
    @Path("deleterawdata")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteTable(@FormDataParam("rawdataID") String TableID) {
        try {
            if(TableID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE RawDatas_" + TableID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
