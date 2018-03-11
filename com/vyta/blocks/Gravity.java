package com.vyta.blocks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Gravity implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	private void onBlockPlace(BlockPlaceEvent event) {
		Block b = event.getBlockPlaced();
		Material type = event.getBlockPlaced().getType();
		
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

		switch (type) {
		case COBBLESTONE:
		case LADDER:
		case WOOD:
		case WOOD_STEP:
		case PURPUR_SLAB:
		case CHEST:
		case WORKBENCH:
		case FENCE:
		case FENCE_GATE:
		case REDSTONE_TORCH_ON:
		case REDSTONE_TORCH_OFF:
		case WOOD_STAIRS:
		case COBBLESTONE_STAIRS:
		case LOG:
		case STAINED_GLASS:
			b.getWorld().spawnFallingBlock(b.getLocation().add(0.5d, 0, 0.5d), b.getType(), b.getData());
			b.setType(Material.AIR);
			break;
		default:
			break;
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void blockEvent(BlockBreakEvent event) {
		Block b = event.getBlock();
		Material bt = b.getType();
		Location loc = b.getLocation();
		
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		
		switch(bt) {
		case LOG:
		case LOG_2:
			treeGravity(loc);
			break;
		case COAL_ORE:
		case DIAMOND_ORE:
		case EMERALD_ORE:
		case GLOWING_REDSTONE_ORE:
		case GOLD_ORE:
		case IRON_ORE:
		case LAPIS_ORE:
		case REDSTONE_ORE:
			//oreSoftenStone(loc);
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void treeGravity(Location loc) {
		loc.add(0.5d, 0, 0.5d);
		for (int y = 0; y < 50; y++) {
			loc.setY(loc.getY() + 1);
			if (loc.getBlock().getType().equals(Material.LOG)) {
				loc.getBlock().setType(Material.AIR);
				loc.getWorld().spawnFallingBlock(loc, Material.LOG, loc.getBlock().getData());
			}
		}
	}

	@SuppressWarnings({ "deprecation", "unused" })
	private void oreSoftenStone(Location loc) {
		loc.setZ(loc.getBlockZ() - 1);
		loc.setY(loc.getBlockY() - 1);
		loc.setX(loc.getBlockX() - 1);
		for (int x = 0; x < 3; x++) {
			loc.add(0.5d, 0, 0.5d);
			for (int z = 0; z < 3; z++) {
				for (int y = 0; y < 3; y++) {
					loc.setY(loc.getBlockY() - 1);
					if (loc.getBlock().getType().equals(Material.AIR)) {
						loc.setY(loc.getBlockY() + 1);
						if (loc.getBlock().getType().equals(Material.STONE)) {
							loc.getBlock().setType(Material.AIR);
							loc.getWorld().spawnFallingBlock(loc, Material.COBBLESTONE, (byte) 0);
						}
					} else {
						loc.setY(loc.getBlockY() + 1);
					}
					loc.setY(loc.getBlockY() + 1);
				}
				loc.setY(loc.getBlockY() - 3);
				loc.setZ(loc.getBlockZ() + 1);
			}
			loc.setZ(loc.getBlockZ() - 3);
			loc.setX(loc.getBlockX() + 1);
		}
	}
}
