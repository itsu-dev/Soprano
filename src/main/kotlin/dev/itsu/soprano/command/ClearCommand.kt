package dev.itsu.soprano.command

import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ClearCommand: ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        AudioManager.getGuildAudioManager(event.guild).trackScheduler.queue.clear()
        event.message.reply("✅  キューをリセットしました。").queue()
    }
}