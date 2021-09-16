package dev.itsu.soprano.command

import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class SummonCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val voiceChannel = MessagingUtils.getAuthorsVoiceChannel(event) ?: run {
            event.message.reply("❌  **このコマンドを実行するにはボイスチャンネルに参加している必要があります！**").queue()
            return
        }

        AudioManager.connectToVoiceChannel(voiceChannel, event.guild.audioManager)

        event.message.reply("✌  こんにちは！").queue()
    }

}