package fr.aiidor.hardcore.rework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.enchants.AiidorEnchant;
import fr.aiidor.hardcore.enchants.EnchantsRessources;

public class Enchant_Table_Inv {
	
	private Player player;
	
	private ItemStack LastItem;
	
	private Inventory inventory;
	private Block enchant;
	
	private HashMap<Enchantment, Integer> enchantsMin = new HashMap<>();
	private HashMap<Enchantment, Integer> enchantsRan = new HashMap<>();
	private HashMap<Enchantment, Integer> enchantsMax = new HashMap<>();
	
	private Integer RandomPrice = null;
	
	public Enchant_Table_Inv(Player player, Block enchant_table) {
		
		this.player = player;
		
		inventory = Bukkit.createInventory(null, 45, "§dTable d'enchantement");
		enchant = enchant_table;
		
		for (int i = 0; i != 45; i++) {
			inventory.setItem(i, getItem(Material.GRAY_STAINED_GLASS_PANE));
		}
		
		inventory.setItem(13, new ItemStack(Material.AIR));
		removeEnchants();
	}
	
	public ItemStack getLastItem() {
		return LastItem;
	}
	
	public void setLastItem() {
		LastItem = getPlayerItem();
	}
	
	public void setLastItem(ItemStack item) {
		LastItem = item;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Block getEnchantTable() {
		return enchant;
	}
	
	public ItemStack getPlayerItem() {
		return inventory.getItem(13);
	}
	
	public void removeEnchants() {
		
		inventory.setItem(29, getItem(Material.BARRIER));
		inventory.setItem(31, getItem(Material.BARRIER));
		inventory.setItem(33, getItem(Material.BARRIER));
		
		enchantsMin = new HashMap<Enchantment, Integer>();
		enchantsRan = new HashMap<Enchantment, Integer>();
		enchantsMax = new HashMap<Enchantment, Integer>();
		
		RandomPrice = null;
		
		player.updateInventory();
	}
	
	public HashMap<Enchantment, Integer> getResult(TableResult result) {
		
		if (result == TableResult.Min) return enchantsMin;
		if (result == TableResult.Random) return enchantsRan;
		if (result == TableResult.Max) return enchantsMax;
		
		return null;
	}
	
	public void generateEnchants() {
		
		ItemStack item = getPlayerItem();
		
		if  (item == null || item.getType() == Material.AIR) {
			removeEnchants();
			return;
		}
		
		List<Enchantment> allowedEnchant = new ArrayList<>();

		if (getPlayerItem() != null && getPlayerItem().getType() == Material.BOOK) {
			
			//BOOK
			for (Enchantment enchant : Enchantment.values()) {
				
				if (enchant instanceof AiidorEnchant) {
					AiidorEnchant aiidorEnchant = (AiidorEnchant) enchant;
						
					if (!aiidorEnchant.isRune()) allowedEnchant.add(enchant);
						
				} else {
					allowedEnchant.add(enchant);
				}
					
			}
			
		} else {
			
			//ITEM
			for (Enchantment enchant : Enchantment.values()) {
				if (enchant.canEnchantItem(item)) {
					allowedEnchant.add(enchant);
				}
			}
			
		}
		
		if (allowedEnchant.contains(Enchantment.MENDING)) allowedEnchant.remove(Enchantment.MENDING);
		
		if (!EnchantsRessources.canEnchantItem(item)) allowedEnchant.clear();
		
		generateEnchants(allowedEnchant, TableResult.Min);
		generateEnchants(allowedEnchant, TableResult.Max);
		generateEnchants(allowedEnchant, TableResult.Random);
		
		for (TableResult result : TableResult.values()) {
			inventory.setItem(result.getSlot(), getEnchantIcon(result));
		}
		
		player.updateInventory();
	}
	
	private void generateEnchants(List<Enchantment> allowedEnchant, TableResult result) {
		
		List<Enchantment> enchants = new ArrayList<Enchantment>();
		
		Random ran = new Random();
		
		int Enchant_number = 1;
		int Enchant_Number_Random = ran.nextInt(100);
		
		int Level = getLevel(result);
		
		if (Level * 2 >= Enchant_Number_Random) Enchant_number = 2;
		if (Level >= Enchant_Number_Random) Enchant_number = 3;
		if (Level /2 >= Enchant_Number_Random) Enchant_number = 4;
		
		if (Level == 60) {
			if (30 >= Enchant_Number_Random) Enchant_number = 5;
		}
		
		for (int i = Enchant_number; i > 0; i--) {
			
			if (!allowedEnchant.isEmpty()) {
				
				Enchantment ench = allowedEnchant.get(ran.nextInt(allowedEnchant.size()));
				allowedEnchant.remove(ench);
				
				if (EnchantsRessources.isCompatible(enchants, ench) && Level >= EnchantsRessources.getStartLevel(ench) && !enchants.contains(ench)) {
					enchants.add(ench);
				}
			}

		}
		
		for (Enchantment ench : enchants) {
			int enchant_level = 1;
			
			for (int i = 1; i <= EnchantsRessources.getMaxLevel(ench); i++) {
				
				if (i <= getLimit()) {
					int proba = (getLevel(result) * 10) / i;
					
					if (proba > ran.nextInt(100)) {
						enchant_level = i;
					}
				}
			}
			
			if (result == TableResult.Min) enchantsMin.put(ench, enchant_level);
			else if (result == TableResult.Random) enchantsRan.put(ench, enchant_level);
			else if (result == TableResult.Max) enchantsMax.put(ench, enchant_level);
		}

	}
	
	private Integer getLimit() {
		
		if (getTier() == 0) return 1;
		if (getTier() == 1 || getTier() == 2) return 2;
		if (getTier() == 3) return 3;
		if (getTier() == 4) return 4;
		if (getTier() == 5) return 6;
		if (getTier() == 6) return 8;
		
		return 1;
	}
	
	public Integer getTier() {
		
		int tier = 0;
		int bookshelf = 0;
		
		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				for (int y = 0; y <= 2; y++) {
					
					Block b = enchant.getWorld().getBlockAt(enchant.getLocation().getBlockX() + x, enchant.getLocation().getBlockY() + y, enchant.getLocation().getBlockZ() + z);
					
					if (!(Math.abs(x) + Math.abs(z) <= 2 && Math.abs(x) != 2 && Math.abs(z) != 2)) {
						
						if (b != null) {
							if (b.getType() == Material.BOOKSHELF) {
								bookshelf ++;
							}
						}
					} else {
						
						if (b.getType() == Material.SOUL_TORCH) {
							tier--;
						}
					}
				}
			}
		}
		
