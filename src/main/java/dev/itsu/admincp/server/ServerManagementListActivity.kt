package dev.itsu.admincp.server

import dev.itsu.admincp.MainActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.ListResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.ListActivity
import net.comorevi.cphone.cphone.widget.element.Button

class ServerManagementListActivity(manifest: ApplicationManifest) : ListActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle
        addButton(Button("ホワイトリスト"))
        addButton(Button("時間と天候"))
        addButton(Button("§cサーバー停止"))
    }

    override fun onStop(response: Response): ReturnType {
        val listResponse = response as ListResponse
        when (listResponse.buttonIndex) {
            0 -> WhiteListActivity(manifest).start(bundle)
            1 -> TimeActivity(manifest).start(bundle)
            2 -> StopServerActivity(manifest).start(bundle)
            else -> MainActivity(manifest).start(bundle)
        }
        return ReturnType.TYPE_CONTINUE
    }

}