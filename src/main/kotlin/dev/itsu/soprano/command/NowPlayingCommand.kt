package dev.itsu.soprano.command

import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class NowPlayingCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val info =
            AudioManager.getGuildAudioManager(event.guild).getSendHandler().audioPlayer.playingTrack.info ?: run {
                event.message.reply("**曲が再生されていません。**").queue()
                return
            }

        event.message.reply(
            """
            🎵  **${info.title}** 
            ⏱  ${
                MessagingUtils.timeToString(
                    AudioManager.getGuildAudioManager(event.guild).getSendHandler().audioPlayer.playingTrack.position
                )
            } / ${MessagingUtils.timeToString(info.length)}
            ${info.author}（${MessagingUtils.timeToString(info.length)}）
            ${info.uri}
        """.trimIndent()
        ).queue()
    }

}