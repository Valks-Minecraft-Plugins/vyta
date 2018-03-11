package com.vyta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.vyta.blocks.MineGold;
import com.vyta.blocks.Gravity;
import com.vyta.chat.Broadcasts;
import com.vyta.chat.Chat;
import com.vyta.classes.Archer;
import com.vyta.commands.Island;
import com.vyta.commands.Kick;
import com.vyta.commands.Color;
import com.vyta.commands.CreateHologram;
import com.vyta.commands.Gold;
import com.vyta.commands.Help;
import com.vyta.commands.Home;
import com.vyta.commands.Mute;
import com.vyta.commands.Rank;
import com.vyta.commands.Reload;
import com.vyta.commands.Test;
import com.vyta.commands.Tpa;
import com.vyta.commands.Version;
import com.vyta.commands.Whisper;
import com.vyta.generator.WorldGenerator;
import com.vyta.mobs.BlockSpawns;
//import com.vyta.mobs.PlayerMount;
import com.vyta.particles.Particles;
import com.vyta.player.UpdateHealth;
import com.vyta.player.UpdateLevel;
import com.vyta.playerjoin.GiveGear;
import com.vyta.configs.LoadPlayerFiles;

public class Vyta extends JavaPlugin {
	File mainConfigFile = new File(getDataFolder(), "config.yml");
	FileConfiguration mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);

	File msgConfigFile = new File(getDataFolder(), "messages.yml");
	FileConfiguration msgConfig = YamlConfiguration.loadConfiguration(msgConfigFile);

	File islandConfigFile = new File(getDataFolder(), "islands.yml");
	FileConfiguration islandConfig = YamlConfiguration.loadConfiguration(islandConfigFile);

	public static Vyta main;

	@Override
	public void onEnable() {
		main = this;
		registerEvents(getServer().getPluginManager());
		registerCommands();
		setConfigMainValues();
		setConfigMessagesValues();
		setConfigIslandValues();
		saveConfig(mainConfigFile, mainConfig);
		saveConfig(msgConfigFile, msgConfig);
		saveConfig(islandConfigFile, islandConfig);
		
		pluginEnabled(getServer().getConsoleSender(), getDescription());
	}

	@Override
	public void onDisable() {
		pluginDisabled(getServer().getConsoleSender(), getDescription());
	}

	private void saveConfig(File file, FileConfiguration config) {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void pluginDisabled(ConsoleCommandSender console, PluginDescriptionFile p) {
		console.sendMessage(ChatColor.DARK_AQUA + p.getName() + " v" + p.getVersion() + " has been disabled.");
		getServer().broadcastMessage(p.getName() + " is reloading.. please wait..");
	}

	private void pluginEnabled(ConsoleCommandSender console, PluginDescriptionFile p) {
		console.sendMessage(ChatColor.AQUA + p.getName() + " v" + p.getVersion() + " has been enabled.");
	}

	private void registerEvents(PluginManager pm) {
		pm.registerEvents(new Chat(), this);
		pm.registerEvents(new Broadcasts(), this);
		pm.registerEvents(new LoadPlayerFiles(), this);
		// pm.registerEvents(new PlayerMount(), this);
		pm.registerEvents(new Particles(), this);
		pm.registerEvents(new UpdateHealth(), this);
		pm.registerEvents(new UpdateLevel(), this);
		pm.registerEvents(new Gravity(), this);
		pm.registerEvents(new MineGold(), this);
		pm.registerEvents(new GiveGear(), this);
		pm.registerEvents(new Help(), this);
		pm.registerEvents(new Island(), this);
		pm.registerEvents(new Test(), this);
		pm.registerEvents(new Archer(), this);
		pm.registerEvents(new BlockSpawns(), this);
	}

	private void registerCommands() {
		getCommand("island").setExecutor(new Island());
		getCommand("version").setExecutor(new Version());
		getCommand("reload").setExecutor(new Reload());
		getCommand("whisper").setExecutor(new Whisper());
		getCommand("home").setExecutor(new Home());
		getCommand("sethome").setExecutor(new Home());
		getCommand("delhome").setExecutor(new Home());
		getCommand("color").setExecutor(new Color());
		getCommand("tpa").setExecutor(new Tpa());
		getCommand("tpaccept").setExecutor(new Tpa());
		getCommand("tpdeny").setExecutor(new Tpa());
		getCommand("rank").setExecutor(new Rank());
		getCommand("mute").setExecutor(new Mute());
		getCommand("gold").setExecutor(new Gold());
		getCommand("test").setExecutor(new Test());
		getCommand("help").setExecutor(new Help());
		getCommand("kick").setExecutor(new Kick());
		getCommand("createhologram").setExecutor(new CreateHologram());
	}

	private void setConfigIslandValues() {
		if (!islandConfig.isSet("islands")) {
			islandConfig.set("islands", 0);
		}
		try {
			for (int i = 0; i < Bukkit.getServer().getOfflinePlayers().length; i++) {
				String id = Bukkit.getOfflinePlayers()[i].getUniqueId().toString();
				if (!islandConfig.isSet(id + ".user")) {
					islandConfig.set(id + ".user", Bukkit.getOfflinePlayers()[i].getName());
				}
				if (!islandConfig.isSet(id + ".island.established")) {
					islandConfig.set(id + ".island.established", false);
				}
			}
		} catch (Exception e) {

		}
	}
	
	private void setConfigMainValues() {

	}

	private void setConfigMessagesValues() {
		if (!msgConfig.isSet("messages.chat")) {
			msgConfig.set("messages.chat", "&8%player%&8: &7#color#%message%");
		}

		if (!msgConfig.isSet("messages.blockedwords")) {
			List<String> msgs = Arrays.asList("fuck", "nigger", "cunt", "cunts", "bitch", "whore", "slut", "motherfucker",
					"fucker", "blowjob", "dick", "kunt", "faggot", "niglet", "prick");
			msgConfig.set("messages.blockedwords", msgs);
		}
		if (!msgConfig.isSet("messages.blockedreplace")) {
			msgConfig.set("messages.blockedreplace", "meeowww");
		}

		if (!msgConfig.isSet("messages.ranks")) {
			List<String> msgs = Arrays.asList("Mod: &8{&2Moderator&8} ", "Admin: &8{&cAdmin&8} ", "Owner: &8{&fOwner&8} ");
			msgConfig.set("messages.ranks", msgs);
		}
		if (!msgConfig.isSet("messages.defaultrank")) {
			msgConfig.set("messages.defaultrank", "&8{&7User&8} ");
		}

		if (!msgConfig.isSet("messages.join")) {
			List<String> msgs = Arrays.asList("&8{&b+&8} &7A wild &3%player% &7appeared!",
					"&8{&b+&8} &7&3%player% &7has been summoned!", "&8{&b+&8} &7&3%player% &7came out of no where!",
					"&8{&b+&8} &7A wild &3%player% &7appeared! Say hi!");
			msgConfig.set("messages.join", msgs);
		}

		if (!msgConfig.isSet("messages.welcome")) {
			List<String> msgs = Arrays.asList("&8{&b+&8} &dIts &3%player% &dfirst time here! Welcome!!",
					"&8{&b+&8} &d&3%player% &dfell from the sky! Its their first time here! Welcome!",
					"&8{&b+&8} &d&3%player% &ddiscovered catlandia! +69XP Its their first time here! Welcome!");
			msgConfig.set("messages.welcome", msgs);
		}

		if (!msgConfig.isSet("messages.leave")) {
			List<String> msgs = Arrays.asList("&8{&b-&8} &3%player% &7vanished into thin air",
					"&8{&b-&8} &3%player% &7dissapeared", "&8{&b-&8} &3%player% &7thought cats were too much for them",
					"&8{&b-&8} &3%player% &7left their cat behind",
					"&8{&b-&8} &3%player% &7learned that kittens are really tough", "&8{&b-&8} &3%player% &7divorced their cats",
					"&8{&b-&8} &3%player% &7ran away from the cats");
			msgConfig.set("messages.leave", msgs);
		}

		if (!msgConfig.isSet("messages.kick")) {
			List<String> msgs = Arrays.asList("&8{&b-&8} &3%player% &7was kicked");
			msgConfig.set("messages.kick", msgs);
		}

		if (!msgConfig.isSet("messages.death")) {
			List<String> msgs = Arrays.asList("&3%player% &7got devoured by a kitten", "&3%player% &7had a date with a cat",
					"&3%player% &7thought they were a cat", "&3%player% &7tried to tame a kitten",
					"&3%player%&7's limbs were smashed",
					"&3%player% &7learned to keep their distance from kittens and cats alike", "&3%player% &7fed a cat",
					"&3%player% &7got a cat as a pet", "&3%player% &7turned into cat food", "&3%player% &7thought cats could fly",
					"&3%player% &7turned into a cat", "&3%player% &7got married to a cat", "&3%player% &7got candy from a cat",
					"&3%player% &7became a kittens slave", "&3%player% &7made a kitten really mad.",
					"&3%player% &7was scratched to death by cute kittens.");
			msgConfig.set("messages.death", msgs);
		}
	}

	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new WorldGenerator();
	}
}