package dev.itsu.soprano.command

import dev.itsu.soprano.MessagingUtils
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class LoopCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        if (!MessagingUtils.checkPermission(event)) {
            event.message.reply("**This command requires you to have role \"DJ\".**").queue()
            return
        }
    }

}