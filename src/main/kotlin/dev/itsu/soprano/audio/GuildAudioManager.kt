package dev.itsu.soprano.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager

class GuildAudioManager(manager: AudioPlayerManager) {

    private val audioPlayer = manager.createPlayer()
    val trackScheduler = TrackScheduler(audioPlayer)

    init {
        audioPlayer.addListener(trackScheduler)
    }

    fun getSendHandler() = AudioPlayerSendHandler(audioPlayer)

}