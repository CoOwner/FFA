package me.dreamzy.ffa.kits.editkit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamzy.ffa.FFA;

public class EditKit {
	private static EditKit instance;
	private static Location editKitSpawn;
	
	public EditKit(){
		instance = this;
		editKitSpawn = new Location(Bukkit.getWorld("overworld"), 0.5,101.25,-47.5,90,0);
	}
	
	public static EditKit get(){
		return instance;
	}
	
	@SuppressWarnings("rawtypes")
	public ArrayList getInEditKit(){
		return FFA.getInstance().inKitEditor;
	}

	public Location getEditkitSpawn(){
		return editKitSpawn;
	}
	
	public void sendBook(Player player, String ladder){
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta itemm = item.getItemMeta();
		itemm.setDisplayName("§eDefault " + ladder);
		item.setItemMeta(itemm);

		ItemStack item1 = new ItemStack(Material.BOOK);
		ItemMeta itemm1 = item1.getItemMeta();
		itemm1.setDisplayName("§eCustom " + ladder);
		item1.setItemMeta(itemm1);
		
		Inventory inventory = player.getInventory();
		inventory.clear();
		
		File file = new File(FFA.getInstance().getDataFolder() + "/database/kits/", player.getUniqueId().toString() + "-" + ladder.toLowerCase() + ".yml");
		if (file.exists()) {
			inventory.setItem(0, item1);
			inventory.setItem(8, item);
		} else {
			inventory.setItem(0, item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getKit(Player p, String kit) throws IOException {
		YamlConfiguration c = YamlConfiguration.loadConfiguration(new File(FFA.getInstance().getDataFolder() + "/database/kits/", p.getUniqueId().toString() + "-" + kit.toLowerCase() + ".yml"));
		ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
		p.getInventory().setArmorContents(content);
		content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
		p.getInventory().setContents(content);
	}
}