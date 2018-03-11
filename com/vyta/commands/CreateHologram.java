package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vyta.utils.Hologram;
import com.vyta.utils.Color;

public class CreateHologram implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("createhologram")) {
			Player p = Bukkit.getPlayer(sender.getName());
			if (!p.isOp()) {
				p.sendMessage("you're not op");
				return true;
			}
			
			Hologram hg = new Hologram(p.getLocation(), Color.message(String.join(" ", args)));
			hg.setVisible(true);
			return true;
		}
		return true;
	}

}
