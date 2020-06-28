package fr.aiidor.hardcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EconManager.Account;

public class CommandMoney implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		if (!(sender instanceof Player)) {
			
			if (args.length == 0) {
				System.out.println("[Money] Seul un joueur peut effectuer cette commande !");
				return true;
			}
			
			if (args.length != 1) {
				System.out.println("[Money] La commande s'ecrit /money <joueur>");
				return true;
			}
			
			if (econManager.hasAccount(args[0])) {
				Account account = econManager.getAccount(args[0]);
				System.out.println("[Money] " + args[0] + " a " + account.getBalance()  + " " + EconManager.devise);
			} else {
				System.out.println("[Money] Ce joueur n'a pas de compte !");
			}
			
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			if (!econManager.hasAccount(player.getUniqueId())) {
				player.sendMessage(Plugin.prefix + "§cVous n'avez pas de compte !");
				return true;
			}
			
			player.sendMessage(Plugin.prefix + "§6Vous détenez §e" + econManager.getAccount(player).getBalance()  + " " +  EconManager.devise);
			return true;
		}
		
		if (args.length == 1) {
			if (!econManager.hasAccount(args[0])) {
				player.sendMessage(Plugin.prefix + "§cCe joueur n'a pas de compte ou n'existe pas !");
				return true;
			} else {
				if (args[0].equals(player.getName())) {
					player.sendMessage(Plugin.prefix + "§6Vous détenez §e" + econManager.getAccount(player).getBalance() + " " +  EconManager.devise);
					return true;
				}
				if (!player.hasPermission("econ.check")) {
					player.sendMessage(Plugin.prefix + "§cVous n'avez pas le droit de regarder le compte d'un autre joueur.");
					return true;
				}
				player.sendMessage(Plugin.prefix + "§6Le joueur " + args[0] + " détient §e" + econManager.getAccount(player).getBalance()  + " " +  EconManager.devise);
			}
			return true;
		}
		
		player.sendMessage(Plugin.prefix + "§cLa commande s'écrit /money");
		return false;
	}

}
