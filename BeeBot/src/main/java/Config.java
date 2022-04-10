import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
    public static String get(String key) throws ConfigurationException {
        Configuration config = new PropertiesConfiguration("app.properties");
        return config.getString(key);
    }
}
