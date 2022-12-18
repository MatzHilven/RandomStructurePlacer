package me.matzhilven.randomstructureplacer.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class StringUtils {

    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> s) {
        return s.stream().map(StringUtils::colorize).collect(Collectors.toList());
    }

    public static String removeColor(String s) {
        return ChatColor.stripColor(colorize(s));
    }

    public static void sendMessage(CommandSender sender, String m) {
        sender.sendMessage(colorize(m));
    }

    public static void sendMessage(CommandSender sender, List<String> m) {
        m.forEach(msg -> sendMessage(sender, msg));
    }

    public static String format(double c) {
        return NumberFormat.getNumberInstance(Locale.US).format(c);
    }

    public static void sendClickableMessage(Player player, String message, String command) {
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(colorize(message)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        player.spigot().sendMessage(component);
    }
}
