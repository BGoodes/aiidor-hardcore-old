package fr.aiidor.hardcore.enchants;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.Plugin;

public class Replanting extends AiidorEnchant  implements Listener {
	
	public Replanting(NamespacedKey id) {
		super(id);
	}
	
	
	@EventHandler
	public void playerBreakBlock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		
		Block b = e.getBlock();
			
		if (b != null && hasEnchant(player)) {
			if (isCompatible(b.getType())) {
				
				Material mat = b.getType();
				
				if (b.getRelative(BlockFace.DOWN) != null) {
					if ((mat == Material.NETHER_WART && b.getRelative(BlockFace.DOWN, 1).getType() == Material.SOUL_SAND) ||
						(b.getRelative(BlockFace.DOWN, 1).getType() == Material.FARMLAND)) {
							
							if (removeItem(player, getSeeds(mat))) {
								Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), new Runnable() {
									
									@Override
									public void run() {
										b.setType(mat);
									}
								}, 2);	
							}
							
							if (player.getGameMode() != GameMode.CREATIVE) {
								
								ItemStack mainhand = player.getInventory().getItemInMainHand();
								
								if (mainhand.getType().name().endsWith("_HOE")) {
									Damageable damageM = (Damageable) mainhand.getItemMeta();
									
									damageM.setDamage(damageM.getDamage() + 1);
									
									mainhand.setItemMeta((ItemMeta) damageM);
								}
							}
							
							return;
						}
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
	
	private Boolean removeItem(Player player, Material mat) {
		
		if (player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() == mat) {
			
			if (player.getGameMode() == GameMode.CREATIVE) return true;
			
			if (player.getInventory().getItemInOffHand().getAmount() > 0) {
				
				player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
				player.updateInventory();
				
				return true;
				
			}
		}
		
		return false;
	}
	
	private Material getSeeds(Material mat) {
		
		if (mat == Material.WHEAT) return Material.WHEAT_SEEDS;
		
		if (mat.name().contains("_STEM")) {
			return Material.getMaterial(mat.name().replace("ATTACHED_", "").replace("_STEM", "_SEEDS"));
		}
		
		if (mat == Material.CARROTS) return Material.CARROT;
		if (mat == Material.POTATOES) return Material.POTATO;
		
		return mat;
	}
	
	private Boolean isCompatible(Material mat) {
		
		switch (mat) {
		
		case WHEAT:
		case CARROTS:
		case POTATOES:
		case MELON_STEM:
		case ATTACHED_MELON_STEM:
		case PUMPKIN_STEM:
		case ATTACHED_PUMPKIN_STEM:
		case BEETROOTS:
		case NETHER_WART: 
			return true;

		default: return false;
			
		}
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		
		if (item.getType().name().endsWith("_HOE")) return true;
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
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
		return "Replanting";
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
