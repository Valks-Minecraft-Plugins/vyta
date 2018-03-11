package com.vyta.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.vyta.Vyta;

import net.minecraft.server.v1_12_R1.EntityInsentient;

public class Test implements CommandExecutor, Listener {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("test")) {
			final Player p = Bukkit.getPlayer(sender.getName());
			//LivingEntity cow = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), EntityType.COW);
			//followPlayer(p, cow, 1.75);
			
			//CustomZombie zombie = new CustomZombie(((CraftWorld) p.getWorld()).getHandle());
			//zombie.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
			//zombie.world.addEntity(zombie, SpawnReason.CUSTOM);
			//p.chat("Hello world!")
			
			final Location loc = p.getLocation();
			
			Vector vec = loc.getDirection();
			
			final Arrow a = p.launchProjectile(Arrow.class, vec);
			
			
			
			
			new BukkitRunnable() {

				@Override
				public void run() {
					double radius = 30;
					List<Entity> near = loc.getWorld().getEntities();
					Location target = null;
					for(Entity e : near) {
					    if(e.getLocation().distance(loc) <= radius) {
					    	if (!(e instanceof Player)) {
					    		target = e.getLocation();
						    	break;
					    	}
					    }
					}
					if (target != null) {
						Vector to = new Vector (target.getX(), target.getY(), target.getZ());
						Vector from = new Vector(loc.getX(), loc.getY(), loc.getZ());
						Vector calc = to.subtract(from);
						a.setVelocity(calc.multiply(0.1));
					}
					
				}
				
			}.runTaskTimer(Vyta.main, 0, 1);
			
			return true;
		}
		return true;
	}


	public void followPlayer(Player player, LivingEntity entity, double d) {
		final LivingEntity e = entity;
		final Player p = player;
		final float f = (float) d;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Vyta.main, new Runnable() {
			@Override
			public void run() {
				((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation().a(p.getLocation().getX(),
						p.getLocation().getY(), p.getLocation().getZ(), f);
			}
		}, 0 * 20, 2 * 20);
	}
}
