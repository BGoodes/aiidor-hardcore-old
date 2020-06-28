package fr.aiidor.hardcore.events.mobs;

import java.util.Random;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Creeper_Events implements Listener {
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		
		if (e.getEntity() instanceof Creeper) {
			 
			Creeper creeper = (Creeper) e.getEntity();

			creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.45);
			creeper.setExplosionRadius(5);
			
			
			Random r = new Random();
			if (r.nextInt(400) == 0) {
				
				creeper.setCustomName("§cCreeper Master");
				
				creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
				
				creeper.setExplosionRadius(50);
				
			} else if (r.nextInt(50) == 0) {
				
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, true, false));
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
				creeper.setExplosionRadius(4);
				
			} else if (r.nextInt(30) == 0) {
				
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.40);
				creeper.setExplosionRadius(2);
				
				creeper.setSilent(true);
				
			} else if (r.nextInt(20) == 0) {
					
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.40);
				creeper.setExplosionRadius(6);
			}
		}
	}
}
