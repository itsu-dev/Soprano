package dev.itsu.soprano

import dev.itsu.soprano.listener.CommandListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

object Bot {

    private val jda: JDA

    private var TOKEN = ""
    const val VERSION = "1.2.5 alpha"
    const val PREFIX = "!"

    init {
        try {
            TOKEN = System.getenv("discordToken") ?: File("token.txt").bufferedReader(StandardCharsets.UTF_8).readText()
        } catch (e: IOException) {
            Utils.log(e)
            e.printStackTrace()
            error("Token not found.")
            exitProcess(1)
        }

        jda = JDABuilder.createDefault(TOKEN)
            .setActivity(Activity.listening("きみの好きな音楽"))
            .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
            .disableCache(CacheFlag.ACTIVITY)
            .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
            .setChunkingFilter(ChunkingFilter.NONE)
            .addEventListeners(CommandListener())
            .build()
    }

}