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

    fun loadAndPlay(message: Message, voiceChannel: VoiceChannel, trackURL: String) {
        val audioManager = getGuildAudioManager(message.guild)
        playerManager.loadItemOrdered(audioManager, trackURL, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                message.reply("▶  キューに追加しました：${track.info.title}（YouTube）").queue()
                play(message.guild, voiceChannel, track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val firstTrack = playlist.selectedTrack ?: run {
                    playlist.tracks[0]
                }
                message.reply("▶  キューに追加しました：${firstTrack.info.title}（YouTube）").queue()
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

    private fun connectToVoiceChannel(voiceChannel: VoiceChannel, audioManager: AudioManager) {
        if (!audioManager.isConnected) audioManager.openAudioConnection(voiceChannel)
    }

    fun disconnectVoiceChannel(audioManager: AudioManager) {
        if (audioManager.isConnected) audioManager.closeAudioConnection()
    }

}