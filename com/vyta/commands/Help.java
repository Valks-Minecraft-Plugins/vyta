package com.vyta.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.vyta.utils.Items;
import com.vyta.utils.Color;

public class Help implements CommandExecutor, Listener {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("help")) {
			Player target = Bukkit.getPlayer(sender.getName());
			target.openInventory(help());
			return true;
		}
		return false;
	}
	
	private Inventory help() {
		Inventory info = Bukkit.createInventory(null, 27, "Help");
		info.setItem(0, Items.invInfo(Material.BOOK, "Discord", new String[] { "Link to discord." }));
		info.setItem(1, Items.invInfo(Material.BOOK, "Tutorial", new String[] { "Helpful guide." }));
		return info;
	}
	
	@EventHandler
	private void registerClicks(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().toLowerCase().equals("help")) {
			e.setCancelled(true);
			int slot = e.getSlot();
			switch(slot) {
			case 0:
				p.sendMessage(Color.message(Color.primary() + "http://discord.gg/FdHzkgy"));
				p.closeInventory();
				break;
			case 1:
				p.sendMessage(Color.message(Color.primary() + "Use " + Color.secondary() + "/island"));
				p.closeInventory();
				break;
			}
		}
	}
}

