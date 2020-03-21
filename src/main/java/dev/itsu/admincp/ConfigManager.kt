package dev.itsu.admincp

import cn.nukkit.utils.Config
import net.comorevi.cphone.presenter.SharingData
import java.io.File

object ConfigManager {

    lateinit var password: String

    fun init() {
        val file = File("${SharingData.server.dataPath}plugins/CPhone/AppData/AdminControlPanel");
        file.mkdirs()

        val config = Config(File(file, "Config.yml"), Config.YAML, LinkedHashMap<String, Any>(linkedMapOf("Password" to "administrator")))
        config.save()

        password = config.getString("Password")
    }

}