package pl.szpanel.discordbot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Test extends Command {
    public Test() {
        name = "test";
        help = "Opis komendy help";
        arguments = "a b c";
    }

    @Override
    protected void execute(CommandEvent event) {
        System.out.println("args: " + event.getArgs());
    }
}
