package fr.aiidor.hardcore.managers;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.enchants.AiidorEnchant;
import fr.aiidor.hardcore.enchants.Balloon_Piercer;
import fr.aiidor.hardcore.enchants.Cubism;
import fr.aiidor.hardcore.enchants.Defuse;
import fr.aiidor.hardcore.enchants.Feather_Wiper;
import fr.aiidor.hardcore.enchants.Flinging;
import fr.aiidor.hardcore.enchants.Inhuman;
import fr.aiidor.hardcore.enchants.Leveling;
import fr.aiidor.hardcore.enchants.Long_Bow;
import fr.aiidor.hardcore.enchants.Natural_Blocking;
import fr.aiidor.hardcore.enchants.Replanting;
import fr.aiidor.hardcore.enchants.Self_Defense;
import fr.aiidor.hardcore.enchants.Short_Bow;
import fr.aiidor.hardcore.enchants.Smelting_Touch;
import fr.aiidor.hardcore.enchants.Stomping;
import fr.aiidor.hardcore.rework.Anvil_Inv;
import fr.aiidor.hardcore.rework.Enchant_Table_Inv;

public class EnchantManager {
	
	public HashMap<Player, Enchant_Table_Inv> enchants = new HashMap<>();
	public HashMap<Player, Anvil_Inv> anvils = new HashMap<>();
	
	public void load() {
		
		Plugin pl = Plugin.getInstance();
		//SMELTING TOUCH -----------------
		AiidorEnchant.SMELTING_TOUCH = new Smelting_Touch(new NamespacedKey(pl, "smelting_touch"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.SMELTING_TOUCH);
		
		//CUBISM ------------------------
		AiidorEnchant.CUBISM = new Cubism(new NamespacedKey(pl, "cubism"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.CUBISM);
		
		//LONG SHOT ------------------------
		AiidorEnchant.LONG_BOW = new Long_Bow(new NamespacedKey(pl, "long_bow"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.LONG_BOW);
		
		//LEVELING ------------------------
		AiidorEnchant.LEVELING = new Leveling(new NamespacedKey(pl, "leveling"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.LEVELING);
		
		//FEATHER WIPER ------------------------
		AiidorEnchant.FEATHER_WIPER = new Feather_Wiper(new NamespacedKey(pl, "feather_wiper"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.FEATHER_WIPER);
		
		//BALLON PIERCER ------------------------
		AiidorEnchant.BALLON_PIERCER = new Balloon_Piercer(new NamespacedKey(pl, "ballon_piercer"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.BALLON_PIERCER);
		
		//Self_Defense ------------------------
		AiidorEnchant.SELF_DEFENSE = new Self_Defense(new NamespacedKey(pl, "self_defense"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.SELF_DEFENSE);
		
		//Replanting ------------------------
		AiidorEnchant.REPLANTING = new Replanting(new NamespacedKey(pl, "replanting"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.REPLANTING);
		
		//Inhuman ------------------------
		AiidorEnchant.INHUMAN = new Inhuman(new NamespacedKey(pl, "inhuman"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.INHUMAN);
		
		//Flinging ------------------------
		AiidorEnchant.FLINGING = new Flinging(new NamespacedKey(pl, "flinging"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.FLINGING);
		
		//STOMPING ------------------------
		AiidorEnchant.STOMPING = new Stomping(new NamespacedKey(pl, "stomping"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.STOMPING);
		
		//NATURAL_BLOCKING ---------------------
		AiidorEnchant.NATURAL_BLOCKING = new Natural_Blocking(new NamespacedKey(pl, "natural_blocking"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.NATURAL_BLOCKING);
		
		//DEFUSE ------------------------
		AiidorEnchant.DEFUSE = new Defuse(new NamespacedKey(pl, "defuse"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.DEFUSE);
		
		//DEFUSE ------------------------
		AiidorEnchant.SHORT_BOW = new Short_Bow(new NamespacedKey(pl, "short_bow"));
		AiidorEnchant.allEnchants.add(AiidorEnchant.SHORT_BOW);
		
		for (AiidorEnchant ench : AiidorEnchant.allEnchants) {
			registerEnchantment(ench);
			
			if (ench instanceof Listener) {
				Bukkit.getPluginManager().registerEvents((Listener) ench, pl);
			}
		}
	}
	
	public void unload() {
		for (AiidorEnchant ench : AiidorEnchant.allEnchants) {
			unregisterEnchantment(ench);
		}
	}

	
	public void unregisterEnchantment(AiidorEnchant enchantment) {
		try {
		    Field keyField = Enchantment.class.getDeclaredField("byKey");
		 
		    keyField.setAccessible(true);
		    @SuppressWarnings("unchecked")
		    HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
		 
		    if(byKey.containsKey(enchantment.getKey())) {
		        byKey.remove(enchantment.getKey());
		    }
		    
		    Field nameField = Enchantment.class.getDeclaredField("byName");

		    nameField.setAccessible(true);
		    @SuppressWarnings("unchecked")
		    HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

		    if(byName.containsKey(enchantment.getName())) {
		        byName.remove(enchantment.getName());
		    }
		    
		} catch (Exception ignored) { };
	}
	
	public void registerEnchantment(Enchantment enchantment) {
		
	    try {
	        Field f = Enchantment.class.getDeclaredField("acceptingNew");
	        f.setAccessible(true);
	        f.set(null, true);
	        Enchantment.registerEnchantment(enchantment);
	    
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
