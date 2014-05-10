package com.lightniinja.ncq;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

	@EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {

			Player p = (Player)e.getEntity();
			Player d = (Player)e.getDamager();
			if(!NoCombatQuit.tagged.containsKey(p.getName())) {
				NoCombatQuit.tagged.put(p.getName(), System.currentTimeMillis());
				p.sendMessage(NoCombatQuit.format(NoCombatQuit.prefix) + " " + NoCombatQuit.format(NoCombatQuit.message_ontag));
			} else {
				NoCombatQuit.tagged.remove(p.getName());
				NoCombatQuit.tagged.put(p.getName(), System.currentTimeMillis());
			}
			
			if(!NoCombatQuit.tagged.containsKey(d.getName())) {
				NoCombatQuit.tagged.put(d.getName(), System.currentTimeMillis());
				d.sendMessage(NoCombatQuit.format(NoCombatQuit.prefix) + " " + NoCombatQuit.format(NoCombatQuit.message_ontag));
			} else {
				NoCombatQuit.tagged.remove(d.getName());
				NoCombatQuit.tagged.put(d.getName(), System.currentTimeMillis());
			}
		} else {
			Player p = (Player)e.getEntity();
			if(!NoCombatQuit.tagged.containsKey(p.getName())) {
				NoCombatQuit.tagged.put(p.getName(), System.currentTimeMillis());
				p.sendMessage(NoCombatQuit.format(NoCombatQuit.prefix) + " " + NoCombatQuit.format(NoCombatQuit.message_ontag));
			} else {
				NoCombatQuit.tagged.remove(p.getName());
				NoCombatQuit.tagged.put(p.getName(), System.currentTimeMillis());
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(NoCombatQuit.tagged.containsKey(p.getName())) {
			if(NoCombatQuit.command.equalsIgnoreCase("%killplayer%")) {
				p.setHealth(0.00);
			} else {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), NoCombatQuit.command.replace("%player%", p.getName()));
			}
			for(Player o: Bukkit.getOnlinePlayers()) {
				String m = NoCombatQuit.format(NoCombatQuit.prefix) + " " + NoCombatQuit.format(NoCombatQuit.message_onlog);
				m = m.replace("{%p}", p.getName());
				o.sendMessage(m);
			}
		}
	}
	
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if(NoCombatQuit.tagged.containsKey(e.getPlayer().getName())) {
			String cmd_name = e.getMessage().split(" ")[0].toLowerCase();
			String cmd_lower = e.getMessage().toLowerCase();
			for (String cmd : NoCombatQuit.instance.getConfig().getStringList("blocked-cmds")) {
				String cmd_blocked = cmd.toLowerCase();
				if (cmd_name.equals(cmd_blocked) || cmd_lower.startsWith(cmd_blocked)) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(NoCombatQuit.prefix + " " + NoCombatQuit.format(NoCombatQuit.message_nocommand));
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
		NoCombatQuit.tagged.remove(e.getEntity().getName());
	}
	
}
