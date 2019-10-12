package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class RawDatasController
{
    public static void InsertRawData(String TweetContents, Timestamp CreatedAt, int RawDataID){
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO RawDatas"+RawDataID+" (TweetContents, CreatedAt)VALUES(?,?)");
            ps.setString(1, TweetContents);
            ps.setTimestamp(2, CreatedAt);
            ps.executeUpdate();
        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }
}
