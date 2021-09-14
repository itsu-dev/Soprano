package dev.itsu.soprano.command

import dev.itsu.soprano.audio.AudioManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class PauseCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        val audioManager = AudioManager.getGuildAudioManager(event.guild)
        audioManager.getSendHandler().audioPlayer.isPaused = true
        event.message.reply("ポーズしました。").queue()
    }

}