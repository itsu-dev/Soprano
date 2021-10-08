package dev.itsu.soprano.audio

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
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.managers.AudioManager
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import java.util.concurrent.ConcurrentLinkedQueue

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
                if (sendMessage) message.reply("▶  キューに追加しました：${track.info.title}（YouTube）").queue()
                play(message.guild, voiceChannel, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val firstTrack = playlist.selectedTrack ?: run {
                    playlist.tracks[0]
                }

                playlist.tracks.forEachIndexed { index, value ->
                    if (index != 0) audioManager.trackScheduler.queue(value)
                }
                message.reply("▶  プレイリスト: **${playlist.name}**をキューに追加しました").queue()
                play(message.guild, voiceChannel, firstTrack)
            }

            override fun noMatches() {
                message.reply("❌  **${trackURL}に該当する動画が見つかりませんでした。**").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                message.reply("❌  **エラーが発生しました。（${exception.message}）**").queue()
            }
        })
    }

    fun play(guild: Guild, voiceChannel: VoiceChannel, track: AudioTrack) {
        connectToVoiceChannel(voiceChannel, guild.audioManager)
        getGuildAudioManager(guild).trackScheduler.queue(track)
    }

    fun skip(message: Message) {
        getGuildAudioManager(message.guild).trackScheduler.next()
        message.reply("⏭  スキップしました。").queue()
    }

    fun seek(message: Message, position: Long) {
        val audioManager = getGuildAudioManager(message.guild)
        val track = audioManager.getSendHandler().audioPlayer.playingTrack
        if (track.isSeekable) {
            if (position !in 0..track.info.length) {
                message.reply("❌  **曲の再生時間の範囲内で指定する必要があります（0:00~${MessagingUtils.timeToString(track.info.length)}）**")
                    .queue()
                return
            }
            audioManager.getSendHandler().audioPlayer.playingTrack.position = position
            message.reply("🎼  ${MessagingUtils.timeToString(position)}へ移動しました。").queue()
            return
        }
        message.reply("❌  **再生中の曲はこの機能に対応しておりません。**").queue()
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