package logic;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Log {
    public static Logger log = LogManager.getLogManager().getLogger("Log");
    public static Logger logmqtt = LogManager.getLogManager().getLogger("LogMQTT");
    public static Logger logdb = LogManager.getLogManager().getLogger("LogDB");
}
