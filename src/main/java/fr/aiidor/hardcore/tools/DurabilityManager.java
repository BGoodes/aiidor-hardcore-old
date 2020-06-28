package fr.aiidor.hardcore.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DurabilityManager {
	
	private ItemStack item;
	
	public DurabilityManager(ItemStack item) {
		this.item = item;
	}
	
	public void setDamageable(Integer durability, Integer maxDurability) {
		List<String> lore;
		ItemMeta itemM = item.getItemMeta();
		
		if (itemM.hasLore()) {
			lore = itemM.getLore();
			
		} else {
			lore = new ArrayList<String>();
			lore.add("");
		}
		 
		lore.add("§7Durabilité :§6 " + durability + " / " + maxDurability);
		itemM.setLore(lore);
		
		item.setItemMeta(itemM);
	}
	
	public Boolean hasDurability() {
		return getArguments() != null;
	}
	
	public Integer getDurabiliy() {
		
		if (hasDurability()) {
			return Integer.valueOf(getArguments()[0]);
		}
		
		return null;
	}
	
	public Integer getMaxDurabiliy() {
			
		if (hasDurability()) {
			return Integer.valueOf(getArguments()[1]);
		}
			
		return null;
	}
	
	public void setDurability(Integer durability) {
		if (hasDurability()) {
			
			ItemMeta itemM = item.getItemMeta();
			List<String> lore = itemM.getLore();
			
			for (String line : lore) {
				if (line.contains("§7Durabilité :§6 ")) {
					lore.remove(line);
					lore.add("§7Durabilité :§6 " + durability + " / " + getMaxDurabiliy());
					break;
				}
			}
			
			itemM.setLore(lore);
			item.setItemMeta(itemM);
		}
	}
	
	public void breakItem(Player player) {
		
		item.setAmount(item.getAmount() - 1);
		player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.AMBIENT, 1, 1);
		
		if (item.getAmount() > 0) {
			this.setDurability(getMaxDurabiliy());
		}
		
		player.updateInventory();
	}
	
	public void removeDurability() {
		if (hasDurability()) setDurability(getDurabiliy() - 1); 
	}
	
	private String[] getArguments() {
		
		if (item.getItemMeta().hasLore()) {
			List<String> lore = item.getItemMeta().getLore();
			
			if (!lore.isEmpty()) {
					
				for (String line : lore) {
						
					if (line.contains("§7Durabilité :§6 ")) {
							
						String[] args = line.replace("§7Durabilité :§6 ", "").split(" / ");
						if (args.length == 2) return args;
					}
				}
			}
		}
		return null;
	}
}
