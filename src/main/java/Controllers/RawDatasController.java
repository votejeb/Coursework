package Controllers;

import Server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
@Path("rawdata/")
public class RawDatasController{
    public static void CreateTable(String TableID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS RawDatas_"+TableID+" (\n"
                    + "TweetContents String, \n"
                    + "CreatedAt String \n"
                    + ");");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }
    public static void InsertRawData(String TweetContents,String CreatedAt, String TableID ){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO RawDatas_"+TableID+" (TweetContents,CreatedAt)VALUES(?,?)");
            ps.setString(1, TweetContents);
            ps.setString(2, CreatedAt);
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }

    //delete whole table
    @POST
    @Path("deleterawdata{rawdataID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteTable(@PathParam("rawdataID")String TableID) {
        try {
            if(TableID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            PreparedStatement ps = Main.db.prepareStatement("DELETE LinkedFilters" + TableID);
            ps.execute();
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}
