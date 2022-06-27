package org.hisparquia.illegalfucker.util;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class ItemUtil {
    public static final List<Item> illegals = Arrays.asList(
            Item.getById(7), //Bedrock
            Item.getById(166), //Barrier
            Item.getById(120), // End portal frames
            Item.getById(52), //Monster spawner
            Item.getById(255), // Structure block
            Item.getById(217), //Structure void
            Item.getById(383), //Spawn egg
            Item.getById(211), //Chain Command Block
            Item.getById(210), //Repeating Command Block
            Item.getById(137), //Command Block
            Item.getById(422), //Command Block Minecart
            Item.getById(453), //Knowledge book
            Item.getById(208), //Grass path
            Item.getById(60), //Farmland
            Item.getById(31) //Shrubs
    );
    private static final List<Item> exempt = Arrays.asList(
            Item.getById(0), //Air
            Item.getById(397), //Skull
            Item.getById(351), //Dye
            Item.getById(322), //Golden apple
            Item.getById(355), //Beds
            Item.getById(349), //Fish
            Item.getById(350), //Cooked fish
            Item.getById(425), //Banner
            Item.getById(263), //Coal
            Item.getById(358)
    );

    public static List<Item> getIllegals() {
        return illegals;
    }

    public static boolean isUnobtainableItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemSkull && itemStack.hasTag() && itemStack.getTag().hasKey("SkullOwner"))
            return true;
        return illegals.contains(itemStack.getItem());
    }

    public static boolean isOverstacked(ItemStack itemStack) {
        return itemStack.getCount() > itemStack.getItem().getMaxStackSize();
    }

    public static boolean isHighDura(ItemStack itemStack) {
        if (exempt.contains(itemStack.getItem())) return false;
        return itemStack.getDamage() < 0 || itemStack.getDamage() > itemStack.getItem().getMaxDurability() && Item.getId(itemStack.getItem()) > 256;
    }

    public static boolean hasAttributes(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        NBTTagCompound tag = itemStack.getTag();
        if (tag == null) return false;
        return tag.hasKey("AttributeModifiers");
    }

    public static boolean hasTag(ItemStack itemStack) {
        return itemStack.hasTag();
    }

    public static boolean hasIllegalEnchants(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        if (!itemStack.hasEnchantments()) return false;
        NBTTagList enchants = (NBTTagList) ((itemStack.getTag().hasKey("ench")) ? itemStack.getTag().get("ench") : itemStack.getTag().get("StoredEnchantments"));
        for (NBTTagCompound compound : enchants.list.stream().map(t -> (NBTTagCompound) t).toArray(NBTTagCompound[]::new)) {
            short level = compound.getShort("lvl");
            Enchantment enchantment = Enchantment.c(compound.getShort("id"));
            if (level <= 0) return true;
            if (level > enchantment.getMaxLevel()) return true;
            if (!canEnchant(itemStack, enchantment)) return true;
        }
        return false;
    }

    public static boolean hasMeta(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        NBTTagCompound tag = itemStack.getTag();
        if (!tag.hasKey("display")) return false;
        NBTTagCompound display = tag.getCompound("display");
        return display.hasKey("Lore");
    }

    public static boolean canEnchant(ItemStack itemStack, Enchantment enchantment) {
        if (Item.getId(itemStack.getItem()) < 256) return false;
        if (!itemStack.canEnchant()) return false;
        if (!enchantment.canEnchant(itemStack)) return false;
        return enchantment.itemTarget.canEnchant(itemStack.getItem());
    }

    public static boolean isUnbreakable(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().hasKey("Unbreakable");
    }

    public static boolean hasHideFlags(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().hasKey("HideFlags");
    }

    public static boolean hasInvalidBlockEntityTag(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemShulkerBox || itemStack.getItem() instanceof ItemBanner) return false;
        if (!hasTag(itemStack)) return false;
        return itemStack.getTag().hasKey("BlockEntityTag");
    }

    public static boolean hasInvalidName(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        NBTTagCompound tag = itemStack.getTag();
        if (!tag.hasKey("display")) return false;
        NBTTagCompound display = tag.getCompound("display");
        if (!display.hasKey("Name")) return false;
        String name = display.getString("Name");
        if (name.length() > 16) return true;
        return ChatColor.stripColor(name).length() != name.length();
    }

    public static boolean hasCustomPotionEffects(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        NBTTagCompound compound = itemStack.getTag();
        return compound.hasKey("CustomPotionEffects");
    }

    public static boolean hasCustomPotionColor(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        return itemStack.getTag().hasKey("CustomPotionColor");
    }

    public static boolean cantBeEnchanted(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemTool) return false;
        if (itemStack.getItem() instanceof ItemSword) return false;
        if (itemStack.getItem() instanceof ItemFlintAndSteel) return false;
        if (itemStack.getItem() instanceof ItemShears) return false;
        if (itemStack.getItem() instanceof ItemElytra) return false;
        if (itemStack.getItem() instanceof ItemBow) return false;
        return !(itemStack.getItem() instanceof ItemArmor);
    }

    public static boolean hasIllegalFlightDuration(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        if (!(itemStack.getItem() instanceof ItemFireworks)) return false;
        if (itemStack.getTag() == null) return false;
        NBTTagCompound compound = itemStack.getTag().getCompound("Fireworks");
        if (compound == null) return false;
        if (!compound.hasKey("Flight")) return false;
        byte duration = compound.getByte("Flight");
        return duration < 1 || duration > 3;
    }

    public static boolean hasMapColorTag(ItemStack itemStack) {
        if (!hasTag(itemStack)) return false;
        if (!(itemStack.getItem() instanceof ItemWorldMap)) return false;
        NBTTagCompound compound = itemStack.getTag();
        if (!compound.hasKey("display")) return false;
        NBTTagCompound display = compound.getCompound("display");
        return display.hasKey("MapColor");
    }

    public static boolean hasConflictingEnchants(ItemStack itemStack) {
        if (!ItemUtil.hasTag(itemStack)) return false;
        NBTTagList enchants = (NBTTagList) ((itemStack.getTag().hasKey("ench")) ? itemStack.getTag().get("ench") : itemStack.getTag().get("StoredEnchantments"));
        if (enchants == null) return false;
        for (int i = 0; i < enchants.size(); i++) {
            NBTTagCompound enchTag = enchants.get(i);
            Enchantment key = Enchantment.c(enchTag.getShort("id"));
            if (
                    Enchantment.getId(key) == 16 && containsEnchantment(enchants, 17) ||
                            Enchantment.getId(key) == 16 && containsEnchantment(enchants, 18) ||
                            Enchantment.getId(key) == 17 && containsEnchantment(enchants, 18) ||
                            Enchantment.getId(key) == 70 && containsEnchantment(enchants, 51) ||
                            Enchantment.getId(key) == 0 && containsEnchantment(enchants, 4) ||
                            Enchantment.getId(key) == 0 && containsEnchantment(enchants, 1) ||
                            Enchantment.getId(key) == 0 && containsEnchantment(enchants, 3) ||
                            Enchantment.getId(key) == 1 && containsEnchantment(enchants, 3) ||
                            Enchantment.getId(key) == 1 && containsEnchantment(enchants, 4) ||
                            Enchantment.getId(key) == 3 && containsEnchantment(enchants, 4) ||
                            Enchantment.getId(key) == 35 && containsEnchantment(enchants, 33) ||
                            isArmor(itemStack) && Enchantment.getId(key) == 71 && containsEnchantment(enchants, 10))
                return true;
        }
        return false;
    }

    private static boolean containsEnchantment(NBTTagList ench, int id) {
        return ench.list.stream().map(t -> (NBTTagCompound) t).anyMatch(c -> id == c.getShort("id"));
    }
    public static boolean isArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemArmor || itemStack.getItem() instanceof ItemElytra;
    }
}
