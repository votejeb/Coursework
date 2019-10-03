package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProcessedDatasController {
    //SQL SELECT//
    public static void ShowProcessedDatas() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT KeywordID, Keyword, TimeFrom, TimeTo FROM ProcessedDatas");
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

    public static void InsertData(int KeywordID, String Keyword, int TimeFrom, int TimeTo){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO ProcessedDatas(KeywordID, Keyword, TimeFrom, TimeTo)VALUES(?,?,?,?)");
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

    public static void deleteUser (int KeywordID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM ProcessedDatas WHERE KeywordID= ?");
            ps.setInt(1, KeywordID);
            ps.executeUpdate();
            System.out.println("Keyword Entry Removed");

        } catch (Exception e) {
            System.out.println("Database error "+e.getMessage());

        }

    }
}
