package fr.aiidor.mch.events;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class MobAttack implements Listener {
	
	@EventHandler
	public void mobAttackEvent(EntityDamageByEntityEvent e) {
		if (e.getEntity().getType().isAlive()) {
			
			//ZOMBIE
			if (e.getDamager() instanceof Zombie) {
				if (e.getEntity() instanceof Player) {
					
					if (e.getFinalDamage() == 0) return;
					
					Player player = (Player) e.getEntity();
					
					if (player.getHealth() - e.getFinalDamage() <= 0) {
						
						Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
						
						zombie.setCanPickupItems(true);
						zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64);
						zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20);
						zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
						
						zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
						zombie.setHealth(50);
						
						zombie.setCustomNameVisible(true);
						zombie.setCustomName(player.getDisplayName());
						
					    EntityEquipment ee = zombie.getEquipment();
					    if (player.getInventory().getItemInMainHand() != null) {
					    	 ee.setItemInMainHand(player.getInventory().getItemInMainHand());
					    }
					    
					    if (player.getInventory().getArmorContents() != null) {
					    	ee.setArmorContents(player.getInventory().getArmorContents() );
					    }
					    
					    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
					    SkullMeta meta = (SkullMeta)head.getItemMeta();
					    
					    meta.setOwningPlayer(player);
					    meta.setDisplayName("§6Tête de §c" + player.getName());
					    
					    head.setItemMeta(meta);
					    
					    ee.setHelmet(head);
					    
					} else {
						giveEffect(player, new PotionEffect(PotionEffectType.HUNGER, 200, 2));
						
						if (new Random().nextBoolean() && new Random().nextBoolean()) {
							giveEffect(player, new PotionEffect(PotionEffectType.POISON, 60, 2));
						}
					}

					return;
				}
			}
			
			if (e.getDamager() instanceof Spider) {
				
				if (new Random().nextBoolean() && new Random().nextBoolean()) {
					if (e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation().add(0, 1, 0)).getType() == Material.AIR) {
						e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation().add(0, 1, 0)).setType(Material.COBWEB);
					}
				} else {
					if (e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation()).getType() == Material.AIR) {
						e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation()).setType(Material.COBWEB);
					}
				}
			}
			
			if (e.getDamager() instanceof Skeleton) {
				if (e.getEntity() instanceof Player) {
					
					if (e.getFinalDamage() == 0) return;
					
					Player player = (Player) e.getEntity();
					giveEffect(player, new PotionEffect(PotionEffectType.SLOW, 100, 1));
				}
			}
			
			if (e.getDamager() instanceof Slime) {
				if (e.getEntity() instanceof Player) {
					
					Slime slime = (Slime) e.getDamager();
					
					Player player = (Player) e.getEntity();
					player.setVelocity(slime.getLocation().getDirection().multiply(slime.getSize() - 2).setY(1.1));
				}
			}
			
			if (e.getDamager() instanceof MagmaCube) {
				if (e.getEntity() instanceof Player) {
					
					MagmaCube player = (MagmaCube) e.getEntity();
					player.setFireTicks(new Random().nextInt(600) + 60);
				}
			}
		}
	}
	
	@EventHandler
	public void onSilverFishBurrow(EntityChangeBlockEvent e) {
		if(e.getEntity().getType().equals(EntityType.SILVERFISH)) {
			
			for (int x = e.getEntity().getLocation().getBlockX()-3; x <= e.getEntity().getLocation().getBlockX() + 3; x++) {
				for (int z = e.getEntity().getLocation().getBlockZ()-3; z <= e.getEntity().getLocation().getBlockZ() + 3; z++) {
					
					if (new Random().nextInt(25) == 0) {
						
						int choose = new Random().nextInt(2);
						if (new Random().nextBoolean()) {
							if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).getType() == Material.STONE) {
								e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() + choose , z).setType(Material.INFESTED_STONE);
							}
							
						}
						else {
							if (e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).getType() == Material.STONE) {
								e.getEntity().getWorld().getBlockAt(x, e.getEntity().getLocation().getBlockY() - choose , z).setType(Material.INFESTED_STONE);
							}
						}
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void ProjectileBoom(ProjectileHitEvent e) {
		
		if (e.getEntityType() == EntityType.SMALL_FIREBALL) {
			
			for (int i = 0; i != 10; i ++) {
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