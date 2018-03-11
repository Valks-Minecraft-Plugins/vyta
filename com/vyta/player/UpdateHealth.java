package com.vyta.player;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateHealth implements Listener {
	/*
	 * Update the player health on join.
	 */
	@EventHandler(priority = EventPriority.LOW)
	private void onPlayerJoin(PlayerJoinEvent e) {
		updateHealth(e.getPlayer());
	}
	
	/*
	 * Give the player xp when they break a block.
	 */
	@EventHandler(priority = EventPriority.LOW)
	private void breakBlock(BlockBreakEvent e) {
		if (!e.getBlock().getType().equals(Material.LONG_GRASS)) {
			Player p = e.getPlayer();
			p.giveExp(1);
			updateHealth(p);
		}
	}
	
	/*
	 * Give the player xp when they kill a entity.
	 */
	@EventHandler(priority = EventPriority.LOW)
	private void entityDeath(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player p = (Player) e.getEntity().getKiller();
			p.giveExp(10);
		}
	}
	
	/*
	 * Give the player xp when they hit a entity.
	 */
	@EventHandler(priority = EventPriority.LOW)
	private void entityDamaged(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			if (event.getDamager() instanceof Player) {
				Player p = (Player) event.getDamager();
				p.giveExp(2);
				updateHealth(p);
			}
		}
	}
	
	/*
	 * Update player health on xp change.
	 */
	@EventHandler(priority = EventPriority.LOW)
	private void expChange(PlayerExpChangeEvent event) {
		Player player = event.getPlayer();
		updateHealth(player);
	}
	
	/*
	 * Only update health when xp level is an even number
	 * so player will not get half hearts on level up.
	 */
	@SuppressWarnings("deprecation")
	public void updateHealth(Player p) {
		if ((p.getLevel() & 1) == 0) {
			p.setMaxHealth(6 + p.getLevel());
		}
	}
}
