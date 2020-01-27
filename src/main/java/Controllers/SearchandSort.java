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

import static Controllers.ProcessedDatasController.*;

@Path("searchandsort/")
//creates hash table for raw data
public class SearchandSort {
    @POST
    @Path("sortdata")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static void SortData(@FormDataParam("tableid")String TableID, @FormDataParam("timeid")String TimeID1, @FormDataParam("mincount")Integer MinCount) {
        CreateTable(TableID);
        String[] TimeID = TimeID1.split("-");
        //creates hashmap
        for (int j = 0; j < TimeID.length; ++j) {

            HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
            try {
                //prepares sql statement, createhash method and select rawdata methods merged
                PreparedStatement ps = Main.db.prepareStatement("SELECT TweetContents FROM RawDatas_" + TimeID[j]);
                ResultSet results = ps.executeQuery();
                //executes for each database line
                while (results.next()) {
                    String TweetContents = results.getString(1);
                    //converrts string to word array using .split

                    String[] crr_array = TweetContents.split(" ");

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
            wordCount.entrySet().forEach(entry -> {
                if (entry.getValue()> MinCount) {
                    InsertWordsToTable(entry.getKey(), TableID);
                }

            });
            //calls alter table from processeddatas
            AlterTable(TableID, TimeID[j]);
            String finalJ = TimeID[j];
            wordCount.entrySet().forEach(entry -> {
                if (entry.getValue()> MinCount) {
                    //updates DataSet
                    UpdateTable(entry.getValue(), entry.getKey(), TableID, finalJ);
                }

            });
        }
    }
}
