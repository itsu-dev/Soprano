package dev.itsu.soprano.command

import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ShuffleCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val shuffled = AudioManager.getGuildAudioManager(event.guild).trackScheduler.queue.shuffled()
        AudioManager.getGuildAudioManager(event.guild).trackScheduler.queue.clear()
        shuffled.forEach {
            AudioManager.getGuildAudioManager(event.guild).trackScheduler.queue.add(it)
        }
        event.message.reply("🔄  シャッフルしました。").queue()
    }

}