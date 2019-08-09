package me.dreamzy.ffa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.api.MessagesAPI;
import me.dreamzy.ffa.utils.ChatUtils;
import me.dreamzy.ffa.utils.DataUtils;
import me.dreamzy.ffa.utils.FilterUtils;

public class MessagesCommand implements CommandExecutor {

	private FFA pl;

	public MessagesCommand(FFA pl) {
		this.pl = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("msg") || label.equalsIgnoreCase("w") || label.equalsIgnoreCase("tell")
				|| label.equalsIgnoreCase("m")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length > 1) {
					Player target = Bukkit.getPlayer(args[0]);
					if (target != null) {
							String msg = "";
							for (int i = 1; i < args.length; i++) {
								msg = msg + " " + args[i];
							}

							p.sendMessage("§7(§6Me §7-> §f" + DataUtils.getPrefix(target) + target.getName() + "§7)§e"
									+ msg);
							
							if (!hasConversing(p)) {
								pl.conversing.add(new MessagesAPI(p, target));
							} else {
								getConversing(p).setTarget(target);
							}
							
							if (ChatUtils.hasBlacklistedWord(msg, p)) {
								FilterUtils.AlertMP(p, target, msg);
								return true;
							}

							target.sendMessage("§7(§f" + DataUtils.getPrefix(p) + p.getName() + " §7-> §6Me§7)§e" + msg);

							if (!hasConversing(target)) {
								pl.conversing.add(new MessagesAPI(target, p));
							} else {
								getConversing(target).setTarget(p);
							}
					} else {
						p.sendMessage("§cThis player is not online..");
					}
				} else {
					p.sendMessage("§cUse /msg <player> <message>");
				}
			}
		}
		if (label.equalsIgnoreCase("r")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length > 0) {
					if (hasConversing(p)) {
						Player target = getConversing(p).getTarget();
						if (target != null && target.isOnline()) {
							String msg = "";
							for (int i = 0; i < args.length; i++) {
								msg = msg + " " + args[i];
							}

							p.sendMessage("§7(§6Me §7-> §f" + DataUtils.getPrefix(target) + target.getName() + "§7)§e"
									+ msg);
							if (!hasConversing(p)) {
								pl.conversing.add(new MessagesAPI(p, target));
							} else {
								getConversing(p).setTarget(target);
							}
							
							if (ChatUtils.hasBlacklistedWord(msg, p)) {
								FilterUtils.AlertMP(p, target, msg);
								return true;
							}
							
							target.sendMessage(
									"§7(§f" + DataUtils.getPrefix(p) + p.getName() + " §7-> §6Me§7)§e" + msg);

							if (!hasConversing(target)) {
								pl.conversing.add(new MessagesAPI(target, p));
							} else {
								getConversing(target).setTarget(p);
							}
						} else {
							p.sendMessage("§cThis player has disconnected.");
						}
					} else {
						p.sendMessage("§cYou don't have anyone to reply.");
					}
				} else {
					p.sendMessage("§cUse /r <message>");
				}
			}
		}
		return false;
	}

	private MessagesAPI getConversing(Player p) {
		for (MessagesAPI c : pl.conversing) {
			if (c.getSender() == p) {
				return c;
			}
		}
		return null;
	}

	private boolean hasConversing(Player p) {
		if (getConversing(p) == null)
			return false;
		return true;
	}
}