package fr.aiidor.hardcore.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.GuildManager;
import fr.aiidor.hardcore.managers.GuildManager.Guild;

public class ChatEvents implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		
		String prefix = "§f" + player.getName() + "§8 »§7 ";
		String msg = e.getMessage();
		
		e.setCancelled(true);
		
		GuildManager guildManager = Plugin.getInstance().getGuildManager();
		Guild guild = null;
		
		if (guildManager.hasGuild(player)) {
			
			guild = guildManager.getPlayerGuild(player);
			
			if (msg.startsWith("@") && msg.length() > 1) {
								
				prefix = "§d" + player.getName() + "§8 »§7 §b@";
				
				if (guild.isMaster(player.getUniqueId())) prefix = "§5" + player.getName() + "§8 »§7 §b@§7";
				
				guild.broadcast(prefix + msg.substring(1));
				return;
			}
		}
		
		if (player.isOp()) prefix = "§c" + player.getName() + "§8 »§f ";
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			
			String before = prefix;
			if (guild != null) {
				if (guildManager.hasGuild(p) && guildManager.getPlayerGuild(p).equals(guild)) {
					
					before = "§f[§d" + guild.getName() + "§f]§f " + prefix;
				} else {
					before = "§f[§9" + guild.getName() + "§f]§f " + prefix;
				}
			}
			
			if (msg.contains(p.getName())) {
				
				p.sendMessage(before + msg.replace(p.getName(), "§b" + p.getName()));
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.PLAYERS, 0.5f, 1f);
				
			} else {
				p.sendMessage(before + msg);
			}
		}
		
		System.out.println(prefix + msg);
	}
}
