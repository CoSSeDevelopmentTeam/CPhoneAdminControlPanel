package dev.itsu.admincp.server

import cn.nukkit.Server
import cn.nukkit.command.defaults.WhitelistCommand
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

class WhiteListActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse
        val whiteListed = customResponse.result[0] as Boolean

        Server.getInstance().setPropertyBoolean("white-list", whiteListed)

        if (whiteListed && customResponse.result[1] as Boolean) {
            Server.getInstance().onlinePlayers.values.forEach {
                if (!it.isWhitelisted) it.kick(PlayerKickEvent.Reason.NOT_WHITELISTED)
            }
        }

        MessageActivity(manifest, if (whiteListed) "ホワイトリスト状態にしました。" else "ホワイトリストを解除しました。", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

        return ReturnType.TYPE_CONTINUE
    }

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle
        addFormElement(Toggle("ホワイトリスト", Server.getInstance().getPropertyBoolean("white-list")))
        addFormElement(Toggle("ホワイトリストのメンバー以外を退出"))
    }

}