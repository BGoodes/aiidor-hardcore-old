package fr.aiidor.hardcore.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EconManager.Account;

public class CommandBaltop implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		if (args.length != 0) {
			sendMessage(sender, "[Aiidor]§cErreur, la commande s'écrit /baltop !");
			return true;
		}
		
		if (econManager.getAllAccounts().isEmpty()) {
			sendMessage(sender, "[Aiidor]§eIl n'y a aucun joueur avec un classement !");
			return true;
		}
		
		sendMessage(sender, "[Aiidor]§6Le classement :");
		
		List<Account> accounts = new ArrayList<EconManager.Account>();
		econManager.getAllAccounts().stream().filter(a-> a.getOwnerName() != null).forEach(a->accounts.add(a));
		accounts.sort(new AccountComparator());
		
		int place = 1;
		for (Account a : accounts) {
			sendMessage(sender, " " + getPlaceColor(place) + place + " - §a" + a.getOwnerName() + " :§e " + a.getBalance() + " " + EconManager.devise);
			place++;
			
			if (place == 11) break;
		}
		
		return true;
	}
	
	class AccountComparator implements Comparator<Account> {

		
		@Override
		public int compare(Account a1, Account a2) {
			
			if (a1.getBalance() == a2.getBalance()) return a1.getOwnerName().compareTo(a2.getOwnerName());
				
			if (a1.getBalance() > a2.getBalance()) return -1;
			else return 1;
		}
	}
	
	private String getPlaceColor(int place) {
		
		if (place == 1) return "§e";
		if (place == 2) return "§7";
		if (place == 3) return "§6";
		return "§f";
	}
	private void sendMessage(CommandSender sender, String msg) {
		if (sender instanceof Player) {
			
			Player p = (Player) sender;
			p.sendMessage(msg.replace("[Aiidor]", Plugin.prefix));
			
		} else {
			System.out.println(msg.replace("[Aiidor]", "[Aiidor] ")
						.replace("§a", "")
						.replace("§e", "")
						.replace("§c", "")
						.replace("§6", "")
						.replace("§2", "")
						.replace("§9", "")
						.replace("§f", "")
						.replace("§d", "")
						.replace("§7", "")
						
						.replace("é", "e")
						.replace("è", "e")
						.replace("à", "a")
						.replace("ê", "e")
						.replace("û", "u")
			);
		}
	}

}
