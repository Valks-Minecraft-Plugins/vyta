package com.vyta.playerjoin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.vyta.utils.ItemBuilder;

public class GiveGear implements Listener {
	@EventHandler
	private void giveGear(PlayerJoinEvent e) {
		if (!e.getPlayer().hasPlayedBefore()) {
			ItemBuilder ib = new ItemBuilder(Material.IRON_PICKAXE);
			ib.addEnchantment(Enchantment.DURABILITY, 10);
			ib.setLore("&2Good for mining gold ore.");
			ib.setName("&aSturdy Pick");
			e.getPlayer().getInventory().addItem(ib.toItemStack());
		}
	}
}
