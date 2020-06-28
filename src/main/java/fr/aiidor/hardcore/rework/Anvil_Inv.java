package fr.aiidor.hardcore.rework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.enchants.EnchantsRessources;

public class Anvil_Inv {
	
	private Player player;
	
	private Inventory inventory;
	private Block anvil;
	
	private Boolean BaseState = false;
	private Boolean OtherState = false;
	
	public ItemStack LastBaseItem = null;
	public ItemStack LastOtherItem = null;
	
	private int price = 0;
	
	public Anvil_Inv(Player player, Block anvil) {
		
		this.player = player;
		
		inventory = Bukkit.createInventory(null, 45, "§bEnclume");
		this.anvil = anvil;
		
		for (int i = 0; i != 45; i++) {
			inventory.setItem(i, getItem(Material.GRAY_STAINED_GLASS_PANE));
		}
		
		inventory.setItem(AnvilSlot.BASE.getSlot(), new ItemStack(Material.AIR));
		
		inventory.setItem(20, getItem(Material.RED_STAINED_GLASS_PANE));
		inventory.setItem(11, getItem(Material.RED_STAINED_GLASS_PANE));
		inventory.setItem(12, getItem(Material.RED_STAINED_GLASS_PANE));
		
		inventory.setItem(AnvilSlot.RESULT.getSlot(), getItem(Material.BARRIER));

		inventory.setItem(14, getItem(Material.RED_STAINED_GLASS_PANE));
		inventory.setItem(15, getItem(Material.RED_STAINED_GLASS_PANE));
		inventory.setItem(24, getItem(Material.RED_STAINED_GLASS_PANE));
		
		inventory.setItem(AnvilSlot.OTHER.getSlot(), new ItemStack(Material.AIR));
		
		inventory.setItem(8, getItem(Material.NAME_TAG, "§aRenommer ?"));
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Block getAnvil() {
		return anvil;
	}
	
	public ItemStack getResult() {
		return inventory.getItem(AnvilSlot.RESULT.getSlot());
	}
	
	public ItemStack getBaseItem() {
		return inventory.getItem(AnvilSlot.BASE.getSlot());
	}
	
	public ItemStack getOtherItem() {
		return inventory.getItem(AnvilSlot.OTHER.getSlot());
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getLevelPrice() {
		return price;
	}
	
	public void setResut() {
		
		ItemStack base = getBaseItem();
		
		price = 0;
		
		if (base == null || base.getType() == Material.AIR || !canEnchant(base) || base.getAmount() != 1) {
			setValid(AnvilSlot.BASE, false);
		} else {
			setValid(AnvilSlot.BASE, true);
		}
		
		ItemStack other = getOtherItem();
		
		if (other == null || other.getType() == Material.AIR || !canEnchant(other) || other.getAmount() != 1) {
			setValid(AnvilSlot.OTHER, false);
		} else {
			setValid(AnvilSlot.OTHER, true);
		}
		
		if (!BaseState || !OtherState) return;
		
		
		if (base.getType() == other.getType() && base.getType() != Material.ENCHANTED_BOOK) {
			ItemStack result = new ItemStack(base.getType());
			
			int repairlvl = 0;
			int enchantUplvl = 0;
			int addEnchantlvl = 0;
			int numberlvl = 0;
			
			//REPAIR
			if (result.getItemMeta() instanceof Damageable) {
				

				Damageable baseD = (Damageable) base.getItemMeta();

				if (baseD.hasDamage()) {
					
					Damageable resultD = (Damageable) result.getItemMeta();
					Damageable otherD = (Damageable) other.getItemMeta();
					
					int repair = other.getType().getMaxDurability() - otherD.getDamage();				
					if (repair > baseD.getDamage()) repair = baseD.getDamage();
					
					int damage =  baseD.getDamage() - repair;
					

					if (damage < 0) damage = 0;
					
					resultD.setDamage(damage); 
					result.setItemMeta((ItemMeta) resultD);
					
					repairlvl = ((int) repair / 50) + 1;
					price = price + repairlvl;
				}
			}
			
			ItemMeta resultM = result.getItemMeta();
			ItemMeta baseM = base.getItemMeta();
			ItemMeta otherM = other.getItemMeta();
			
			HashMap<Enchantment, Integer> enchants = new HashMap<>();
			
			for (Enchantment ench : baseM.getEnchants().keySet()) {
				
				int level = baseM.getEnchantLevel(ench);
				
				if (ench.canEnchantItem(result) && EnchantsRessources.isCompatible(enchants, ench)) {
					if (otherM.getEnchants().containsKey(ench)) {
						if (otherM.getEnchantLevel(ench) == level) {
							
							if (level + 1 <= EnchantsRessources.getMaxLevel(ench)) {
								price = price + (level * level);
								enchantUplvl = enchantUplvl + (level * level);
								
								level++;
							}
						}
						
						if (otherM.getEnchantLevel(ench) > level) {
							
							level = otherM.getEnchantLevel(ench);
							
							price = price + ((level - 1 ) * (level - 1));
							enchantUplvl = enchantUplvl + ((level - 1 ) * (level - 1));
						}
					}
				}
				
				enchants.put(ench, level);
			}
			
			if (enchants.size() < 5) {
				
				ArrayList<Enchantment> allowedEnchants = new ArrayList<Enchantment>();
				
				for (Enchantment ench : otherM.getEnchants().keySet()) {
					if (ench.canEnchantItem(result) && EnchantsRessources.isCompatible(enchants, ench) && !enchants.containsKey(ench)) {
						allowedEnchants.add(ench);
					}
				}
				
				if (!allowedEnchants.isEmpty()) {
					while (!allowedEnchants.isEmpty() && enchants.size() < 5) {
						
						int choose = new Random().nextInt(allowedEnchants.size());
						Enchantment ench = allowedEnchants.get(choose);
						
						if (ench != null) {
							int level = otherM.getEnchantLevel(ench);
							
							enchants.put(ench, level);
							
							price = price + ((level) * (level));
							addEnchantlvl = addEnchantlvl + ((level) * (level));
						}
						
						allowedEnchants.remove(choose);
					}
				}

			}
			
			for (Enchantment ench : enchants.keySet()) {
				resultM.addEnchant(ench, enchants.get(ench), true);
			}
			
			if (price > 0)  {
				if (enchants.size() > 1) {
					numberlvl = enchants.size() * 2;
					price = price + numberlvl;
				}
			} else {
				setValid(AnvilSlot.BASE, false);
				setValid(AnvilSlot.OTHER, false);
				return;
			}

			resultM.setLore(EnchantsRessources.getLore(resultM));
			
			result.setItemMeta(resultM);
			setResultItem(result);
			inventory.setItem(22, getLevelIcon(repairlvl, enchantUplvl, addEnchantlvl, numberlvl));
			return;
		}
		
		//MINERAIS
		
		
		//BOOKS --------------------------------------------------------------------------------------------------
		if (other.getType() == Material.ENCHANTED_BOOK) {
			ItemStack result = new ItemStack(base.getType());
			
			int enchantUplvl = 0;
			int addEnchantlvl = 0;
			int numberlvl = 0;
		
			EnchantmentStorageMeta otherM = (EnchantmentStorageMeta) other.getItemMeta();
			
			HashMap<Enchantment, Integer> enchants = new HashMap<>();
			
			if (base.getType() != Material.ENCHANTED_BOOK) {
				ItemMeta resultM = result.getItemMeta();
				ItemMeta baseM = base.getItemMeta();
				
				if (baseM instanceof Damageable) {
					
					Damageable baseD = (Damageable) baseM;

					if (baseD.hasDamage()) {
						
						Damageable resultD = (Damageable) resultM;
						
						resultD.setDamage(baseD.getDamage()); 
						result.setItemMeta((ItemMeta) resultD);
					}
				}
				
				for (Enchantment ench : baseM.getEnchants().keySet()) {
					
					int level = baseM.getEnchantLevel(ench);
					
					if (otherM.getStoredEnchants().containsKey(ench)) {
						if (otherM.getStoredEnchantLevel(ench) == level) {
								
							if (level + 1 <= EnchantsRessources.getMaxLevel(ench)) {
								price = price + (level * level);
								enchantUplvl = enchantUplvl + (level * level);
									
								level++;
							}
						}
							
						if (otherM.getStoredEnchantLevel(ench) > level) {
								
							level = otherM.getStoredEnchantLevel(ench);
								
							price = price + ((level - 1 ) * (level - 1));
							enchantUplvl = enchantUplvl + ((level - 1 ) * (level - 1));
						}
					}
					
					enchants.put(ench, level);
				}
				
				if (enchants.size() < 5) {
					
					ArrayList<Enchantment> allowedEnchants = new ArrayList<Enchantment>();
					
					for (Enchantment ench : otherM.getStoredEnchants().keySet()) {
						if (ench.canEnchantItem(result) && EnchantsRessources.isCompatible(enchants, ench) && !enchants.containsKey(ench)) {
							allowedEnchants.add(ench);
						}
					}
					
					ArrayList<Enchantment> posibilities = new ArrayList<Enchantment>();
					posibilities.addAll(allowedEnchants);
					
					for (Enchantment ench : posibilities) {
						for (Enchantment ench2 : posibilities) {
							
							if (!ench.equals(ench2)) {
								if (EnchantsRessources.hasConflict(ench, ench2)) {
									if (new Random().nextBoolean()) allowedEnchants.remove(ench);
									else allowedEnchants.remove(ench2);
								}
							}
						}
					}
		
					
					if (!allowedEnchants.isEmpty()) {
						while (!allowedEnchants.isEmpty() && enchants.size() < 5) {
							
							int choose = new Random().nextInt(allowedEnchants.size());
							Enchantment ench = allowedEnchants.get(choose);
							
							if (ench != null) {
								int level = otherM.getStoredEnchantLevel(ench);
								
								enchants.put(ench, level);
								
								price = price + ((level) * (level));
								addEnchantlvl = addEnchantlvl + ((level) * (level));
							}
							
							allowedEnchants.remove(choose);
						}
					}

				}
				
				for (Enchantment ench : enchants.keySet()) {
					resultM.addEnchant(ench, enchants.get(ench), true);
				}
				
				if (price > 0)  {
					if (enchants.size() > 1) {
						numberlvl = enchants.size() * 2;
						price = price + numberlvl;
					}
					
				} else {
					setValid(AnvilSlot.BASE, false);
					setValid(AnvilSlot.OTHER, false);
					return;
				}

				resultM.setLore(EnchantsRessources.getLore(resultM));
				result.setItemMeta(resultM);
				
			} else {
				
				EnchantmentStorageMeta baseM = (EnchantmentStorageMeta) base.getItemMeta();
				EnchantmentStorageMeta resultM = (EnchantmentStorageMeta) result.getItemMeta();
				
				for (Enchantment ench : baseM.getStoredEnchants().keySet()) {
					
					int level = baseM.getStoredEnchantLevel(ench);
					
					if (otherM.getStoredEnchants().containsKey(ench)) {
						if (otherM.getStoredEnchantLevel(ench) == level) {
								
							if (level + 1 <= EnchantsRessources.getMaxLevel(ench)) {
								price = price + (level * level);
								enchantUplvl = enchantUplvl + (level * level);
									
								level++;
							}
						}
							
						if (otherM.getStoredEnchantLevel(ench) > level) {
								
							level = otherM.getStoredEnchantLevel(ench);
								
							price = price + ((level - 1 ) * (level - 1));
							enchantUplvl = enchantUplvl + ((level - 1 ) * (level - 1));
						}
					}
					
					enchants.put(ench, level);
				}
				
				if (enchants.size() < 5) {
					
					ArrayList<Enchantment> allowedEnchants = new ArrayList<Enchantment>();
					
					for (Enchantment ench : otherM.getStoredEnchants().keySet()) {
						if (!enchants.containsKey(ench)) {
							allowedEnchants.add(ench);
						}
					}
					
					if (!allowedEnchants.isEmpty()) {
						while (!allowedEnchants.isEmpty() && enchants.size() < 5) {
							
							int choose = new Random().nextInt(allowedEnchants.size());
							Enchantment ench = allowedEnchants.get(choose);
							
							if (ench != null) {
								int level = otherM.getStoredEnchantLevel(ench);
								
								enchants.put(ench, level);
								
								price = price + ((level) * (level));
								addEnchantlvl = addEnchantlvl + ((level) * (level));
							}
							
							allowedEnchants.remove(choose);
						}
					}

				}
				
				for (Enchantment ench : enchants.keySet()) {
					resultM.addStoredEnchant(ench, enchants.get(ench), true);
				}
				
				if (price > 0)  {
					if (enchants.size() > 1) {
						numberlvl = enchants.size() * 2;
						price = price + numberlvl;
					}

				} else {
					setValid(AnvilSlot.BASE, false);
					setValid(AnvilSlot.OTHER, false);
					return;
				}

				resultM.setLore(EnchantsRessources.getLore(resultM));
				result.setItemMeta(resultM);
			}
			
			setResultItem(result);
			inventory.setItem(22, getLevelIcon(0, enchantUplvl, addEnchantlvl, numberlvl));
			return;
		}
		
		setValid(AnvilSlot.OTHER, false);
		return;
	}
	
	
	private ItemStack getLevelIcon(int repair, int up, int addEnchant, int number) {
		
		if (price == 0) return getItem(Material.GRAY_STAINED_GLASS_PANE);
		
		ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
		ItemMeta itemM = item.getItemMeta();
		
		if (price == 1) itemM.setDisplayName("§bCoûte " + price + " §bniveaux !");
		else itemM.setDisplayName("§bCoûte " + price + " §bniveau !");
		
		ArrayList<String> lore = new ArrayList<>();
		
		lore.add("§6Les coûts :");
		if (repair != 0) lore.add("§7Réparation : §a" + repair + " lvl");
		if (up != 0) lore.add("§7Amélioration : §a" + up + " lvl");
		if (addEnchant != 0) lore.add("§7Nouveau(x) Enchant(s) : §a" + addEnchant + " lvl");
		if (number != 0) lore.add("§7Nombre d'enchant : §a" + number + " lvl");
		
		itemM.setLore(lore);
		
		item.setItemMeta(itemM);
		return item;
	}
	
	private Boolean canEnchant(ItemStack item) {
		

		if (!EnchantsRessources.canEnchantItem(item)) return false;
		
		if (item.getType() == Material.ENCHANTED_BOOK) return true;
		
		for (Enchantment ench : Enchantment.values()) {
			if (ench.canEnchantItem(item)) return true;
		}
		
		return false;
	}
	
	public void setResultItem(ItemStack item) {
		inventory.setItem(13, item);
	}
	
	public void setBaseItem(ItemStack item) {
		inventory.setItem(AnvilSlot.BASE.getSlot(), item);
	}
	
	public void setOtherItem(ItemStack item) {
		inventory.setItem(AnvilSlot.OTHER.getSlot(), item);
	}
	
	private void setValid(AnvilSlot slot, Boolean state) {
		
		if (slot == AnvilSlot.BASE) {
			
			BaseState = state;
			
			if (state) {
				
				inventory.setItem(20, getItem(Material.GREEN_STAINED_GLASS_PANE));
				inventory.setItem(11, getItem(Material.GREEN_STAINED_GLASS_PANE));
				inventory.setItem(12, getItem(Material.GREEN_STAINED_GLASS_PANE));
				
			} else {
				
				inventory.setItem(13, getItem(Material.BARRIER));
				inventory.setItem(22, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
				
				inventory.setItem(20, getItem(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(11, getItem(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(12, getItem(Material.RED_STAINED_GLASS_PANE));
			}
		}
		
		if (slot == AnvilSlot.OTHER) {
			
			OtherState = state;
			
			if (state) {
				
				inventory.setItem(14, getItem(Material.GREEN_STAINED_GLASS_PANE));
				inventory.setItem(15, getItem(Material.GREEN_STAINED_GLASS_PANE));
				inventory.setItem(24, getItem(Material.GREEN_STAINED_GLASS_PANE));
				
			} else {
				
				inventory.setItem(13, getItem(Material.BARRIER));
				inventory.setItem(22, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
				
				inventory.setItem(14, getItem(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(15, getItem(Material.RED_STAINED_GLASS_PANE));
				inventory.setItem(24, getItem(Material.RED_STAINED_GLASS_PANE));
			}
		}

	}
	
	private ItemStack getItem(Material mat) {
		return getItem(mat, "§7 ");
	}
	
	private ItemStack getItem(Material mat, String name) {
		
		ItemStack item = new ItemStack(mat);
		ItemMeta itemM = item.getItemMeta();
		
		itemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		if (name != null) itemM.setDisplayName(name);
		item.setItemMeta(itemM);
		
		
		return item;
	}
	
	public enum AnvilSlot {
		BASE(29), OTHER(33), RESULT(13);
		
		private int slot;
		
		private AnvilSlot(int slot) {
			this.slot = slot;
		}
		
		public int getSlot() {
			return slot;
		}
	}
}
