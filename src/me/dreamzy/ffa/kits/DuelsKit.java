package me.dreamzy.ffa.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamzy.ffa.FPlayer;

public class DuelsKit{
	public static void giveKit(FPlayer p) {
		p.getInventory().clear();

		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta swordm = sword.getItemMeta();
		swordm.setDisplayName("§9Duel players §7(Right-click)");
		swordm.spigot().setUnbreakable(true);
		sword.setItemMeta(swordm);
		
		ItemStack leave = new ItemStack(Material.PAPER);
		ItemMeta leavem = leave.getItemMeta();
		leavem.setDisplayName("§cBack to spawn");
		leave.setItemMeta(leavem);
		
		p.getInventory().setItem(0, leave);
		p.getInventory().setItem(4, sword);
		
		p.getInventory().setHeldItemSlot(4);
		
		p.updatePInventory();
	}
}
