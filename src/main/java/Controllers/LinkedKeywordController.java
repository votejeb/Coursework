package Controllers;

import Server.Main;

import java.sql.PreparedStatement;

public class LinkedKeywordController {

    public static void createTable(String keywordID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS LinkedKeywords"+keywordID+" (\n"
                    + "words String ,\n"
                    + "OccurenceCount int,\n"
                    + "LikeCount int\n"
                    + ");");
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());

        }
    }
}
