package fr.aiidor.hardcore.events.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.tasks.Mob_Miner;
import fr.aiidor.hardcore.tasks.Zombie_Boomer;

public class Zombie_Events implements Listener {
	
	@EventHandler
	public void onCombust(EntityCombustEvent e) {
		if (e.getEntity() instanceof Zombie) e.setCancelled(true);
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Zombie) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Zombie) {
			
			Zombie zombie = (Zombie) e.getDamager();
			
			if (zombie.getEquipment().getHelmet() != null && zombie.getEquipment().getHelmet().getType() == Material.TNT) {
				e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 3);
				e.setDamage(0);
				return;
			}
			
			if (e.getFinalDamage() == 0) return;
			
			if (e.getEntity() instanceof LivingEntity) {
				LivingEntity ent = (LivingEntity) e.getEntity();
				
				giveEffect(ent, new PotionEffect(PotionEffectType.HUNGER, 200, 2));
				
				if (new Random().nextInt(5) == 0) {
					giveEffect(ent, new PotionEffect(PotionEffectType.POISON, 60, 2));
				}

			}
		}
		
		if (e.getEntity() instanceof Zombie) {
			
			Zombie zombie = (Zombie) e.getEntity();
			if (e.getDamager() instanceof Arrow) {
				if (zombie.getEquipment().getItemInOffHand() != null && zombie.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
					if (new Random().nextBoolean()) {
						e.setCancelled(true);
					}
				}
			}
			
			Random r = new Random();
			
			if (zombie.getScoreboardTags().contains("pelleteuse") && e.getDamager() instanceof Player) {
				if (r.nextInt(3) == 0) {
					
					Location loc = zombie.getLocation();
					
					for (int x = -1; x != 2; x ++) {
						for (int y = 0; y != 4; y ++) {
							for (int z = -1; z != 2; z ++) {
								
								Block b = loc.clone().add(x, y, z).getBlock();
								
								if (b != null && b.getType() != Material.AIR) {
									
									if (b.getType() != Material.BEDROCK && b.getType() != Material.BARRIER && b.getType() != Material.WATER && b.getType() != Material.LAVA) {
										b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getType());
										b.setType(Material.AIR);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		
		if (!(e.getEntity() instanceof Zombie)) return;
		
		Random r = new Random();
		
		Zombie zombie = (Zombie) e.getEntity();
		
		zombie.setCanPickupItems(true);
		zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64);
		zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(8);
		zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
		
		if (r.nextInt(15) == 0) {
			Silverfish sv = (Silverfish) e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.SILVERFISH);
			zombie.addPassenger(sv);
		}
		
		if (r.nextInt(100) == 0) {
			
			zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
			zombie.setCanPickupItems(false);
			
			EntityEquipment ee = zombie.getEquipment();
			ee.setHelmet(new ItemStack(Material.TNT));
				
			new Zombie_Boomer(zombie).runTaskTimer(Plugin.getInstance(), 0, 20);
			return;
		}
		
		if (r.nextInt(200) == 0) {
			
			zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
			zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
			zombie.setCanPickupItems(false);
				
			EntityEquipment ee = zombie.getEquipment();
				
			ee.setItemInMainHand(new ItemStack(Material.IRON_SHOVEL));
			ee.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
			
			zombie.addScoreboardTag("pelleteuse");
			return;
		}
		
		if (r.nextInt(350) == 0) {
			
			List<Player> players = getPlayersNext(zombie);
			if (!players.isEmpty() && zombie.getType() != EntityType.ZOMBIFIED_PIGLIN) {
				
				int choose = r.nextInt(players.size());
				zombie.setTarget(players.get(choose));
				
				zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.2);
				zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(3);
				zombie.setCanPickupItems(false);
				
				EntityEquipment ee = zombie.getEquipment();
				
				ee.setItemInMainHand(new ItemStack(Material.IRON_PICKAXE));
				ee.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
				
				new Mob_Miner(zombie).runTaskTimer(Plugin.getInstance(), 0, 60);
				return;
			}
		}
		
		int ran = 20;
		int choose = r.nextInt(ran);
		
		if (choose == ran) {
			zombie.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.5);
		}
		
		if (choose == ran - 1) {
			zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
		}
		
		if (choose == ran - 2) {
			zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
		}
		
		zombie.setHealth(zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
	}
	
	
	private List<Player> getPlayersNext(Zombie z) {
		
		List<Player> player = new ArrayList<Player>();
		
		if (Bukkit.getOnlinePlayers().size() == 0) return player;
		for (Player p : Bukkit.getOnlinePlayers()) {
			
			if (p.getWorld().equals(z.getWorld()) && p.getGameMode() == GameMode.SURVIVAL) {
				
				if (p.getLocation().distance(z.getLocation()) <= 50) {
					player.add(p);
				}
			}

		}
		return player;
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

