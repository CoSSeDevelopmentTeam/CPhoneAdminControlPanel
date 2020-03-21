package dev.itsu.admincp.console

import cn.nukkit.Server
import cn.nukkit.event.player.PlayerKickEvent
import dev.itsu.admincp.ConfigManager
import dev.itsu.admincp.MainActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.CustomResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.CustomActivity
import net.comorevi.cphone.cphone.widget.activity.original.MessageActivity
import net.comorevi.cphone.cphone.widget.element.Input
import net.comorevi.cphone.cphone.widget.element.Label

class PasswordActivity(manifest: ApplicationManifest) : CustomActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onStop(response: Response): ReturnType {
        val customResponse = response as CustomResponse
        val pass = customResponse.result[1] as String

        if (pass != ConfigManager.password) {
            MessageActivity(
                    manifest,
                    "§cパスワードが正しくありません。",
                    "もう一度試行する",
                    "メニューへ戻る",
                    PasswordActivity(manifest),
                    MainActivity(manifest)
            ).start(bundle)

        } else {
            ConsoleActivity(manifest).start(bundle)
        }

        return ReturnType.TYPE_CONTINUE
    }

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle
        addFormElement(Label("この操作を行うにはパスワードが必要です。"))
        addFormElement(Input("パスワード"))
    }

}