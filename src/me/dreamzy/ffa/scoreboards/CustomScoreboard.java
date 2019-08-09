package me.dreamzy.ffa.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import net.minecraft.util.org.apache.commons.lang3.RandomStringUtils;

public class CustomScoreboard {
	private FPlayer player;
	private Scoreboard sb;
	private Team tab;
	private Objective o;
	private Team lines1;
	private Team lines2;
	private boolean isEnable = true;

	public CustomScoreboard(FPlayer player) {
		if (player == null) {
			return;
		}
		if (FFA.getInstance().scoreboards.containsKey(player)) {
			return;
		}
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();

		this.player = player;
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
		this.o = this.sb.registerNewObjective(RandomStringUtils.randomAlphanumeric(15), "dummy");
		this.o.setDisplayName("§b§lVexus §8(§7FFA§8)");
		this.o.setDisplaySlot(DisplaySlot.SIDEBAR);

		this.lines1 = this.sb.registerNewTeam("lines1");
		this.lines1.addEntry("§7§m-------");
		this.lines1.setSuffix("----------");

		this.lines2 = this.sb.registerNewTeam("lines2");
		this.lines2.addEntry("§7§m-------- ");
		this.lines2.setSuffix("---------");

		this.tab = this.sb.registerNewTeam("tab");
		this.tab.addEntry(player.getName());
		this.tab.setPrefix("§2");

		FFA.getInstance().scoreboards.put(player, this);
	}

	@SuppressWarnings("deprecation")
	public void sendSb() {
		if (!this.isEnable) {
			return;
		}
		for (String lines : this.sb.getEntries()) {
			this.sb.resetScores(lines);
		}
		this.o.getScore("§7§m-------").setScore(6);
		this.o.getScore("§bOnline: §f" + Bukkit.getOnlinePlayers().length).setScore(4);
		this.o.getScore("§bKills: §f" + this.player.getStatsManager().getStats("kills")).setScore(3);
		this.o.getScore("§bDeaths: §f" + this.player.getStatsManager().getStats("deaths")).setScore(2);
		this.o.getScore("§7§m-------- ").setScore(1);

		this.player.setScoreboard(this.sb);
	}

	@SuppressWarnings("deprecation")
	public void refreshSb(boolean quit) {
		if (!this.isEnable) {
			return;
		}
		for (String lines : this.sb.getEntries()) {
			if (lines.contains("§bOnline:")) {
				String text = lines.split(":")[0] + ": §f";
				int score = this.o.getScore(lines).getScore();
				this.sb.resetScores(lines);
				this.o.getScore(text + (quit ? Bukkit.getOnlinePlayers().length - 1 : Bukkit.getOnlinePlayers().length)).setScore(score);
			}
		}
	}

	public void refreshKillsSb() {
		if (!this.isEnable) {
			return;
		}
		for (String lines : this.sb.getEntries()) {
			if (lines.contains("§bKills:")) {
				String text = lines.split(":")[0] + ": §f";
				int score = this.o.getScore(lines).getScore();
				this.sb.resetScores(lines);
				this.o.getScore(text + this.player.getStatsManager().getStats("kills")).setScore(score);
			} else if (lines.contains("§bDeaths:")) {
				String text = lines.split(":")[0] + ": §f";
				int score = this.o.getScore(lines).getScore();
				this.sb.resetScores(lines);
				this.o.getScore(text + this.player.getStatsManager().getStats("deaths")).setScore(score);
			}
		}
	}
	
	public void refreshPearlSb() {
		if (!this.isEnable) {
			return;
		}
		for (String lines : this.sb.getEntries()) {
			if (lines.contains("§bPearl:")) {
				String text = lines.split(":")[0] + ": §f";
				int score = this.o.getScore(lines).getScore();
				this.sb.resetScores(lines);
				this.o.getScore(text + "variable").setScore(score);
			}
		}
	}

	public boolean isEnable() {
		return this.isEnable;
	}

	public void setEnable(boolean enable) {
		this.isEnable = enable;
	}
}