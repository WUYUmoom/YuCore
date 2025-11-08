package com.wuyumoom.yucobblemonapi.lang

import com.google.gson.JsonParser
import com.wuyumoom.yucore.YuCore
import net.fabricmc.loader.api.FabricLoader
import org.bukkit.Bukkit
import kotlin.io.path.reader

class CobblemonLang {



    //中文语言文件
    val zh : Map<String,String> by lazy { onLoadLang() }

    private fun onLoadLang():Map<String,String> {
        return try {
            val modContainer = FabricLoader.getInstance().getModContainer("cobblemon").orElseThrow { IllegalStateException("未找到mod") }
            val path = modContainer.findPath("assets/cobblemon/lang/zh_cn.json").orElseThrow { IllegalStateException("未找到中文文件") }
            JsonParser.parseReader(path.reader()).asJsonObject.entrySet().associate { it.key to it.value.asString }.also {
                Bukkit.getConsoleSender().sendMessage("§e§l已加载"+it.size+"条cobblemon的翻译")
            }
        }catch (e:Exception){
            Bukkit.getConsoleSender().sendMessage("加载cobblemon的翻译失败")
            YuCore.setIsTranslatePath(false)
            emptyMap()
        }
    }
}