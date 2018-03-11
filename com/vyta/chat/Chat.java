package com.vyta.chat;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.vyta.Vyta;
import com.vyta.configs.PlayerFiles;
import com.vyta.utils.Prefix;

public class Chat implements Listener {
	@EventHandler
	private boolean playerChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		PlayerFiles cm = PlayerFiles.getConfig(p);
		FileConfiguration playerConfig = cm.getConfig();
		
		File f = new File(Vyta.main.getDataFolder(), "messages.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		if (playerConfig.getBoolean("muted") == true) {
			p.sendMessage(Prefix.prefix() + "You're muted, you may not speak.");
			event.setCancelled(true);
			return true;
		}

		String name = event.getPlayer().getDisplayName();
		String message = event.getMessage();
		List<String> msgs = config.getStringList("messages.blockedwords");
		final int SIZE = msgs.size();
		String[] messages = new String[SIZE];
		for (int i = 0; i < SIZE; i++) {
			messages[i] = msgs.get(i);
		}
		String chatColor = playerConfig.getString("color");

		String msg = config.getString("messages.chat");

		String change1 = msg.replace("%player%", name);
		String change2 = change1.replace("%message%", message);
		String change3 = change2.replace("#color#", chatColor);
		String change4 = change3.replaceAll("%", "%%");

		String msgColor = ChatColor.translateAlternateColorCodes('&', change4);

		//String prefix = plugin.getConfig().getString("ranks." + playerConfig.getString("rank").toLowerCase());
		String prefix = config.getString("messages.defaultrank");
		List<String> stringPrefixs = config.getStringList("messages.ranks");
		final int xSIZE = stringPrefixs.size();
		String[] prefixs = new String[xSIZE];
		for (int i = 0; i < xSIZE; i++) {
			prefixs[i] = stringPrefixs.get(i);
		}
		for (int i = 0; i < xSIZE; i++) {
			String check = prefixs[i];
			int divider = check.indexOf(':');
			if (divider != -1) {
				String rank = check.substring(0, divider);
				if (rank.toLowerCase().equals(playerConfig.getString("rank").toLowerCase())) {
					final int DIVIDER_SPACE = 1;
					final int SPACE = 1;
					prefix = prefixs[i].substring(divider + DIVIDER_SPACE + SPACE);
				}
			}
		}

		String prefixColor = ChatColor.translateAlternateColorCodes('&', prefix);

		for (String s : messages) {
			String check = s.toLowerCase();
			if (message.toLowerCase().contains(check)) {
				String clean = msgColor.toLowerCase().replaceAll(check, config.getString("messages.blockedreplace"));
				event.setFormat(prefixColor + clean);
				return true;
			} else {
				event.setFormat(prefixColor + msgColor);
				return true;
			}
		}
		
		return true;
	}
}
