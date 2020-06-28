package fr.aiidor.hardcore.enchants;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Balloon_Piercer extends AiidorEnchant implements Listener {
	
	public Balloon_Piercer(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		
		if (hasEnchant(e.getBow())) {
			
			Random r = new Random();
			
			int level = e.getBow().getEnchantmentLevel(this);
			int randomizer = 4 - level;
			
			if (randomizer <= 0) randomizer = 1;
			
			if (r.nextInt(randomizer) == 0) {
				e.getProjectile().addScoreboardTag("ballon_piercer");
			}
			
		}
	}
	
public Boolean hasEnchant(ItemStack hand) {
		
		if (hand != null && hand.getType() != Material.AIR) {
			if (hand.hasItemMeta()) {
				
				ItemMeta meta = hand.getItemMeta();
				
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
		
		if (EnchantmentTarget.CROSSBOW.includes(item)) return true;
		
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.CROSSBOW;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return "Ballon Piercer";
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
		return 15;
	}
	
}
