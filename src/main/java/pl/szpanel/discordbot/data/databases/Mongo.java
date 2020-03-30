package pl.szpanel.discordbot.data.databases;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import pl.szpanel.discordbot.data.configs.Config;

public final class Mongo {
    private static MongoClient mongoClient = null;
    private static Config config = Config.getConfig();

    private Mongo() {

    }

    public static MongoClient getConnection() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(
                    "mongodb+srv://%host%:%password%@%username%-catbh.mongodb.net/test?retryWrites=true&w=majority"
                            .replace("%host%", config.DB_HOSTNAME())
                            .replace("%password%", config.DB_PASSWORD())
                            .replace("%username%", config.DB_USERNAME()));
            return mongoClient;
        }
        return mongoClient;
    }
}
