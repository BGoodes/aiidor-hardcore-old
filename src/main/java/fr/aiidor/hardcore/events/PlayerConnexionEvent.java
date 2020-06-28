package fr.aiidor.hardcore.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;

public class PlayerConnexionEvent implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		player.setInvulnerable(false);
		
		if (player.isOp()) {
			player.setPlayerListName("§c" + player.getName());
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Plugin.getInstance().getEntityManager().vanish.contains(p)) player.hidePlayer(Plugin.getInstance(), p);
		}
		
		if (!econManager.hasAccount(player)) {
			
			econManager.createAccount(player);
			e.setJoinMessage(Plugin.prefix + "§6" + player.getName() + "§e a decidé de rejoindre l'Aventure ! §a(+)");
			
		} else {
			e.setJoinMessage(Plugin.prefix + "§6" + player.getName() + "§e est de retour ! §a(+)");
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		if (Plugin.getInstance().getEntityManager().vanish.contains(player)) 
			Plugin.getInstance().getEntityManager().vanish.remove(player);
		
		if (Plugin.getInstance().getGuildManager().invites.containsKey(player.getUniqueId())) 
			Plugin.getInstance().getGuildManager().invites.remove(player.getUniqueId());
		
	}
}
