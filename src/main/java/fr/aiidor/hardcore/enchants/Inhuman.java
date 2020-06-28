package fr.aiidor.hardcore.enchants;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inhuman extends AiidorEnchant  implements Listener {
	
	public Inhuman(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof LivingEntity) {
			
			if (e.getEntityType() == EntityType.EVOKER || e.getEntityType() == EntityType.VINDICATOR || e.getEntityType() == EntityType.ILLUSIONER || 
				e.getEntityType() == EntityType.PILLAGER || e.getEntityType() == EntityType.WITCH) {
				
				LivingEntity ent = (LivingEntity) e.getDamager();
				
				if (hasEnchant(ent)) {
					
					ItemStack mainhand = ent.getEquipment().getItemInMainHand();
					
					int level = mainhand.getItemMeta().getEnchantLevel(this);
					e.setDamage(e.getDamage() + (1.25 * level));
					
					Location loc = e.getEntity().getLocation().clone().add(0, e.getEntity().getHeight() / 2, 0);
					Random r = new Random();
					
					loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc, 30 + r.nextInt(40 - 30), 0.025, 0.025, 0.025, 0.5);
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
		
		if (enchant.equals(AiidorEnchant.FEATHER_WIPER)) return true;
		if (enchant.equals(AiidorEnchant.CUBISM)) return true;
		if (enchant.equals(Enchantment.DAMAGE_ALL)) return true;
		if (enchant.equals(Enchantment.DAMAGE_ARTHROPODS)) return true;
		if (enchant.equals(Enchantment.DAMAGE_UNDEAD)) return true;
		
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public String getName() {
		return "Inhuman";
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
		return 1;
	}
}
