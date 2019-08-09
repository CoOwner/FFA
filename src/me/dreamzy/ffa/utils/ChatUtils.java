package me.dreamzy.ffa.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.base.CharMatcher;

public class ChatUtils {

	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static List<String> BLACKLISTEDWORDS = new ArrayList<>();

	public static void clearChat() {
		for (int i = 0; i < 100; i++) {
			Bukkit.broadcastMessage("");
		}
	}

	
	public static Player getMentionned(String arg) {
		return Bukkit.getPlayer(arg);
	}

	public static void load() {
		BLACKLISTEDWORDS.addAll(Arrays.asList(new String[] {

				"merde", "servde", "grosserv", "serveurde", "putain", "fdp", "ntm", "gueule", "cancer", "kys",
				"killyourself", "faggot", "salo", "mère", "mere", "conna", "mourrir", "nègre", "negre", "ztz",
				"staline", "tg", "musulman", "arabe", "chrétien", "chretien", "catholique", "easy", "ez", "funpvp",
				"izi", "soeur", "frère", "frere", "père", "pere", "fourre", "??", "??", "ftg", "pd", "fuck", "nvm",
				"nique", "nul", "shit", "valux", "g-com", "gcom", "liptonpvp", "kbde", "looser", "bad", "idiot", "race", "toxi",
				"meurt", "fils", "random", "rdm", "encul", "filsde", "**" }));
	}

	public static boolean hasBlacklistedWord(String msg, Player sender) {
		if (hasBypass(sender))
			return false;

		String[] args = msg.split(" ");

		for (String s : args) {
			if (BLACKLISTEDWORDS.contains(s.toLowerCase()) && !isNickName(s))
				return true;
		}
		CharMatcher matcher = CharMatcher.inRange('A', 'Z').or(CharMatcher.inRange('a', 'z'))
				.or(CharMatcher.inRange('0', '9')).or(CharMatcher.inRange(' ', ' '));

		String withoutSpecial = matcher.retainFrom(msg);

		String withoutSpace = withoutSpecial.trim().replace(" ", "");

		for (String bl : BLACKLISTEDWORDS) {
			if (!bl.equals("ez") && !bl.equals("pd")) {
				if (withoutSpace.toLowerCase().contains(bl) && !isNickNameFromMsgByArgs(msg))
					return true;
			} else {
				if (withoutSpace.contains(bl)) {
					int index = 0;
					for (String s : withoutSpecial.split(" ")) {
						String nextArg = "";
						if (index + 1 < withoutSpecial.split(" ").length) {
							nextArg = withoutSpecial.split(" ")[index + 1];
						}
						String sandnextArg = s + nextArg;
						if ((s.toLowerCase().equals(bl) || sandnextArg.toLowerCase().equals(bl)) && !isNickName(s)
								&& !isNickName(nextArg) && !s.endsWith(bl) && !nextArg.endsWith(bl))
							return true;
						index++;
					}
				}
			}
		}
		return false;
	}

	private static boolean isNickNameFromMsgByArgs(String msg) {
		String[] args = msg.split(" ");

		for (String s : args) {
			if (isNickName(s))
				return true;
		}

		return false;
	}

	public static boolean isNickName(String arg) {
		return Bukkit.getPlayer(arg) != null && Bukkit.getPlayer(arg).getName().equalsIgnoreCase(arg);
	}

	@SuppressWarnings("deprecation")
	public static boolean isNickNameFromMsg(String withoutSpace) {
		for (Player pls : Bukkit.getOnlinePlayers()) {
			if (withoutSpace.toLowerCase().contains(pls.getName().toLowerCase()))
				return true;
		}
		return false;
	}

	public static boolean hasBypass(Player p) {
		return p.hasPermission("admin.bypassfilter");
	}
}