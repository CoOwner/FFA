package me.dreamzy.ffa.utils;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamzy.ffa.FFA;

public class ModUtils {

	private Player player;
	private ArrayList<Player> staffMode = new ArrayList<>();

	public ModUtils(Player p) {
		p = player;
	}

	public void toggleStaffMode() {
		if (staffMode.contains(player)) {
			staffMode.remove(player);
			showPlayerForOthers();
			player.teleport(FFA.getInstance().getSpawn());
		} else {
			staffMode.add(player);
			hidePlayerForOthers();
			modInitialisations();
			player.teleport(FFA.getInstance().getSpawn());
		}
		player.sendMessage("§6Your staff mode has been " + (staffMode.contains(player) ? "§aenabled" : "§cdisabled"));
	}

	public void hidePlayerForOthers() {
		for (Player players : FFA.getInstance().getOnlinePlayers()) {
			players.hidePlayer(player);
		}
	}

	public void showPlayerForOthers() {
		for (Player players : FFA.getInstance().getOnlinePlayers()) {
			players.showPlayer(player);
		}
	}

	public void modInitialisations() {
		player.setGameMode(GameMode.CREATIVE);
		getInventory();
	}

	public void getInventory() {
		clearInventory();

		ItemStack item = new ItemStack(Material.WOOD_SWORD);
		ItemMeta itemm = item.getItemMeta();
		itemm.setDisplayName("§cKnockback");
		itemm.spigot().setUnbreakable(true);
		item.setItemMeta(itemm);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);

		ItemStack item2 = new ItemStack(Material.IRON_FENCE);
		ItemMeta itemm2 = item2.getItemMeta();
		itemm2.setDisplayName("§dFreeze");
		item2.setItemMeta(itemm2);

		ItemStack item3 = new ItemStack(Material.BOOK);
		ItemMeta itemm3 = item3.getItemMeta();
		itemm3.setDisplayName("§6Inspection");
		item3.setItemMeta(itemm3);

		ItemStack item5 = new ItemStack(Material.NETHER_STAR);
		ItemMeta itemm5 = item5.getItemMeta();
		itemm5.setDisplayName("§3Random TP");
		item5.setItemMeta(itemm5);

		@SuppressWarnings("deprecation")
		ItemStack item6 = new ItemStack(351, 1, (short) 14);
		ItemMeta itemm6 = item6.getItemMeta();
		itemm6.setDisplayName(("§cQuit Mod"));
		item6.setItemMeta(itemm6);

		player.getInventory().setItem(4, item);
		player.getInventory().setItem(0, item3);
		player.getInventory().setItem(1, item2);
		player.getInventory().setItem(7, item5);
		player.getInventory().setItem(8, item6);

		player.updateInventory();
	}

	public void clearInventory() {
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[] { null, null, null, null });
	}

}
