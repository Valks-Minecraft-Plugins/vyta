package com.vyta.commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.vyta.Vyta;
import com.vyta.configs.PlayerFiles;
import com.vyta.structures.Structure;
import com.vyta.utils.GenIslands;
import com.vyta.utils.Items;
import com.vyta.utils.Schematic;
import com.vyta.utils.Color;

public class Island implements CommandExecutor, Listener {
	private Inventory help() {
		Inventory help = Bukkit.createInventory(null, 27, "Island");
		help.setItem(0, Items.invInfo(Material.GRASS, "Create", new String[] { "Create an Island" }));
		help.setItem(1, Items.invInfo(Material.IRON_INGOT, "Upgrade", new String[] { "Upgrade Island Structures" }));
		help.setItem(2, Items.invInfo(Material.PAPER, "Stats", new String[] { "View Island Stats" }));
		help.setItem(3, Items.invInfo(Material.COMPASS, "Teleport", new String[] { "Teleport to Island" }));
		help.setItem(4,
				Items.invInfo(Material.GOLD_INGOT, "Deposit", new String[] { "Deposit your Gold into Island Storage" }));
		return help;
	}

	private Inventory structures() {
		Inventory structures = Bukkit.createInventory(null, 27, "Structures");
		structures.setItem(0, Items.invInfo(Material.LOG, "Town Hall", new String[] { "Main Island Structure" }));
		structures.setItem(1, Items.invInfo(Material.CHEST, "Storage", new String[] { "For storing gold." }));
		structures.setItem(2, Items.invInfo(Material.WOOD_PICKAXE, "Mine", new String[] { "For mining gold." }));
		structures.setItem(3, Items.invInfo(Material.WHEAT, "Farm", new String[] { "Food matters. Lives matter. lol" }));
		structures.setItem(4, Items.invInfo(Material.COBBLE_WALL, "Wall", new String[] { "Protect colony." }));
		structures.setItem(5, Items.invInfo(Material.WOOL, "Towers", new String[] { "Look out towers." }));
		return structures;
	}

	@EventHandler
	private void registerClicks(InventoryClickEvent e) {
		final Player p = (Player) e.getWhoClicked();

		File islandConfigFile = new File(Vyta.main.getDataFolder(), "islands.yml");
		FileConfiguration islandConfig = YamlConfiguration.loadConfiguration(islandConfigFile);

		String id = p.getUniqueId().toString();
		String path = id + ".island";

		if (!islandConfig.isSet(path + ".established")) {
			islandConfig.set(path + ".established", false);
		}

		if (e.getInventory().getName().toLowerCase().equals("structures")) {
			int islandGold = islandConfig.getInt(path + ".gold");

			int slot = e.getSlot();
			switch (slot) {
			case 0: // townhall
				if (canBuyStructure(p, path, "townhall", islandGold, islandConfig, islandConfigFile)) {
					upgradeStructure(islandConfigFile, islandConfig, path, p, "townhall", true);
				}
				p.closeInventory();
				break;
			case 1: // storage
				if (canBuyStructure(p, path, "storage", islandGold, islandConfig, islandConfigFile)) {
					int storagetier = islandConfig.getInt(path + ".structures.storage");
					islandConfig.set(path + ".maxgold", (storagetier + 1) * 100);
					try {
						islandConfig.save(islandConfigFile);
					} catch (IOException err) {
						err.printStackTrace();
					}
					upgradeStructure(islandConfigFile, islandConfig, path, p, "storage", true);
				}
				p.closeInventory();
				break;
			case 2: // mine
				if (canBuyStructure(p, path, "mine", islandGold, islandConfig, islandConfigFile)) {
					upgradeStructure(islandConfigFile, islandConfig, path, p, "mine", true);
				}
				p.closeInventory();
				break;
			case 3: //farm
				if (canBuyStructure(p, path, "farm", islandGold, islandConfig, islandConfigFile)) {
					upgradeStructure(islandConfigFile, islandConfig, path, p, "farm", true);
				}
				p.closeInventory();
				break;
			case 4: //wall
				if (canBuyStructure(p, path, "wall", islandGold, islandConfig, islandConfigFile)) {
					upgradeStructure(islandConfigFile, islandConfig, path, p, "wall", true);
				}
				p.closeInventory();
				break;
			case 5: //towers
				if (canBuyStructure(p, path, "towers", islandGold, islandConfig, islandConfigFile)) {
					upgradeStructure(islandConfigFile, islandConfig, path, p, "towers", true);
				}
				p.closeInventory();
				break;
			}
		}

		if (e.getInventory().getName().toLowerCase().equals("island")) {
			int slot = e.getSlot();
			switch (slot) {
			case 0: // CREATE
				int spacing = 128;

				GenIslands gi = new GenIslands(islandConfig.getInt("islands") + 1);
				final int x = gi.x * spacing;
				final int y = 35;
				final int z = gi.z * spacing;

				if (islandConfig.getBoolean(path + ".established") == false) {
					islandConfig.set(path + ".established", true);
					islandConfig.set(path + ".x", x);
					islandConfig.set(path + ".y", y);
					islandConfig.set(path + ".z", z);
					islandConfig.set(path + ".world", p.getWorld().getName());

					islandConfig.set("islands", islandConfig.getInt("islands") + 1);

					islandConfig.set(path + ".gold", 0);
					islandConfig.set(path + ".maxgold", 0);
					try {
						islandConfig.save(islandConfigFile);
					} catch (IOException err) {
						err.printStackTrace();
					}
					Schematic struct = new Schematic(path, islandConfig, p, "island", false);
					struct.pasteSchematic();
					p.sendMessage(Color.message(Color.primary() + "Your island was created. Teleporting you to it in "
							+ Color.secondary() + "5 " + Color.primary() + "seconds.."));
					final World w = Bukkit.getWorld(islandConfig.getString(path + ".world"));
					new BukkitRunnable() {
						@Override
						public void run() {
							p.teleport(new Location(w, x, y, z));
							p.sendTitle("Withered", "", 50, 100, 50);
						}
					}.runTaskLater(Vyta.main, 100);
				} else {
					p.sendMessage(Color.message(Color.primary() + "You've already created your island."));
				}
				p.closeInventory();
				break;
			case 1: // upgrade
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(Color.message(Color.primary() + "You must create your island before you may upgrade it."));
					p.closeInventory();
					return;
				}

				p.openInventory(structures());
				break;
			case 2: // STATS
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(Color.message(Color.primary() + "You must create your island before you may upgrade it."));
					p.closeInventory();
					return;
				}
				if (islandConfig.getInt(path + ".maxgold") == 0) {
					p.sendMessage(Color.message(Color.primary() + "Your island cannot hold any gold yet. Upgrade your storage!"));
				} else {
					p.sendMessage(Color.message(Color.primary() + "Island gold: " + Color.secondary()
							+ islandConfig.getInt(path + ".gold") + "/" + islandConfig.getInt(path + ".maxgold")));
				}
				p.closeInventory();
				break;
			case 3: // tp
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(Color.message(Color.primary() + "You must create your island before you may teleport to it."));
					p.closeInventory();
				} else {
					int xx = islandConfig.getInt(path + ".x");
					int yy = islandConfig.getInt(path + ".y");
					int zz = islandConfig.getInt(path + ".z");
					final World w = Bukkit.getWorld(islandConfig.getString(path + ".world"));
					p.teleport(new Location(w, xx, yy, zz));
					p.sendTitle("Withered", "", 50, 100, 50);
				}
				p.closeInventory();
				break;
			case 4: // deposit into storage
				if (islandConfig.getBoolean(path + ".established") == false) {
					p.sendMessage(
							Color.message(Color.primary() + "You must create your island before you may deposit gold into it."));
					p.closeInventory();
					return;
				}
				PlayerFiles cm = PlayerFiles.getConfig(p);
				FileConfiguration playerConfig = cm.getConfig();

