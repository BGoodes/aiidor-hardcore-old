package fr.aiidor.mch.events;

import java.util.Random;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoreDamage implements Listener {
	
	@EventHandler
	public void damage(EntityDamageEvent e) {
		if (e.getEntity() instanceof LivingEntity) {
			
			LivingEntity entity = (LivingEntity) e.getEntity();
			
			if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION) {
				giveEffect(entity, new PotionEffect(PotionEffectType.BLINDNESS, (int) (e.getFinalDamage() * 20 * 0.7), 0, false, false, false));
				giveEffect(entity, new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 1.8 * 20), 1, false, false, true));
				return;
			}
			
			if (e.getCause() == DamageCause.FALL) {
				giveEffect(entity, new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 2 * 20), 2, false, false, true));
				return;
			}
			
			if (e.getCause() == DamageCause.HOT_FLOOR) {
				entity.setFireTicks(new Random().nextInt(180));
				return;
			}
			
			if (e.getCause() == DamageCause.STARVATION) {
				giveEffect(entity, new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false, true));
				giveEffect(entity, new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1, false, false, true));
				return;
			}
		}
	}
	
	@EventHandler
	public void onExtinguishFire(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			
			if (p.getGameMode() != GameMode.SURVIVAL || p.getGameMode() != GameMode.ADVENTURE) return;
			
			if (p.getTargetBlock((Set<Material>) null, 5) != null) {
				if (p.getTargetBlock((Set<Material>) null, 5).getType() == Material.FIRE) {
					
					if (p.getInventory().getItemInMainHand() == null || p.getInventory().getItemInMainHand().getType() == Material.AIR) {
						p.setFireTicks(60 + new Random().nextInt(60));

					}
				}
			}
		}
	}
	
	private void giveEffect(LivingEntity ent, PotionEffect pe) {
		if (ent.hasPotionEffect(pe.getType())) {
			if (ent.getPotionEffect(pe.getType()).getDuration() < pe.getDuration() && ent.getPotionEffect(pe.getType()).getAmplifier() <= pe.getAmplifier()) {
				ent.removePotionEffect(pe.getType());
				ent.addPotionEffect(pe);
			}
		} else {
			ent.addPotionEffect(pe);
		}
	}
	
}
