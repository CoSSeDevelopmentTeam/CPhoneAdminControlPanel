package dev.itsu.admincp.console

import cn.nukkit.Server
import dev.itsu.admincp.MainActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.CustomResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.CustomActivity
import net.comorevi.cphone.cphone.widget.activity.original.MessageActivity
import net.comorevi.cphone.cphone.widget.element.Input

class ConsoleActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse
        val commands = customResponse.result[0] as String

        Server.getInstance().dispatchCommand(Server.getInstance().consoleSender, commands)

        MessageActivity(manifest, "コマンドを実行しました: \n${commands}", "メニューへ戻る", "トップへ戻る", MainActivity(manifest)).start(bundle)

        return ReturnType.TYPE_CONTINUE
    }

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle
        addFormElement(Input("コンソール", "time set 0"))
    }

}