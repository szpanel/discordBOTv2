package pl.szpanel.discordbot.data.configs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config instance = null;
    // Database credentials
    private String DB_HOSTNAME;
    private String DB_USERNAME;
    private String DB_PASSWORD;

    // Guild credentials
    private String API_TOKEN;
    private long GUILD_OWNER;

    private Config() {
        final Properties properties = new Properties();
        try {
            properties.load(new FileReader(new File(getClass().getClassLoader()
                    .getResource("config.properties").getFile())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DB_HOSTNAME = properties.getProperty("db.host");
        DB_USERNAME = properties.getProperty("db.username");
        DB_PASSWORD = properties.getProperty("db.password");

        API_TOKEN = properties.getProperty("api.token");
        GUILD_OWNER = Long.parseLong(properties.getProperty("guild.owner"));
    }

    public static Config getConfig() {
        if (instance == null) {
            return new Config();
        }
        return instance;
    }

    public String DB_HOSTNAME() {
        return DB_HOSTNAME;
    }

    public String DB_USERNAME() {
        return DB_USERNAME;
    }

    public String DB_PASSWORD() {
        return DB_PASSWORD;
    }

    public String API_TOKEN() {
        return API_TOKEN;
    }

    public long GUILD_OWNER() {
        return GUILD_OWNER;
    }
}
