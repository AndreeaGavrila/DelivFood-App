package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;


public class CsvReadWrite< T extends ICsvConvertible<T>> {

    public static <T extends ICsvConvertible<T>> void writeOne(T t, String filePath) {

        var data = t.stringify();
        var csv = String.join(",", data);

        FileWriter csvOutputFile;
        try {
            csvOutputFile = new FileWriter(filePath, true);
            csvOutputFile.write(csv);
            csvOutputFile.write('\n');
            csvOutputFile.close();
        }
        catch(java.io.IOException e) {
            System.out.println("Error - Cannot open file.");

        }
    }


    public static <T extends ICsvConvertible<T>> void writeAll(ArrayList<T> lst, String filePath) {

        FileWriter csvOutputFile;
        try {
            csvOutputFile = new FileWriter(filePath);
            for(var entry : lst) {
                var data = entry.stringify();
                var csv = String.join(",", data);

                csvOutputFile.write(csv);
                csvOutputFile.write('\n');
            }
            csvOutputFile.close();
        }
        catch(java.io.IOException e) {
            System.out.println("Error - Cannot open file.");
        }
    }


    public static Optional<String> readOne(String filePath) {

        String data = null;
        BufferedReader csvReader;
        try {
            csvReader  = new BufferedReader(new FileReader(filePath));
            data = csvReader.readLine();
            csvReader.close();
        }
        catch(java.io.IOException e) {
            System.out.println("Error - Cannot open file.");
        }
        if(data == null) {
            return Optional.empty();
        }
        return Optional.of(data);
    }


    public static Optional<ArrayList<String>> readAll(String filePath) {

        var res = new ArrayList<String>();
        BufferedReader csvReader;
        try {
            csvReader  = new BufferedReader(new FileReader(filePath));
            String line = csvReader.readLine();
            while (line != null) {
                res.add(line);
                line = csvReader.readLine();
            }
            csvReader.close();
        }
        catch(java.io.IOException e) {
            System.out.println("Error - Cannot open file.");
        }
        if(res.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(res);
    }
}
