package fr.aiidor.mch.raids;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.mch.Sounds;

public class Dead_Hunt extends BukkitRunnable {

	private Location location;
	private List<Entity> Raiders = new ArrayList<>();
	private int Size = 0;
	
	public Dead_Hunt(Location location) {
		
		//LOCATION
		World world = location.getWorld();
		
		int x = location.getBlockX();
		int z = location.getBlockZ();
		int y = world.getHighestBlockYAt(x, z);
		
		this.location = new Location(world, x, y, z);
	}
	
	@Override
	public void run() {
		
	}
	
	public void startHunt() {
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getWorld().equals(location.getWorld())) {
				
				if (player.getLocation().distance(location) <= 300) {
					
					//player.sendMessage(Main.serveur_tag + "�9La Lune de sang se l�ve ...");
					new Sounds(player).PlaySound(Sound.AMBIENT_CAVE, SoundCategory.HOSTILE);
					Size ++;
				}
			}
		}
		
		spawnHunters();
		
	}
	
	public void spawnHunters() {
		
		Random r = new Random();
		
		World world = location.getWorld();
		
		final int raidX = location.getBlockX();
		final int raidZ = location.getBlockZ();
		
		
		//CORPS PRINCIPAL DE L'ARMEE
		int body_size = (10 + r.nextInt(20)) * Size;
		
		for (int i = body_size; i != 0; i --) {
			
			int x = raidX + -10 + r.nextInt(20);
			int z = raidZ + -10 + r.nextInt(20);
			int y = world.getHighestBlockYAt(x, z) + 1;
			
			if (r.nextBoolean()) {
				
				Zombie zombie = (Zombie) world.spawnEntity(new Location(world, x, y, z), EntityType.ZOMBIE);
				EntityEquipment ee = zombie.getEquipment();
				
				zombie.setCustomName("§cHunter");
				
				ItemStack[] item = {
					new ItemStack(Material.IRON_BOOTS),
					new ItemStack(Material.IRON_LEGGINGS),
					new ItemStack(Material.IRON_CHESTPLATE),
					new ItemStack(Material.IRON_HELMET),
				};
				
				zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				
				ee.setItemInMainHand(new ItemStack(Material.IRON_SWORD));
				ee.setItemInOffHand(new ItemStack(Material.SHIELD));
				ee.setArmorContents(item);
				
				Raiders.add(zombie);
				
			} else {
				
				Skeleton skeleton = (Skeleton) world.spawnEntity(new Location(world, x, y, z), EntityType.SKELETON);
				EntityEquipment ee = skeleton.getEquipment();
				
				skeleton.setCustomName("§cHunter");
				
				ItemStack[] item = {
						new ItemStack(Material.IRON_BOOTS),
						new ItemStack(Material.CHAINMAIL_LEGGINGS),
						new ItemStack(Material.IRON_CHESTPLATE),
						new ItemStack(Material.CHAINMAIL_HELMET),
					};
				
				ee.setArmorContents(item);
				
				skeleton.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
				
				Raiders.add(skeleton);
			}
		}
		
		//CORP AERIEN DE L'ARMEE
		int head_Size = (3 + r.nextInt(5)) * Size;
		
		for (int i = head_Size; i != 0; i --) {
			
			int x = raidX + -10 + r.nextInt(20);
			int z = raidZ + -10 + r.nextInt(20);
			int y = world.getHighestBlockYAt(x, z) + 15;
			
			Phantom phantom = (Phantom) world.spawnEntity(new Location(world, x, y, z), EntityType.PHANTOM);
			
			phantom.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
			phantom.setCustomName("§cHunter");
			
			Skeleton skeleton = (Skeleton) world.spawnEntity(new Location(world, x, y, z), EntityType.SKELETON);
			EntityEquipment ee = skeleton.getEquipment();
			
			skeleton.setCustomName("§cHunter");
			
			ItemStack[] item = {
					new ItemStack(Material.LEATHER_BOOTS),
					new ItemStack(Material.LEATHER_LEGGINGS),
					new ItemStack(Material.LEATHER_CHESTPLATE),
					new ItemStack(Material.LEATHER_HELMET),
				};
			
			ee.setArmorContents(item);
			skeleton.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
			
			phantom.addPassenger(skeleton);

			Raiders.add(phantom);
			Raiders.add(skeleton);
		}
	}
	
	public Location getLocation() {
		return location;
	}
	
	public List<Entity> getRaiders() {
		return Raiders;
	}
}
