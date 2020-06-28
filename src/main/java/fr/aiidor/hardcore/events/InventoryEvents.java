package fr.aiidor.hardcore.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		if (e.getCurrentItem() == null) return;
		
		InventoryView inv = e.getView();
		
		if (inv.getTitle().equalsIgnoreCase("§cLock")) {
			e.setCancelled(true);
			return;
		}
		
		ItemStack item = e.getCurrentItem();
		
		if (item.getType().name().startsWith("NETHERITE_")) {
			if (item.getType() != Material.NETHERITE_BLOCK && item.getType() != Material.NETHERITE_INGOT && item.getType() != Material.NETHERITE_SCRAP) {
				ItemMeta itemM = item.getItemMeta();
				itemM.setUnbreakable(true);
				item.setItemMeta(itemM);
				return;
			}
		}
	}
}
