package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vyta.utils.Color;
import com.vyta.utils.Prefix;

public class Whisper implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("whisper")) {	
			if (args.length < 2 || args.length == 0) {
				sender.sendMessage(Prefix.prefix() + "Usage: /whisper <player> <message>");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target == null) {
				sender.sendMessage(Prefix.prefix() + args[0] + " is not online.");
				return true;
			}
			
			if (target == sender) {
				sender.sendMessage(Prefix.prefix() + "You can't whisper to yourself.");
				return true;
			}
			
			StringBuilder strBuilder = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				if (i != 0) {
					strBuilder.append(args[i] + " ");
				}
			}
			String str = strBuilder.toString();
			
			sender.sendMessage(Color.message("&a&o" + sender.getName() + " &8&o>> &a&o" + target.getName() + "&8&o: &2&o" + str));
			target.sendMessage(Color.message("&a&o" + sender.getName() + " &8&o>> &a&o" + "YOU" + "&8&o: &2&o" + str));
			return true;
		}
		return false;
	}

}
