package pl.szpanel.discordbot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import pl.szpanel.discordbot.Bot;
import pl.szpanel.discordbot.data.configs.VoiceChannels;
import pl.szpanel.discordbot.musicbot.GuildMusicManager;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class Music extends Command {

    public Music() {
        name = "music";
        arguments = "<akcja> [źródło]";
        help = ";music help";
    }

    @Override
    protected void execute(CommandEvent event) {
        final GuildMusicManager guildMusicManager = Bot.musicBotManager.getGuildAudioPlayer(event.getGuild());
        guildMusicManager.setMusicBotChannel(
                Objects.requireNonNull(event.getGuild().getVoiceChannelById(VoiceChannels.MUSIC_BOT)));
        String[] args = event.getArgs().split(" ");
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("stop")) {
                    guildMusicManager.stopPlaying(event.getGuild());
                } else if (args[0].equalsIgnoreCase("help")) {
                    sendHelp(event.getAuthor());
                } else if (args[0].equalsIgnoreCase("pause")) {
                    guildMusicManager.player.setPaused(!guildMusicManager.player.isPaused());

                } else if (args[0].equalsIgnoreCase("song")) {
                    sendCurrentSongInfo(event.getTextChannel(), guildMusicManager);
                } else if (args[0].equalsIgnoreCase("queue")) {
                    sendQueue(event.getChannel(), guildMusicManager);
                } else if (args[0].equalsIgnoreCase("skip")) {
                    skipSong(event.getChannel(), guildMusicManager);
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("play")) {
                    final String trackUrl = args[1];
                    guildMusicManager.loadAndPlay(event.getTextChannel(), trackUrl, event.getMember());
                }
                break;
        }
    }

    private void skipSong(MessageChannel channel, GuildMusicManager guildMusicManager) {
        final String songTitle = guildMusicManager.player.getPlayingTrack().getInfo().title;
        guildMusicManager.scheduler.nextTrack();
        channel.sendMessage("Utwór " + songTitle + " pomyślnie pominięty.").queue();
    }

    private void sendCurrentSongInfo(TextChannel textChannel, GuildMusicManager guildMusicManager) {
        AudioTrack playingTrack = guildMusicManager.player.getPlayingTrack();
        if (playingTrack == null) return;
        AudioTrackInfo info = playingTrack.getInfo();
        textChannel.sendMessage(new EmbedBuilder().setColor(Color.YELLOW)
                .addField(info.title, info.uri + " [" + info.length + "]", true).build())
                .queue();
    }

    private void sendQueue(MessageChannel channel, GuildMusicManager guildMusicManager) {
        BlockingQueue<AudioTrack> queue = guildMusicManager.scheduler.getQueue();
        if (queue.isEmpty()) {
            channel.sendMessage("`Kolejka utworów jest pusta.`").queue();
            return;
        }
        EmbedBuilder qEmbed = new EmbedBuilder().setColor(Color.YELLOW).setTitle("Lista utworów w kolejce");
        queue.forEach(audioTrack -> {
            AudioTrackInfo info = audioTrack.getInfo();
            qEmbed.addField(info.title, info.uri + " [" + info.length + "]", true);
        });
        channel.sendMessage(qEmbed.build()).queue();
    }

    private void sendHelp(User author) {
    }
}
