package fr.aiidor.hardcore.enchants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantsRessources {
	
	public static String getName(Enchantment ench) {
		
		if (AiidorEnchant.allEnchants.contains(ench)) return ((AiidorEnchant) ench).getName();
		
		if (ench.equals(Enchantment.ARROW_DAMAGE)) return "Power";
		if (ench.equals(Enchantment.ARROW_FIRE)) return "Flame";
		if (ench.equals(Enchantment.ARROW_INFINITE)) return "Infinity";
		if (ench.equals(Enchantment.ARROW_KNOCKBACK)) return "Punch";
		if (ench.equals(Enchantment.BINDING_CURSE)) return "§cCurse of Binding§7";
		if (ench.equals(Enchantment.CHANNELING)) return "Channeling";
		if (ench.equals(Enchantment.DAMAGE_ALL)) return "Sharpness";
		if (ench.equals(Enchantment.DAMAGE_ARTHROPODS)) return "Bane of Arthropods";
		if (ench.equals(Enchantment.DAMAGE_UNDEAD)) return "Smite";
		if (ench.equals(Enchantment.DEPTH_STRIDER)) return "Depth Strider";
		if (ench.equals(Enchantment.DIG_SPEED)) return "Efficiency";
		if (ench.equals(Enchantment.DURABILITY)) return "Unbreaking";
		if (ench.equals(Enchantment.FIRE_ASPECT)) return "Fire Aspect";
		if (ench.equals(Enchantment.FROST_WALKER)) return "Frost Walker";
		if (ench.equals(Enchantment.IMPALING)) return "Impaling";
		if (ench.equals(Enchantment.KNOCKBACK)) return "Knockback";
		if (ench.equals(Enchantment.LOOT_BONUS_BLOCKS)) return "Fortune";
		if (ench.equals(Enchantment.LOOT_BONUS_MOBS)) return "Looting";
		if (ench.equals(Enchantment.LOYALTY)) return "Loyalty";
		if (ench.equals(Enchantment.LUCK)) return "Luck of the Sea";
		if (ench.equals(Enchantment.LURE)) return "Lure";
		if (ench.equals(Enchantment.MENDING)) return "Mending";
		if (ench.equals(Enchantment.MULTISHOT)) return "Multishot";
		if (ench.equals(Enchantment.OXYGEN)) return "Respiration";
		if (ench.equals(Enchantment.PIERCING)) return "Piercing";
		if (ench.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) return "Protection";
		if (ench.equals(Enchantment.PROTECTION_EXPLOSIONS)) return "Blast Protection";
		if (ench.equals(Enchantment.PROTECTION_FALL)) return "Feather Falling";
		if (ench.equals(Enchantment.PROTECTION_FIRE)) return "Fire Protection";
		if (ench.equals(Enchantment.PROTECTION_PROJECTILE)) return "Projectile Protection";
		if (ench.equals(Enchantment.QUICK_CHARGE)) return "Quick Charge";
		if (ench.equals(Enchantment.RIPTIDE)) return "Riptide";
		if (ench.equals(Enchantment.SILK_TOUCH)) return "Silk Touch";
		if (ench.equals(Enchantment.SWEEPING_EDGE)) return "Sweeping Edge";
		if (ench.equals(Enchantment.THORNS)) return "Thorns";
		if (ench.equals(Enchantment.VANISHING_CURSE)) return "§cCurse of Vanishing";
		if (ench.equals(Enchantment.WATER_WORKER)) return "Aqua Affinity";
		if (ench.equals(Enchantment.SOUL_SPEED)) return "Soul Speed";
		
		return "Unknown";
	}
	
	public static Boolean canEnchantItem(ItemStack item) {
		
		if (item.getItemMeta().hasCustomModelData()) {
			if (item.getItemMeta().getCustomModelData() != 0) {
				if (item.getType() == Material.STONE_SWORD) return false;
			}
		}
		
		return true;
	}

	public static String getLevelString(int level) {
		
		if (level == 0) return "";
		if (level == 1) return "I";
		if (level == 2) return "II";
		if (level == 3) return "III";
		if (level == 4) return "IV";
		if (level == 5) return "V";
		if (level == 6) return "VI";
		if (level == 7) return "VII";
		if (level == 8) return "VIII";
		if (level == 9) return "IX";
		if (level == 10) return "X";
		
		return "" + level;
	}
	
	public static Boolean hasConflict(Enchantment enchant1, Enchantment enchant2) {
		
		if (enchant1.equals(Enchantment.MENDING) && enchant2.equals(Enchantment.ARROW_INFINITE)) return false;
		if (enchant2.equals(Enchantment.MENDING) && enchant1.equals(Enchantment.ARROW_INFINITE)) return false;
		
		if (enchant1.conflictsWith(enchant2) || enchant2.conflictsWith(enchant1)) {
			return true;
		}
		
		return false;
	}
	
	public static Boolean isCompatible(List<Enchantment> enchants, Enchantment enchant) {
		
		for (Enchantment ench : enchants) {
			if (hasConflict(ench, enchant)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static Boolean isCompatible(HashMap<Enchantment, Integer> enchants, Enchantment enchant) {
		
		for (Enchantment ench : enchants.keySet()) {
			if (hasConflict(ench, enchant)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static  int getMaxLevel(Enchantment enchant) {
		
		if (enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) return 8;
		if (enchant.equals(Enchantment.PROTECTION_EXPLOSIONS)) return 8;
		if (enchant.equals(Enchantment.PROTECTION_FALL)) return 8;
		if (enchant.equals(Enchantment.PROTECTION_FIRE)) return 8;
		if (enchant.equals(Enchantment.THORNS)) return 5;
		
		if (enchant.equals(Enchantment.DAMAGE_ALL)) return 10;
		if (enchant.equals(Enchantment.DAMAGE_ARTHROPODS)) return 10;
		if (enchant.equals(Enchantment.DAMAGE_UNDEAD)) return 10;
		
		if (enchant.equals(Enchantment.FIRE_ASPECT)) return 4;
		
		if (enchant.equals(Enchantment.ARROW_DAMAGE)) return 8;
		if (enchant.equals(Enchantment.ARROW_KNOCKBACK)) return 3;
		
		if (enchant.equals(Enchantment.LUCK)) return 10;
		if (enchant.equals(Enchantment.LURE)) return 10;
		
		if (enchant.equals(Enchantment.DEPTH_STRIDER)) return 5;
		if (enchant.equals(Enchantment.RIPTIDE)) return 5;
		
		if (enchant.equals(Enchantment.LOOT_BONUS_BLOCKS)) return 5;
		if (enchant.equals(Enchantment.LOOT_BONUS_MOBS)) return 5;
		
		return enchant.getMaxLevel();
	}
	
	public static int getStartLevel (Enchantment enchant) {
		
		if (enchant instanceof AiidorEnchant) return((AiidorEnchant) enchant).getMinLevel();
		
		if (enchant.equals(Enchantment.ARROW_INFINITE)) return 30;
		if (enchant.equals(Enchantment.ARROW_FIRE)) return 20;
		
		if (enchant.equals(Enchantment.FIRE_ASPECT)) return 10;
		if (enchant.equals(Enchantment.THORNS)) return 30;
		
		if (enchant.equals(Enchantment.FROST_WALKER)) return 30;
		
		if (enchant.equals(Enchantment.SILK_TOUCH)) return 30;
		if (enchant.equals(Enchantment.SOUL_SPEED)) return 45;
		if (enchant.equals(Enchantment.LOOT_BONUS_BLOCKS)) return 10;
		
		return 1;
	}
	
	public static List<String> removeEnchantsLore(ItemMeta itemM) {
		
		List<String> lore = new ArrayList<String>();
		
		for (String string : itemM.getLore()) {
			
			for (Enchantment ench : AiidorEnchant.allEnchants) {
				if (string.contains(getName(ench))) {
					
					lore.remove(string);
				}
			}
		}
		
		return lore;
	}
	
	public static List<String> getLore(ItemMeta itemM) {
		
		List<String> lore = new ArrayList<String>();
		
		if (itemM.getLore() != null) lore = itemM.getLore();
		
		for (AiidorEnchant ench : AiidorEnchant.allEnchants) {

			for (String line : lore) {
				if (line.contains(ench.getName())) {
					lore.remove(line);
					break;
				}
			}
			
			for (Enchantment enchant : itemM.getEnchants().keySet()) {
				
				if (enchant.equals(ench)) {
						
					if (ench.isRune()) {
							
						if (ench.getMaxLevel() == 1 || ench.getMaxLevel() == 0) {
								
							lore.add("§aRune : " + getName(enchant));
						} else {
								lore.add("§aRune : " + getName(enchant) + " " + getLevelString(itemM.getEnchantLevel(enchant)));
						}
							
					} else {
							
						if (ench.getMaxLevel() == 1 || ench.getMaxLevel() == 0) {
								
							lore.add("§7" + getName(enchant));
						} else {
							lore.add("§7" + getName(enchant) + " " + getLevelString(itemM.getEnchantLevel(enchant)));
						}
						
					}
				}
			}
		}
		
		return lore;
	}
	
	public static List<String> getStoredLore(EnchantmentStorageMeta itemM) {
		
		List<String> lore = new ArrayList<String>();
		
		if (itemM.getLore() != null) lore = itemM.getLore();
		
		for (Enchantment enchant : itemM.getStoredEnchants().keySet()) {
			
			for (Enchantment ench : AiidorEnchant.allEnchants) {
				
				if (enchant.equals(ench)) {
					
					if (ench.getMaxLevel() == 1) {
						lore.add("§7" + getName(enchant));
					} else {
						lore.add("§7" + getName(enchant) + " " + getLevelString(itemM.getStoredEnchantLevel(enchant)));
					}
					
				}
			}
		}

		
		return lore;
	}

}
