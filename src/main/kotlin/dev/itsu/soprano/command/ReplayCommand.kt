package dev.itsu.soprano.command

import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ReplayCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        AudioManager.seek(event.message, 0)
    }

}