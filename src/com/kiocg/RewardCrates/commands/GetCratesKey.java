package com.kiocg.RewardCrates.commands;

import com.kiocg.RewardCrates.Utils.Utils;
import com.kiocg.ZLibraries.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GetCratesKey implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String @NotNull [] args) {
        if (!(sender instanceof final @NotNull Player player)) {
            sender.sendMessage("此指令仅限玩家使用.");
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        final ItemStack cratesKey = new ItemStack(Material.TRIPWIRE_HOOK);
        cratesKey.editMeta(itemMeta -> {
            itemMeta.displayName(Component.text(args[0] + "钥匙", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
            itemMeta.lore(new ArrayList<>() {{
                add(Component.text("用来打开某个奖励箱", NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
            }});
            itemMeta.getPersistentDataContainer().set(Utils.cratesNamespacedKey, PersistentDataType.STRING, args[0]);
            itemMeta.setCustomModelData(15);
            itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });

        PlayerUtils.giveItemLeftoverDrop(player, cratesKey);
        return true;
    }
}
