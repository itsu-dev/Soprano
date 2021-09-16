package dev.itsu.soprano.command

import dev.itsu.soprano.Bot
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class HelpCommand : ICommand {

    override fun processCommand(label: String, args: Array<String>, event: MessageReceivedEvent) {
        event.message.reply("""
            **🎮  コマンド[エイリアス]** 説明
            --------------------------------------
            **${Bot.PREFIX}play[p] <URL>** 指定されたYouTubeの音楽をキューに追加します。
            **${Bot.PREFIX}skip[s]** スキップ
            **${Bot.PREFIX}pause** ポーズ
            **${Bot.PREFIX}resume** 再生
            **${Bot.PREFIX}seek <TIME>** TIMEで指定した時間から曲を再生します。
            **${Bot.PREFIX}forward[fwd] <TIME>** TIMEで指定した時間だけスキップします。
            **${Bot.PREFIX}rewind[rwd] <TIME>** TIMEで指定した時間だけ戻ります。
            **${Bot.PREFIX}replay** 再生中の曲を初めから再生します。
            **${Bot.PREFIX}nowplaying[np]** 再生中の曲の情報を表示します。
            **${Bot.PREFIX}summon** 現在参加しているボイスチャンネルにSopranoを呼び出します。
            **${Bot.PREFIX}disconnect[dc]** ボイスチャンネルから退出します。
            **${Bot.PREFIX}queue[q]** 現在のキューを表示します。
            **${Bot.PREFIX}shuffle** キューをシャッフルします。
            **${Bot.PREFIX}clear** キューをリセットします。
            **${Bot.PREFIX}help[h]** ヘルプを表示します。
            **${Bot.PREFIX}version[ver]** バージョン情報を表示します。
            **${Bot.PREFIX}ping** Ping値を表示します。
        """.trimIndent()).queue()
    }

}