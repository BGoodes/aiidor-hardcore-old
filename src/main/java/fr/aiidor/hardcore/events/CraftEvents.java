package fr.aiidor.hardcore.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class CraftEvents implements Listener {
	
	@EventHandler
	public void ChangeCraft(PrepareItemCraftEvent e) {
		
		//Craft se passe dans une zone de craft
		if (!(e.getInventory() instanceof CraftingInventory)) return;
		
		
		
		CraftingInventory inv = e.getInventory();
		
		if (inv.getResult() != null && inv.getResult().getType() != Material.AIR) {
			
			
			
		
			if (inv.getResult().getType().name().startsWith("NETHERITE_")) {
				if (inv.getResult().getType() != Material.NETHERITE_BLOCK && inv.getResult().getType() != Material.NETHERITE_INGOT && inv.getResult().getType() != Material.NETHERITE_SCRAP) {
					
					ItemStack item = inv.getResult();
					ItemMeta itemM = item.getItemMeta();
					
					itemM.setUnbreakable(true);
					
					item.setItemMeta(itemM);
					inv.setResult(item);
					return;
				}
			}
			
			
			
			
			
			
			
			
			if (inv.getResult().getType() == Material.SUSPICIOUS_STEW) {
				
				for (ItemStack item : inv.getMatrix()) {
					if (item != null && item.getType() == Material.OXEYE_DAISY) {
						inv.setResult(new ItemStack(Material.SUSPICIOUS_STEW));
					}
				}
			}
		} else {
			
			ItemStack[] matrix = inv.getMatrix();
			
			//TIPPED ARROWS
			int arrowNumber = 0;
			
			if (matrix.length == 9) {
				for (int i = 0; i != matrix.length; i ++) {
					
					if (matrix[i] != null) {
						
						if (i != 4) {
							if (matrix[i].getType() != Material.ARROW) return;
							else arrowNumber ++;
						}
					}
				}
				
				if (arrowNumber == 0) return;
				
				ItemStack potion = matrix[4];
				
				if (potion != null && potion.getType() == Material.SPLASH_POTION) {
					
					PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
					PotionType potionType = potionMeta.getBasePotionData().getType();
					
					ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, arrowNumber);
					PotionMeta arrowMeta = (PotionMeta) arrow.getItemMeta();
					
					arrowMeta.setBasePotionData(new PotionData(potionType));
					arrow.setItemMeta(arrowMeta);
					
					inv.setResult(arrow);
				}
			}

		}
	}
}