				int maxStorageGold = islandConfig.getInt(path + ".maxgold");
				int storageGold = islandConfig.getInt(path + ".gold");
				int playerGold = playerConfig.getInt("gold");
				int canBePutInto = Math.min(playerGold, maxStorageGold - storageGold);
				playerConfig.set("gold", playerConfig.getInt("gold") - canBePutInto);
				islandConfig.set(path + ".gold", islandConfig.getInt(path + ".gold") + canBePutInto);
				if (canBePutInto == 0) {
					p.sendMessage(Color.message(Color.primary() + "You either have no gold to deposit or cannot deposit anymore because your storage is maxed out."));
				} else {
					p.sendMessage(Color.message(Color.primary() + "Deposited " + canBePutInto + " gold into storage."));
				}
				p.closeInventory();

				cm.saveConfig();
				try {
					islandConfig.save(islandConfigFile);
				} catch (IOException err) {
					err.printStackTrace();
				}
				break;
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("island")) {
			final Player p = Bukkit.getPlayer(sender.getName());
			p.openInventory(help());
			return true;
		}
		return true;
	}

	private boolean canBuyStructure(CommandSender sender, String path, String structure, int islandGold,
			FileConfiguration islandConfig, File islandConfigFile) {
		Structure struct = new Structure(path, structure, 10);
		int structCost = struct.getCost();
		int structTier = struct.getTier();

		int townhalltier = islandConfig.getInt(path + ".structures.townhall");

		if (!structure.equals("townhall")) {
			if (townhalltier < structTier) {
				sender.sendMessage(Color.message(Color.primary() + "You need to upgrade your level " + Color.secondary()
						+ townhalltier + Color.primary() + " Town Hall if you want to upgrade " + Color.secondary() + structure
						+ Color.primary() + " to level " + Color.secondary() + structTier));
				return false;
			}
		}
		
		if (structTier != 1) {
			if (islandGold < structCost) {
				sender.sendMessage(Color.message(Color.primary() + "You need " + Color.secondary() + (structCost - islandGold)
						+ Color.primary() + " more gold to upgrade " + Color.secondary() + structure + Color.primary() + "!"));
				return false;
			} else {
				islandConfig.set(path + ".gold", islandConfig.getInt(path + ".gold") - structCost);
				try {
					islandConfig.save(islandConfigFile);
				} catch (IOException err) {
					err.printStackTrace();
				}
				return true;
			}
		}
		return true;
	}

	public void upgradeStructure(File configFile, FileConfiguration config, String path, CommandSender sender,
			String structure, boolean pasteSlow) {
		int newLvl = config.getInt(path + ".structures." + structure) + 1;
		File schematic = new File(Vyta.main.getDataFolder().toString() + "\\schematics\\",
				structure + newLvl + ".schematic");
		if (!schematic.exists()) {
			sender.sendMessage(Color.message(Color.primary() + "Level " + Color.secondary() + (newLvl - 1) + Color.primary()
					+ " is the max level for " + Color.secondary() + structure + Color.primary() + "!"));
			return;
		}
		sender.sendMessage(Color.message(Color.primary() + "Upgrading " + Color.secondary() + structure + Color.primary()
				+ " to level " + Color.secondary() + newLvl + Color.primary() + "."));
		Schematic struct = new Schematic(path, config, sender, structure + newLvl, pasteSlow);
		struct.pasteSchematic();
		config.set(path + ".structures." + structure, newLvl);
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
