package Util;

import java.util.Date;
import java.sql.Timestamp;

public class ConvertUtilToSQL {

    public static Timestamp convert(Date date) {
        date = new Date();
        return new Timestamp(date.getTime());
    }
}
//parsed this format of date as input, [Sat Oct 12 23:20:44 BST 2019] outputs a value of data type long, plz halp???????