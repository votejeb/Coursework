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
    public static void CreateTable(String TimeID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE RawDatas_" + TimeID + " (\n"
                    + "TweetContents String)");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }

    //insert data
    public static void InsertToTable(String TweetContents, String TimeID ){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO RawDatas_"+TimeID+" (TweetContents)VALUES(?)");
            ps.setString(1, TweetContents);
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
    public String DeleteTable(@FormDataParam("rawdataID") String TimeID) {
        try {
            if(TimeID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE RawDatas_" + TimeID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
