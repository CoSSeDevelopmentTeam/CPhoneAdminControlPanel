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

class UnBanActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse
        var player = customResponse.result[0] as String
        if (player.isEmpty()) player = customResponse.result[1] as String

        if (Server.getInstance().getPlayerExact(player) == null) {
            MessageActivity(manifest, "プレイヤー: ${player}は存在しません。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

        } else if (!Server.getInstance().nameBans.isBanned(player) && !Server.getInstance().ipBans.isBanned(player)) {
            MessageActivity(manifest, "プレイヤー: ${player}はBANされていません。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

        } else {
            Server.getInstance().nameBans.remove(player)
            Server.getInstance().ipBans.remove(player)
            MessageActivity(manifest, player + "のBANを解除しました。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)
        }

        return ReturnType.TYPE_CONTINUE
    }

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle

        val players = mutableListOf<String>()
        Server.getInstance().onlinePlayers.values.forEach {
            players.add(it.name)
        }

        addFormElement(Input("名前で指定する\n§7§o名前を入力した場合そちらが優先されます。"))
        addFormElement(Dropdown("オンラインのプレイヤーから選ぶ", players))
    }

}