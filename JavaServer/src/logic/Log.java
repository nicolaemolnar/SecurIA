package logic;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Log {
    public static Logger log = LogManager.getLogger("Log");
    public static Logger logmqtt = LogManager.getLogger("LogMQTT");
    public static Logger logdb = LogManager.getLogger("LogDB");
}
