package Controllers;

import Server.Main;

import java.sql.PreparedStatement;

public class LinkedFiltersController {
    public static void createTable(String filterID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("CREATE TABLE IF NOT EXISTS LinkedFilters"+filterID+" (\n"
                    + "words String ,\n"
                    + ");");
            ps.execute();

        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());

        }
    }
}

}
