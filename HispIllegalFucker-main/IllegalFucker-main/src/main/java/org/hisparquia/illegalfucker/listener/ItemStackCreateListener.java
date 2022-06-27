package me.sevj6.illegalfucker.listener;

import org.hisparquia.illegalfucker.util.ItemReverter;
import org.hisparquia.illegalfucker.util.ItemUtil;
import org.hisparquia.illegalfucker.util.Utils;
import me.txmc.paperapiextentions.events.ItemStackCreateEvent;
import me.txmc.paperapiextentions.mixin.mixins.MixinTileEntity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class ItemStackCreateListener implements Listener {

    @EventHandler
    public void onCreate(ItemStackCreateEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() == Item.getById(0)) return;
        ItemReverter.revert(itemStack);
        if (itemStack.getItem() instanceof ItemShulkerBox) {
            if (!itemStack.hasTag()) return;
            NBTTagCompound compound = itemStack.getTag();
            if (!compound.hasKey("BlockEntityTag")) return;
            NBTTagCompound blockEntityTag = compound.getCompound("BlockEntityTag");
            if (!blockEntityTag.hasKey("Items")) return;
            NBTTagList items = (NBTTagList) blockEntityTag.get("Items");
            for (int i = 0; i < items.size(); i++) {
                NBTTagCompound internalCompound = items.get(i);
                Item item = internalCompound.hasKeyOfType("id", 8) ? Item.b(internalCompound.getString("id")) : Item.getItemOf(Blocks.AIR);
                byte count = internalCompound.getByte("Count");
                if (count > item.getMaxStackSize()) {
                    internalCompound.setByte("Count", (byte) item.getMaxStackSize());
                    Utils.log("&aReverted a &r&a%s&3 because it was overstacked (Inside ShulkerBox)", item.getName());
                }
                if (item instanceof ItemShulkerBox) {
                    internalCompound.setString("id", "minecraft:air");
                    Utils.log("&3Removed a shulkerbox from inside a shulkerbox");
                } else if (ItemUtil.getIllegals().contains(item)) {
                    internalCompound.setString("id", "minecraft:air");
                    Utils.log("&3Removed illegal block from inside a shulker %s", item.getName());
                }
            }
        }
    }
}
