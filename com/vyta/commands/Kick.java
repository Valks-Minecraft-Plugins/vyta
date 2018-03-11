package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vyta.utils.Prefix;

public class Kick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kick")) {
			Player p = Bukkit.getPlayer(sender.getName());
			if (!p.isOp()) {
				p.sendMessage("You're not op.");
				return true;
			}
			
			if (args.length < 1) {
				p.sendMessage("Specify player name to kick.");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0].toLowerCase());
			if (target == null) {
				sender.sendMessage(Prefix.prefix() + args[0] + " is not online.");
				return true;
			}
			
			target.kickPlayer("Kicked");
			
			return true;
		}
		// TODO Auto-generated method stub
		return true;
	}

}
