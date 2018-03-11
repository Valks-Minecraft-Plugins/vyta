package com.vyta.utils;

import net.md_5.bungee.api.ChatColor;

public class Color {
	public static String message(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static String primary() {
		return "&7";
	}
	
	public static String secondary() {
		return "&f";
	}
}
