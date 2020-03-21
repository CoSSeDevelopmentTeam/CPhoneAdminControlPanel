package dev.itsu.admincp

import dev.itsu.admincp.console.PasswordActivity
import dev.itsu.admincp.server.ServerManagementListActivity
import dev.itsu.admincp.user.UserManagementListActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.ListResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.ListActivity
import net.comorevi.cphone.cphone.widget.element.Button

class MainActivity(manifest: ApplicationManifest) : ListActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle

        addButton(Button("ユーザー操作"))
        addButton(Button("サーバー操作"))
        addButton(Button("コンソールからコマンドを実行"))
    }

    override fun onStop(response: Response): ReturnType {
        val listResponse = response as ListResponse
        when (listResponse.buttonIndex) {
            0 -> UserManagementListActivity(manifest).start(bundle)
            1 -> ServerManagementListActivity(manifest).start(bundle)
            2 -> PasswordActivity(manifest).start(bundle)
            else -> return ReturnType.TYPE_END
        }
        return ReturnType.TYPE_CONTINUE
    }

    override fun onStart() {
        ConfigManager.init()
    }

}