package fr.aiidor.hardcore.enchants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Defuse extends AiidorEnchant  implements Listener {
	
	public Defuse(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof LivingEntity) {
			
			if (e.getEntity() instanceof Creeper) {
				if (hasEnchant((LivingEntity) e.getDamager())) {
					Creeper creeper = (Creeper) e.getEntity();
					creeper.setExplosionRadius(0);
				}
			}
		}
	}
	
	public Boolean hasEnchant(LivingEntity ent) {
		
		if (ent.getEquipment() != null) {
			ItemStack mainhand = ent.getEquipment().getItemInMainHand();
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
		}

		return false;
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		
		if (EnchantmentTarget.WEAPON.includes(item)) return true;
		
		if (item.getType().name().endsWith("_AXE")) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Defuse";
	}

	@Override
	public int getStartLevel() {
		return 15;
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
		return 1;
	}
}
