package com.kiocg.RewardCrates.listeners;

import com.kiocg.RewardCrates.Utils.Utils;
import com.kiocg.ZLibraries.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Listeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(final @NotNull InventoryOpenEvent e) {
        final Player player = (Player) e.getPlayer();
        final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (!Utils.isCratesKey(itemInMainHand, true)) {
            return;
        }

        final Inventory inventory = e.getInventory();
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }

        final Location location = inventory.getLocation();
        if (location == null) {
            return;
        }

        if (!Utils.isKeyFor(itemInMainHand, (Chest) location.getBlock().getState(false))) {
            player.sendMessage("§a[§b豆渣子§a] §6这把钥匙不能开启这个奖励箱.");
            e.setCancelled(true);
            return;
        }

        // 开始抽奖

        e.setCancelled(true);

        final Map<ItemStack, Integer> gifts = new HashMap<>();
        int totalChance = 0;
        for (final ItemStack content : inventory.getStorageContents()) {
            if (content == null) {
                // 优化遍历 (要求所有奖品存放在箱子最前端)
                break;
            }

            final List<Component> lore = content.lore();
            if (lore == null) {
                player.sendMessage("§a[§b豆渣子§a] §4奖品" + content.getType() + "设置错误, 请联系管理员!");
                return;
            }

            // 获取奖品概率
            final String chanceString = PlainTextComponentSerializer.plainText().serialize(lore.get(lore.size() - 1));
            final int index = chanceString.lastIndexOf(':');
            if (index == -1) {
                player.sendMessage("§a[§b豆渣子§a] §4奖品" + content.getType() + "设置错误, 请联系管理员!");
                return;
            }

            final int chance = Integer.parseInt(chanceString.substring(index + 1).trim());
            totalChance += chance;
            gifts.put(content, chance);
        }

        int thisRandomNum = new SecureRandom().nextInt(totalChance);
        for (final Map.Entry<ItemStack, Integer> which : gifts.entrySet()) {
            thisRandomNum -= which.getValue();
            if (thisRandomNum < 0) {
                itemInMainHand.subtract();

                // 克隆物品
                final ItemStack itemStack = which.getKey().clone();
                // 去除概率Lore
                itemStack.editMeta(itemMeta -> {
                    final List<Component> lore = itemMeta.lore();
                    //noinspection MethodCallInLoopCondition
                    do {
                        Objects.requireNonNull(lore).remove(lore.size() - 1);
                    } while (!lore.isEmpty() && PlainTextComponentSerializer.plainText().serialize(lore.get(lore.size() - 1)).isEmpty());
                    itemMeta.lore(!lore.isEmpty() ? lore : null);
                });
                //TODO 奖励动画
                PlayerUtils.giveItemLeftoverDrop(player, itemStack);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelBlockPlace(final @NotNull BlockPlaceEvent e) {
        if (Utils.isCratesKey(e.getItemInHand(), true)) {
            e.setCancelled(true);
        }
    }
}
