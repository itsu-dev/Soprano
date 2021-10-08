package dev.itsu.soprano.command

import dev.itsu.soprano.Bot
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.net.InetAddress

class VersionCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        event.channel.sendMessage(
            """
            🤖  Soprano v${Bot.VERSION}
            Running on ${System.getProperty("os.name")} / ${InetAddress.getLocalHost().address}
            Open Source Project Licensed: GPL v3.0
            https://github.com/itsu-dev/Soprano
            """.trimIndent()
        ).queue()
    }

}