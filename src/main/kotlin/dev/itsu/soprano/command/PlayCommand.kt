package dev.itsu.soprano.command

import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class PlayCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        if (args.isEmpty()) {
            event.message.reply("❌  **YouTubeのURLまたはキューの番号を入力してください！**").queue()
            return
        }

        val voiceChannel = MessagingUtils.getAuthorsVoiceChannel(event) ?: run {
            event.message.reply("❌  **このコマンドを実行するにはボイスチャンネルに参加している必要があります！**").queue()
            return
        }

        if (args[0].toIntOrNull() == null) {
            AudioManager.loadAndPlay(event.message, voiceChannel, args[0])

        } else {
            val index = args[0].toInt()
            val queue = AudioManager.getGuildAudioManager(event.guild).trackScheduler.queue
            if (index - 1 in 0..queue.size) {
                for (i in 0 until index - 1) {
                    queue.poll()
                }

                AudioManager.getGuildAudioManager(event.guild).getSendHandler().audioPlayer.stopTrack()
                event.message.reply("⏭  再生：**${queue.first().info.title}**").queue()

                AudioManager.loadAndPlay(event.message, voiceChannel, queue.first().info.uri, false)
            } else {
                event.message.reply("❌  **キューの範囲内で番号を指定してください！**").queue()
            }
        }
    }
}