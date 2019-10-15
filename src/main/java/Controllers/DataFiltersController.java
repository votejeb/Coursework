package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataFiltersController {

    //SQL SELECT//
    public static void ShowFilters() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT DataFilterName FROM DataFilters ");
            ResultSet results  = ps.executeQuery();
            while (results.next()) {
                String DataFilterName = results.getString(1);
                System.out.println(DataFilterName);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
        }
    }
//insert//

    public static void InsertFilter(int DataFilterID, String DataFilterName, boolean WhitelistBlacklist){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Users(DataFilterID, DataFilterName, WhitelistBlacklist)VALUES(?,?,?)");
            ps.setInt(1, DataFilterID);
            ps.setString(2, DataFilterName);
            ps.setBoolean(3,WhitelistBlacklist);
            ps.executeUpdate();
            System.out.println("Filter Added");
            LinkedFiltersController.CreateTable(DataFilterID);
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }
//delete//

    @POST
    @Path("deletefilter{datafilterID}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String DeleteFilter (@PathParam("datafilterID") Integer DataFilterID){
        try {
            if(DataFilterID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            System.out.println("users/deletefilter id=" + DataFilterID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM DataFilters WHERE DataFilterID= ?");
            ps.setInt(1, DataFilterID);
            ps.execute();
            LinkedFiltersController.DeleteTable(DataFilterID);
            return "{\"status\"; \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}

