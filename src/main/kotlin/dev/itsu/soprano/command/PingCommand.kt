package dev.itsu.soprano.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class PingCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val before = System.currentTimeMillis()
        event.message.reply("Pong!").queue { response ->
            response.editMessage("🏓  Ping: ${System.currentTimeMillis() - before}ms").queue()
        }
    }

}