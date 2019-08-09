package me.dreamzy.ffa.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DefaultNoDebuff{
	public static void giveKit(Player p) {
		p.getInventory().clear();

		ItemStack air = new ItemStack(Material.AIR);

		p.getInventory().setHelmet(air);
		p.getInventory().setChestplate(air);
		p.getInventory().setLeggings(air);
		p.getInventory().setBoots(air);
		for (int i = 0; i < 36; i++) {
			ItemStack item = new ItemStack(Material.POTION, 1, (short) 16421);
			p.getInventory().setItem(i, item);
		}
		ItemStack item = new ItemStack(Material.DIAMOND_HELMET);
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

		ItemStack item1 = new ItemStack(Material.DIAMOND_CHESTPLATE);
		item1.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		item1.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

		ItemStack item2 = new ItemStack(Material.DIAMOND_LEGGINGS);
		item2.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		item2.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

		ItemStack item3 = new ItemStack(Material.DIAMOND_BOOTS);
		item3.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		item3.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 4);
		item3.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

		ItemStack item4 = new ItemStack(Material.DIAMOND_SWORD);
		item4.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
		item4.addUnsafeEnchantment(Enchantment.DURABILITY, 3);

		ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
		ItemStack speed = new ItemStack(Material.POTION, 1, (short) 8226);
		ItemStack bouffe = new ItemStack(Material.GOLDEN_CARROT, 64);

		p.getInventory().setItem(0, item4);
		p.getInventory().setItem(1, enderpearl);
		p.getInventory().setItem(8, bouffe);
		p.getInventory().setItem(2, speed);
		p.getInventory().setItem(26, speed);
		p.getInventory().setItem(35, speed);
		p.getInventory().setItem(17, speed);

		p.getInventory().setHelmet(item);
		p.getInventory().setChestplate(item1);
		p.getInventory().setLeggings(item2);
		p.getInventory().setBoots(item3);
		
		p.setMaximumNoDamageTicks(20);

		p.updateInventory();
	}
}
