package guojian.smart.snake;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Util {
    private static Util u;
    private static Properties properties;

    private Util() throws IOException {
        properties = new Properties();
        properties.load(new InputStreamReader(this.getClass().getResourceAsStream("/sys.properties")));
    }

    public static Util getInstance() throws IOException {
        if (u == null) {
            u = new Util();
        }
        return u;
    }

    public static String get(String key){
        return properties.getProperty(key);
    }

    public static String get(String key , String defaultValue ){
        return properties.getProperty(key,defaultValue);
    }
}
