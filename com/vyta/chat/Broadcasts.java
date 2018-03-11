package com.vyta.chat;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.vyta.Vyta;

public class Broadcasts implements Listener {
	@EventHandler(priority = EventPriority.HIGH)
	private void onPlayerJoin(PlayerJoinEvent event) {
		File f = new File(Vyta.main.getDataFolder(), "messages.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		Player p = event.getPlayer();
		String name = p.getName();
		Bukkit.createBossBar(name + " joined", BarColor.PURPLE, BarStyle.SOLID, BarFlag.DARKEN_SKY);
		if (p.hasPlayedBefore()) {
			p.sendTitle(Vyta.main.getName(), "Welcome Back", 100, 300, 100);
			List<String> msgs = config.getStringList("messages.join");
			final int SIZE = msgs.size();
			String[] messages = new String[SIZE];
			for (int i = 0; i < SIZE; i++) {
				messages[i] = msgs.get(i);
			}
			String finalMsg = ChatColor.translateAlternateColorCodes('&',
					messages[new Random().nextInt(messages.length)]);
			String msgFinal = finalMsg.replace("%player%", name);
			event.setJoinMessage(msgFinal);
		} else {
			p.sendTitle(Vyta.main.getName(), "Welcome", 100, 300, 100);
			Bukkit.createBossBar("Its " + p.getName() + " first time here! Welcome!", BarColor.WHITE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
			List<String> msgs = config.getStringList("messages.welcome");
			final int SIZE = msgs.size();
			String[] messages = new String[SIZE];
			for (int i = 0; i < SIZE; i++) {
				messages[i] = msgs.get(i);
			}
			String finalMsg = ChatColor.translateAlternateColorCodes('&',
					messages[new Random().nextInt(messages.length)]);
			String msgFinal = finalMsg.replace("%player%", name);
			event.setJoinMessage(msgFinal);
		}
	}

	@EventHandler
	private void onPlayerLeave(PlayerQuitEvent event) {
		String name = event.getPlayer().getName();
		
		File f = new File(Vyta.main.getDataFolder(), "messages.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		List<String> msgs = config.getStringList("messages.leave");
		final int SIZE = msgs.size();
		String[] messages = new String[SIZE];
		for (int i = 0; i < SIZE; i++) {
			messages[i] = msgs.get(i);
		}
		String finalMsg = ChatColor.translateAlternateColorCodes('&',
				messages[new Random().nextInt(messages.length)]);
		String msgFinal = finalMsg.replace("%player%", name);
		event.setQuitMessage(msgFinal);

	}

	@EventHandler
	private void onPlayerKick(PlayerKickEvent event) {
		String name = event.getPlayer().getName();
		
		File f = new File(Vyta.main.getDataFolder(), "messages.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		List<String> msgs = config.getStringList("messages.kick");
		final int SIZE = msgs.size();
		String[] messages = new String[SIZE];
		for (int i = 0; i < SIZE; i++) {
			messages[i] = msgs.get(i);
		}
		String finalMsg = ChatColor.translateAlternateColorCodes('&',
				messages[new Random().nextInt(messages.length)]);
		String msgFinal = finalMsg.replace("%player%", name);
		Bukkit.broadcastMessage(msgFinal);
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event) {
		String name = event.getEntity().getName();
		
		File f = new File(Vyta.main.getDataFolder(), "messages.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		List<String> msgs = config.getStringList("messages.death");
		final int SIZE = msgs.size();
		String[] messages = new String[SIZE];
		for (int i = 0; i < SIZE; i++) {
			messages[i] = msgs.get(i);
		}
		String msgColor = ChatColor.translateAlternateColorCodes('&', messages[new Random().nextInt(messages.length)]);
		String msgFinal = msgColor.replace("%player%", name);
		event.setDeathMessage(msgFinal);
	}
}
