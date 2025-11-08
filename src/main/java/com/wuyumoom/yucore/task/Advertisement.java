package com.wuyumoom.yucore.task;

import com.wuyumoom.yucore.api.BukkitAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.Socket;

public class Advertisement {
    private String QQ;
    private String CDK;

    private final Socket socket;

    public Advertisement(String QQ, String CDK) throws Exception {
        //socket = new Socket("127.0.0.1", 55555);
        socket = new Socket("mcxuan.top", 1166);
        // 设置超时时间
        socket.setSoTimeout(10000); // 10秒超时
        this.CDK = CDK;
        this.QQ = QQ;
    }
    public void onSend() throws Exception {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("渝可梦服务端," + QQ + "," + CDK);
            out.flush();
            // 移除 socket.shutdownOutput() 调用，避免过早关闭输出流

            onPacketAccept(); // 接收响应
        } catch (Exception e) {
            System.err.println("发送数据时发生错误: " + e.getMessage());
            throw e;
        } finally {
            // 确保在最后关闭连接
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private void onPacketAccept() {
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // 检查是否有可用数据
            if (socket.getInputStream().available() == 0) {
                // 等待一小段时间让数据到达
                Thread.sleep(100);
            }
            String response = in.readUTF();
            if (!response.equals("true")) {
                Bukkit.shutdown();
            }
        } catch (IOException e) {
            System.err.println("读取服务器响应时发生错误: " + e.getMessage());
            // 如果是EOFException，可能是服务器没有发送数据
            if (e instanceof java.io.EOFException) {
                System.err.println("服务器没有发送响应数据，可能是服务器端问题");
            }
            throw new RuntimeException("网络通信失败", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("等待响应时被中断", e);
        }
    }


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
