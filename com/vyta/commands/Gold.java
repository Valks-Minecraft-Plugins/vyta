package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.vyta.configs.PlayerFiles;
import com.vyta.utils.Color;

public class Gold implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("Console cannot use this command.");
		}
		
		Player p = Bukkit.getPlayer(sender.getName());
		PlayerFiles cm = PlayerFiles.getConfig(p);
		FileConfiguration config = cm.getConfig();
		
		sender.sendMessage(Color.message(Color.primary() + "Gold: " + Color.secondary() + config.get("gold").toString()));
		return true;
	}

}
