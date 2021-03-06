package dev.itsu.soprano.audio

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats
import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.Utils
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.AudioManager
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig

object AudioManager {

    private val guildAudioManagers = mutableMapOf<Long, GuildAudioManager>()
    private val playerManager = DefaultAudioPlayerManager()

    init {
        playerManager.registerSourceManager(YoutubeAudioSourceManager().also {
            it.configureRequests { config ->
                RequestConfig.copy(config)
                    .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .build()
            }
        })
        playerManager.configuration.resamplingQuality = AudioConfiguration.ResamplingQuality.HIGH
        playerManager.configuration.opusEncodingQuality = AudioConfiguration.OPUS_QUALITY_MAX
        playerManager.configuration.outputFormat = StandardAudioDataFormats.DISCORD_OPUS
        playerManager.registerSourceManager(HttpAudioSourceManager())
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    private fun addGuildAudioManager(guildId: Long, guildAudioManager: GuildAudioManager) {
        guildAudioManagers[guildId] = guildAudioManager
    }

    @Synchronized
    fun getGuildAudioManager(guild: Guild): GuildAudioManager {
        val guildId = guild.idLong
        val guildAudioManager = guildAudioManagers[guildId] ?: run {
            addGuildAudioManager(guildId, GuildAudioManager(playerManager))
            guildAudioManagers[guildId]!!
        }
        guild.audioManager.sendingHandler = guildAudioManager.getSendHandler()
        return guildAudioManager
    }

    fun loadAndPlay(message: Message, voiceChannel: VoiceChannel, trackURL: String, sendMessage: Boolean = true) {
        val audioManager = getGuildAudioManager(message.guild)

        playerManager.loadItemOrdered(audioManager, trackURL, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                if (sendMessage) message.reply("???  ?????????????????????????????????${track.info.title}???YouTube???").queue()
                play(message.guild, voiceChannel, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val firstTrack = playlist.selectedTrack ?: run {
                    playlist.tracks[0]
                }

                playlist.tracks.forEachIndexed { index, value ->
                    if (index != 0) audioManager.trackScheduler.queue(value)
                }

                message.reply("???  ??????????????????: **${playlist.name}**?????????????????????????????????").queue()
                play(message.guild, voiceChannel, firstTrack)
            }

            override fun noMatches() {
                message.reply("???  **${trackURL}?????????????????????????????????????????????????????????**").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                message.reply("???  **????????????????????????????????????${exception.message}???**").queue()
                exception.printStackTrace()
                Utils.log("CAUGHT AN ERROR WHEN LOADING A MUSIC FROM PLAYLIST. (${trackURL})\n")
                Utils.log(exception)
            }
        })
    }

    fun play(guild: Guild, voiceChannel: VoiceChannel, track: AudioTrack) {
        connectToVoiceChannel(voiceChannel, guild.audioManager)
        getGuildAudioManager(guild).trackScheduler.queue(track)
    }

    fun skip(message: Message) {
        getGuildAudioManager(message.guild).trackScheduler.next()
        message.reply("???  ???????????????????????????").queue()
    }

    fun seek(message: Message, position: Long) {
        val audioManager = getGuildAudioManager(message.guild)
        val track = audioManager.getSendHandler().audioPlayer.playingTrack
        if (track.isSeekable) {
            if (position !in 0..track.info.length) {
                message.reply("???  **?????????????????????????????????????????????????????????????????????0:00~${MessagingUtils.timeToString(track.info.length)}???**")
                    .queue()
                return
            }
            audioManager.getSendHandler().audioPlayer.playingTrack.position = position
            message.reply("????  ${MessagingUtils.timeToString(position)}????????????????????????").queue()
            return
        }
        message.reply("???  **???????????????????????????????????????????????????????????????**").queue()
    }

    fun forward(message: Message, delta: Long) {
        val audioManager = getGuildAudioManager(message.guild)
        val track = audioManager.getSendHandler().audioPlayer.playingTrack
        val time =
            if (track.position + delta !in 0..track.info.length + delta) track.info.length
            else track.position + delta
        seek(message, time)
    }

    fun rewind(message: Message, delta: Long) {
        val audioManager = getGuildAudioManager(message.guild)
        val track = audioManager.getSendHandler().audioPlayer.playingTrack
        val time =
            if (track.position - delta !in 0..track.info.length) 0
            else track.position - delta
        seek(message, time)
    }

    fun connectToVoiceChannel(voiceChannel: VoiceChannel, audioManager: AudioManager) {
        if (!audioManager.isConnected) audioManager.openAudioConnection(voiceChannel)
    }

    fun disconnectVoiceChannel(audioManager: AudioManager) {
        if (audioManager.isConnected) audioManager.closeAudioConnection()
    }

}