package com.wuyumoom.yucore.file.view;

import com.wuyumoom.yucore.api.BukkitAPI;
import org.bukkit.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;

public class ViewConfiguration {
    private String fileName;
    private String title;
    private int size;
    private final Map<String ,Button> button = new HashMap<>();

    public ViewConfiguration(Configuration file) {
        title = BukkitAPI.onReplace(file.getString("title"));
        size = file.getInt("size");
        fileName = file.getName();

        for (String view :file.getConfigurationSection("Button").getKeys(false)) {
            button.put(view, new Button(view,file.getString("Button."+view+".name"),file.getString("Button."+view+".slot"),file.getStringList("Button."+view+".lore"),file.getString("Button."+view+".id")));
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<String, Button> getButton() {
        return button;
    }
}

