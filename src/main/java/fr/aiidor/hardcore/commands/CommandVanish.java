package fr.aiidor.hardcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EntityManager;

public class CommandVanish implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Vous devez etre un jouer pour effectuer cette commande");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length != 0) {
			player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit /vanish.");
			return true;
		}
		
		EntityManager m = Plugin.getInstance().getEntityManager();
		
		if (!m.vanish.contains(player)) {
			//METTRE INVISIBLE
			
			m.vanish.add(player);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				player.showPlayer(Plugin.getInstance(), p);
				if (!m.vanish.contains(p)) p.hidePlayer(Plugin.getInstance(), player);
			}
			
			player.sendMessage(Plugin.prefix + "§aVous êtes désormais invisible sur le serveur !");
			
		} else {
			//METTRE VISIBLE
			
			m.vanish.remove(player);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				p.showPlayer(Plugin.getInstance(), player);
				if (m.vanish.contains(p)) player.hidePlayer(Plugin.getInstance(), p);
			}
			
			player.sendMessage(Plugin.prefix + "§aVous n'êtes désormais plus invisible sur le serveur !");
		}
		
		return true;
	}

}
