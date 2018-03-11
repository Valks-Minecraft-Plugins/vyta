package com.vyta.structures;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.vyta.Vyta;

public class Structure {
	File file = new File(Vyta.main.getDataFolder(), "islands.yml");
	FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	
	private String structure;
	private String path;
	private int costFactor;
	
	public Structure(String path, String structure, int costFactor) {
		this.structure = structure;
		this.path = path;
		this.costFactor = costFactor;
	}
	
	public int getCost() {
		int cost = (int) Math.pow(getTier() * costFactor, 1.1);
		return cost;
	}
	
	public int getTier() {
		checkConfig(path);
		return config.getInt(path + ".structures." + structure) + 1;
	}
	
	private void checkConfig(String path) {
		if (!config.isSet(path + ".structures." + structure)) {
			config.set(path + ".structures." + structure, 0);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
