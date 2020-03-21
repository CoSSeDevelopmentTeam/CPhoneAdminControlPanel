package dev.itsu.admincp.server

import cn.nukkit.Server
import cn.nukkit.command.defaults.TimeCommand
import cn.nukkit.command.defaults.WeatherCommand
import cn.nukkit.event.player.PlayerKickEvent
import cn.nukkit.level.Level
import dev.itsu.admincp.MainActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.CustomResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.CustomActivity
import net.comorevi.cphone.cphone.widget.activity.original.MessageActivity
import net.comorevi.cphone.cphone.widget.element.Dropdown
import net.comorevi.cphone.cphone.widget.element.Input
import net.comorevi.cphone.cphone.widget.element.Label
import net.comorevi.cphone.cphone.widget.element.Toggle

class TimeActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle
    private val timePresets = listOf("未選択", "朝", "昼", "夕方", "夜", "深夜")
    private val weatherPresets = listOf("未選択", "晴れ", "雨", "雷")

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse

        val worldForTime = customResponse.result[1] as String
        var time = (customResponse.result[2] as String).toIntOrNull()
        if (time == null) {
            time = when (customResponse.result[3] as String) {
                "朝" -> Level.TIME_SUNRISE
                "昼" -> Level.TIME_NOON
                "夕方" -> Level.TIME_SUNSET
                "夜" -> Level.TIME_MIDNIGHT
                "深夜" -> Level.TIME_NIGHT
                else -> null
            }
        }

        val worldForWeather = customResponse.result[6] as String
        val weather = customResponse.result[7] as String

        setTime(worldForTime, time)
        setStopped(worldForTime, customResponse.result[4] as Boolean)
        setWeather(worldForWeather, weather)

        MessageActivity(manifest, "時間と天気を変更しました。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

        return ReturnType.TYPE_CONTINUE
    }

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle

        val worlds = mutableListOf<String>()
        worlds.add("未選択")
        worlds.add("全てのワールド")
        Server.getInstance().levels.values.forEach {
            worlds.add(it.name)
        }

        addFormElement(Label("§a時間"))
        addFormElement(Dropdown("対象のワールド", worlds))
        addFormElement(Input("時間で指定する\n§7§o時間を入力した場合そちらが優先されます。", "数値"))
        addFormElement(Dropdown("プリセット", timePresets))
        addFormElement(Toggle("時間を再生/停止", bundle.cPhone.player.level.stopTime))
        addFormElement(Label("§a天候"))
        addFormElement(Dropdown("対象のワールド", worlds))
        addFormElement(Dropdown("プリセット", weatherPresets))
}

    private fun setTime(world: String, time: Int?) {
        if (world == "未選択" || time == null) return

        if (world == "全てのワールド") {
            Server.getInstance().levels.values.forEach {
                it.time = time
                it.sendTime()
            }
            Server.getInstance().broadcastMessage("§aシステム§r>>${bundle.cPhone.player.name}が時間を変更しました。")

        } else {
            Server.getInstance().getLevelByName(world)?.time = time
            Server.getInstance().getLevelByName(world)?.sendTime()
            Server.getInstance().broadcastMessage("§aシステム§r>>${bundle.cPhone.player.name}がワールド: ${world}の時間を変更しました。")
        }
    }

    private fun setStopped(world: String, bool: Boolean) {
        if (world == "未選択") return

        if (world == "全てのワールド") {
            Server.getInstance().levels.values.forEach {
                it.checkTime()
                if (bool) it.stopTime() else it.startTime()
                it.checkTime()
            }
            Server.getInstance().broadcastMessage("§aシステム§r>>${bundle.cPhone.player.name}が時間を${if (bool) "停止" else "再生"}しました。")

        } else {
            val w = Server.getInstance().getLevelByName(world) ?: return
            w.checkTime()
            if (bool) w.stopTime() else w.startTime()
            w.checkTime()
            Server.getInstance().broadcastMessage("§aシステム§r>>${bundle.cPhone.player.name}がワールド: ${world}の時間を${if (bool) "停止" else "再生"}しました。")
        }
    }

    private fun setWeather(world: String, type: String) {
        if (type == "未選択") return

        if (world == "全てのワールド") {
            Server.getInstance().levels.values.forEach {
                setWeather(it, type)
            }
            Server.getInstance().broadcastMessage("§aシステム§r>>${bundle.cPhone.player.name}が天気を${type}に変更しました。")

        } else {
            setWeather(Server.getInstance().getLevelByName(world) ?: return, type)
            Server.getInstance().broadcastMessage("§aシステム§r>>${bundle.cPhone.player.name}がワールド: ${world}の天気を${type}に変更しました。")
        }
    }

    private fun setWeather(level: Level, type: String) {
        when (type) {
            "晴れ" -> {
                level.isRaining = false
                level.isThundering = false
                level.rainTime = 12000 * 20
                level.thunderTime = 12000 * 20
            }
            "雨" -> {
                level.isRaining = true
                level.rainTime = 12000 * 20
            }
            "雷" -> {
                level.isThundering = true
                level.rainTime = 12000 * 20
                level.thunderTime = 12000 * 20
            }
        }
    }

}