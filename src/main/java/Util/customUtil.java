package Util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class customUtil {

    public static String SqlToStr(Date takenDate){
        Date date = takenDate;
        DateFormat dateFormat = new SimpleDateFormat("YYYY_MM_dd_hh_mm_ss");
        String strDate = dateFormat.format(date);
        return (strDate);
    }
    public static String getTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return(dtf.format(now));
    }
    public static String listToString(List list){
        String delim = "-";
        String res = String.join(delim, list);
        return res;
    }
}
