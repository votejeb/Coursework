package Util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class GetDateTime {

    public static String getCurrentTime(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("YYYY_MM_dd_hh_mm_ss");
        String strDate = dateFormat.format(date);
        return (strDate);
    }
}