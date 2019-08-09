package me.dreamzy.ffa.gui;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.commands.mods.ModCommand;
import me.dreamzy.ffa.kits.DefaultNoDebuff;
import me.dreamzy.ffa.kits.editkit.EditKit;

public class PlayGui implements Listener {

	private static PlayGui instancePlayGui;

	public PlayGui() {
		instancePlayGui = this;
	}

	public static PlayGui get() {
		return instancePlayGui;
	}

	public void openGui(FPlayer player) {
		Inventory inv = Bukkit.createInventory(null, 9 * 1, "§9Choose your kit!");

		ItemStack defaultkit = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta defaultkitm = defaultkit.getItemMeta();
		defaultkitm.setDisplayName("§eDefault NoDebuff");
		defaultkit.setItemMeta(defaultkitm);

		ItemStack customkit = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta customkitm = customkit.getItemMeta();
		customkitm.setDisplayName("§eCustom NoDebuff");
		customkit.setItemMeta(customkitm);

		File file = new File(FFA.getInstance().getDataFolder() + "/database/kits/",
				player.getUniqueId().toString() + "-nodebuff.yml");
		if (file.exists()) {
			inv.setItem(0, customkit);
			inv.setItem(8, defaultkit);
		} else {
			inv.setItem(0, defaultkit);
		}
		player.openInventory(inv);
	}

	@EventHandler
	public void onKitsClick(InventoryClickEvent e) {
		if (e.getInventory().getName().equalsIgnoreCase("§9Choose your kit!")) {
			FPlayer player = FFA.getInstance().players.get(e.getWhoClicked().getUniqueId());
			if (player.getPing() > 250) {
				player.sendMessage("§cYour ping is too high to join ffa arena. (" + player.getPing() + "ms / 250ms)");
				player.closeInventory();
				return;
			}
			if (ModCommand.inmod.contains(player)) {
				player.sendMessage("§cYou can't join the arena while are in staff mode.");
				player.closeInventory();
				return;
			}
			e.setCancelled(true);
			if ((e.getWhoClicked() instanceof Player)) {
				if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() != null)
						&& (e.getCurrentItem().getType() != Material.AIR) && (e.getCurrentItem().getItemMeta() != null)
						&& (e.getCurrentItem().getItemMeta().getDisplayName() != null)) {
					String kit = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§e", "");
					if (kit.equals("Default NoDebuff") || kit.contains("Custom NoDebuff")) {
						FFA.getInstance().hideModeratorsForObserver((Player) e.getWhoClicked());
						for (Player players : FFA.getInstance().getOnlinePlayers()){
							FFA.getInstance().showPlayerForObserver((Player) e.getWhoClicked(), players);
						}
						player.closeInventory();
						FFA.getInstance().inFFA.add(player);
						player.teleport(FFA.getInstance().ffaSpawns.get(FFA.getInstance().getRandomNumber(1, 4)));
						if (kit.contains("Custom")) {
							try {
								EditKit.get().getKit(player, "nodebuff");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} else {
							DefaultNoDebuff.giveKit(player);
						}

						player.sendMessage(new String[] { "","§6You have been teleported to free for all arena. Please §4do not team §6or you will be permanently banned." });
						player.sendMessage("§eEquipped you " + kit + " Kit§e.");
					}
				}
			}
		}
	}
}