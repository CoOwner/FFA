package me.dreamzy.ffa.commands.mods;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.utils.DataUtils;

public class ModCommand implements CommandExecutor, Listener {

	public static ArrayList<Player> inmod = new ArrayList<>();

	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		Player p = (Player) sender;
		if (p != null) {
			if (p.hasPermission("mod.*") || p.isOp()) {
				if (!inmod.contains(p)) {
					if (p.getLocation().distance(FFA.getInstance().getSpawn()) > 15) {
						p.sendMessage("§cYou need to are in spawn to do that.");
						return false;
					}
					p.setGameMode(GameMode.CREATIVE);
					inmod.add(p);
					for (Player players : FFA.getInstance().getOnlinePlayers()) {
						FFA.getInstance().hidePlayerForObserver(p, players);
					}
					giveModItems(p);
				} else {
					FPlayer pp = FFA.getInstance().players.get(p.getUniqueId());
					p.setGameMode(GameMode.SURVIVAL);
					inmod.remove(p);
					for (Player players : FFA.getInstance().getOnlinePlayers()) {
						FFA.getInstance().showPlayerForObserver(p, players);
					}
					pp.spawnInitialisation();
					pp.giveMainItems();
					p.teleport(FFA.getInstance().getSpawn());
				}
				p.sendMessage("§6Your staff mode has been " + (ModCommand.inmod.contains(p) ? "§aenabled" : "§cdisabled"));
				p.closeInventory();
			} else {
				p.sendMessage("§cNo permission.");
			}
		}
		return true;
	}

	public void giveModItems(final Player p) {

		p.getInventory().clear();

		final ItemStack item = new ItemStack(Material.WOOD_SWORD);
		final ItemMeta itemm = item.getItemMeta();
		itemm.setDisplayName("§6Knockback");
		itemm.spigot().setUnbreakable(true);
		item.setItemMeta(itemm);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

		final ItemStack item2 = new ItemStack(Material.IRON_FENCE);
		final ItemMeta itemm2 = item2.getItemMeta();
		itemm2.setDisplayName("§6Freeze");
		item2.setItemMeta(itemm2);

		final ItemStack item3 = new ItemStack(Material.BOOK);
		final ItemMeta itemm3 = item3.getItemMeta();
		itemm3.setDisplayName("§6Inspection");
		item3.setItemMeta(itemm3);

		final ItemStack item5 = new ItemStack(Material.NETHER_STAR);
		final ItemMeta itemm5 = item5.getItemMeta();
		itemm5.setDisplayName("§6Random teleportation");
		item5.setItemMeta(itemm5);

		@SuppressWarnings("deprecation")
		final ItemStack item6 = new ItemStack(351, 1, (short) 14);
		final ItemMeta itemm6 = item6.getItemMeta();
		itemm6.setDisplayName(("§cQuit Mod"));
		item6.setItemMeta(itemm6);

		p.getInventory().setItem(4, item);
		p.getInventory().setItem(0, item3);
		p.getInventory().setItem(1, item2);
		p.getInventory().setItem(7, item5);
		p.getInventory().setItem(8, item6);

		p.updateInventory();
	}

	@EventHandler
	public void onTakeDamage(final EntityDamageByEntityEvent e) {
		final Player p = (Player) e.getEntity();
		if (p instanceof Player) {
			if (ModCommand.inmod.contains(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClickPlayer(final PlayerInteractEntityEvent e) {
		final Player p = e.getPlayer();
		final Player t = (Player) e.getRightClicked();
		if (ModCommand.inmod.contains(p)) {
			if (p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getDisplayName() != null
					&& p.getItemInHand().getType() == Material.BOOK
					&& p.getItemInHand().getItemMeta().getDisplayName().equals("§6Inspection")) {
				p.performCommand("verif " + t.getName());
			}
			if (p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getDisplayName() != null
					&& p.getItemInHand().getType() == Material.IRON_FENCE
					&& p.getItemInHand().getItemMeta().getDisplayName().equals("§6Freeze")) {
				p.performCommand("freeze " + t.getName());
			}
		}
	}

	@EventHandler
	public void onItemClick(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		final Action action = e.getAction();
		if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
				&& p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getDisplayName() != null
				&& p.getItemInHand().getType() == Material.NETHER_STAR
				&& p.getItemInHand().getItemMeta().getDisplayName().equals("§6Random teleportation")) {
			if (FFA.getInstance().getOnlinePlayers().length != 1) {
				if (inmod.contains(p)) {
					final Random r = new Random();
					final int random = r.nextInt(FFA.getInstance().getOnlinePlayers().length);
					Player t = FFA.getInstance().getOnlinePlayers()[random];
					p.sendMessage("§6You have been random teleported to " + DataUtils.getPrefix(t) + t.getName() + "§6.");
					p.teleport(t.getLocation());
				}
			} else
				p.sendMessage("§cYou are the only one online player.");
			return;
		} else if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
				&& p.getItemInHand().getItemMeta() != null && p.getItemInHand().getItemMeta().getDisplayName() != null
				&& p.getItemInHand().getItemMeta().getDisplayName().equals("§cQuit Mod")) {
			if (inmod.contains(p)) {
				e.setCancelled(true);
				for (Player players : FFA.getInstance().getOnlinePlayers()) {
					players.showPlayer(p);
				}
				p.performCommand("mod");
			}
		}
	}

	@EventHandler
	public void onDrop(final PlayerDropItemEvent e) {
		final Player p = e.getPlayer();
		if (ModCommand.inmod.contains(p)) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	@EventHandler
	public void onDrop(final PlayerPickupItemEvent e) {
		final Player p = e.getPlayer();
		if (ModCommand.inmod.contains(p)) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (ModCommand.inmod.contains(p)) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}
}