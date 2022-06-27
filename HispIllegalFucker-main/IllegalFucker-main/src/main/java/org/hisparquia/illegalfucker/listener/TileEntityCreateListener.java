package me.sevj6.illegalfucker.listener;

import me.txmc.paperapiextentions.events.TileEntityCreateEvent;
import me.txmc.paperapiextentions.mixin.mixins.MixinTileEntity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.stream.Collectors;

public class TileEntityCreateListener implements Listener {

    @EventHandler
    public void onTileEntityCreate(TileEntityCreateEvent event) {
        NBTTagCompound compound = event.getCompound();
        if (!compound.hasKey("CustomName")) return;
        String name = compound.getString("CustomName");
        if (name.length() > 30) name = name.substring(0, 30);
        name = ChatColor.stripColor(name);
        compound.setString("CustomName", name);
        if (event.getType().equals(TileEntityShulkerBox.class)) {
            NBTTagList items = (NBTTagList) compound.get("Items");
            if (items == null) return;
            for (NBTTagCompound itemComp : items.list.stream().map(t -> (NBTTagCompound)t).collect(Collectors.toList())) {
                Item item = itemComp.hasKeyOfType("id", 8) ? Item.b(itemComp.getString("id")) : Item.getItemOf(Blocks.AIR);
                if (item instanceof ItemShulkerBox) itemComp.setString("id", "minecraft:air");
            }
        }
    }
}
