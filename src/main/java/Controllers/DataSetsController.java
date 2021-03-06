package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static Util.customUtil.stringToList;

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
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT SetID, Keyword, RawSets, Runtime, PublicPrivate FROM DataSets WHERE PublicPrivate=1 OR SetID IN (SELECT SetID FROM UserDataLink Where UserID = ?)");
            ps.setInt(1,OriginUser);
            ResultSet results  = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("SetID", results.getInt(1));
                item.put("KeyWord", results.getString(2));
                item.put("RawSets", results.getString(3));
                item.put("RunTime", results.getInt(4));
                item.put("PublicPrivate", results.getBoolean(5));
                list.add(item);
            }
            return list.toString();
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
            //gathers parameters
            @FormDataParam("Keyword")String Keyword,
            @FormDataParam("RunTime")Integer RunTime,
            @CookieParam("userid") Integer UserID,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (Keyword == null || RunTime==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            //inserts dataset table
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO DataSets(Keyword, RunTime, PublicPrivate)VALUES(?,?,?)");
            ps.setString(1, Keyword);
            ps.setInt(2,RunTime);
            ps.setBoolean(3,true);
            ps.execute();
            //gathers last inserted table id from dataset
            PreparedStatement ps1 = Main.db.prepareStatement("SELECT last_insert_rowid() AS rowid FROM DataSets LIMIT 1");
            ResultSet results  = ps1.executeQuery();
            int results1 = results.getInt(1);
            //adds composite key filter link table
            PreparedStatement ps2 = Main.db.prepareStatement("INSERT INTO UserDataLink(UserID,SetID)VALUES(?,?)");
            ps2.setInt(1, UserID);
            ps2.setInt(2,results1);
            ps2.execute();

            //returns created set to server for more processes
            return "{\"status\": \"OK\",\"SetID\": \""+results1+"\",\"PublicPrivate\": \"true\"}";
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

    //Update//
    @POST
    @Path("updateset")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String UpdateSet (
            @FormDataParam("SetID") Integer SetID,
            @FormDataParam("RunTime") Integer RunTime,
            @FormDataParam("PublicPrivate")Boolean PublicPrivate,
            @FormDataParam("RawSets")String RawSets,
            @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (SetID == null || PublicPrivate == null || RawSets == null){
                throw new Exception("One or more data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("UPDATE DataSets SET RawSets=?, RunTime=?, PublicPrivate = ? WHERE SetID = ?");
            ps.setString(1, RawSets);
            ps.setInt(2, RunTime);
            ps.setBoolean(3, PublicPrivate);
            ps.setInt(4, SetID);
            ps.execute();
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }

///delete//
    @POST
    @Path("deletedataset")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String DeleteDataSet (@FormDataParam("SetID")Integer SetID,
                                 @CookieParam("token") String token) throws Exception {
        if (!UsersController.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if(SetID==null){
                throw new Exception("One or more form data parameters are missing in the HTTP request.");
            }
            //selects raw sets from database
            PreparedStatement ps1 = Main.db.prepareStatement("Select RawSets FROM DataSets WHERE SetID = ?");
            ps1.setInt(1,SetID);
            ResultSet results  = ps1.executeQuery();
            String[] iterator=stringToList(results.getString(1));
            //iterates over rawsets and deletes relevant tables
            for (int i = 0; i < iterator.length; ++i) {
                RawDatasController.DeleteTable(iterator[i]);
            }
            //deletes associated processed data controller
            ProcessedDatasController.DeleteTable(SetID.toString());
            //deletes dataset table
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM DataSets WHERE SetID= ?");
            ps.setInt(1, SetID);
            ps.execute();
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database Error "+exception.getMessage());
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
}

