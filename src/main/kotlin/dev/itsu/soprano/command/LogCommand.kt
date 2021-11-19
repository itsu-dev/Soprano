package dev.itsu.soprano.command

import dev.itsu.soprano.Bot
import dev.itsu.soprano.Utils
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.lang.Exception
import java.lang.RuntimeException

class LogCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        if (args.isEmpty()) {
            event.channel.sendMessage("```${Utils.readLog()}```").queue()

        } else if (args[0].toIntOrNull() != null) {
            val page = args[0].toInt()
            val log = Utils.readLog()

            if (log.length < 2000 * (page - 1)) {
                event.channel.sendMessage("```${Utils.readLog()}```").queue()
            } else if (log.substring(2000 * (page - 1)).length < 2000) {
                event.channel.sendMessage("```${log.substring(2000 * (page - 1))}```").queue()
            } else {
                event.channel.sendMessage("```${log.substring(2000 * (page - 1), 2000 * page - 7)}```").queue()
            }

        } else if (args[0] == "test") {
            try {
                throw RuntimeException("Created by log command.")
            } catch (e: Exception) {
                Utils.log("BELOW ERROR IS CREATED BY MANUALLY.\n")
                Utils.log(e)
                event.channel.sendMessage("Created an error manually.").queue()
            }

        } else if (args[0] == "clear") {
            Utils.clearLog()
            event.channel.sendMessage("Cleared the log.").queue()

        } else {
            Utils.log("")
            event.channel.sendMessage("Page number must be integer. (e.g. ${Bot.PREFIX}log 1").queue()
        }
    }

}