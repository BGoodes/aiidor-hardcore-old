package fr.aiidor.hardcore.enchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.hardcore.tools.WorldSound;

public class Stomping extends AiidorEnchant  implements Listener {
	
	public Stomping(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageEvent e) {
		
		if (e.getCause() == DamageCause.FALL) {
			
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) e.getEntity();
				
				if (hasEnchant(entity)) {
					Integer level = entity.getEquipment().getBoots().getItemMeta().getEnchantLevel(this);
					
					List<Damageable> entities = new ArrayList<>();
					
					for (Entity ent : entity.getNearbyEntities(2 * level, 2 * level, 2 * level)) {
						if (ent instanceof Damageable) {
							entities.add((Damageable) ent);
						}
					}
					
					for (Damageable ent : entities) {
						ent.damage(e.getDamage() / (entities.size() + 1), entity);
						System.out.println(ent.getType().name() + " : " + e.getDamage() / (entities.size() + 1));
					}
					
					e.setDamage(e.getDamage() / (entities.size() + 1));
					new WorldSound(entity.getLocation()).PlaySound(Sound.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 1.0f);
					System.out.println(e.getFinalDamage());
				}
			}
		}
	}
	
	public Boolean hasEnchant(LivingEntity entity) {
		
		if (entity.getEquipment() != null) {
			if (entity.getEquipment().getBoots() != null && getItemTarget().includes(entity.getEquipment().getBoots())) {
				
				if (entity.getEquipment().getBoots().getItemMeta().hasEnchant(this)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		
		if (getItemTarget().includes(item)) return true;
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.ARMOR_FEET;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return "Stomping";
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
