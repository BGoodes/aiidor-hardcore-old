package fr.aiidor.hardcore.events.mobs;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.tasks.RemoveWeb;

public class Spider_Events implements Listener {
	
	private ArrayList<PotionEffect> effect = new ArrayList<PotionEffect>();
	
	public Spider_Events() {
		effect.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));
		effect.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
		effect.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
		effect.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
		effect.add(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		
		if (!(e.getDamager() instanceof Spider)) return;
		
		if (e.getDamager().getType() == EntityType.SPIDER) {
			
			if (new Random().nextInt(3) == 0) {
				
				Block web = e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation().clone().add(0, 1, 0));
				
				if (web.getType() == Material.AIR) {
					web.setType(Material.COBWEB);
					
					RemoveWeb webTask = new RemoveWeb(web);
					webTask.runTaskTimer(Plugin.getInstance(), 0, 20);
					
					Plugin.getInstance().getEntityManager().webTasks.add(webTask);
				}
				
			} else {
				
				Block web = e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation());
				
				if (web.getType() == Material.AIR) {
					web.setType(Material.COBWEB);
					
					RemoveWeb webTask = new RemoveWeb(web);
					webTask.runTaskTimer(Plugin.getInstance(), 0, 20);
					
					Plugin.getInstance().getEntityManager().webTasks.add(webTask);
				}
				
			}
		}
		
		if (e.getDamager().getType() == EntityType.CAVE_SPIDER) {
			if (new Random().nextInt(3) == 0) {
				
				Block web = e.getEntity().getWorld().getBlockAt(e.getEntity().getLocation());
				
				if (web.getType() == Material.AIR) {
					web.setType(Material.COBWEB);
					
					RemoveWeb webTask = new RemoveWeb(web);
					webTask.runTaskTimer(Plugin.getInstance(), 0, 20);
					
					Plugin.getInstance().getEntityManager().webTasks.add(webTask);
				}
			}
		}
	}
	
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		
		if (e.getEntityType() == EntityType.SPIDER) {
			
			Spider spider = (Spider) e.getEntity();
			spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.50);
			
			Random r = new Random();
			
			if (r.nextInt(12) == 0) {
				
				spider.addPotionEffect(effect.get(r.nextInt(effect.size())));
				
				if (r.nextInt(5) == 0) {
					spider.addPotionEffect(effect.get(r.nextInt(effect.size())));
				}
			}
			
			if (spider.getPassengers().isEmpty()) {
				if (r.nextInt(60) == 0) {
					Zombie zombie = (Zombie) spider.getWorld().spawnEntity(spider.getLocation(), EntityType.ZOMBIE);
					
					zombie.setCanPickupItems(false);
					
					ItemStack mainHand = new ItemStack(Material.GOLDEN_AXE);
					ItemMeta mainHandM = mainHand.getItemMeta();
					mainHandM.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
					mainHand.setItemMeta(mainHandM);
					
					ItemStack offHand = new ItemStack(Material.IRON_SWORD);
					ItemMeta offHandM = offHand.getItemMeta();
					offHandM.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
					offHand.setItemMeta(offHandM);
					
					ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
					ItemMeta leggingsM = leggings.getItemMeta();
					leggingsM.addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true);
					leggings.setItemMeta(leggingsM);
					
					ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
					ItemMeta BootsM = boots.getItemMeta();
					BootsM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
					boots.setItemMeta(BootsM);
					
					zombie.getEquipment().setItemInMainHand(mainHand);
					zombie.getEquipment().setItemInOffHand(offHand);
					zombie.getEquipment().setLeggings(leggings);
					zombie.getEquipment().setBoots(boots);
					
					spider.addPassenger(zombie);
					
				}
			}

		}
		
		
		if (e.getEntityType() == EntityType.CAVE_SPIDER) {
			
			CaveSpider spider = (CaveSpider) e.getEntity();
			
			if (new Random().nextInt(10) == 0) {
				spider.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Spider) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FALL) e.setDamage(e.getDamage() / 2);
		}
	}
	
	@EventHandler
	public void PlayerBreakWebEvent(BlockBreakEvent e) {
		
		if (e.getBlock().getType() != Material.COBWEB) return;
		
		if (!Plugin.getInstance().getEntityManager().webTasks.isEmpty()) {
			
			Block b = e.getBlock();
			
			for (RemoveWeb webTask : Plugin.getInstance().getEntityManager().webTasks) {
				
				if (b.getLocation().equals(webTask.getCobweb().getLocation())) {
					e.setDropItems(false);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		
		if (e.getEntityType() == EntityType.SPIDER) {
			
			Random r = new Random();
			World world = e.getEntity().getWorld();
			
			Spider spider = (Spider) e.getEntity();
			
			for (int x = spider.getLocation().getBlockX()-4; x <= spider.getLocation().getBlockX() + 4; x++) {
				for (int z = spider.getLocation().getBlockZ()-4; z <= spider.getLocation().getBlockZ() + 4; z++) {
					
					if (r.nextInt(3) == 0) {
						
						int choose = r.nextInt(2);
						
						
						if (r.nextBoolean()) {
							
							Block web = world.getBlockAt(x, spider.getLocation().getBlockY() + choose , z);
							
							if (web != null && web.getType() == Material.AIR) {
								
								web.setType(Material.COBWEB);
								
								RemoveWeb webTask = new RemoveWeb(web);
								webTask.runTaskTimer(Plugin.getInstance(), 0, 20);
								
								Plugin.getInstance().getEntityManager().webTasks.add(webTask);
							}
							
						} else {
							
							Block web = world.getBlockAt(x, spider.getLocation().getBlockY() - choose , z);
							
							if (web != null && web.getType() == Material.AIR) {
								
								web.setType(Material.COBWEB);
								
								RemoveWeb webTask = new RemoveWeb(web);
								webTask.runTaskTimer(Plugin.getInstance(), 0, 20);
								
								Plugin.getInstance().getEntityManager().webTasks.add(webTask);
							}
						}
						
					}
				}
			}
		}
	}
	
}

