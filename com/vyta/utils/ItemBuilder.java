package com.vyta.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemBuilder {
	private ItemStack is;

	/**
	 * Constructor
	 * 
	 * @param is
	 *          The item stack.
	 */
	public ItemBuilder(ItemStack is) {
		this.is = is;
	}

	/**
	 * Constructor
	 * 
	 * @param material
	 *          The material.
	 */
	public ItemBuilder(Material material) {
		is = new ItemStack(material);
	}

	/**
	 * Constructor
	 * 
	 * @param material
	 *          The Material.
	 * @param amount
	 *          The amount.
	 * @param data
	 *          Damage values or data values.
	 */
	public ItemBuilder(Material material, int amount, byte data) {
		is = new ItemStack(material, amount, data);
	}

	/**
	 * Set the dye color of leather armor.
	 * @param color The desired color you want.
	 * @return The new colored leather armor.
	 */
	public ItemBuilder setLeatherColor(Color color) {
		try {
			LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
			im.setColor(color);
			is.setItemMeta(im);
			return this;
		} catch (Exception err) {
			Bukkit.getLogger().info(err.toString());
		}
		return this;
		
	}

	/**
	 * Set the display name.
	 * 
	 * @param name
	 *          The name you want it to be.
	 * @return The name of the item.
	 */
	public ItemBuilder setName(String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		is.setItemMeta(im);
		return this;
	}
	
	/**
	 * 
	 * @param lore
	 * @return
	 */
	public ItemBuilder setLore(String lore) {
		ItemMeta im = is.getItemMeta();
		List<String> list = new ArrayList<>();
		list.add(ChatColor.translateAlternateColorCodes('&', lore));
		im.setLore(list);
		is.setItemMeta(im);
		return this;
	}
	
	public ItemBuilder setLore(String[] lore) {
		ItemMeta im = is.getItemMeta();
		List<String> list = new ArrayList<>();
		for (int i = 0; i < lore.length; i++) {
			list.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
		}
		im.setLore(list);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add a single line of lore.
	 * 
	 * @param lore
	 *          The lore you want to add.
	 * @return The lore that was added.
	 */
	public ItemBuilder addLore(String lore) {
		ItemMeta im = is.getItemMeta();
		if (im.getLore() == null) return this;
		List<String> list = new ArrayList<>(im.getLore());
		list.add(ChatColor.translateAlternateColorCodes('&', lore));
		im.setLore(list);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add lore at a specified index.
	 * 
	 * @param lore
	 *          The lore you want to add.
	 * @param index
	 *          The index you want to put it in.
	 * @return The lore.
	 */
	public ItemBuilder addLore(String lore, int index) {
		ItemMeta im = is.getItemMeta();
		if (im.getLore() == null) return this;
		List<String> list = new ArrayList<>(im.getLore());
		list.set(index, ChatColor.translateAlternateColorCodes('&', lore));
		im.setLore(list);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Add a string object to the lore.
	 * 
	 * @param lore
	 *          The lore you want to add.
	 * @return The lore list.
	 */
	public ItemBuilder addLore(String[] lore) {
		ItemMeta im = is.getItemMeta();
		if (im.getLore() == null) return this;
		List<String> list = new ArrayList<>(im.getLore());
		for (int i = 0; i < lore.length; i++) {
			list.add(ChatColor.translateAlternateColorCodes('&', lore[i]));
		}
		im.setLore(list);
		is.setItemMeta(im);
		return this;
	}

	/**
	 * Remove a specified lore index.
	 * 
	 * @param index
	 *          The index you want to remove.
	 * @return The lore list.
	 */
	public ItemBuilder removeLoreLine(int index) {
		ItemMeta im = is.getItemMeta();
		if (im.getLore() == null) return this;
		List<String> list = new ArrayList<>(im.getLore());
		if (list.get(index) != null && index >= 0 && index <= list.size()) {
			list.remove(index);
			im.setLore(list);
			is.setItemMeta(im);
			return this;
		}
		return this;
	}

	/**
	 * Set the durability of the item.
	 * 
	 * @param dur
	 *          Durability of the item you want.
	 * @return The new durability.
	 */
	public ItemBuilder setDurability(short dur) {
		is.setDurability(dur);
		return this;
	}

	/**
	 * Make the durability infinite.
	 * 
	 * @return The new durability.
	 */
	public ItemBuilder setInfiniteDurability() {
		is.setDurability(Short.MAX_VALUE);
		return this;
	}

	/**
	 * Add a new enchantment to the item.
	 * 
	 * @param enchantment
	 *          The enchantment you want to add.
	 * @param level
	 *          The level you want.
	 * @return The new enchanted item.
	 */
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		is.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	/**
	 * Remove a specified enchantment.
	 * 
	 * @param enchantment
	 *          The enchantment you want to remove.
	 * @return The new item with the removed enchantment.
	 */
	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		is.removeEnchantment(enchantment);
		return this;
	}

	/**
	 * Set the skull owner of the item (only works for SKULL_ITEM material with data
	 * byte 3
	 * 
	 * @param owner
	 *          The preferred owner.
	 * @return The new skull item updated with the new owner.
	 */
	@SuppressWarnings("deprecation")
	public ItemBuilder setSkullOwner(String owner) {
		try {
			SkullMeta im = (SkullMeta) is.getItemMeta();
			im.setOwner(owner);
			is.setItemMeta(im);
		} catch (Exception err) {
			Bukkit.getLogger().info(err.toString());
		}
		return this;
	}

	/**
	 * Clone a item build.
	 */
	public ItemBuilder clone() {
		is.clone();
		return new ItemBuilder(is);
	}

	/**
	 * Set the stack of the item to a new stack amount.
	 * 
	 * @param amount
	 *          Item stack amount.
	 * @return The new stack item amount.
	 */
	public ItemBuilder setAmount(int amount) {
		is.setAmount(amount);
		return this;
	}

	/**
	 * Convert the item build to a item stack.
	 * 
	 * @return The item stack.
	 */
	public ItemStack toItemStack() {
		return is;
	}
}
