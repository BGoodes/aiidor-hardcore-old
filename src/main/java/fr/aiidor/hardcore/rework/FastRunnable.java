package fr.aiidor.hardcore.rework;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.enchants.EnchantsRessources;
import fr.aiidor.hardcore.managers.EnchantManager;

public class FastRunnable extends BukkitRunnable {
	
	public EnchantManager em;
	
	public FastRunnable() {
		em = Plugin.getInstance().getEnchantManager();
	}
	
	@Override
	public void run() {
		
		if (!Bukkit.getOnlinePlayers().isEmpty()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				
				if (p.getOpenInventory() != null) {
					
					if (p.getOpenInventory().getType() == InventoryType.GRINDSTONE) {
						GrindStone(p);
					}
					
					if (p.getOpenInventory().getTitle().equals("§dTable d'enchantement")) {
						EnchantementTable(p);
					}
					
					if (p.getOpenInventory().getTitle().equals("§bEnclume")) {
						Anvil(p);
					}
				}
			}
		}
	}
	
	private void GrindStone(Player p) {
		
		if (p.getOpenInventory().getTopInventory().getType() == InventoryType.GRINDSTONE) {
			
        	GrindstoneInventory grind = (GrindstoneInventory) p.getOpenInventory().getTopInventory();
        	ItemStack item = grind.getItem(2);
        	
        	if (item != null && item.getType() != Material.AIR) {
        		
        		ItemMeta itemM = item.getItemMeta();
				
        		if (itemM.hasLore()) {
            		
    				itemM.setLore(EnchantsRessources.removeEnchantsLore(itemM));
        			item.setItemMeta(itemM);
        		}
        	}
		}

	}
	
	private void EnchantementTable(Player p) {

		if (!em.enchants.containsKey(p)) {
			p.closeInventory();
			return;
		}
		
		Enchant_Table_Inv table = em.enchants.get(p);
		
		if (table.getEnchantTable() == null || table.getEnchantTable().getType() != Material.ENCHANTING_TABLE) {
			em.enchants.remove(p);
			p.closeInventory();
			return;
		}
		
		if (table.getPlayerItem() == null) {
			table.removeEnchants();
			table.setLastItem(null);
			return;
		}
		
		if (!table.getPlayerItem().equals(table.getLastItem())) {
			ItemStack item = table.getPlayerItem();
			
			if (item.getEnchantments().isEmpty()) {
				table.generateEnchants();
			}
		}
		
		table.setLastItem();
	}
	
	
	
	private void Anvil(Player p) {

		if (!em.anvils.containsKey(p)) {
			p.closeInventory();
			return;
		}
		
		Anvil_Inv anvil = em.anvils.get(p);
		
		if (anvil.getAnvil() == null) {
			em.anvils.remove(p);
			p.closeInventory();
			return;
		}
		
		if (anvil.getAnvil().getType() != Material.ANVIL && anvil.getAnvil().getType() != Material.DAMAGED_ANVIL && anvil.getAnvil().getType() != Material.CHIPPED_ANVIL) {
			em.anvils.remove(p);
			p.closeInventory();
			return;
		}
		
		if (anvil.getBaseItem() == null) {
			anvil.setResut();
			anvil.LastBaseItem = null;
			return;
		}
		
		if (anvil.getOtherItem() == null) {
			anvil.setResut();
			anvil.LastOtherItem = null;
			return;
		}
		
		if (!anvil.getBaseItem().equals(anvil.LastBaseItem) || !anvil.getOtherItem().equals(anvil.LastOtherItem)) {
			anvil.setResut();
		}
		
		
		
		anvil.LastBaseItem = anvil.getBaseItem();
		anvil.LastOtherItem = anvil.getOtherItem();
	}
	
}
