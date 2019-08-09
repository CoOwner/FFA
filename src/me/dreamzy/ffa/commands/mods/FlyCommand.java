package me.dreamzy.ffa.commands.mods;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if ((p.isOp() || p.hasPermission("mod.*"))) {
				if (args.length == 0) {
					p.sendMessage("§6Your fly mode has been " + (p.getAllowFlight() ? "§cdisabled" : "§aenabled"));
					if (p.getAllowFlight() == true) {
						p.setAllowFlight(false);
						p.setFlying(false);
					} else {
						p.setAllowFlight(true);
						p.setFlying(true);
					}
				} else {
					if (!p.hasPermission("mod.fly.other")) return true;
					
					Player target = Bukkit.getPlayer(args[0]);
					if (target != null && target.isOnline()) {
						p.sendMessage("§e" + target.getName() + "§6's fly mode has been " + (target.getAllowFlight() ? "§cdisabled" : "§aenabled"));
						if (target.getAllowFlight() == true) {
							target.setAllowFlight(false);
							target.setFlying(false);
						} else {
							target.setAllowFlight(true);
							target.setFlying(true);
						}
					} else {
						p.sendMessage("§cThis player is not connected.");
					}
				}
			} else {
				p.sendMessage("§cNo permission.");
			}
		}
		return false;
	}
}