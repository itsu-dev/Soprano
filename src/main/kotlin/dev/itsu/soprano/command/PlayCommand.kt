package dev.itsu.soprano.command

import com.google.api.services.youtube.YouTube
import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class PlayCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        if (args.isEmpty()) {
            event.message.reply("❌  **YouTubeのURLを入力してください！**").queue()
            return
        }

        val voiceChannel = MessagingUtils.getAuthorsVoiceChannel(event) ?: run {
            event.message.reply("❌  **このコマンドを実行するにはボイスチャンネルに参加している必要があります！**").queue()
            return
        }

        /*
        val url = args[0]
        if (url.contains("playlist=")) {
            val query = YouTube

        } else {
            AudioManager.loadAndPlay(event.message, voiceChannel, args[0])
        }

         */

        AudioManager.loadAndPlay(event.message, voiceChannel, args[0])
    }
}