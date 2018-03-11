package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.vyta.configs.PlayerFiles;
import com.vyta.utils.Prefix;

public class Mute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("mute")) {
			if (!sender.isOp()) {
				sender.sendMessage(Prefix.prefix() + "You're not op.");
				return true;
			}
			
			if (args.length < 1) {
				sender.sendMessage(Prefix.prefix() + "Usage: /mute <player>");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(Prefix.prefix() + args[0] + " is not online.");
				return true;
			}
			
			PlayerFiles cm = PlayerFiles.getConfig(target);
			FileConfiguration config = cm.getConfig();
			if (config.getBoolean("muted") == false){
				config.set("muted", true);
				sender.sendMessage(Prefix.prefix() + "Muted " + args[0] + ".");
				target.sendMessage(Prefix.prefix() + "You were muted by " + sender.getName() + ".");
			} else {
				config.set("muted", false);
				sender.sendMessage(Prefix.prefix() + "Unmuted " + args[0] + ".");
				target.sendMessage(Prefix.prefix() + "You were unmuted by " + sender.getName() + ".");
			}
			return true;
		}
		return false;
	}

}
