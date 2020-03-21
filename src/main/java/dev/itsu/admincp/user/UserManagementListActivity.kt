package dev.itsu.admincp.user

import dev.itsu.admincp.MainActivity
import net.comorevi.cphone.cphone.application.ApplicationManifest
import net.comorevi.cphone.cphone.model.Bundle
import net.comorevi.cphone.cphone.model.ListResponse
import net.comorevi.cphone.cphone.model.Response
import net.comorevi.cphone.cphone.widget.activity.ReturnType
import net.comorevi.cphone.cphone.widget.activity.base.ListActivity
import net.comorevi.cphone.cphone.widget.element.Button

class UserManagementListActivity(manifest: ApplicationManifest) : ListActivity(manifest) {

    private lateinit var bundle: Bundle

    override fun onCreate(bundle: Bundle) {
        this.bundle = bundle
        addButton(Button("BAN"))
        addButton(Button("BAN解除"))
        addButton(Button("キック"))
        addButton(Button("個人メッセージを送信"))
    }

    override fun onStop(response: Response): ReturnType {
        val listResponse = response as ListResponse
        when (listResponse.buttonIndex) {
            0 -> BanActivity(manifest).start(bundle)
            1 -> UnBanActivity(manifest).start(bundle)
            2 -> KickActivity(manifest).start(bundle)
            3 -> PersonalMessageActivity(manifest).start(bundle)
            else -> MainActivity(manifest).start(bundle)
        }
        return ReturnType.TYPE_CONTINUE
    }

}