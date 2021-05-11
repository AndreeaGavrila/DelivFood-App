package utils;

import java.time.ZonedDateTime;

public class Log implements ICsvConvertible<Log>  {

    private  ZonedDateTime timestamp;
    private  String log;

    public Log(ZonedDateTime timestamp, String log) {

        this.timestamp = timestamp;
        this.log = log;
    }

    @Override
    public String[] stringify() {
        return new String[]{ this.timestamp.toString(), this.log };
    }

}
