package dev.itsu.soprano.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(private val player: AudioPlayer) : AudioEventAdapter() {

    val queue = LinkedBlockingQueue<AudioTrack>()

    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) queue.offer(track)
    }

    fun next() {
        player.startTrack(queue.poll(), false)
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack?, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) next()
    }

}