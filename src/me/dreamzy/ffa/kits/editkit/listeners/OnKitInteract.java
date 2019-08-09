package me.dreamzy.ffa.kits.editkit.listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.kits.DefaultNoDebuff;
import me.dreamzy.ffa.kits.editkit.EditKit;

public class OnKitInteract implements Listener {

	FFA pl = FFA.getInstance();
	EditKit editKitClass = new EditKit();

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onItemClick(PlayerInteractEvent e) {
		FPlayer player = FFA.getInstance().players.get(e.getPlayer().getUniqueId());

		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.TRAP_DOOR)
			e.setCancelled(true);

		if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				String[] l = sign.getLines();
				if (player.getLocation().distance(FFA.getInstance().getSpawn()) > 15)
					return;
				for (String lignes : l) {
					if (lignes.toLowerCase().contains("edit")) {
						
						if (!EditKit.get().getInEditKit().contains(player)) {
							EditKit.get().getInEditKit().add(player);
							player.sendMessage("§eEditing your Custom NoDebuff Kit.");
								player.teleport(editKitClass.getEditkitSpawn());
						} else { 
							EditKit.get().getInEditKit().remove(player);
							player.teleport(FFA.getInstance().getSpawn());
							player.spawnInitialisation();
							player.sendMessage("§cError, you have been teleport to spawn..");
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		FPlayer p = FFA.getInstance().players.get(event.getPlayer().getUniqueId());
		if (EditKit.get().getInEditKit().contains(p)) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (event.getClickedBlock().getType().equals(Material.ANVIL)) {
					event.setCancelled(true);
					openGUI(p);
				} else if (event.getClickedBlock().getType().equals(Material.CHEST)) {
					event.setCancelled(true);
					openChest(p);
				} else if (event.getClickedBlock().getType().equals(Material.WOODEN_DOOR)) {
					event.setCancelled(true);
					p.getInventory().clear();
					p.giveMainItems();
					p.spawnInitialisation();
					p.sendMessage(new String[] { "", "§6You have been teleported to spawn." });
					p.teleport(FFA.getInstance().getSpawn());
					if (EditKit.get().getInEditKit().contains(p)) {
						EditKit.get().getInEditKit().remove(p);
					}
				}
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) throws IOException {
		if (e.getWhoClicked() instanceof Player) {
			FPlayer p = FFA.getInstance().players.get(e.getWhoClicked().getUniqueId());
			if (e.getCurrentItem() == null || (e.getCurrentItem().getItemMeta() == null) || (e.getCurrentItem().getItemMeta().getDisplayName() == null)) {
				return;
			}
			String ladder = "NoDebuff";

			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aSave")) {
				saveKit(ladder, p);
				p.sendMessage("§eSaved your Custom " + ladder + " kit§e.");
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cDelete")) {
				deleteKit(ladder, p);
				p.sendMessage("§eDeleted your Custom " + ladder + " kit§e.");
				p.closeInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§eLoad")) {
				File file = new File(FFA.getInstance().getDataFolder() + "/database/kits/", p.getUniqueId().toString() + "-" + ladder.toLowerCase() + ".yml");
				if (file.exists()){
					EditKit.get().getKit(p, ladder);
					p.sendMessage("§eEquipped you Custom " + ladder + " kit§e.");
				} else {
					p.sendMessage("§cYour custom " + ladder + " kit does not exist or is not set.");
				}
			}
			e.setCancelled(true);
		}
	}

	private void saveKit(String kit, Player p) {
		File file = new File(pl.getDataFolder() + "/database/kits/",p.getUniqueId().toString() + "-" + kit.toLowerCase() + ".yml");
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
			try {
				c.set("inventory.armor", p.getInventory().getArmorContents());
				c.set("inventory.content", p.getInventory().getContents());
				c.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			c.set("inventory.armor", p.getInventory().getArmorContents());
			c.set("inventory.content", p.getInventory().getContents());
			try {
				c.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void deleteKit(String kit, Player p) {
		File file = new File(pl.getDataFolder() + "/database/kits/",p.getUniqueId().toString() + "-" + kit.toLowerCase() + ".yml");
		if (file.exists()) {
			file.delete();
		}
	}
	
	public void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "§cKit Management");

		ItemStack save1 = new ItemStack(Material.HOPPER);
		ItemMeta save1M = save1.getItemMeta();
		save1M.setDisplayName("§aSave");
		save1.setItemMeta(save1M);

		ItemStack save3 = new ItemStack(Material.BOOK);
		ItemMeta save3M = save3.getItemMeta();
		save3M.setDisplayName("§eLoad");
		save3.setItemMeta(save3M);
		
		ItemStack save2 = new ItemStack(Material.FIRE);
		ItemMeta save2M = save2.getItemMeta();
		save2M.setDisplayName("§cDelete");
		save2.setItemMeta(save2M);

		inv.setItem(4, save1);
		inv.setItem(13, save3);
		inv.setItem(22, save2);

		p.openInventory(inv);
	}

	public void openChest(Player player){
		Inventory inventory = Bukkit.createInventory(null, 9 * 5);
		
		ItemStack carrote = new ItemStack(Material.GOLDEN_CARROT, 64);
		ItemStack ender = new ItemStack(Material.ENDER_PEARL, 16);
		
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
		
		ItemStack speed2 = new ItemStack(Material.POTION, 1, (short) 8226);
		ItemStack health2 = new ItemStack(Material.POTION, 1, (short) 16421);
		
		for (int i = 0; i < (9*4); i++) {
			inventory.setItem(i, health2);
		}
		
		inventory.setItem(0, item4);
		inventory.setItem(1, item);
		inventory.setItem(2, item1);
		inventory.setItem(3, item2);
		inventory.setItem(4, item3);
		inventory.setItem(5, ender);
		inventory.setItem(6, carrote);
		inventory.setItem(7, speed2);
		inventory.setItem(16, speed2);
		inventory.setItem(25, speed2);
		
		player.openInventory(inventory);
	}
	
	@EventHandler
	public void onKitsClick(PlayerInteractEvent e) {
			FPlayer player = FFA.getInstance().players.get(e.getPlayer().getUniqueId());
			if ((e.getPlayer() instanceof Player)) {
				if ((player.getItemInHand() != null) && (player.getItemInHand().getType() != null)
						&& (player.getItemInHand().getType() != Material.AIR) && (player.getItemInHand().getItemMeta() != null)
						&& (player.getItemInHand().getItemMeta().getDisplayName() != null)) {
					String kit = player.getItemInHand().getItemMeta().getDisplayName().replaceAll("§e", "");
					if (kit.equals("Default NoDebuff") || kit.contains("Custom NoDebuff")) {
						if (kit.contains("Custom")) {
							try {
								EditKit.get().getKit(player, "nodebuff");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} else {
							DefaultNoDebuff.giveKit(player);
						}
						player.sendMessage("§eEquipped you " + kit + " Kit§e.");
					}
				}
			}
		}
}