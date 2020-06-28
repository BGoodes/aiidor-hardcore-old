package fr.aiidor.hardcore.events.mobs;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.enchants.AiidorEnchant;
import fr.aiidor.hardcore.tasks.Phantom_Boost;

public class Skeleton_Events implements Listener {
	
	@EventHandler
	public void onCombust(EntityCombustEvent e) {
		if (e.getEntity() instanceof Skeleton) {
			
			Skeleton skeleton = (Skeleton) e.getEntity();
			if (skeleton.getScoreboardTags().contains("fire")) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Skeleton) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE) {
				
				Skeleton skeleton = (Skeleton) e.getEntity();
				
				if (skeleton.getScoreboardTags().contains("fire")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		
		if (e.getEntityType() == EntityType.SKELETON) {
			
			Random r = new Random();
			
			//PHANTOM
			if (r.nextInt(300) == 0) {
				e.setCancelled(true);
				
				Location loc = e.getLocation();
				loc.setY(loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockY()) + 20);
				
				Phantom phantom = (Phantom) loc.getWorld().spawnEntity(loc, EntityType.PHANTOM);
				
				phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
				
				Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
				EntityEquipment ee = skeleton.getEquipment();
				
				ItemStack[] item = {
						new ItemStack(Material.IRON_BOOTS),
						new ItemStack(Material.CHAINMAIL_LEGGINGS),
						new ItemStack(Material.IRON_CHESTPLATE),
						new ItemStack(Material.CHAINMAIL_HELMET),
					};
				
				ee.setArmorContents(item);
				
				skeleton.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				
				phantom.addPassenger(skeleton);

				if (r.nextInt(100) == 0) {
					
					new Phantom_Boost(phantom, 3).runTaskTimer(Plugin.getInstance(), 0, 20);
					phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
					phantom.setHealth(phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
					
					skeleton.addScoreboardTag("fire");
				}
				
				return;
			}
			
			Skeleton skeleton = (Skeleton) e.getEntity();
			skeleton.setCanPickupItems(true);
			

			
			EntityEquipment ee = skeleton.getEquipment();
			
			
			if (r.nextInt(30) == 0) {
			
				ItemStack item = new ItemStack(Material.IRON_AXE);
				ItemMeta itemM = item.getItemMeta();
				itemM.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
				item.setItemMeta(itemM);
				
				ee.setItemInMainHand(item);
			
			} else if (r.nextInt(30) == 0) {
					
					ItemStack item = new ItemStack(Material.IRON_HOE);
					ItemMeta itemM = item.getItemMeta();
					itemM.addEnchant(AiidorEnchant.FLINGING, 2, true);
					item.setItemMeta(itemM);
					
					ee.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
					ee.setItemInMainHand(item);
					
			} else if (r.nextInt(30) == 0) {
				
				ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
				LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
				meta.setColor(Color.LIME);
				helmet.setItemMeta(meta);
				
				ItemStack item = new ItemStack(Material.BOW);
				ItemMeta itemM = item.getItemMeta();
				
				itemM.addEnchant(Enchantment.ARROW_KNOCKBACK, 10, true);
				item.setItemMeta(itemM);
				
				ee.setHelmet(helmet);
				ee.setItemInMainHand(item);
				
				
			} else if (r.nextInt(75) == 0) {
				
				ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
				LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
				meta.setColor(Color.RED);
				helmet.setItemMeta(meta);
				ee.setHelmet(helmet);
				
				skeleton.setCanPickupItems(false);
				skeleton.addScoreboardTag("fire");
			}
			
			skeleton.setHealth(skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		}
		
		if (e.getEntityType() == EntityType.WITHER_SKELETON) {
			 
			WitherSkeleton skeleton = (WitherSkeleton) e.getEntity();
			EntityEquipment ee = skeleton.getEquipment();
			
			skeleton.setCanPickupItems(true);
			
			Random r = new Random();
			
			if (r.nextInt(5) == 0) {
				
				ee.setItemInMainHand(new ItemStack(Material.BOW));
				return;
			}
			
			
			ee.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
			ee.setItemInOffHand(new ItemStack(Material.SHIELD));
			
			skeleton.setHealth(skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		}
	}
	
	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() != null && e.getEntity().getShooter()  instanceof Skeleton) {
			Skeleton skeleton = (Skeleton) e.getEntity().getShooter();
			
			if (!skeleton.getScoreboardTags().isEmpty()) {
				
				if (skeleton.getScoreboardTags().contains("fire")) {
					skeleton.getWorld().spawnParticle(Particle.FLAME, skeleton.getLocation().clone().add(0, 1.5, 0), 15, 0.1, 0.1, 0.1, 0.1);
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.getEntity().getShooter() != null && e.getEntity().getShooter()  instanceof Skeleton) {
			Skeleton skeleton = (Skeleton) e.getEntity().getShooter();
			
			if (!skeleton.getScoreboardTags().isEmpty()) {
				
				if (skeleton.getScoreboardTags().contains("fire")) {
					
					int number = 3 + new Random().nextInt(7);
					
					for (int i = 0; i != number; i ++) {
						float x = -2.0F + (float)(Math.random() * 2.0D + 1.0D);

						float y = -3.0F + (float)(Math.random() * 3.0D + 1.0D);

						float z = -2.0F + (float)(Math.random() * 2.0D + 1.0D);
						
						BlockData mt = Material.FIRE.createBlockData();
						
						FallingBlock fallingBlock = e.getEntity().getWorld().spawnFallingBlock(e.getEntity().getLocation(), mt);
						fallingBlock.setDropItem(false);
						fallingBlock.setVelocity(new Vector(x, y, z));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		
		if (e.getEntity() instanceof Skeleton) {
			Skeleton skeleton = (Skeleton) e.getEntity();
			
			if (e.getDamager() instanceof Arrow) {
				if (skeleton.getEquipment().getItemInOffHand() != null && skeleton.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
					if (new Random().nextBoolean()) {
						e.setCancelled(true);
					}
				}
			}
			return;
		}
		
		if (e.getDamager() instanceof Arrow) {
			
			Arrow arrow = (Arrow) e.getDamager();
			
			if (arrow.getShooter() != null && arrow.getShooter() instanceof Skeleton) {
				
				if (e.getEntity() instanceof LivingEntity) {
					
					Skeleton skeleton = (Skeleton) arrow.getShooter();
					LivingEntity ent = (LivingEntity) e.getEntity();
					
					if (skeleton.getType() == EntityType.WITHER_SKELETON) {
						giveEffect(ent, new PotionEffect(PotionEffectType.WITHER, 200, 0));
					}
					
					if (skeleton.getScoreboardTags().contains("fire")) {
						ent.setFireTicks((new Random().nextInt(15) + 5) * 20);
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
