package dev.itsu.soprano.command

import dev.itsu.soprano.Bot
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class HelpCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        event.message.reply("""
            **コマンド[エイリアス]** 説明
            -----------------------------------
            **${Bot.PREFIX}play[p] <URL>** 指定されたYouTubeの音楽をキューに追加します。
            **${Bot.PREFIX}skip[s]** スキップ
            **${Bot.PREFIX}pause** ポーズ
            **${Bot.PREFIX}resume** 再生
            **${Bot.PREFIX}disconnect[dc]** ボイスチャンネルから退出します。
            **${Bot.PREFIX}help[h]** ヘルプを表示します。
            **${Bot.PREFIX}version** バージョン情報を表示します。
            **${Bot.PREFIX}ping** Ping値を表示します。
        """.trimIndent()).queue()
    }

}