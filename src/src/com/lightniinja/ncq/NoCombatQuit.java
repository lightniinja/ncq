package com.lightniinja.ncq;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoCombatQuit extends JavaPlugin {
	
	public static String prefix = "&4AntiLog:";
	public static String message_ontag = "&eYou have been put in combat! Do not log out!";
	public static String message_onlog = "&c{%p} logged out while in combat!";
	public static String message_onsafe = "&eYou can now safely log out!";
	public static String message_incombat = "&cYou are currently in combat!";
	public static String message_notincombat = "&aYou are currently not in combat!";
	public static String message_nocommand = "&cYou cannot use that command while in combat!";
	public static String command = "%killplayer%";
	public static long time = 100L;
	public static HashMap<String, Long> tagged = new HashMap<String, Long>();
	public static NoCombatQuit instance;
	
	public void onEnable() {
		this.saveDefaultConfig();
		prefix = this.getConfig().getString("prefix");
		message_ontag = this.getConfig().getString("message-ontag");
		message_onlog = this.getConfig().getString("message-onlog");
		message_onsafe = this.getConfig().getString("message-onsafe");
		message_incombat = this.getConfig().getString("message-incombat");
		message_notincombat = this.getConfig().getString("message-notincombat");
		message_nocommand = this.getConfig().getString("message-nocommand");
		command = this.getConfig().getString("command");
		time = this.getConfig().getLong("time");
		instance = this;
		this.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while(i < tagged.size()) {
					String s = (String) tagged.keySet().toArray()[i];
					if((tagged.get(s) + (time * 100)) < System.currentTimeMillis()) {
						tagged.remove(s);
						if(Bukkit.getPlayer(s) != null) {
							Bukkit.getPlayer(s).sendMessage(format(prefix) + " " + format(message_onsafe));
						}
					}
				}
			}
		}, (time * 10L), (time * 10L));
	}
	
	public boolean onCommand(CommandSender s, Command c, String label, String[] args) {
		if(c.getName().equalsIgnoreCase("ncq")) {
			if(s instanceof Player) {
				Player p = (Player)s;
				if(tagged.containsKey(p.getName())) {
					s.sendMessage(format(prefix) + " " + format(message_incombat));
				} else {
					s.sendMessage(format(prefix) + " " + format(message_notincombat));
				}
			} else {
				s.sendMessage(ChatColor.RED + "Only usable as a player");
			}
		} 
		return true;
	}
	
	
	public static String format(String s) {
		return s.replace("&", "§");
	}
}