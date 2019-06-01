package fr.aiidor.mch.events;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoreDamage implements Listener {
	
	@EventHandler
	public void damage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION) {
				giveEffect(player, new PotionEffect(PotionEffectType.BLINDNESS, (int) (e.getFinalDamage() * 20 * 0.7), 0, false, false, false));
				giveEffect(player, new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 1.8 * 20), 1, false, false, true));
				return;
			}
			
			if (e.getCause() == DamageCause.FALL) {
				giveEffect(player, new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 2 * 20), 2, false, false, true));
				return;
			}
			
			if (e.getCause() == DamageCause.HOT_FLOOR) {
				player.setFireTicks(new Random().nextInt(180));
				return;
			}
			
			if (e.getCause() == DamageCause.STARVATION) {
				giveEffect(player, new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false, true));
				giveEffect(player, new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1, false, false, true));
				return;
			}
		}
	}
	
	private void giveEffect(Player p, PotionEffect pe) {
		if (p.hasPotionEffect(pe.getType())) {
			if (p.getPotionEffect(pe.getType()).getDuration() < pe.getDuration() && p.getPotionEffect(pe.getType()).getAmplifier() <= pe.getAmplifier()) {
				p.removePotionEffect(pe.getType());
				p.addPotionEffect(pe);
			}
		} else {
			p.addPotionEffect(pe);
		}
	}
	
}
