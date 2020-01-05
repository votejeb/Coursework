package Util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class customUtil {

    public static String SqlToStr(Date takenDate){
        Date date = takenDate;
        DateFormat dateFormat = new SimpleDateFormat("YYYY_MM_dd_hh_mm_ss");
        String strDate = dateFormat.format(date);
        return (strDate);
    }
}
