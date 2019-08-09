package me.dreamzy.ffa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.dreamzy.ffa.api.MessagesAPI;
import me.dreamzy.ffa.commands.BuildCommand;
import me.dreamzy.ffa.commands.DebugCommand;
import me.dreamzy.ffa.commands.MessagesCommand;
import me.dreamzy.ffa.commands.PingCommand;
import me.dreamzy.ffa.commands.StatsCommand;
import me.dreamzy.ffa.commands.admins.HideCommand;
import me.dreamzy.ffa.commands.admins.ShowCommand;
import me.dreamzy.ffa.commands.mods.ClearchatCommand;
import me.dreamzy.ffa.commands.mods.FlyCommand;
import me.dreamzy.ffa.commands.mods.FreezeCommand;
import me.dreamzy.ffa.commands.mods.ModCommand;
import me.dreamzy.ffa.duels.Duels;

import me.dreamzy.ffa.duels.listeners.OnPlayerRightclick;
import me.dreamzy.ffa.gui.FreezeGui;
import me.dreamzy.ffa.gui.PlayGui;
import me.dreamzy.ffa.kits.editkit.EditKit;
import me.dreamzy.ffa.kits.editkit.listeners.OnKitInteract;
import me.dreamzy.ffa.listeners.OnChat;
import me.dreamzy.ffa.listeners.OnDamage;
import me.dreamzy.ffa.listeners.OnDeath;
import me.dreamzy.ffa.listeners.OnInteract;
import me.dreamzy.ffa.listeners.OnJoin;
import me.dreamzy.ffa.listeners.OnPearl;
import me.dreamzy.ffa.listeners.OnQuit;
import me.dreamzy.ffa.listeners.OnThrowPotions;
import me.dreamzy.ffa.scoreboards.CustomScoreboard;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class FFA extends JavaPlugin {

	public HashMap<UUID, FPlayer> players = new HashMap<>();
	public ArrayList<MessagesAPI> conversing = new ArrayList<>();
	
	public ArrayList<Player> inFFA = new ArrayList<>();
	public ArrayList<Player> inKitEditor = new ArrayList<>();
	
	public HashMap<Integer, Location>  ffaSpawns = new HashMap<>();

	public HashMap<Player, CustomScoreboard> scoreboards = new HashMap<>();
	
	public HashMap<Player, TabList> tabs= new HashMap<>();

	private static FFA instance;
	
	// Todo:
	// Optimiser au maximum le plugin.
	// Ajouter l'ender-pearl cooldown dans le scoreboard. (et dans le tab maybe?)
	// ----------------------------------------------------------------------------------------------------------------
	// Propose des idées.
	
	public static FFA getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		instance = this;
		TabList.setPlugin(this);

		loadInstances();
		loadSpawns();
		registerListeners();
		registerCommands();

		for (Player players : this.getOnlinePlayers()) {
			if (players.getLocation().distance(FreezeCommand.getFrozePoint()) < 10) {
				FreezeCommand.getArray().add(players);
				FreezeGui.openGui(players);
				players.teleport(FreezeCommand.getFrozePoint());
			}
			this.players.put(players.getUniqueId(), new FPlayer(players));
			FPlayer fPlayers = this.players.get(players.getUniqueId());

			CustomScoreboard cs = new CustomScoreboard(fPlayers);
			cs.sendSb();
		}
	}

	@Override
	public void onDisable() {
		for (Player players : this.getOnlinePlayers()) {
			if (Duels.get().inDuel.contains(players)){
				Duels.get().inDuel.remove(players);
				players.teleport(FFA.getInstance().getSpawn());
				players.getInventory().clear();
				players.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
			}
			if (ModCommand.inmod.contains(players)){
				ModCommand.inmod.remove(players);
				players.teleport(FFA.getInstance().getSpawn());
				players.getInventory().clear();
				players.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
			}
			if (players.getLocation().distance(EditKit.get().getEditkitSpawn()) < 10) {
				EditKit.get().getInEditKit().remove(players);
				players.teleport(FFA.getInstance().getSpawn());
				players.getInventory().clear();
				players.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
			}
		}
		instance  = null;
	}

	private void loadInstances() {
		new EditKit();
		new PlayGui();
		new Duels();
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new OnJoin(), this);
		pm.registerEvents(new OnChat(), this);
		pm.registerEvents(new OnInteract(), this);
		pm.registerEvents(new PlayGui(), this);
		pm.registerEvents(new OnDamage(), this);
		pm.registerEvents(new OnDeath(), this);
		pm.registerEvents(new BuildCommand(), this);
		pm.registerEvents(new OnPearl(), this);
		pm.registerEvents(new OnThrowPotions(), this);
		pm.registerEvents(new OnQuit(), this);
		pm.registerEvents(new ModCommand(), this);
		pm.registerEvents(new FreezeGui(), this);
		pm.registerEvents(new FreezeCommand(), this);

		pm.registerEvents(new OnKitInteract(), this);
		
		pm.registerEvents(new OnPlayerRightclick(), this);
	}

	private void registerCommands() {
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("msg").setExecutor(new MessagesCommand(this));
		getCommand("tell").setExecutor(new MessagesCommand(this));
		getCommand("m").setExecutor(new MessagesCommand(this));
		getCommand("r").setExecutor(new MessagesCommand(this));
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("build").setExecutor(new BuildCommand());
		getCommand("clearchat").setExecutor(new ClearchatCommand());
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("freeze").setExecutor(new FreezeCommand());
		getCommand("mod").setExecutor(new ModCommand());
		getCommand("show").setExecutor(new ShowCommand());
		getCommand("hide").setExecutor(new HideCommand());
		getCommand("debug").setExecutor(new DebugCommand());
	}
	
	private void loadSpawns(){
		ffaSpawns.clear();
		ffaSpawns.put(1, (new Location(Bukkit.getWorld("overworld"), 0.5, 101.5, 46.5, 0, 0)));
		ffaSpawns.put(2, (new Location(Bukkit.getWorld("overworld"), 20.5,101.5,117.5,135,0))); // 
		ffaSpawns.put(3, (new Location(Bukkit.getWorld("overworld"), -26.5,101.5,95.5,-110,0)));
	}

	@SuppressWarnings("deprecation")
	public Player[] getOnlinePlayers() {
		return Bukkit.getOnlinePlayers();
	}

	public Location getSpawn() {
		return new Location(Bukkit.getWorld("overworld"), 0.5, 101.5, 0.5, 0, 0);
	}

	public boolean isInFFA(FPlayer player) {
		if (player.getLocation().distance(FFA.getInstance().getSpawn()) > 15 && (!EditKit.get().getInEditKit().contains(player) ) && (!Duels.get().in1v1Lobby.contains(player))) {
			return true;
		}
		return false;
	}
	
	public Integer getRandomNumber(Integer min, Integer max){
		Random r = new Random();
		return  r.nextInt(max-min) + min;
	}
	
	public void hidePlayerForObserver(Player player, Player observer) {
		observer.hidePlayer(player);
	}

	public void showPlayerForObserver(Player player, Player observer) {
			observer.showPlayer(player);
	}
	
	public void jsonChat(Player target, String message, String command) {
		String msg = "{\"text\":\"\",\"extra\":[{\"text\":\"\",\"color\":\"white\"},{\"text\":\"" + message + "\",\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}}]}";

		IChatBaseComponent c = ChatSerializer.a(msg);
		PacketPlayOutChat packet = new PacketPlayOutChat(c);

		((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
	}
	
	public void hideModeratorsForObserver(Player observer){
		for (Player players : this.getOnlinePlayers()){
			if (ModCommand.inmod.contains(players)){
				observer.hidePlayer(players);
			}
		}
	}
	
	public double arrondi(double A, int B) {
		return ((A * Math.pow(10, B) + .5)) / Math.pow(10, B);
	}
}
