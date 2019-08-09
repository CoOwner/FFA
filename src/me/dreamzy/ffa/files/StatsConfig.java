package me.dreamzy.ffa.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.dreamzy.ffa.FFA;
import me.dreamzy.ffa.FPlayer;
import me.dreamzy.ffa.scoreboards.CustomScoreboard;

public class StatsConfig {

	// Practice/database/Elos/%player's uuid%.yml

	private File conf;
	private FPlayer p;
	private FileConfiguration config;

	public StatsConfig(FPlayer player) {
		
		conf = new File(FFA.getInstance().getDataFolder() + "/database/stats/" + player.getUniqueId().toString() + ".yml");
		config = YamlConfiguration.loadConfiguration(conf);
		p = player;
		if (!conf.exists()) {
			try {
				conf.createNewFile();
				config = YamlConfiguration.loadConfiguration(conf);
				config.set("stats.kills", 0);
				config.set("stats.deaths", 0);
				config.save(conf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(conf);
	}
	
	public void addStats(String mode, Integer Elo) {
		config.set("stats." + mode.toLowerCase(), Integer.valueOf(getStats(mode) + Elo));
		saveStats();
		if (p != null) {
			CustomScoreboard cs = FFA.getInstance().scoreboards.get(p);
			cs.refreshKillsSb();
		}
	}

	public void removeStats(String mode, Integer Elo) {
		config.set("stats." + mode.toLowerCase(), Integer.valueOf(getStats(mode.toLowerCase()) - Elo));
		saveStats();
	}

	public Integer getStats(String mode) {
		return config.getInt("stats." + mode.toLowerCase());
	}

	public void saveStats() {
		try {
			config.save(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}