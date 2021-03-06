package dev.itsu.soprano.command

import dev.itsu.soprano.Bot
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class HelpCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        event.message.reply("""
            **ð®  ã³ãã³ã[ã¨ã¤ãªã¢ã¹]** èª¬æ
            --------------------------------------
            **${Bot.PREFIX}play[p] <URL|ã­ã¥ã¼çªå·>** æå®ãããYouTubeã®é³æ¥½ãã­ã¥ã¼ã«è¿½å ãã¾ããã­ã¥ã¼çªå·ãæå®ããå ´åãçªå·ã«å¯¾å¿ããæ²ãåçãã¾ãã
            **${Bot.PREFIX}skip[s]** ã¹ã­ãã
            **${Bot.PREFIX}pause** ãã¼ãº
            **${Bot.PREFIX}resume** åç
            **${Bot.PREFIX}seek <TIME>** TIMEã§æå®ããæéããæ²ãåçãã¾ãã
            **${Bot.PREFIX}forward[fwd] <TIME>** TIMEã§æå®ããæéã ãã¹ã­ãããã¾ãã
            **${Bot.PREFIX}rewind[rwd] <TIME>** TIMEã§æå®ããæéã ãæ»ãã¾ãã
            **${Bot.PREFIX}replay** åçä¸­ã®æ²ãåãããåçãã¾ãã
            **${Bot.PREFIX}nowplaying[np]** åçä¸­ã®æ²ã®æå ±ãè¡¨ç¤ºãã¾ãã
            **${Bot.PREFIX}summon** ç¾å¨åå ãã¦ãããã¤ã¹ãã£ã³ãã«ã«Sopranoãå¼ã³åºãã¾ãã
            **${Bot.PREFIX}disconnect[dc]** ãã¤ã¹ãã£ã³ãã«ããéåºãã¾ãã
            **${Bot.PREFIX}queue[q]** ç¾å¨ã®ã­ã¥ã¼ãè¡¨ç¤ºãã¾ãã
            **${Bot.PREFIX}shuffle** ã­ã¥ã¼ãã·ã£ããã«ãã¾ãã
            **${Bot.PREFIX}clear** ã­ã¥ã¼ããªã»ãããã¾ãã
            **${Bot.PREFIX}help[h]** ãã«ããè¡¨ç¤ºãã¾ãã
            **${Bot.PREFIX}version[ver]** ãã¼ã¸ã§ã³æå ±ãè¡¨ç¤ºãã¾ãã
            **${Bot.PREFIX}ping** Pingå¤ãè¡¨ç¤ºãã¾ãã
        """.trimIndent()).queue()
    }

}