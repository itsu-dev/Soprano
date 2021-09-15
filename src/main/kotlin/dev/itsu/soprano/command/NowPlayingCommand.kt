package dev.itsu.soprano.command

import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.text.SimpleDateFormat
import java.util.*

class NowPlayingCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val info = AudioManager.getGuildAudioManager(event.guild).getSendHandler().audioPlayer.playingTrack.info ?: run {
            event.message.reply("**曲が再生されていません。**").queue()
            return
        }

        val format = SimpleDateFormat("H:mm:ss")
        format.timeZone = TimeZone.getTimeZone("UTC")

        var str = format.format(Date(info.length))
        if (str.startsWith("0:")) {
            val split = str.split(":")
            str = split[1] + ":" + split[2]
        }

        event.message.reply("""
            🎵  **${info.title}** 
            ${info.author}（$str）
            ${info.uri}
        """.trimIndent()).queue()
    }

}