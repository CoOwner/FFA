package me.dreamzy.ffa;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.comphenix.tinyprotocol.TinyProtocol;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.channel.Channel;

public class TabList {

	// Static Manager
	private static Plugin plugin;
	private static HashMap<UUID, TabList> players = new HashMap<>();
	private static ArrayList<String> tabEntrys = getTabEntrys();
	private static ArrayList<String> teamNames = getTeamNames();

	public static void setPlugin(Plugin plugin) {
		TabList.plugin = plugin;
		Protocol.enablePacketListener();
	}

	public static boolean hasTabList(Player player) {
		return players.containsKey(player.getUniqueId());
	}

	public static TabList createTabList(Player player) {
		return new TabList(player);
	}

	public static TabList deleteTabList(Player player) {
		return players.remove(player.getUniqueId());
	}

	public static TabList getByPlayer(Player player) { 
		return players.get(player.getUniqueId());
	}

	// Class
	private Player player;
	private boolean client18;
	private int tabSize;
	private Scoreboard scoreboard;

	private TabList(Player player) {
		this.player = player;
		this.scoreboard = player.getScoreboard();
		if(scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			player.setScoreboard(scoreboard);
		}
		this.setupTab();
		TabList.players.put(player.getUniqueId(), this);
	}

	private void setupTab() {
		MinecraftServer server = MinecraftServer.getServer();
		WorldServer world = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
		PlayerInteractManager manager = new PlayerInteractManager(world);

		for(int i=1; i<=tabSize; i++) {
			Team team = scoreboard.registerNewTeam(teamNames.get(i-1));
			GameProfile profile = new GameProfile(UUID.randomUUID(), tabEntrys.get(i-1));
			EntityPlayer entity = new EntityPlayer(server, world, profile, manager);
			team.addEntry(entity.getName());
			if(client18) {
			} else {
				PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.updateDisplayName(entity);
				Protocol.sendPacket(player, packet);
			}
		}
	}

	public int getTabSize() {
		return tabSize;
	}

	public boolean isClient18() {
		return client18;
	}

	public void setSlot(int slot, String value) {
		if(client18) {
			// 1
			if(slot==4) slot=2;
			else if(slot==7) slot=3;
			else if(slot==10) slot=4;
			else if(slot==13) slot=5;
			else if(slot==16) slot=6;
			else if(slot==19) slot=7;
			else if(slot==22) slot=8;
			else if(slot==25) slot=9;
			else if(slot==28) slot=10;
			else if(slot==31) slot=11;
			else if(slot==34) slot=12;
			else if(slot==37) slot=13;
			else if(slot==40) slot=14;
			else if(slot==43) slot=15;
			else if(slot==46) slot=16;
			else if(slot==49) slot=17;
			else if(slot==52) slot=18;
			else if(slot==55) slot=19;
			else if(slot==58) slot=20;
			// 2
			else if(slot==2) slot=21;
			else if(slot==5) slot=22;
			else if(slot==8) slot=23;
			else if(slot==11) slot=24;
			else if(slot==14) slot=25;
			else if(slot==17) slot=26;
			else if(slot==20) slot=27;
			else if(slot==23) slot=28;
			else if(slot==26) slot=29;
			else if(slot==29) slot=30;
			else if(slot==32) slot=31;
			else if(slot==35) slot=32;
			else if(slot==38) slot=33;
			else if(slot==41) slot=34;
			else if(slot==44) slot=35;
			else if(slot==47) slot=36;
			else if(slot==50) slot=37;
			else if(slot==53) slot=38;
			else if(slot==56) slot=39;
			else if(slot==59) slot=40;
			// 3
			else if(slot==3) slot=41;
			else if(slot==6) slot=42;
			else if(slot==9) slot=43;
			else if(slot==12) slot=44;
			else if(slot==15) slot=45;
			else if(slot==18) slot=46;
			else if(slot==21) slot=47;
			else if(slot==24) slot=48;
			else if(slot==27) slot=49;
			else if(slot==30) slot=50;
			else if(slot==33) slot=51;
			else if(slot==36) slot=52;
			else if(slot==39) slot=53;
			else if(slot==42) slot=54;
			else if(slot==45) slot=55;
			else if(slot==48) slot=56;
			else if(slot==51) slot=57;
			else if(slot==54) slot=58;
			else if(slot==57) slot=59;
		}
		Team team = scoreboard.getTeam(teamNames.get(slot-1));
		updateTeam(team, value);
	}

	public void clearTab() {
		for(int i=1; i<=tabSize; i++) {
			setSlot(i, "");
		}
		
	}

	private void updateTeam(Team team, String text) {
		text = color(text);
		String pre = getFirstSplit(text);
		String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
		team.setPrefix(pre);
		team.setSuffix(suf);
	}

	private String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	private String getFirstSplit(String s) {
		return s.length()>16 ? s.substring(0, 16) : s;
	}

	private String getSecondSplit(String s) {
		if(s.length()>32) {
			s = s.substring(0, 32);
		}
		return s.length()>16 ? s.substring(16) : "";
	}

	private static ArrayList<String> getTabEntrys() {
		ArrayList<String> list = new ArrayList<>();
		for(int i=1; i<=15; i++) {
			String entry = ChatColor.values()[i].toString();
			list.add(ChatColor.RED + entry);
			list.add(ChatColor.DARK_RED + entry);
			list.add(ChatColor.GREEN + entry);
			list.add(ChatColor.DARK_GREEN + entry);
			list.add(ChatColor.BLUE + entry);
			list.add(ChatColor.DARK_BLUE + entry);
		}
		return list;
	}

	private static ArrayList<String> getTeamNames() {
		ArrayList<String> list = new ArrayList<>();
		for(int i=0; i<80; i++) {
			String s = (i<10 ? "\\u00010" : "\\u0001") + i;
			list.add(s);
		}
		return list;
	}

	private static class Protocol {

		private static void enablePacketListener() {
			new TinyProtocol(TabList.plugin) {

				@Override
				public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {

					if(packet instanceof PacketPlayOutPlayerInfo) {

						Object profile = Reflex.getValue(packet, "player");
						String name = Reflex.getValue(profile, "name").toString();
						
						if(!name.startsWith("�")) {
							//receiver.sendMessage("Packet canceled from: " + name);
							return null;
						}
					}

					return packet;
				}

			};
		}

		private static EntityPlayer getEntity(Player player) {
			return ((CraftPlayer)player).getHandle();
		}

		private static boolean isClient18(Player player) {
			return getEntity(player).playerConnection.networkManager.getVersion()>5;
		}

		private static void sendPacket(Player player, Packet packet) {
			getEntity(player).playerConnection.sendPacket(packet);
		}

	}

	private static class Reflex {

		private static Object getValue(Object object, String field) {
			try {
				Field jField = object.getClass().getDeclaredField(field);
				if(!jField.isAccessible()) jField.setAccessible(true);
				return jField.get(object);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

	}

}