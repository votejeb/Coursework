package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static Controllers.ProcessedDatasController.CreateTable;
import static Controllers.ProcessedDatasController.InsertToTable;

@Path("searchandsort/")
//creates hash table for raw data
public class SearchandSort {
    @POST
    @Path("streamdata")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static void SortData(@FormDataParam("tableid")String TableID, @FormDataParam("mincount")Integer MinCount) {
        //creates hashmap
        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
        try {
            //prepares sql statement, createhash method and select rawdata methods merged
            PreparedStatement ps = Main.db.prepareStatement("SELECT TweetContents FROM RawDatas_" + TableID);
            ResultSet results = ps.executeQuery();
            //executes for each database line
            while (results.next()) {
                String TweetContents = results.getString(1);
                //converrts string to word array using .split
                String TweetContentsLower=TweetContents.toLowerCase();
                String[] crr_array = TweetContentsLower.split(" ");

                for (int i = 0; i < crr_array.length; ++i) {
                    String item = crr_array[i];
                    //adds 1 if word exists
                    if (wordCount.containsKey(item))
                        wordCount.put(item, wordCount.get(item) + 1);
                    else
                        //adds entry if word doesnt exist
                        wordCount.put(item, 1);
                }
            }
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
        CreateTable(TableID);
        wordCount.entrySet().forEach(entry -> {
            if (entry.getValue()> MinCount) {
                InsertToTable(entry.getKey(), entry.getValue(), TableID);
            }
        });
    }
}
