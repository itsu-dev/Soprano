package dev.itsu.soprano

import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

object MessagingUtils {

    fun checkPermission(event: MessageReceivedEvent): Boolean {
        event.guild.members
            .firstOrNull { it.id == event.message.author.id }
            ?.roles
            ?.firstOrNull { it.name == "DJ" }
            ?: return false
        return true
    }

    fun getAuthorsVoiceChannel(event: MessageReceivedEvent): VoiceChannel? {
        return event.guild.voiceChannels.firstOrNull()
    }

}