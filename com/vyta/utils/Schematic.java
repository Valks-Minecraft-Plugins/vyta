package com.vyta.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.vyta.Vyta;

public class Schematic {

	String path;
	FileConfiguration config;
	final CommandSender sender;
	String fileName;
	boolean pasteSlow;

	public Schematic(String path, FileConfiguration config, final CommandSender sender, String fileName,
			boolean pasteSlow) {
		this.path = path;
		this.config = config;
		this.sender = sender;
		this.fileName = fileName;
		this.pasteSlow = pasteSlow;
	}

	public void pasteSchematic() {
		final ArrayList<Location> list = new ArrayList<Location>();
		NBTStorageFile schematic = new NBTStorageFile(Vyta.main.getDataFolder().toString() + "\\schematics\\", fileName);
		final byte[] blocks = schematic.getByteArray("Blocks");
		final byte[] data = schematic.getByteArray("Data");

		short offsetX = schematic.getShort("WEOffsetX");
		short offsetY = schematic.getShort("WEOffsetY");
		short offsetZ = schematic.getShort("WEOffsetZ");

		short width = schematic.getShort("Width");
		short height = schematic.getShort("Height");
		short length = schematic.getShort("Length");

		final World w = Bukkit.getWorld(config.getString(path + ".world"));
		Location configLoc = new Location(w, config.getInt(path + ".x"), config.getInt(path + ".y"),
				config.getInt(path + ".z"));
		Location loc = configLoc.add(offsetX, offsetY, offsetZ);
		for (int y = 0; y < height; y++) {
			for (int z = 0; z < length; z++) {
				for (int x = 0; x < width; x++) {
					Location newLoc = loc.clone();
					newLoc.add(x, y, z);
					list.add(newLoc);
				}
			}
		}

		if (pasteSlow) {
			pasteSlow(list, blocks, data, w);
		} else {
			pasteFast(list, blocks, data, w, true);
		}
	}

	@SuppressWarnings("deprecation")
	private void pasteFast(final ArrayList<Location> list, final byte[] blocks, final byte[] data, final World w,
			boolean ignoreAir) {
		for (int i = 0; i < list.size(); i++) {
			Block b = w.getBlockAt(list.get(i));
			if (ignoreAir) {
				if (blocks[i] != 0) {
					if (blocks[i] < 0) {
						b.setTypeId(blocks[i] + 256);
						b.setData(data[i]);
					} else {
						b.setTypeId(blocks[i]);
						b.setData(data[i]);
					}
				}
			} else {
				if (blocks[i] < 0) {
					b.setTypeId(blocks[i] + 256);
					b.setData(data[i]);
				} else {
					b.setTypeId(blocks[i]);
					b.setData(data[i]);
				}
			}

		}
	}

	private void pasteSlow(final ArrayList<Location> list, final byte[] blocks, final byte[] data, final World w) {
		new BukkitRunnable() {
			int counter = 0;

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (counter >= list.size()) {
					cancel();
				} else {
					Block b = w.getBlockAt(list.get(counter));
					b.setTypeId(35);
					b.setData((byte) 7);
					counter++;
				}
			}
		}.runTaskTimer(Vyta.main, 0, 1);

		new BukkitRunnable() {
			int counter = 0;

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (counter >= list.size()) {
					cancel();
				} else {
					Block b = w.getBlockAt(list.get(counter));
					b.setTypeId(0);
					counter++;
				}
			}
		}.runTaskTimer(Vyta.main, 5, 1);

		new BukkitRunnable() {
			int counter = 0;

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (counter >= list.size()) {
					cancel();
				} else {
					Block b = w.getBlockAt(list.get(counter));
					if (blocks[counter] < 0) {
						b.setTypeId(blocks[counter] + 256);
						b.setData(data[counter]);
					} else {
						b.setTypeId(blocks[counter]);
						b.setData(data[counter]);
					}
					counter++;
				}
			}
		}.runTaskTimer(Vyta.main, 6, 1);
	}
}
