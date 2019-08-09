package me.dreamzy.ffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;

public class PingCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))return true;

		FPlayer p = FFA.getInstance().players.get(((Player) sender).getUniqueId());
		if (args.length == 0) {
			p.sendMessage("§c" + p.getName() + "'s ping: §7" + p.getPing() + "ms");
		} else {
			if (Bukkit.getPlayer(args[0]) == null) {
				p.sendMessage("§cThis player is not online.");
				return true;
			}
			FPlayer t = FFA.getInstance().players.get(Bukkit.getPlayer(args[0]).getUniqueId());
			p.sendMessage("§c" + t.getName() + "'s ping: §7" + t.getPing() + "ms");
		}
		return false;
	}
}