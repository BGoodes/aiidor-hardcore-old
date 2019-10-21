package fr.aiidor.mch.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.aiidor.mch.Main;
import fr.aiidor.mch.Sounds;
import fr.aiidor.mch.task.RespawnTime;

public class Dead implements Listener {

	private ArrayList<RespawnTime> respawn = new ArrayList<>();
	private Main main;
	
	public Dead(Main main) {
		this.main = main;
	}
	
	@EventHandler 
	public void onRespawn(PlayerRespawnEvent e) {
		checkList();
		
		RespawnTime rt = new RespawnTime(e.getPlayer());
		rt.runTaskTimer(main, 0, 20);
		
		respawn.add(rt);
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onDamager(EntityDamageEvent e) {
		
		if (respawn.isEmpty()) return;
		
		checkList();
		
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			
			for (RespawnTime rt : respawn) {
				
				if (rt.isPlayer(player)) {
					e.setDamage(0);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		
		Player player = e.getEntity();
		
		if (player.getKiller() == null) {
			e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
			e.getDrops().clear();
			e.setDroppedExp(0);
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new Sounds(p).PlaySound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT);

		}
		
		e.setDeathMessage("§6[§cMORT§6] §6" + e.getDeathMessage());
		
		
	}
	
	@EventHandler
	public void mobAttackEvent(EntityDamageByEntityEvent e) {
		
		
		if (e.getDamager() instanceof Zombie) {
			if (e.getEntity() instanceof Player) {
					
				Player player = (Player) e.getEntity();
					
				if (respawn.isEmpty()) return;
				
				checkList();
				
					
				for (RespawnTime rt : respawn) {
						
					if (rt.isPlayer(player)) {
						
						e.setDamage(0);
						return;
					}
				}
				
				if (player.getHealth() - e.getFinalDamage() <= 0) {
					Zombie zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
					
					zombie.setCanPickupItems(true);
					zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64);
					zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
					zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
						
					zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
					zombie.setHealth(50);
						
					zombie.setCustomNameVisible(true);
					zombie.setCustomName("§cZombie " + player.getDisplayName());
						
				    EntityEquipment ee = zombie.getEquipment();
				    if (player.getInventory().getItemInMainHand() != null) {
				    	 ee.setItemInMainHand(player.getInventory().getItemInMainHand());
				    }
					    
				    if (player.getInventory().getArmorContents() != null) {
				    	ee.setArmorContents(player.getInventory().getArmorContents() );
				    }
					    
				   ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
				   SkullMeta meta = (SkullMeta) head.getItemMeta();
					    
				    meta.setOwningPlayer(player);
				    meta.setDisplayName("§6Tête de §c" + player.getName());
					    
				    head.setItemMeta(meta);
					    
				    ee.setHelmet(head);
					    
				}
			}
		}
	}
	
	
	public void checkList() {
		ArrayList<RespawnTime> remove = new ArrayList<>();
		
		for (RespawnTime rt : respawn) {
			if (rt.isCancelled()) remove.add(rt);
		}
		
		for (RespawnTime rt : remove) {
			respawn.remove(rt);
		}
	}
	
		
}
