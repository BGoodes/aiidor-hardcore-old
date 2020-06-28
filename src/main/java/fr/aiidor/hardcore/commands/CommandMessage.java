package fr.aiidor.hardcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandMessage implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(Plugin.prefix + "§eLa commande s'écrit /" + label.toLowerCase() + " <joueur> <message>");
			return true;
		}
		
		if (args.length < 2) {
			player.sendMessage(Plugin.prefix + "§cErreur, la commande s'écrit /" + label.toLowerCase() + " <joueur> <message>");
			return true;
		}
		
		String targetname = args[0];
		if (!isConnected(targetname)) {
			player.sendMessage(Plugin.prefix + "§cCe joueur n'est pas connecté ou n'existe pas !");
			return true;
		}
		
		Player target = getPlayer(targetname);
		
		if (sender.getName().equals(targetname)) {
			player.sendMessage(Plugin.prefix + "§cVous ne pouvez envoyer un message à vous-même !");
			return true;
		}
		
		StringBuilder msg = new StringBuilder();
		
		for (int i = 1; i != args.length; i++) {
			
			if (i + 1 < args.length) msg.append(args[i] + " ");
			else msg.append(args[i]);
		}
		
		sender.sendMessage("§8[§7Moi §7-> §b" + targetname + "§8] §8>> §6" + msg.toString());
		
		TextComponent infos = new TextComponent("§8[§b" + player.getDisplayName() + " §7-> Moi" + "§8] ");
		TextComponent rep = new TextComponent("§6[R] ");
		TextComponent send = new TextComponent("§8>> §6" + msg.toString());
		
		rep.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Répondre").create()));
		rep.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));
		
		target.spigot().sendMessage(infos, rep, send);
		return true;
	}
	
	private Boolean isConnected(String name) {
		return getPlayer(name) != null;
	}
	
	private Player getPlayer(String name) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(name)) return p;
		}
		return null;
	}
}
