package pl.szpanel.discordbot;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import net.dv8tion.jda.api.entities.Guild;
import pl.szpanel.discordbot.data.configs.Config;

import javax.annotation.Nullable;

public class BotsGuildSettingsManager implements GuildSettingsManager {

    @Nullable
    @Override
    public Object getSettings(Guild guild) {
        return "Testowa wiadomość";
    }

    @Override
    public void init() {

    }

    @Override
    public void shutdown() {

    }
}
