package com.wuyumoom.yucore.task;

import com.wuyumoom.yucore.api.BukkitAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.Socket;

public class Advertisement {
    public static void startTimer(Plugin plugin) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // 1. 创建 Socket 连接
                Socket socket = new Socket("v4.wuyumoom.top", 25565);

                // 2. 获取输入流，用于接收服务器返回的字符串
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // 3. 读取一行数据（你可以根据协议修改为读取多行或固定长度）
                String response = in.readLine();
                String[] strings = BukkitAPI.onSetString(BukkitAPI.onReplace(response), "￥");

                // 4. 打印接收到的消息（由于这是在异步线程中，不能直接操作 Bukkit API，需要再切换回主线程）
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    for (String string : strings) {
                        plugin.getServer().getLogger().info(string);
                    }
                });

                // 5. 关闭资源
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("无法连接到服务器语之服务器");
            }
        });
    }
}
