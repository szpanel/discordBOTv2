package pl.szpanel.discordbot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.mongodb.client.MongoDatabase;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import net.dv8tion.jda.api.JDABuilder;
import org.bson.Document;
import pl.szpanel.discordbot.command.HelpConsumer;
import pl.szpanel.discordbot.command.Music;
import pl.szpanel.discordbot.command.Test;
import pl.szpanel.discordbot.data.configs.Config;
import pl.szpanel.discordbot.data.databases.Mongo;
import pl.szpanel.discordbot.musicbot.MusicBotManager;

import javax.security.auth.login.LoginException;
import java.util.function.Consumer;

public class Bot {
    private static Config config = Config.getConfig();
    public static MusicBotManager musicBotManager = new MusicBotManager();

    public static void main(String[] args) throws LoginException {
        BotsGuildSettingsManager botsGuildSettingsManager = new BotsGuildSettingsManager();
        CommandClient commandClient = new CommandClientBuilder().setPrefix(";")
                .setGuildSettingsManager(botsGuildSettingsManager)
                .setHelpConsumer(new HelpConsumer())
                .addCommands(
                        new Test(),
                        new Music()
                ).setOwnerId(String.valueOf(config.GUILD_OWNER()))
                .build();

        JDABuilder.createDefault(config.API_TOKEN())
                .addEventListeners(commandClient)
                .setAudioSendFactory(new NativeAudioSendFactory())
                .build();

        MongoDatabase database = Mongo.getConnection().getDatabase("mc");
        database.getCollection("guilds").find().forEach((Consumer<? super Document>) System.out::println);
    }
}
