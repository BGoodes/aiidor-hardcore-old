package fr.aiidor.hardcore.enchants;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Leveling extends AiidorEnchant implements Listener {
	
	public Leveling(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void playerBreakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		
		if (player.getGameMode() != GameMode.CREATIVE) {
					
			Block b = e.getBlock();
			
			if (b != null && hasEnchant(player)) {

				int level = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this);
				e.setExpToDrop(e.getExpToDrop() * (level + 1));
			}
		}	
	}
	
	
	@EventHandler
	public void onHit(EntityDeathEvent e) {
		
		if (e.getEntity().getKiller() != null) {
			Player player = e.getEntity().getKiller();
			
			if (player != null && hasEnchant(player)) {
				
				int level = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this);
				e.setDroppedExp(e.getDroppedExp() * (level + 1));
			}
		}
	}
	
	public Boolean hasEnchant(Player player) {
		
		ItemStack mainhand = player.getInventory().getItemInMainHand();
		if (mainhand != null && mainhand.getType() != Material.AIR) {
			if (mainhand.hasItemMeta()) {
				
				ItemMeta meta = mainhand.getItemMeta();
				
				if (meta.hasEnchants()) {
					if (meta.hasEnchant(this)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if (item.getType().name().endsWith("_PICKAXE") || item.getType().name().endsWith("_AXE")) {
			return true;
		}
		
		if (EnchantmentTarget.WEAPON.includes(item)) {
			return true;
		}
		
		if (item.getType() == Material.BOW || item.getType() == Material.CROSSBOW) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		
		if (enchant.equals(Enchantment.SILK_TOUCH)) return true;
		
		return false; 
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.VANISHABLE;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return "Leveling";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	
	@Override
	public boolean isRune() {
		return false;
	}
	
	@Override
	public int getMinLevel() {
		return 45;
	}
}
