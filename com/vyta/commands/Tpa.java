package com.vyta.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.vyta.Vyta;
import com.vyta.utils.Prefix;

import net.md_5.bungee.api.ChatColor;

public class Tpa implements CommandExecutor {
	Map<String, Long> tpaCooldown = new HashMap<String, Long>();
	Map<String, String> currentRequest = new HashMap<String, String>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (command.getName().equalsIgnoreCase("tpa")) {
			if (!(p == null)) {
				if (p.hasPermission(Vyta.main.getName() + ".overridecooldown")) {

				} else {
					int cooldown = 60;
					if (tpaCooldown.containsKey(p.getName())) {
						long diff = (System.currentTimeMillis() - tpaCooldown.get(sender.getName())) / 1000;
						if (diff < cooldown) {
							p.sendMessage(Prefix.prefix() + "Please wait " + cooldown
									+ " seconds between teleport requests.");
							return true;
						}
					}
				}

				if (args.length > 0) {
					final Player target = Bukkit.getServer().getPlayer(args[0]);
					long keepAlive = 30 * 20;

					if (target == null) {
						sender.sendMessage(Prefix.prefix() + args[0] + " is not online.");
						return true;
					}

					if (target == p) {
						sender.sendMessage(Prefix.prefix() + "You can't teleport to yourself!");
						return true;
					}

					sendRequest(p, target);

					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
						public void run() {
							killRequest(target.getName());
						}
					}, keepAlive);

					tpaCooldown.put(p.getName(), System.currentTimeMillis());
				} else {
					p.sendMessage(Prefix.prefix() + "/tpa <player>");
				}
			} else {
				sender.sendMessage(Prefix.prefix() + "Console can't teleport to players.");
				return true;
			}
			return true;
		}

		if (command.getName().equalsIgnoreCase("tpaccept")) {
			if (!(p == null)) {
				if (currentRequest.containsKey(p.getName())) {

					Player heIsGoingOutOnADate = Bukkit.getServer().getPlayer(currentRequest.get(p.getName()));
					currentRequest.remove(p.getName());

					if (!(heIsGoingOutOnADate == null)) {
						heIsGoingOutOnADate.teleport(p);
						p.sendMessage(Prefix.prefix() + "Teleporting...");
						heIsGoingOutOnADate.sendMessage(ChatColor.GRAY + "Teleporting...");
					} else {
						sender.sendMessage(Prefix.prefix()
								+ "It appears that the person trying to teleport to you doesn't exist anymore. WHOA!");
						return true;
					}
				} else {
					sender.sendMessage(Prefix.prefix()
							+ "It doesn't appear that there are any current tp requests. Maybe it timed out?");
					return true;
				}
			} else {
				sender.sendMessage(Prefix.prefix() + "The console can't accept teleport requests, silly!");
				return true;
			}
			return true;
		}

		if (command.getName().equalsIgnoreCase("tpdeny")) {
			if (!(p == null)) {
				if (currentRequest.containsKey(p.getName())) {
					Player poorRejectedGuy = Bukkit.getServer().getPlayer(currentRequest.get(p.getName()));
					currentRequest.remove(p.getName());

					if (!(poorRejectedGuy == null)) {
						poorRejectedGuy
								.sendMessage(Prefix.prefix() + p.getName() + " rejected your teleport request! :(");
						p.sendMessage(Prefix.prefix() + poorRejectedGuy.getName() + " was rejected!");
						return true;
					}
				} else {
					sender.sendMessage(Prefix.prefix()
							+ "It doesn't appear that there are any current tp requests. Maybe it timed out?");
					return true;
				}
			} else {
				sender.sendMessage(Prefix.prefix() + "The console can't deny teleport requests, silly!");
				return true;
			}
			return true;
		}
		return false;
	}

	public boolean killRequest(String key) {
		if (currentRequest.containsKey(key)) {
			Player loser = Bukkit.getServer().getPlayer(currentRequest.get(key));
			if (!(loser == null)) {
				loser.sendMessage(Prefix.prefix() + "Your teleport request timed out.");
			}

			currentRequest.remove(key);

			return true;
		} else {
			return false;
		}
	}

	public void sendRequest(Player sender, Player recipient) {
		sender.sendMessage(Prefix.prefix() + "Sending a teleport request to " + recipient.getName() + ".");

		String sendtpaccept = "";
		String sendtpdeny = "";

		sendtpaccept = " To accept the teleport request, type " + ChatColor.RED + "/tpaccept" + ChatColor.DARK_GRAY
				+ ".";

		sendtpdeny = " To deny the teleport request, type " + ChatColor.RED + "/tpdeny" + ChatColor.DARK_GRAY + ".";

		recipient.sendMessage(Prefix.prefix() + sender.getName() + ChatColor.DARK_GRAY
				+ " has sent a request to teleport to you." + sendtpaccept + sendtpdeny);
		currentRequest.put(recipient.getName(), sender.getName());
	}
}
