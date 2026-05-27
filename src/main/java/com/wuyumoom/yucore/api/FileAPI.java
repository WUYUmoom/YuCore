package com.wuyumoom.yucore.api;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileAPI {
    public static YamlConfiguration getPlayer(String player,Plugin plugin) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(player,plugin));
    }

    public static File getPlayerFile(String player,Plugin plugin) {
        return new File(new File(plugin.getDataFolder(), "data"), player + ".yml");
    }

    public static File[] folderFiles(@Nonnull Plugin plugin, String fileName, File pluginFile) {
        return folderFiles(plugin, fileName, pluginFile, true);
    }
    public static File[] folderFiles(@Nonnull Plugin plugin, String fileName, File pluginFile, boolean recursive) {
        File directory = new File(plugin.getDataFolder(), fileName);
        if (!directory.exists()) {
            plugin.getLogger().info(fileName + "文件夹不存在，正在从插件内部加载...");
            saveResource(plugin, fileName, false, pluginFile);
            plugin.getLogger().info(fileName + "文件夹已成功加载。");
        }

        if (recursive) {
            List<File> allFiles = new ArrayList<>();
            collectFiles(directory, allFiles);
            return allFiles.toArray(new File[0]);
        } else {
            return directory.listFiles();
        }
    }



    private static void collectFiles(File directory, List<File> result) {
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                collectFiles(file, result);
            } else {
                result.add(file);
            }
        }
    }
    public static void saveResource(@Nonnull Plugin plugin,String resourcePath, boolean replace,File pluginFile) {
        resourcePath = resourcePath.replace('\\', '/');
        File destFolder = new File(plugin.getDataFolder(), resourcePath);

        if (!destFolder.exists() || replace) {
            try {
                copyResourceFolder(resourcePath, destFolder,pluginFile);
            } catch (IOException e) {
                plugin.getLogger().info("无法保存资源文件夹 '" + destFolder + "': " + e.getMessage());
            }
        }
    }

    private static void copyResourceFolder(String resourcePath, File destFolder,File pluginFile) throws IOException {
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }
        try (ZipFile zip = new ZipFile(pluginFile)) { // 正确打开插件jar文件
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(resourcePath + "/") && !entry.isDirectory()) {
                    File destFile = new File(destFolder, name.substring(resourcePath.length() + 1));
                    File destParent = destFile.getParentFile();
                    if (!destParent.exists()) {
                        destParent.mkdirs();
                    }
                    try (InputStream in = zip.getInputStream(entry)) {
                        Files.copy(in, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }
}
