package dev.itsu.soprano

import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.text.SimpleDateFormat
import java.util.*

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
        val member = event.guild.getMember(event.author) ?: return event.guild.voiceChannels.firstOrNull()
        return event.guild.voiceChannels.firstOrNull { it.members.firstOrNull { it.id == member.id } != null }
    }

    fun timeToString(time: Long): String {
        val format = SimpleDateFormat("H:mm:ss")
        format.timeZone = TimeZone.getTimeZone("UTC")

        var str = format.format(Date(time))
        if (str.startsWith("0:")) {
            val split = str.split(":")
            str = split[1] + ":" + split[2]
        }

        return str
    }

    fun stringToTime(time: String): Long {
        val format =
            when {
                time.split(":").size > 2 -> SimpleDateFormat("H:mm:ss")
                time.split(":").size > 1 -> SimpleDateFormat("m:ss")
                else -> SimpleDateFormat("ss")
            }
        format.timeZone = TimeZone.getTimeZone("UTC")

        val date = format.parse(time)
        return date.time
    }

}