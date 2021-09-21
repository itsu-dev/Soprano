package dev.itsu.soprano.command

import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class QueueCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val audioManager = AudioManager.getGuildAudioManager(event.guild)
        if (audioManager.getSendHandler().audioPlayer.playingTrack == null) {
            event.message.reply("😕  再生中の曲はありません。").queue()
            return
        }

        var text = "🎶  **再生中**：${audioManager.getSendHandler().audioPlayer.playingTrack.info.title}（${
            MessagingUtils.timeToString(
                audioManager.getSendHandler().audioPlayer.playingTrack.position
            ) + " / " +
            MessagingUtils.timeToString(
                audioManager.getSendHandler().audioPlayer.playingTrack.info.length
            )
        }）\n"
        audioManager.trackScheduler.queue.forEachIndexed { index, track ->
            text += "**${index + 1}**：${track.info.title}（${MessagingUtils.timeToString(track.info.length)}）\n"
        }
        event.message.reply(text).queue()
    }


}