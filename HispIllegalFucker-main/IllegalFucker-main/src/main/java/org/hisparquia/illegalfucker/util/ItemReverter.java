package org.hisparquia.illegalfucker.util;

import net.minecraft.server.v1_12_R1.Enchantment;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.ChatColor;

/**
 * @author 254n_m
 * @since 6/10/22/ 12:32 AM
 * This file was created as a part of IllegalFucker
 */
public class ItemReverter {
    public static void revert(ItemStack itemStack) {
        if (ItemUtil.isUnobtainableItem(itemStack)) {
            Utils.log("&3Deleted a &r&a%s&3 because it was unobtainable", Utils.formatItem(itemStack));
            itemStack.setCount(-1);
        }
        if (ItemUtil.isOverstacked(itemStack)) {
            Utils.log("&aReverted a &r&a%s&3 because it was overstacked", Utils.formatItem(itemStack));
            itemStack.setCount(itemStack.getItem().getMaxStackSize());
        }
        if (ItemUtil.isHighDura(itemStack)) {
            Utils.log("&aReverted a &r&a%s&3 because it had&r&a %d/%d&r&3 durability", Utils.formatItem(itemStack), itemStack.getDamage(), itemStack.getItem().getMaxDurability());
            itemStack.setDamage(0); //Damage in minecraft is fucking weird
        }
        if (ItemUtil.hasAttributes(itemStack)) {
            Utils.log("&aRemoving attributes from item %s", Utils.formatItem(itemStack));
            NBTTagCompound tagCompound = itemStack.getTag();
            tagCompound.remove("AttributeModifiers");
            itemStack.setTag(tagCompound);
        }
        if (ItemUtil.cantBeEnchanted(itemStack)) {
            if (itemStack.hasTag()) {
                NBTTagCompound tag = itemStack.getTag();
                if (tag.hasKey("ench")) {
                    tag.remove("ench");
                    Utils.log("&3Removed all enchantments from item %s", Utils.formatItem(itemStack));
                }
            }
        }

        if (ItemUtil.hasConflictingEnchants(itemStack)) {
            if (itemStack.getTag().hasKey("StoredEnchantments")) {
                itemStack.setCount(-1);
            } else if (itemStack.getTag().hasKey("ench")) {
                itemStack.getTag().remove("ench");
            }
        }
        if (ItemUtil.hasIllegalEnchants(itemStack)) {
            revertEnchants(itemStack);
        }
        if (ItemUtil.hasCustomPotionEffects(itemStack)) {
            itemStack.getTag().remove("CustomPotionEffects");
            Utils.log("&3Removed all custom potion effects from %s", Utils.formatItem(itemStack));
        }
        if (ItemUtil.hasMeta(itemStack)) {
            NBTTagCompound display = itemStack.getTag().getCompound("display");
            display.remove("Lore");
            Utils.log("&3Removed lore form %s", Utils.formatItem(itemStack));
        }
        if (ItemUtil.isUnbreakable(itemStack)) {
            itemStack.getTag().remove("Unbreakable");
            Utils.log("&3Removed the Unbreakable tag from item %s", Utils.formatItem(itemStack));
        }
        if (ItemUtil.hasHideFlags(itemStack)) {
            itemStack.getTag().remove("HideFlags");
            Utils.log("&3Removed the HideFlags tag from item %s", Utils.formatItem(itemStack));
        }
        if (ItemUtil.hasInvalidBlockEntityTag(itemStack)) {
            itemStack.getTag().remove("BlockEntityTag");
            Utils.log("&3Removed the BlockEntityTag tag from item %s", Utils.formatItem(itemStack));
        }
        if (ItemUtil.hasInvalidName(itemStack)) {
            NBTTagCompound display = itemStack.getTag().getCompound("display");
            String name = display.getString("Name");
            if (ChatColor.stripColor(name).length() != name.length()) name = ChatColor.stripColor(name);
            if (name.length() > 30) name = name.substring(0, 30);
            display.setString("Name", name);
        }
        if (ItemUtil.hasIllegalFlightDuration(itemStack)) {
            NBTTagCompound compound = itemStack.getTag().getCompound("Fireworks");
            byte duration = compound.getByte("Flight");
            if (duration < 1) compound.setByte("Flight", (byte) 1);
            else if (duration > 3) compound.setByte("Flight", (byte) 3);
            Utils.log("&3Reverted the flight duration of a firework with the duration &a%d", duration);
        }
        if (ItemUtil.hasMapColorTag(itemStack)) {
            itemStack.getTag().getCompound("display").remove("MapColor");
        }
    }

    private static void revertEnchants(ItemStack itemStack) {
        NBTTagList enchants = (NBTTagList) ((itemStack.getTag().hasKey("ench")) ? itemStack.getTag().get("ench") : itemStack.getTag().get("StoredEnchantments"));
        if (enchants == null) return;
        for (int i = 0; i < enchants.size(); i++) {
            NBTTagCompound compound = enchants.get(i);
            short level = compound.getShort("lvl");
            Enchantment enchantment = Enchantment.c(compound.getShort("id"));
            if (level <= 0) {
                compound.setShort("lvl", (short) 1);
                Utils.log("&3Reverted enchant&r&a %s&r&3 from level&r&a %d&r&3 to&r&a %d&r", enchantment.a(), level, enchantment.getMaxLevel());
            } else if (level > enchantment.getMaxLevel()) {
                compound.setShort("lvl", (short) enchantment.getMaxLevel());
                Utils.log("&3Reverted enchant&r&a %s&r&3 from level&r&a %d&r&3 to&r&a %d&r", enchantment.a(), level, enchantment.getMaxLevel());
            }
        }
    }
}
