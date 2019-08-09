package me.dreamzy.ffa.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.commands.mods.FreezeCommand;

public class FreezeGui implements Listener {

	private static ArrayList<String> lore = new ArrayList<>();

	public static void openGui(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, "§3You have been frozen");

		ItemStack freeze = new ItemStack(Material.BOOK);
		ItemMeta freezem = freeze.getItemMeta();
		freezem.setDisplayName("§3You have been frozen");
		lore.add("");
		lore.add("§3Please download §fTeamSpeak §3from");
		lore.add("§fwww.teamspeak.com/downloads.html");
		lore.add("§3and join §fts.vexus.rip");
		lore.add("");
		lore.add("§3You have §f2 minutes §3to join teamspeak.");
		lore.add("");
		lore.add("§3DO NOT REMOVE ANY ITEMS FROM RECYCLE BIN");
		lore.add("§3OR LEAVE THE SERVER. DOING THAT WILL");
		lore.add("§3RESULT IN PERMANENT BAN FROM THE NETWORK.");
		freezem.setLore(lore);
		lore.clear();
		freeze.setItemMeta(freezem);
		
		
		

		inv.setItem(0, freeze);
		inv.setItem(1, freeze);
		inv.setItem(2, freeze);
		inv.setItem(3, freeze);
		inv.setItem(4, freeze);
		inv.setItem(5, freeze);
		inv.setItem(6, freeze);
		inv.setItem(7, freeze);
		inv.setItem(8, freeze);

		p.closeInventory();

		if (FreezeCommand.getArray().contains(p)) {
			if (p.getOpenInventory() != inv) {
				p.openInventory(inv);
			}
		}
	}

	@EventHandler
	public void onKitsClick(InventoryClickEvent e) {
		if (e.getInventory().getName().equalsIgnoreCase("§3You have been frozen")) {
			e.setCancelled(true);
		}
		
	}
	

	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		final Player player = (Player) e.getPlayer();
		if (FreezeCommand.getArray().contains(player)) {
			new BukkitRunnable() {
				public void run() {
					openGui(player);
				}
			}.runTaskLater(FFA.getInstance(), 1L);
		}
	}
}