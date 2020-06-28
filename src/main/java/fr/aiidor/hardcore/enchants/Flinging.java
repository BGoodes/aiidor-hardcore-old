package fr.aiidor.hardcore.enchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import fr.aiidor.hardcore.Plugin;

public class Flinging extends AiidorEnchant  implements Listener {
	
	public Flinging(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof LivingEntity && e.getEntity() instanceof LivingEntity) {
			
			LivingEntity damager = (LivingEntity) e.getDamager();
			
			if (hasEnchant(damager)) {
				
				ItemStack mainhand = damager.getEquipment().getItemInMainHand();
				final LivingEntity ent = (LivingEntity) e.getEntity();
				
				int level = mainhand.getItemMeta().getEnchantLevel(this);
				
				Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						double x = ent.getVelocity().getX();
						double z = ent.getVelocity().getZ();
						
						if (level == 1) ent.setVelocity(new Vector(x, 0.55, z));
						else ent.setVelocity(new Vector(x, (level / 2.8), z));
					}
				}, 1);
				
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
		
		if (enchant.equals(Enchantment.KNOCKBACK)) return true;
		
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "Flinging";
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
