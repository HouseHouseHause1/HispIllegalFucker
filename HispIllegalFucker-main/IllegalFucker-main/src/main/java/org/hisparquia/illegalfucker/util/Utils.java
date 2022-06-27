package org.hisparquia.illegalfucker.util;

import org.hisparquia.illegalfucker.IllegalFucker;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.ChatColor;

import java.util.logging.Level;

/**
 * @author 254n_m
 * @since 6/9/22/ 8:48 PM
 * This file was created as a part of IllegalFucker
 */
public class Utils {

    public static void log(String message, Object... args) {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        if (args != null) message = String.format(message, args);
        message = translateChars(message);
        String className = element.getClassName() + ":" + element.getLineNumber();
        IllegalFucker.getInstance().getLogger().log(Level.INFO, String.format("%s%c%s", message, Character.MIN_VALUE, className));
    }

    public static String translateChars(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String formatItem(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        Item item = itemStack.getItem();
        sb.append("&r&a").append(item.getName()).append(" X ").append(itemStack.getCount()).append("&r");
        return sb.toString();
    }
}
