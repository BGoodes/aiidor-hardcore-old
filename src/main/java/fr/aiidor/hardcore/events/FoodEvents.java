package fr.aiidor.hardcore.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.hardcore.managers.EntityManager;

public class FoodEvents implements Listener {
	
	@EventHandler
	public void playerConsumeEvent(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		ItemStack item = e.getItem();
		
		if (item != null) {
			if (item.getItemMeta().hasCustomModelData()) {
				
				if (item.getType() == Material.GOLDEN_APPLE) {
					
					//DIAMOND APPLE
					if (item.getItemMeta().getCustomModelData() == 1) {
						
						e.setCancelled(true);
						
						player.setFoodLevel(player.getFoodLevel() + 4);
						
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.ABSORPTION, 4800, 1));
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 0));
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.SPEED, 600, 0));
						
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5f, 1f);
						
						if (player.getGameMode() != GameMode.CREATIVE) {
							if (player.getInventory().getItemInMainHand().equals(item)) {
								
								ItemStack apple = player.getInventory().getItemInMainHand();
								apple.setAmount(apple.getAmount() - 1);
								
							} else if (player.getInventory().getItemInOffHand().equals(item)){
								
								ItemStack apple = player.getInventory().getItemInOffHand();
								apple.setAmount(apple.getAmount() - 1);
							}
						}
					}
					
					//NETHERITE APPLE
					if (item.getItemMeta().getCustomModelData() == 2) {
						
						e.setCancelled(true);
						
						player.setFoodLevel(20);
						player.setSaturation(20);
						
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.REGENERATION, 1200, 2));
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.ABSORPTION, 12000, 4));
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 2));
						EntityManager.giveEffect(player, new PotionEffect(PotionEffectType.SPEED, 6000, 1));
						
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5f, 1f);
						
						if (player.getGameMode() != GameMode.CREATIVE) {
							if (player.getInventory().getItemInMainHand().equals(item)) {
								
								ItemStack apple = player.getInventory().getItemInMainHand();
								apple.setAmount(apple.getAmount() - 1);
								
							} else if (player.getInventory().getItemInOffHand().equals(item)){
								
								ItemStack apple = player.getInventory().getItemInOffHand();
								apple.setAmount(apple.getAmount() - 1);
							}
						}
					}
				}
			
				if (item.getType() == Material.GOLDEN_CARROT) {
				
					//DIAMOND CARROT
					if (item.getItemMeta().getCustomModelData() == 1) {
					
						e.setCancelled(true);
						
						player.setFoodLevel(player.getFoodLevel() + 8);
						player.setSaturation(20);
					
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5f, 1f);
					
						if (player.getGameMode() != GameMode.CREATIVE) {
							if (player.getInventory().getItemInMainHand().equals(item)) {
							
								ItemStack apple = player.getInventory().getItemInMainHand();
								apple.setAmount(apple.getAmount() - 1);
							
							} else if (player.getInventory().getItemInOffHand().equals(item)){
							
								ItemStack apple = player.getInventory().getItemInOffHand();
								apple.setAmount(apple.getAmount() - 1);
							}
						}
					}
				}
			}
		}
	}
}
