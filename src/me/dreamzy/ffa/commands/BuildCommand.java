package me.dreamzy.ffa.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildCommand implements CommandExecutor, Listener {

	public static ArrayList<Player> canbuild = new ArrayList<>();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("build")) {
			if (p.hasPermission("admin.build")) {
				if (canbuild.contains(p)) {
					canbuild.remove(p);
				} else {
					canbuild.add(p);
				}
				p.sendMessage("§6Your build mode has been " + (canbuild.contains(p) ? "§aenabled" : "§cdisabled"));
			}
		}
		return false;
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (canbuild.contains(e.getPlayer()))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (canbuild.contains(e.getPlayer()))
			return;
		e.setCancelled(true);
	}

	@EventHandler
	public void onBlock(BlockBurnEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlock(BlockFadeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlock(BlockFormEvent event) {
		event.setCancelled(true);
	}
}