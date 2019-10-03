package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataFiltersController {

    //SQL SELECT//
    public static void ShowFilters() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT DataFilterName FROM DataFilters");
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

        } catch (Exception exception) {
            System.out.println("Database error "+exception.getMessage());
        }
    }

    public static void DeleteFilter (int DataFilterID){
        try {

            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM DataFilters WHERE DataFilterID= ?");
            ps.setInt(1, DataFilterID);
            ps.executeUpdate();
            System.out.println("Filter Deleted");

        } catch (Exception e) {
            System.out.println("Database error "+e.getMessage());

        }

    }
}

