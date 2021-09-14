package dev.itsu.soprano.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

interface ICommand {

    fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent)

}