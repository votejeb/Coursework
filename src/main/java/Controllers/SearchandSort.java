package Controllers;

// Java program to sort an array using hash
// function
import java.lang.reflect.Array;
import java.util.*;

public class SearchandSort {

    public static void SearchandSort(String dataString) {

        String[] crr_array = dataString.split(" ");
        HashMap<String, Integer> repetitions = new HashMap< String, Integer>();

        for (int i = 0; i < crr_array.length; ++i) {
            String item = crr_array[i];

            if (repetitions.containsKey(item))
                repetitions.put(item, repetitions.get(item) + 1);
            else
                repetitions.put(item, 1);
        }

        repetitions.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }
}