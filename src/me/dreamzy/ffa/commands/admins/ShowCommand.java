package me.dreamzy.ffa.commands.admins;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;

public class ShowCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("admin.show")) {
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) != null) {
						Player target = Bukkit.getPlayer(args[0]);
						FFA.getInstance().showPlayerForObserver(target, player);
						player.sendMessage("§a" + target.getName() + " showed for " + player.getName() + ".");
					} else {
						player.sendMessage("§cThis player is not online.");
					}
				} else {
					player.sendMessage("§cCorrect usage: /show <player>");
				}
			} else {
				player.sendMessage("§cNo permission.");
			}
		}
		return false;
	}
}