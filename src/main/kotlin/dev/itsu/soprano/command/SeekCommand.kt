package dev.itsu.soprano.command

import dev.itsu.soprano.MessagingUtils
import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class SeekCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        if (args.isEmpty()) {
            event.message.reply("**❌  再生位置を指定する必要があります。（例：1:32）**").queue()
            return
        }

        try {
            args[0].split(":").forEach { it.toLong() }
        } catch (e: NumberFormatException) {
            event.message.reply("**❌  正しい形式で入力してください。（例：1:32）**").queue()
            return
        }

        AudioManager.seek(event.message, MessagingUtils.stringToTime(args[0]))
    }

}