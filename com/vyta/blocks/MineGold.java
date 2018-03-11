package com.vyta.blocks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.vyta.Vyta;
import com.vyta.configs.PlayerFiles;
import com.vyta.utils.Hologram;

import net.md_5.bungee.api.ChatColor;

public class MineGold implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void updateGold(BlockBreakEvent e) {
		Player p = e.getPlayer();
		e.setCancelled(true);
		Player target = Bukkit.getPlayer(p.getName());
		PlayerFiles cm = PlayerFiles.getConfig(target);
		FileConfiguration config = cm.getConfig();
		
		final Block b = e.getBlock();
		if (p.getGameMode().equals(GameMode.CREATIVE)) {
			b.setType(Material.AIR);
		}
		
		Hologram hg = null;
		if (b.getType().equals(Material.GOLD_ORE) && p.getGameMode().equals(GameMode.SURVIVAL)) {
			b.setType(Material.COBBLESTONE);
			config.set("gold", config.getInt("gold") + 1);
			hg = new Hologram(b.getLocation().add(0.5d, 0, 0.5d), ChatColor.YELLOW + "+1 gold");
			hg.setVisible(true);
			
			hg.move();
			
			cm.saveConfig();
			
			final Hologram hg2 = hg;
			
			new BukkitRunnable() {
				@Override
				public void run() {
					if (hg2 != null) {
						hg2.destroy();
					}
				}
			}.runTaskLater(Vyta.main, 40); //2 seconds later
			
			new BukkitRunnable() {
				@Override
				public void run() {
					b.setType(Material.GOLD_ORE);
				}
			}.runTaskLater(Vyta.main, 200); //10 seconds later
		}
		
		if (b.getType().equals(Material.CROPS) && p.getGameMode().equals(GameMode.SURVIVAL)) {
			if (b.getData() == 7) {
				b.setType(Material.SEEDS);
				p.getInventory().addItem(new ItemStack(Material.WHEAT, 2));
				new BukkitRunnable() {
					@Override
					public void run() {
						b.setType(Material.CROPS);
						b.setData((byte) 7);
					}
				}.runTaskLater(Vyta.main, 200); //10 seconds later
			}
		}
	}
}
