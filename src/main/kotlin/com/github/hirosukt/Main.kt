package com.github.hirosukt

import net.kyori.adventure.text.Component
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        super.onEnable()

        saveDefaultConfig()

        val messageContainers = config.getConfigurationSection("containers") ?: return

        messageContainers.getKeys(false).forEach {
            val container = messageContainers.getConfigurationSection(it) ?: return
            val seconds = container.getLong("seconds")
            val minutes = container.getLong("minutes")
            val hours = container.getLong("hours")

            val messages = container.getStringList("messages")

            val randomMessage = container.getBoolean("randomMessage")

            val parent = server.pluginManager.getPermission("tips.*") ?: fun(): Permission {
                val tempParentPerm = Permission("tips.*")
                server.pluginManager.addPermission(tempParentPerm)
                return tempParentPerm
            }.invoke()
            val permission = Permission("tips.${it}")
            try {
                server.pluginManager.addPermission(permission)
                permission.addParent(parent, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                var lastMessage = ""
                server.scheduler.runTaskTimer(this, Runnable {
                    val lastMessageIndex = messages.indexOf(lastMessage).inc() % messages.size
                    val message = if (randomMessage) messages.random() else messages[lastMessageIndex]

                    server.broadcast(Component.text(message), permission.name)

                    lastMessage = message
                }, 0L, (seconds * 20) + (minutes * 1200) + (hours * 72000))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}