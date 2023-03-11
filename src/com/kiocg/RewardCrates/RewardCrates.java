package com.kiocg.RewardCrates;

import com.kiocg.RewardCrates.commands.GetCratesKey;
import com.kiocg.RewardCrates.commands.SetCrates;
import com.kiocg.RewardCrates.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class RewardCrates extends JavaPlugin {
    private static RewardCrates instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    public static RewardCrates getInstance() {
        //noinspection StaticVariableUsedBeforeInitialization
        return instance;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        Objects.requireNonNull(Bukkit.getPluginCommand("getcrateskey")).setExecutor(new GetCratesKey());
        Objects.requireNonNull(Bukkit.getPluginCommand("setcrates")).setExecutor(new SetCrates());
    }
}
