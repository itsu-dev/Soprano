package dev.itsu.soprano.listener

import dev.itsu.soprano.Bot
import dev.itsu.soprano.command.*
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener : ListenerAdapter() {

    private val commands = mutableMapOf<String, Class<out ICommand>>()

    init {
        addCommand("play:p", PlayCommand::class.java)
        addCommand("loop", LoopCommand::class.java)
        addCommand("version:ver", VersionCommand::class.java)
        addCommand("skip:s", SkipCommand::class.java)
        addCommand("help:h", HelpCommand::class.java)
        addCommand("ping", PingCommand::class.java)
        addCommand("disconnect:dc", DisconnectCommand::class.java)
        addCommand("pause", PauseCommand::class.java)
        addCommand("resume", ResumeCommand::class.java)
        addCommand("nowplaying:np", NowPlayingCommand::class.java)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val message = event.message

        if (message.author.isBot) {
            message.reply("**Botがこのコマンドをすることはできません。**")
            return
        }

        if (message.contentRaw.startsWith(Bot.PREFIX)) {
            val split = message.contentRaw.split(" ")
            val label = split[0].substring(1).lowercase()
            val key = commands.keys.firstOrNull { it.contains(label) }
            if (key != null) {
                commands[key]
                    ?.asSubclass(ICommand::class.java)
                    ?.getConstructor()
                    ?.newInstance()
                    ?.processCommand(
                        split[0].substring(1),
                        split.slice(IntRange(1, split.size - 1)).toTypedArray(),
                        event
                    )
            }
        }
    }

    fun addCommand(label: String, clazz: Class<out ICommand>) {
        commands[label] = clazz
    }

}