		if (bookshelf >= 4) {
			tier ++;
		}
		
		if (bookshelf >= 9) {
			tier ++;
		}
		
		if (bookshelf >= 15) {
			tier ++;
		}
		
		if (bookshelf >= 30) {
			tier ++;
		}
		
		if (bookshelf >= 59) {
			tier ++;
		}
		
		if (bookshelf >= 88) {
			tier ++;
		}
		
		if (tier < 0) tier = 0;
		
		return tier;
	}
	
	public int getRemoveLevels(TableResult result) {
		
		if (getTier() == 0 || getTier() == 1 || getTier() == 2) return 1;
		
		if (getTier() == 3) {
			if (result == TableResult.Min) return 1;
			if (result == TableResult.Random) return 2;
			if (result == TableResult.Max) return 3;
		}
		
		if (getTier() == 4) {
			if (result == TableResult.Min) return 2;
			if (result == TableResult.Random) return 3;
			if (result == TableResult.Max) return 4;
		}
		
		if (getTier() == 5) {
			if (result == TableResult.Min) return 2;
			if (result == TableResult.Random) return 4;
			if (result == TableResult.Max) return 6;
		}
		
		if (getTier() == 6) {
			if (result == TableResult.Min) return 3;
			if (result == TableResult.Random) return 5;
			if (result == TableResult.Max) return 8;
		}
		
		return 999;
	}
	
	public int getLevel(TableResult result) {
		
		if (result == TableResult.Random) {
			if (RandomPrice != null) {
				return RandomPrice;
			} else {
				
				RandomPrice = getLevel(TableResult.Min) + (int)(Math.random() * ((getLevel(TableResult.Max) - getLevel(TableResult.Min)) + 1)); 
				// Min + (int)(Math.random() * ((Max - Min) + 1));
				return RandomPrice;
			}
			
		}
		
		if (getTier() == 0) {
			if (result == TableResult.Min) return 1;
			if (result == TableResult.Max) return 3;
		}
		
		if (getTier() == 1) {
			if (result == TableResult.Min) return 3;
			if (result == TableResult.Max) return 6;
		}
		
		if (getTier() == 2) {
			if (result == TableResult.Min) return 6;
			if (result == TableResult.Max) return 10;
		}
		
		if (getTier() == 3) {
			if (result == TableResult.Min) return 10;
			if (result == TableResult.Max) return 20;
		}
		
		if (getTier() == 4) {
			if (result == TableResult.Min) return 20;
			if (result == TableResult.Max) return 30;
		}
		
		if (getTier() == 5) {
			if (result == TableResult.Min) return 30;
			if (result == TableResult.Max) return 45;
		}
		
		if (getTier() == 6) {
			if (result == TableResult.Min) return 45;
			if (result == TableResult.Max) return 60;
		}
		
		return 999;
	}
	
	private ItemStack getItem(Material mat, String name) {
		
		ItemStack item = new ItemStack(mat);
		ItemMeta itemM = item.getItemMeta();
		
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		if (name != null) itemM.setDisplayName(name);
		item.setItemMeta(itemM);
		
		
		return item;
	}
	
	private ItemStack getItem(Material mat) {
		return getItem(mat, "§7 ");
	}
	
	public List<Enchantment> getEnchantementList(TableResult result) {
		
		List<Enchantment> enchants = new ArrayList<Enchantment>();
		
		if (result == TableResult.Min) {
			for (Enchantment ench : enchantsMin.keySet()) {
				enchants.add(ench);
			}
		}
		
		if (result == TableResult.Random) {
			for (Enchantment ench : enchantsRan.keySet()) {
				enchants.add(ench);
			}
		}
		
		if (result == TableResult.Max) {
			for (Enchantment ench : enchantsMax.keySet()) {
				enchants.add(ench);
			}
		}
		
		return enchants;
	}
	
	private ItemStack getEnchantIcon(TableResult result) {
		
		HashMap<Enchantment, Integer> enchants = new HashMap<>();
		
		if (result == TableResult.Min) enchants.putAll(enchantsMin);
		if (result == TableResult.Random) enchants.putAll(enchantsRan);
		if (result == TableResult.Max) enchants.putAll(enchantsMax);
		
		if (enchants.isEmpty()) {
			return getItem(Material.BARRIER);
		}
		
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		ItemMeta itemM = item.getItemMeta();
		
		itemM.setDisplayName("§a" + getLevel(result) + "§a Niveaux");
		
		List<String> lore = new ArrayList<>();
		
		int garantis = 1;
		
		Random ran = new Random();
		
		if (result == TableResult.Random && getTier() > 4) garantis = 2;
		
		if (result == TableResult.Max && getTier() == 4) garantis = 2;
		if (result == TableResult.Max && getTier() > 4) garantis = 3;
		
		if (!enchants.isEmpty()) {
			
			if (enchants.size() > garantis) {
				
				while (enchants.size() > garantis) {
					enchants.remove(getEnchantementList(result).get(ran.nextInt(getEnchantementList(result).size())));
				}
			}
			
			if (enchants.size() == 1) {
				lore.add("§dL'enchantement Garanti :");
				
			} else {
				lore.add("§dLes enchantements Garantis :");
			}
			
			for (Enchantment ench : enchants.keySet()) {
				
				StringBuilder name = new StringBuilder();
				
				name.append("§7- " + EnchantsRessources.getName(ench));
				
				if (ench.getMaxLevel() > 1) {
					name.append(" " + EnchantsRessources.getLevelString(enchants.get(ench)));
				} 
				
				if (ench instanceof AiidorEnchant) {
					AiidorEnchant enchant = (AiidorEnchant) ench;
					
					if (enchant.isRune()) {
						name.append(" §a(Rune)");
					}
				}
				
				lore.add(name.toString());
			}
		}
		
		lore.add(" ");
		if (getLevel(result) == 1) lore.add("§b§oCoûte " + getRemoveLevels(result) + " niveau !");
		else lore.add("§b§oCoûte " + getRemoveLevels(result) + " niveaux !");
		
		itemM.setLore(lore);
		
		item.setItemMeta(itemM);
		
		return item;
	}
	
	public enum TableResult {
		Min(29), Max(33), Random(31);
		
		private int slot;
		
		private TableResult(int slot) {
			this.slot = slot;
		}
		
		public int getSlot() {
			return slot;
		}
	}
}
