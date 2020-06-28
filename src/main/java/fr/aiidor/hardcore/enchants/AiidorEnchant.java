package fr.aiidor.hardcore.enchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public abstract class AiidorEnchant extends Enchantment {
	
	public AiidorEnchant( NamespacedKey key) {
		super(key);
	}
	
	public abstract String getName(); 
	public abstract boolean isRune(); 
	
	public abstract int getMinLevel(); 
	
	@Override
	public boolean isTreasure() {
		return false;
	}

	//STATICS
	public static List<AiidorEnchant> allEnchants = new ArrayList<>();
	
	public static Smelting_Touch SMELTING_TOUCH;
	public static Cubism CUBISM;
	public static Long_Bow LONG_BOW;
	public static Leveling LEVELING;
	public static Feather_Wiper FEATHER_WIPER;
	public static Balloon_Piercer BALLON_PIERCER;
	public static Self_Defense SELF_DEFENSE;
	public static Replanting REPLANTING;
	public static Inhuman INHUMAN;
	public static Flinging FLINGING;
	public static Stomping STOMPING;
	public static Natural_Blocking NATURAL_BLOCKING;
	public static Defuse DEFUSE;
	public static Short_Bow SHORT_BOW;
}
