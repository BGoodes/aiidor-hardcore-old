package fr.aiidor.hardcore.rework;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.enchants.EnchantsRessources;
import fr.aiidor.hardcore.managers.EnchantManager;
import fr.aiidor.hardcore.rework.Enchant_Table_Inv.TableResult;
import fr.aiidor.hardcore.tools.WorldSound;

public class Enchant_Table_Events implements Listener {
	
	public EnchantManager em;
	
	public Enchant_Table_Events() {
		em = Plugin.getInstance().getEnchantManager();
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (em.enchants.containsKey(e.getPlayer())) {
			e.getPlayer().closeInventory();
			em.enchants.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {
		
		if (e.getBlock().getType() == Material.ENCHANTING_TABLE) {
			
			Location loc = e.getBlock().getLocation();
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getOpenInventory() != null) {

					if (p.getOpenInventory().getTitle().equals("§dTable d'enchantement")) {
						if (!em.enchants.containsKey(p)) {
							p.closeInventory();
							return;
						}
						
						if (loc.equals(em.enchants.get(p).getEnchantTable().getLocation())) {
							p.closeInventory();
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
				
				e.setCancelled(true);
				
				//OPEN INVENTORY
				
				Enchant_Table_Inv table = new Enchant_Table_Inv(e.getPlayer(), e.getClickedBlock());
				em.enchants.put(e.getPlayer(), table);
				e.getPlayer().openInventory(table.getInventory());
				
				return;
			}
		}
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		if (e.getCurrentItem() == null) return;
		
		InventoryView inv = e.getView();
		Player p = (Player) e.getWhoClicked();	
		
        if (e.getClickedInventory().getType() == InventoryType.GRINDSTONE && e.getSlotType() == SlotType.RESULT) {
        	
        	ItemStack item  = e.getCurrentItem();
        	
        	if (item != null && item.getType() != Material.AIR) {
        		
        		ItemMeta itemM = item.getItemMeta();

        		if (itemM.hasLore()) {
            		
    				itemM.setLore(EnchantsRessources.removeEnchantsLore(itemM));
        			item.setItemMeta(itemM);
        		}
        	}
        	
        	return;
         }
        
		if (e.getInventory().getHolder() != null) return;
		
		if (inv.getTitle().equals("§dTable d'enchantement")) {
			
			if (!em.enchants.containsKey(p)) {
				p.closeInventory();
				return;
			}
			
			if (!e.getInventory().equals(e.getClickedInventory())) {
				return;
			}
			
			Enchant_Table_Inv table = em.enchants.get(p);
			
			if (!e.getInventory().equals(table.getInventory())) {
				
				p.closeInventory();
				em.enchants.remove(p);
				return;
			}
			
        	ItemStack item = e.getCurrentItem();
			
			if (table.getEnchantTable() == null || table.getEnchantTable().getType() != Material.ENCHANTING_TABLE) {
				em.enchants.remove(p);
				p.closeInventory();
				return;
			}
			
			
			if (item.getType() == Material.BARRIER) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7 ")) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7 ")) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (item.getType() == Material.ENCHANTED_BOOK) {
				if (item.getItemMeta().getDisplayName().contains("§a Niveaux")) {
					e.setCancelled(true);
					
					for (TableResult result : TableResult.values()) {
						if (e.getSlot() == result.getSlot()) {
							
							if (p.getGameMode() != GameMode.CREATIVE) {
								
								int level = p.getLevel();
								if (table.getLevel(result) > level) {
									p.sendMessage(Plugin.prefix + "§cVous n'avez pas assez de niveaux pour effectuer ceci !");
									return;
								}
								
								p.setLevel(p.getLevel() - table.getRemoveLevels(result));
							}
							
							//PLUSIEURS ITEMS
							if (table.getPlayerItem() == null || table.getPlayerItem().getType() == Material.AIR) return;
							
							if (table.getPlayerItem().getAmount() > 1) {
								table.getEnchantTable().getLocation().getWorld().dropItem(table.getEnchantTable().getLocation(), new ItemStack(table.getPlayerItem().getType(), table.getPlayerItem().getAmount() - 1));
								table.getPlayerItem().setAmount(1);
							}
								
							if (table.getPlayerItem().getType() == Material.BOOK) {
								table.getPlayerItem().setType(Material.ENCHANTED_BOOK);
								
								EnchantmentStorageMeta itemM = (EnchantmentStorageMeta) table.getPlayerItem().getItemMeta();
								
								for (Enchantment ench : table.getResult(result).keySet()) {
									itemM.addStoredEnchant(ench, table.getResult(result).get(ench), true);
									
									itemM.setLore(EnchantsRessources.getStoredLore(itemM));
									table.getPlayerItem().setItemMeta(itemM);
								}
								
							} else {
								ItemMeta itemM = table.getPlayerItem().getItemMeta();
								
								for (Enchantment ench : table.getResult(result).keySet()) {
									itemM.addEnchant(ench, table.getResult(result).get(ench), true);
								}
								
								itemM.setLore(EnchantsRessources.getLore(itemM));
								table.getPlayerItem().setItemMeta(itemM);
							}
							
							table.removeEnchants();
							new WorldSound(table.getEnchantTable().getLocation()).PlaySound(Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS);
						}
					}
					
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void playerCloseEvent(InventoryCloseEvent e) {
		
		if (e.getPlayer() instanceof Player) {
			
			if (e.getView().getTitle().equals("§dTable d'enchantement")) {
				
				Player player = (Player) e.getPlayer();
				
				if (em.enchants == null || em.enchants.isEmpty()) return;
					
				if (!em.enchants.containsKey(player)) {
					player.closeInventory();
					return;
				}
				
				Enchant_Table_Inv table = em.enchants.get(player);
				em.enchants.remove(player);
				
				if (table.getPlayerItem() != null) {
					if (table.getPlayerItem().getType() != Material.AIR) {
						table.getEnchantTable().getLocation().getWorld().dropItem(table.getEnchantTable().getLocation().clone().add(0.5, 1, 0.5), table.getPlayerItem());
					}
				}
			}
		}
	}
}
