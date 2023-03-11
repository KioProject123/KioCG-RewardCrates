package com.kiocg.RewardCrates.commands;

import com.kiocg.RewardCrates.Utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SetCrates implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        final Block block = player.getTargetBlockExact(10);
        if (block == null || block.getType() != Material.CHEST) {
            player.sendMessage("§a[§b豆渣子§a] §6你需要看着一个箱子.");
            return true;
        }

        ((Chest) block.getState(false)).getPersistentDataContainer().set(Utils.cratesNamespacedKey, PersistentDataType.STRING, args[0]);
        player.sendMessage("§a[§b豆渣子§a] §2奖励箱已创建: " + args[0]);
        return true;
    }
}
