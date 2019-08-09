package me.dreamzy.ffa.commands.mods;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.gui.FreezeGui;

public class FreezeCommand implements CommandExecutor, Listener {
	private static ArrayList<Player> haxfreeze = new ArrayList<>();
	
	private static HashMap<Player, Location> oldLocation = new HashMap<>();
	
	@SuppressWarnings("rawtypes")
	public static ArrayList getArray(){
		return haxfreeze;
	}
	
	public static  Location getFrozePoint(){
		return new Location(Bukkit.getWorld("overworld"),17.0,80.5,49.5);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
			if (p.hasPermission("mod.*")) {
				if (args.length == 1) {
					Player target = Bukkit.getPlayer(args[0]);
					if (target != p) {
						if (target != null) {
							if (!target.hasPermission("mod.*") || (p.isOp())) {
								for (Player staff : FFA.getInstance().getOnlinePlayers()) {
									if (staff.hasPermission("mod.*")) {
										staff.sendMessage("§8[§9Staff§8] §c" + target.getName() + " §7has been " + (haxfreeze.contains(target) ? "un" : "") + "frozed by §a" + p.getName() + "§7.");
									}
								}
								if (!haxfreeze.contains(target)) {
									oldLocation.put(target, target.getLocation());
									target.teleport(getFrozePoint());
									haxfreeze.add(target);
									sendFreezeMessage(target);
									target.closeInventory();
									FreezeGui.openGui(target);
								} else {
									target.teleport(oldLocation.get(target));
									oldLocation.remove(target);
									target.sendMessage("§aYou have been unfrozed.");
									haxfreeze.remove(target);
									target.closeInventory();
								}
							} else {
								p.sendMessage("§cYou can not freeze staff members.");
							}
						} else {
							p.sendMessage("§cThis player is not online.");
						}
					} else {
						p.sendMessage("§cYou can not freeze yourself.");
					}
				} else {
					p.sendMessage("§cUse /freeze <player>");
				}
			} else {
				p.sendMessage("§cNo permission.");
			}		
			return true;
	}
	

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if ((haxfreeze.contains(p)) && ((e.getFrom().getBlockX() != e.getTo().getBlockX())|| (e.getFrom().getBlockZ() != e.getTo().getBlockZ()))) {
			p.teleport(e.getFrom());
			FreezeGui.openGui(p);
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (haxfreeze.contains(p)) {
			for (Player staff : FFA.getInstance().getOnlinePlayers()) {
				if (staff.hasPermission("mod.*")) {
					staff.sendMessage("§8[§9Staff§8] §4" + p.getName()	+ " §chas disconnected while frozen.");
				}
			}
		}
	}

	@EventHandler
	public void onEnitityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (haxfreeze.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEnitityDamageByEntity(final EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		Player damager = (Player) e.getDamager();
		if (haxfreeze.contains(p)) {
			e.setCancelled(true);
			damager.sendMessage("§cDamage cancelled! " + p.getName() + " is actually frozen.");
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
			Player p = e.getPlayer();
			if (haxfreeze.contains(p)) {
				e.setCancelled(true);
				sendFreezeMessage(p);
			}
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (haxfreeze.contains(p)) {
			e.setCancelled(true);
			sendFreezeMessage(p);
		}
	}

	private void sendFreezeMessage(Player p) {
		Bukkit.getScheduler().cancelTasks(FFA.getInstance());

		if (haxfreeze.contains(p)) {
			p.sendMessage("");
			p.sendMessage("§e[FragMC] §fYou have been frozen, please connect you on our teamspeak.");
			p.sendMessage("§e[FragMC] §fIf you log out, you will be permanently banned.");
			p.sendMessage("§e[FragMC] §fOur teamspeak adress: §ats.fragmc.com");
			p.sendMessage("");
		}
	}
}