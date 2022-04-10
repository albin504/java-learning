import org.apache.logging.log4j.LogManager;

public class Logger {
    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    public static void info(String text)
    {
        log.info(text);
    }
}
