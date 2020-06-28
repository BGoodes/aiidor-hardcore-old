package fr.aiidor.hardcore.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.enchants.EnchantsRessources;

public class CommandEnchant implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("[Aiidor] Seul un joueur peut effectuer cette commande !");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(Plugin.prefix  + "§eLa commande s'écrit /" + label.toLowerCase() + " <enchantement> <niveau>");
			return true;
		}
		
		if (args.length != 2) {
			player.sendMessage(Plugin.prefix  + "§cErreur, la commande s'écrit /" + label.toLowerCase() + " <enchantement> <niveau>");
			return true;
		}
		
		Key key = null;
		
		String enchantKey = args[0];
		
		for (Key value : Key.values()) {
			if (enchantKey.toLowerCase().startsWith(value.name)) {
				key = value;
			}
		}
		
		if (key == null) {
			player.sendMessage(Plugin.prefix  + "§cErreur, l'enchantement doit être écrit selon le format NamespacedKey.");
			return true;
		}
		
		Enchantment enchant = null;
		
		enchantKey = enchantKey.substring(key.name.length());
		enchantKey = enchantKey.toLowerCase();
		
		if (key == Key.MINECRAFT) {
			enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantKey));
		}
		
		if (key == Key.AIIDOR) {
			enchant = Enchantment.getByKey(new NamespacedKey(Plugin.getInstance(), enchantKey));
		}
		
		if (enchant == null) {
			player.sendMessage(Plugin.prefix  + "§cErreur, cette enchantement n'existe pas !");
			return true;
		}
			
		Integer level;
		
		try {
			level = Integer.valueOf(args[1]);
				
		} catch (Exception e) {
			player.sendMessage(Plugin.prefix + "§cLe niveau doit être un nombre entier.");
			return true;
		} 
			
		if (level > 10000 || level < 0) {
			player.sendMessage(Plugin.prefix  + "§cVous devez choisir un niveau compris dans l'intervalle allant de 0 à 1000.");
			return true;
		}
		
		if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
			player.sendMessage(Plugin.prefix  + "§cVous devez tenir un item dans votre main principal pour l'enchanter !");
			return true;
		}
		
		ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
		
		if (level == 0) {
			
			if (meta.hasEnchant(enchant)) {
				
				meta.removeEnchant(enchant);
				
				if (key == Key.AIIDOR) {
					meta.setLore(EnchantsRessources.getLore(meta));
				}
				
				player.getInventory().getItemInMainHand().setItemMeta(meta);
			}
			
			player.sendMessage(Plugin.prefix + "§aDésenchantement réussi !");
			player.playSound(player.getLocation(), Sound.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 1, 1);
			return true;
		}
		
		//ADD ENCHANT + LORE
		meta.addEnchant(enchant, level, true);
		
		if (key == Key.AIIDOR) {
			meta.setLore(EnchantsRessources.getLore(meta));
		}
		
		player.getInventory().getItemInMainHand().setItemMeta(meta);
		
		player.sendMessage(Plugin.prefix  + "§aEnchantement réussi !");
		player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, 1);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 1) {
			
			List<String> completion = new ArrayList<>();
			
			for (Enchantment enchant : Enchantment.values()) {
				
				String string = enchant.getKey().toString();
				
				if (args[0].equals("") || string.toLowerCase().startsWith(args[0].toLowerCase())) {
					completion.add(string);
				}
				
				for (Key key : Key.values()) {
					if (string.toLowerCase().substring(key.name.length()).startsWith(args[0].toLowerCase())) {
						
						if (!completion.contains(string)) completion.add(string);
					}
				}

			}
				
			Collections.sort(completion);
			return completion;
		}
		
		return null;
	}
	
	private enum Key {
		MINECRAFT("minecraft:"), AIIDOR("aiidor:");
		
		public String name;
		
		private Key(String name) {
			this.name = name;
		}
	}
}
