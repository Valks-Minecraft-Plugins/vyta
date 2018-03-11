package com.vyta.classes;

import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.vyta.Vyta;

public class Archer implements Listener {
	@EventHandler
	private void shoot(EntityShootBowEvent e) {
		e.setCancelled(true);
		Player p = (Player) e.getEntity();
		int diff = 15;
		for (int i = 0; i < 3; i++) {
			double pitch = ((p.getLocation().getPitch() + 90) * Math.PI) / 180;
			double yaw  = ((p.getLocation().getYaw() + ((90 - diff) + (i * diff)))  * Math.PI) / 180;
			double x = Math.sin(pitch) * Math.cos(yaw);
			double y = Math.sin(pitch) * Math.sin(yaw);
			double z = Math.cos(pitch);
			Vector vector = new Vector(x, z, y);
			final Arrow a = p.launchProjectile(Arrow.class, vector.multiply(e.getForce() * 3));
			new BukkitRunnable() {
				@Override
				public void run() {
					if (a.isDead() || a.isOnGround()) {
						cancel();
					}
					a.getWorld().spawnParticle(Particle.DRIP_LAVA, a.getLocation(), 1);
				}
			}.runTaskTimer(Vyta.main, 0, 1);
		}
	}
}
