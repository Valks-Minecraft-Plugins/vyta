package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.vyta.utils.Prefix;

import com.vyta.configs.PlayerFiles;

public class Color implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("color")) {
			if (!sender.isOp()) {
				sender.sendMessage(Prefix.prefix() + "You're not op.");
				return true;
			}
			
			if (args.length == 0) {
				sender.sendMessage(Prefix.prefix() + "Specify at least one argument.");
				return true;
			}

			if (args.length >= 2) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(Prefix.prefix() + args[0] + " is not online.");
					return true;
				}
				if (!args[1].contains("&")) {
					sender.sendMessage(Prefix.prefix() + "Enter a valid color, ex. &2");
					return true;
				}
				PlayerFiles cm = PlayerFiles.getConfig(target);
				FileConfiguration config = cm.getConfig();
				config.set("color", args[1]);
				cm.saveConfig();
				sender.sendMessage(Prefix.prefix() + "Set color " + args[1] + " for " + target.getName());
				target.sendMessage(Prefix.prefix() + "Your color was changed to " + args[1] + " by " + sender.getName());
				return true;
			} else {
				Player target = Bukkit.getServer().getPlayer(sender.getName());
				if (sender instanceof ConsoleCommandSender) {
					sender.sendMessage(Prefix.prefix() + "You can't give the console color chat.");
					return true;
				}
				
				if (!args[0].contains("&")) {
					sender.sendMessage(Prefix.prefix() + "Enter a valid color, ex. &2");
					return true;
				}
				PlayerFiles cm = PlayerFiles.getConfig(target);
				FileConfiguration config = cm.getConfig();
				config.set("color", args[0]);
				cm.saveConfig();
				sender.sendMessage(Prefix.prefix() + "Your color was changed to " + args[0]);
				return true;
			}
		}

		return false;
	}

}
