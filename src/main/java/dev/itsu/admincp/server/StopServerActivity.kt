package dev.itsu.admincp.server

import cn.nukkit.Server
import dev.itsu.admincp.MainActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.ListResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.ListActivity
import net.comorevi.cphone.cphone.widget.element.Button

class StopServerActivity(manifest: ApplicationManifest) : ListActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle
        content = "以下のボタンをタップするとサーバーが停止します。\n§c起動はサーバー管理者が行う必要があります。"
        addButton(Button("§cサーバーを停止する"))
        addButton(Button("メニューに戻る"))
    }

    override fun onStop(response: Response): ReturnType {
        val listResponse = response as ListResponse
        when (listResponse.buttonIndex) {
            0 -> {
                Server.getInstance().shutdown()
                Server.getInstance().broadcastMessage("§aシステム§r>>サーバーを停止しています...")
                return ReturnType.TYPE_END
            }
            else -> MainActivity(manifest).start(bundle)
        }
        return ReturnType.TYPE_CONTINUE
    }
}