package utils;

import java.io.File;
import java.time.ZonedDateTime;


public class Logger {

    private static Logger instance;
    private static String file_path;

    public Logger(String file_path) {

        this.file_path = file_path;

        // delete previous log file, if it exists
        File file = new File(file_path);
        if(file.exists()) {
            file.delete();
        }
    }

    public static Logger getInstance(String file_path) {
        if (instance == null) {
            instance = new Logger(file_path);
        }
        return instance;
    }

    public void write(String log) {
        CsvReadWrite.<Log>writeOne(new Log(ZonedDateTime.now(), log), file_path);
    }

}
