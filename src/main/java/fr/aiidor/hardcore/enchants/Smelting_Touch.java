package fr.aiidor.hardcore.enchants;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.tools.WorldSound;

public class Smelting_Touch extends AiidorEnchant implements Listener {
	
	public Smelting_Touch(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void playerBreakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		
		if (player.getGameMode() != GameMode.CREATIVE) {
					
			Block b = e.getBlock();
					
			if (b != null && hasEnchant(player)) {
				
				if (b.getType() == Material.IRON_ORE || b.getType() == Material.GOLD_ORE) {
					
					ItemStack mainhand = player.getInventory().getItemInMainHand();
					ItemMeta meta = mainhand.getItemMeta();
					
					Location loc = b.getLocation();
							
					e.setDropItems(false);
					Random r = new Random();
					
					ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
					
					Integer drop = 1;
					Integer leveling = 1 + meta.getEnchantLevel(AiidorEnchant.LEVELING);
					
					if (meta.hasEnchant(LOOT_BONUS_BLOCKS)) {
						
						int proba = meta.getEnchantLevel(LOOT_BONUS_BLOCKS) * 8;
						
						if (r.nextInt(100) <= proba) {
							drop++;
						}
						
						if (proba >= 30) {
							proba = meta.getEnchantLevel(LOOT_BONUS_BLOCKS) * 3;
							
							if (r.nextInt(100) <= proba) {
								drop++;
							}
						}		
					}
					
					if (b.getType() == Material.IRON_ORE) {
								
						b.getWorld().dropItemNaturally(loc.clone().add(0.25, 0, 0.25), new ItemStack(Material.IRON_INGOT, drop));
						orb.setExperience(1 * leveling);
					}
						
					if (b.getType() == Material.GOLD_ORE) {
								
						b.getWorld().dropItemNaturally(loc.clone().add(0.25, 0, 0.25), new ItemStack(Material.GOLD_INGOT, drop));
						orb.setExperience(2 * leveling);
								
					}
						
					new WorldSound(loc).PlaySound(Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f);
					b.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc.clone().add(0.5, 0.2, 0.5), 10, 0.01, 0.05, 0.05, 0.05);
					return;
				}
			}
		}	
	}
	
	public Boolean hasEnchant(Player player) {
		
		ItemStack mainhand = player.getInventory().getItemInMainHand();
		if (mainhand != null && mainhand.getType() != Material.AIR) {
			if (mainhand.hasItemMeta()) {
				
				ItemMeta meta = mainhand.getItemMeta();
				
				if (meta.hasEnchants()) {
					if (meta.hasEnchant(this)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		if (item.getType().name().endsWith("_PICKAXE")) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		
		if (enchant.equals(SILK_TOUCH)) return true;
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public String getName() {
		return "Smelting Touch";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean isRune() {
		return false;
	}
	
	@Override
	public int getMinLevel() {
		return 30;
	}
}
