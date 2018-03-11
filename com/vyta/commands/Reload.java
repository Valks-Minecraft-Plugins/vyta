package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vyta.Vyta;
import com.vyta.configs.PlayerFiles;
import com.vyta.utils.Prefix;

public class Reload implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("reload")) {
			if (!sender.isOp()) {
				sender.sendMessage(Prefix.prefix() + "You're not op.");
				return true;
			}
			
			if (args.length == 0) {
				sender.sendMessage(Prefix.prefix() + "Usage: /vyta reload <main | player>");
				return true;
			}
			
			if (args[0].toLowerCase().equals("main")) {
				Vyta.main.reloadConfig();
				Vyta.main.saveConfig();
				sender.sendMessage(Prefix.prefix() + "Reloaded main config.");
				return true;
			} else {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(Prefix.prefix() + args[0] + "is not online.");
					return true;
				}
				
				PlayerFiles cm = PlayerFiles.getConfig(target);
				cm.reload();
				cm.saveConfig();
				sender.sendMessage(Prefix.prefix() + "Reloaded config for " + args[0] + ".");
			}
			return true;
		}
		return false;
	}

}
