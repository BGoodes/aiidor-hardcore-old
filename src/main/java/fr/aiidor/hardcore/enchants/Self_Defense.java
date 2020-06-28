package fr.aiidor.hardcore.enchants;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Self_Defense extends AiidorEnchant  implements Listener {
	
	public Self_Defense(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof LivingEntity) {
			
			if (e.getEntity() instanceof Mob) {
				
				Mob mob = (Mob) e.getEntity();
				LivingEntity ent = (LivingEntity) e.getDamager();
				
				if (mob.getTarget() != null && mob.getTarget().equals(ent)) {
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
		
		if (enchant.equals(Enchantment.DAMAGE_ALL)) return true;
		
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	@Override
	public String getName() {
		return "Self Defense";
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
		return 5;
	}
}
