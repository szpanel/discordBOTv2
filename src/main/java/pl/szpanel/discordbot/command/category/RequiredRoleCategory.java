package pl.szpanel.discordbot.command.category;

import com.jagrosh.jdautilities.command.Command;
import pl.szpanel.discordbot.data.configs.MessagesConfig;

public class RequiredRoleCategory extends Command.Category {
    private static MessagesConfig messagesConfig = new MessagesConfig();

    public RequiredRoleCategory(String name, String requiredRole) {
        super(name, null, commandEvent -> {
            boolean b = commandEvent.getMember().getRoles().stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(requiredRole));
            if (!b) commandEvent.replyInDm(messagesConfig.noRequiredRole.replace("%role%", requiredRole));
            return b;
        });
    }

}
