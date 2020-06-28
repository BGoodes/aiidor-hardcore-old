package fr.aiidor.hardcore.events;

import java.util.Random;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.hardcore.managers.LockManager;

public class PlayerInteractEvents implements Listener {
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock() != null) {
				
				if (e.getClickedBlock().getType() == Material.SMITHING_TABLE) {
					e.setCancelled(true);
					return;
				}
				
				LockManager lock = new LockManager(e.getClickedBlock(), player);
				
				if (!lock.canOpen()) {
					e.setCancelled(true);
					
					if (lock.hasInventory()) player.openInventory(lock.getInventory());
					return;
				}
			}
		}
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {
				launchFireBall(player.getInventory().getItemInMainHand(), player);
			}
			
			if (player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() == Material.FIRE_CHARGE) {
				launchFireBall(player.getInventory().getItemInOffHand(), player);
			}
		}
		
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

			
			//FIRE DAMAGE
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				if (player.getTargetBlock((Set<Material>) null, 5) != null) {
					if (player.getTargetBlock((Set<Material>) null, 5).getType() == Material.FIRE) {
						
						if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
							player.setFireTicks(60 + new Random().nextInt(60));
						}
					}
				}
			}
		}
	}
	
	private void launchFireBall(ItemStack fire_charge, Player player) {
		
		if (player.getGameMode() != GameMode.CREATIVE) fire_charge.setAmount(fire_charge.getAmount() - 1);
		
		player.updateInventory();
		
		Fireball ball = (Fireball) player.getWorld().spawnEntity(player.getLocation().add(0, 0.5, 0), EntityType.FIREBALL);

		ball.setVelocity(player.getLocation().getDirection().multiply(0.5));
		
		ball.setIsIncendiary(true);
		ball.setYield(2);
		
		ball.setShooter(player);
	}
	
	@EventHandler 
	public void playerInteractEntityEvent(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof Player) {
			
			Player player = e.getPlayer();
			Player rider = (Player) e.getRightClicked();
			
			if (!player.getPassengers().isEmpty() && (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() != Material.STRING)) {
				if (player.getPassengers().contains(rider)) {
					player.removePassenger(rider);
					return;
				}
			}
			
			if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.STRING) {
				if (player.getWorld().equals(rider.getWorld())) {
					if (player.getLocation().distance(rider.getLocation()) <= 1.5) {
						player.addPassenger(rider);
					}
				}
			}
		}
	}
	
	
}
