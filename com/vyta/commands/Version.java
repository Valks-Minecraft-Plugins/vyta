package com.vyta.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import com.vyta.Vyta;
import com.vyta.utils.Prefix;

public class Version implements CommandExecutor{
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("version")) {
			PluginDescriptionFile p = Vyta.main.getDescription();
			sender.sendMessage(Prefix.prefix() + p.getName() + " v" + p.getVersion() + " by " + p.getAuthors());
			return true;
		}
		return false;
	}
}