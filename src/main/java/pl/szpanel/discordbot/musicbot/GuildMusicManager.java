package pl.szpanel.discordbot.musicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;
import pl.szpanel.discordbot.data.configs.VoiceChannels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;
    /**
     * Track scheduler for the player.
     */
    public final TrackScheduler scheduler;

    /**
     * Music bot channel for the guild.
     */
    private VoiceChannel musicBotChannel;

    private VoiceChannel senderVoiceChannel;

    /**
     * Creates a player and a track scheduler.
     *
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    public void setMusicBotChannel(@Nonnull VoiceChannel voiceChannel) {
        musicBotChannel = voiceChannel;
    }

    @Nullable
    public VoiceChannel getMusicBotChannel() {
        return musicBotChannel;
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    public void loadAndPlay(final TextChannel channel, final String trackUrl, Member commandSender) {
        senderVoiceChannel = channel.getGuild().getVoiceChannels().stream()
                .filter(voice -> voice.getMembers().contains(commandSender))
                .findFirst().orElse(musicBotChannel);
        AudioPlayerManager musicManager = MusicBotManager.playerManager;
        musicManager.loadItemOrdered(this, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                channel.sendMessage("Dodano do kolejki utwór " + track.getInfo().title).queue();

                play(channel.getGuild(), track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.sendMessage("Dodawanie do kolejki " + firstTrack.getInfo().title + " (pierwszy utwór playlisty " + playlist.getName() + ")").queue();

                play(channel.getGuild(), firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nic nie znaleziono w " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Nie można włączyć: " + exception.getMessage()).queue();
            }
        });
    }

    private void play(Guild guild, AudioTrack track) {
        connectToMusicVoiceChannel(guild.getAudioManager());

        scheduler.queue(track);
    }

    public void stopPlaying(Guild guild) {
        player.stopTrack();
        guild.getAudioManager().closeAudioConnection();
    }

    private void connectToMusicVoiceChannel(AudioManager audioManager) {
        if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
            if (senderVoiceChannel == null) {
                audioManager.openAudioConnection(musicBotChannel);
                return;
            }
            audioManager.openAudioConnection(senderVoiceChannel);
        }
    }
}
