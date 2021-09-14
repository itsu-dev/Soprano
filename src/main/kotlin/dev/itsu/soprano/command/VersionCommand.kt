package dev.itsu.soprano.command

import dev.itsu.soprano.Bot
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class VersionCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        event.channel.sendMessage("Soprano バージョン v${Bot.VERSION}").queue()
    }

}