package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RawDatasController{
    public static void CreateTable(String ldt) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS RawDatas_"+ldt+" (\n"
                    + "TweetContents String\n"
                    + ");");
            ps.execute();
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
        }
    }
    public static void InsertRawData(String TweetContents, String ldt ){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO RawDatas_"+ldt+" (TweetContents)VALUES(?)");
            ps.setString(1, TweetContents);
            ps.executeUpdate();
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }
}
