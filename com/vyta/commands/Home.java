package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.vyta.configs.PlayerFiles;
import com.vyta.utils.Prefix;

public class Home implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("home")) {
			Player target = Bukkit.getPlayer(sender.getName());

			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(Prefix.prefix() + "The console should not have a bed to teleport to.. lol..");
				return true;
			}

			PlayerFiles cm = PlayerFiles.getConfig(target);
			FileConfiguration config = cm.getConfig();
			Location bed = target.getBedSpawnLocation();
			
			if (config.isSet("home.world")) {
				target.sendMessage(Prefix.prefix() + "Teleporting to your home set in config.");
				String world = config.getString("home.world");
				int x = config.getInt("home.x");
				int y = config.getInt("home.y");
				int z = config.getInt("home.z");
				Location loc = new Location(Bukkit.getWorld(world), x, y, z);
				target.teleport(loc);
			} else if (bed != null) {
				target.sendMessage(Prefix.prefix() + "Teleporting you to your bed that you created.");
				target.teleport(bed);
			} else {
				target.sendMessage(Prefix.prefix() + "Make a bed or set a home with /sethome.");
			}
			return true;
		}

		if (command.getName().equalsIgnoreCase("sethome")) {
			Player target = Bukkit.getPlayer(sender.getName());
			PlayerFiles cm = PlayerFiles.getConfig(target);
			FileConfiguration config = cm.getConfig();
			Location loc = target.getLocation();
			config.set("home.x", loc.getBlockX());
			config.set("home.y", loc.getBlockY());
			config.set("home.z", loc.getBlockZ());
			config.set("home.world", target.getWorld().getName());
			cm.saveConfig();
			sender.sendMessage(Prefix.prefix() + "Home set.");
			return true;
		}
		
		if (command.getName().equalsIgnoreCase("delhome")) {
			Player target = Bukkit.getPlayer(sender.getName());
			PlayerFiles cm = PlayerFiles.getConfig(target);
			FileConfiguration config = cm.getConfig();
			config.set("home.x", null);
			config.set("home.y", null);
			config.set("home.z", null);
			config.set("home.world", null);
			cm.saveConfig();
			sender.sendMessage(Prefix.prefix() + "Home removed.");
			return true;
		}
		return false;
	}

}
