package fr.aiidor.mch.events;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.mch.Main;

public class MobSpawn implements Listener {
	private Main main;
	
	public MobSpawn(Main main) {
		this.main = main;
	}

	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		
		//ZOMBIE
		if (e.getEntity() instanceof Zombie) {
			
			Zombie zombie = (Zombie) e.getEntity();
			
			zombie.setCanPickupItems(true);
			zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64);
			zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
			zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
			
			int choose = new Random().nextInt(5);
			
			if (choose == 0) {
				zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.38);
			}
			
			//ZOMBIE TRACKER
			if (new Random().nextInt(30) == 0) {
				if (PlayerNext(zombie) != null && e.getEntityType() != EntityType.PIG_ZOMBIE) {
					zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
					zombie.setTarget(PlayerNext(zombie));
					zombie.setCanPickupItems(false);
					
					EntityEquipment ee = zombie.getEquipment();
					
					ee.setItemInMainHand(new ItemStack(Material.IRON_PICKAXE));
					ee.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
					
					new ZombieBreak(zombie).runTaskTimer(main, 2, 60);
				}
			}
			return;
		}
		
		if (e.getEntity().getType() == EntityType.SPIDER) {
			
			Spider spider = (Spider) e.getEntity();
			spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.55);
			
			return;
		}
		
		if (e.getEntity().getType() == EntityType.CAVE_SPIDER) {
			
			CaveSpider spider = (CaveSpider) e.getEntity();
			if (new Random().nextInt(11) == 0) {
				spider.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false));
			}
			
			return;
		}
		
		if (e.getEntity() instanceof Creeper) {
			Creeper creeper = (Creeper) e.getEntity();
			
			creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.45);
			creeper.setExplosionRadius(6);
			
			if (new Random().nextInt(29) == 0) {
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, true, false));
				creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
				creeper.setExplosionRadius(4);
			}
		}
		
		if (e.getEntity().getType() == EntityType.FIREBALL) {
			Fireball fire = (Fireball) e.getEntity();
			
			fire.setYield(4);
			fire.setVelocity(fire.getDirection().multiply(5));
			return;
		}
	}
	
	private Player PlayerNext(Zombie z) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (z.getWorld().equals(p.getWorld())) {
				if (p.getLocation().distance(z.getLocation()) <= 64) {
					return p;
				}
			}

		}
		return null;
	}
}
