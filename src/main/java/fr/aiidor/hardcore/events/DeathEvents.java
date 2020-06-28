package fr.aiidor.hardcore.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EconManager;
import fr.aiidor.hardcore.managers.EconManager.Account;
import fr.aiidor.hardcore.managers.GuildManager;
import fr.aiidor.hardcore.managers.GuildManager.Guild;

public class DeathEvents implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		
		EconManager econManager = Plugin.getInstance().getEconManager();
		
		if (e.getEntity().getKiller() != null) {
			
			Entity ent = e.getEntity();
			Player player = e.getEntity().getKiller();
			
			if (econManager.hasAccount(player)) {
	
				Account account = econManager.getAccount(player);
				if (ent.getScoreboardTags().contains("Légendaire")) {
					account.addMoney(1000.0);
					player.sendMessage("§a+ §7" + 1000.0 + " §e" + EconManager.devise);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		
		Player player = e.getPlayer();
		UUID uuid = player.getUniqueId();
		
		player.setInvulnerable(true);
		
		Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if (Bukkit.getPlayer(uuid) != null) {
					Bukkit.getPlayer(uuid).setInvulnerable(false);
				}
			}
		}, 200);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		
		Player player = e.getEntity();
		if (player.getKiller() == null) {
			player.getWorld().strikeLightning(player.getLocation());
			e.getDrops().clear();
			e.setDroppedExp(0);
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT, 0.5f, 1f);
		}
		
		EconManager econManager = Plugin.getInstance().getEconManager();
		GuildManager guildManager = Plugin.getInstance().getGuildManager();
		
		if (econManager.hasAccount(player)) {
			Account account = econManager.getAccount(player);
			
			if (account.getBalance() >= 10) {
				Double montant = account.getBalance() / 2;
				montant = (double)Math.round(montant * 100d) / 100d;
				
				account.removeMoney(montant);
				player.sendMessage(Plugin.prefix + "§cVous venez de perdre " + montant + " " + EconManager.devise);
			}
		}
		
		if (guildManager.hasGuild(player)) {
			Guild guild = guildManager.getPlayerGuild(player);
			if (guild.hasMoney(10000.0)) guild.removeMoney(200.0);
		}
		
		e.setDeathMessage("§4[§cMORT§4] §6" + e.getDeathMessage());
	}
	
	@EventHandler
	public void mobAttackEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Zombie) {
			if (e.getEntity() instanceof Player) {
					
				Player player = (Player) e.getEntity();
				
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
}
