package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static Controllers.ProcessedDatasController.CreateTable;
import static Controllers.ProcessedDatasController.InsertToTable;

//creates hash table for raw data
public class SearchandSort {

    public static void CreateHash(String TableID) {
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
            InsertToTable(entry.getKey(),  entry.getValue(),TableID);
        });
    }
}
