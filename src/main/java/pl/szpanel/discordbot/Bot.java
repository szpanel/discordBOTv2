package pl.szpanel.discordbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bson.Document;
import pl.szpanel.discordbot.commands.HelpConsumer;
import pl.szpanel.discordbot.commands.Test;
import pl.szpanel.discordbot.data.configs.Config;
import pl.szpanel.discordbot.data.databases.Mongo;

import javax.security.auth.login.LoginException;
import java.util.function.Consumer;

public class Bot {
    private static Config config = Config.getConfig();

    public static void main(String[] args) throws LoginException {
        BotsGuildSettingsManager botsGuildSettingsManager = new BotsGuildSettingsManager();
        CommandClient commandClient = new CommandClientBuilder().setPrefix(";")
                .setGuildSettingsManager(botsGuildSettingsManager)
                .setHelpConsumer(new HelpConsumer())
                .addCommands(
                        new Test()
                ).setOwnerId(String.valueOf(config.GUILD_OWNER()))
                .build();

        JDABuilder.createDefault(config.API_TOKEN())
                .addEventListeners(commandClient)
                .build();

        MongoDatabase database = Mongo.getConnection().getDatabase("mc");
        database.getCollection("guilds").find().forEach((Consumer<? super Document>) System.out::println);
    }
}
