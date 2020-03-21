package dev.itsu.admincp.user

import cn.nukkit.Server
import cn.nukkit.event.player.PlayerKickEvent
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

class BanActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse
        var player = customResponse.result[0] as String
        val banType = customResponse.result[2] as Boolean
        val reason = customResponse.result[3] as String

        if (player.isEmpty()) player = customResponse.result[1] as String

        if (Server.getInstance().getPlayer(player) == null) {
            MessageActivity(manifest, "プレイヤー: ${player}は存在しません。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)
        }

        if (banType) {
            Server.getInstance().ipBans.addBan(player, reason)
        } else {
            Server.getInstance().nameBans.addBan(player, reason)
        }

        Server.getInstance().getPlayer(player)?.kick(
                if (banType) PlayerKickEvent.Reason.IP_BANNED else PlayerKickEvent.Reason.NAME_BANNED,
                reason
        )

        MessageActivity(manifest, player + "をBANしました。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

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
        addFormElement(Toggle("名前BAN / IP-BAN"))
        addFormElement(Input("BANする理由"))
    }

}