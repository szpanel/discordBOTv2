package pl.szpanel.discordbot.command;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;

import java.util.function.Consumer;

public final class HelpConsumer implements Consumer<CommandEvent> {
    @Override
    public void accept(CommandEvent commandEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("**Lista komend**\n\n");
        commandEvent.getClient().getCommands().forEach(command -> sb.append("`")
                .append(commandEvent.getClient().getPrefix())
                .append(command.getName())
                .append(" ")
                .append(command.getArguments())
                .append(" - ").append(command.getHelp())
                .append("`\n"));
        User owner = commandEvent.getJDA().getUserById(commandEvent.getClient().getOwnerIdLong());
        if (owner != null) sb.append("\nPo więcej informacji skontaktuj się z ").append(owner.getName());
        commandEvent.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage(sb.toString()).queue());
    }
}
