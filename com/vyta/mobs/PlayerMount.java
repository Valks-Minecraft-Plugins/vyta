package com.vyta.mobs;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMount implements Listener {
	HashMap<UUID, UUID> mounts = new HashMap<UUID, UUID>();
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void giveMount(PlayerJoinEvent e) {
		Location loc = e.getPlayer().getLocation();
		spawnMount(loc, e.getPlayer());
	}
	
	@EventHandler
	public void playerRespawn(PlayerRespawnEvent e) {
		Location loc = e.getPlayer().getLocation();
		spawnMount(loc, e.getPlayer());
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		if (mounts.containsKey(e.getPlayer().getUniqueId())) {
			Bukkit.getEntity(mounts.get(e.getPlayer().getUniqueId())).remove();
			mounts.remove(e.getPlayer().getUniqueId());
		}
	}
	
	@EventHandler
	public void playerDeath(PlayerDeathEvent e) {
		if (mounts.containsKey(e.getEntity().getUniqueId())) {
			Bukkit.getEntity(mounts.get(e.getEntity().getUniqueId())).remove();
			mounts.remove(e.getEntity().getUniqueId());
		}
	}
	
	@EventHandler
	public void entityDeath(EntityDeathEvent e) {
		if (mounts.containsKey(e.getEntity().getUniqueId())) {
			Bukkit.getEntity(mounts.get(e.getEntity().getUniqueId())).remove();
			mounts.remove(e.getEntity().getUniqueId());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void spawnMount(Location loc, LivingEntity rider) {
		World w = loc.getWorld();		
		
		Horse mount = (Horse) w.spawnEntity(loc, EntityType.HORSE);
		mount.setBreed(true);
		mount.setPassenger(rider);
		mount.setColor(Color.WHITE);
		mount.setSilent(true);
		mount.setRemoveWhenFarAway(true);
		mount.setTamed(true);
		mount.setStyle(Style.NONE);
		mount.getInventory().setSaddle(new ItemStack(Material.SADDLE));
		mount.getInventory().setArmor(new ItemStack(Material.IRON_BARDING));
		
		mounts.put(rider.getUniqueId(), mount.getUniqueId());
	}
}
