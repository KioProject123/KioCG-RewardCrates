package com.kiocg.RewardCrates.Utils;

import com.kiocg.RewardCrates.RewardCrates;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Utils {
    public static final NamespacedKey cratesNamespacedKey = new NamespacedKey(RewardCrates.getInstance(), "RewardCrates");

    public static boolean isCratesKey(final @NotNull ItemStack itemStack, final boolean checkMaterialFirst) {
        if (checkMaterialFirst && itemStack.getType() != Material.TRIPWIRE_HOOK) {
            return false;
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.hasCustomModelData() && itemMeta.getCustomModelData() == 15;
    }

    // 返回钥匙是否和奖励箱匹配
    public static boolean isKeyFor(final @NotNull ItemStack key, final @NotNull Chest crates) {
        return key.getPersistentDataContainer().getOrDefault(cratesNamespacedKey, PersistentDataType.STRING, "")
                  .equals(crates.getPersistentDataContainer().get(cratesNamespacedKey, PersistentDataType.STRING));
    }
}
