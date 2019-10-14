package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@Path("datasets/")
public class DataSetsController {
    //SQL SELECT//

    public static void ShowDataSet() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT SetID, Keyword, TimeFrom, TimeTo FROM ProcessedDatas");
            ResultSet results  = ps.executeQuery();
            while (results.next()) {
                int KeywordID = results.getInt(1);
                String Keyword = results.getString(2);
                System.out.println(KeywordID + " " + Keyword);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
//insert//

    public static void InsertDataSet(int KeywordID, String Keyword, int TimeFrom, int TimeTo){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO DataSets(KeywordID, Keyword, TimeFrom, TimeTo)VALUES(?,?,?,?)");
            ps.setInt(1, KeywordID);
            ps.setString(2, Keyword);
            ps.setInt(3, TimeFrom);
            ps.setInt(4,TimeTo);
            ps.executeUpdate();
            System.out.println("Keyword Entry Added");

        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }
///delete//

    @POST
    @Path("deleted dataset")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteDataSet (@FormDataParam("KeywordID")Integer KeywordID){
        try {
            if(KeywordID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/deleteuser id=" + KeywordID);
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