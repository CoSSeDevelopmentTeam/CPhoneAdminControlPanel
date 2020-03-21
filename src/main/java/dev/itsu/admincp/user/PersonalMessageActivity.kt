package dev.itsu.admincp.user

import cn.nukkit.Server
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

class PersonalMessageActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse
        var name = customResponse.result[0] as String
        if (name.isEmpty()) name = customResponse.result[1] as String

        val player = Server.getInstance().getPlayer(name)
        if (player == null) {
            MessageActivity(manifest, "プレイヤー: ${name}は存在しません。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

        } else {
            player.sendMessage("§e[${response.player.name}] §r-> ${customResponse.result[2] as String}")
            response.player.sendMessage("§e[${response.player.name}] §r-> ${customResponse.result[2] as String}")

            MessageActivity(manifest, """
                ${name}にチャットを送信しました: 
                ${customResponse.result[2] as String}
            """.trimIndent(), "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)
        }

        return ReturnType.TYPE_IGNORE
    }

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle

        val players = mutableListOf<String>()
        Server.getInstance().onlinePlayers.values.forEach {
            players.add(it.name)
        }

        addFormElement(Input("名前で指定する\n§7§o名前を入力した場合そちらが優先されます。"))
        addFormElement(Dropdown("オンラインのプレイヤーから選ぶ", players))
        addFormElement(Input("内容"))
    }

}