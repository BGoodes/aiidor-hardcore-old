package fr.aiidor.hardcore.enchants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

public class Long_Bow extends AiidorEnchant  implements Listener {
	
	public Long_Bow(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
			
			if (arrow.getShooter() != null) {
				ProjectileSource shooter = arrow.getShooter();
				
				if (shooter instanceof Player) {
					Player player = (Player) arrow.getShooter();
					
					if (hasEnchant(player.getInventory().getItemInMainHand())) {
						
						int level = player.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(this);
						e.setDamage(e.getDamage() + getBonusDamage(player, e.getEntity(), level));
					}
				}
				
				if (shooter instanceof Skeleton) {
					Skeleton skeleton = (Skeleton) arrow.getShooter();
					
					if (hasEnchant(skeleton.getEquipment().getItemInMainHand())) {
						int level = skeleton.getEquipment().getItemInMainHand().getEnchantmentLevel(this);
						
						e.setDamage(e.getDamage() + getBonusDamage(e.getEntity(), skeleton, level));
					}
				}
			}
		}
	}
	
	public Double getBonusDamage(Entity target, Entity shooter, int level)  {
		
		double distance = target.getLocation().distance(shooter.getLocation());
		return (distance/15) * level;
	}
	
	public Boolean hasEnchant(ItemStack hand) {
		
		if (hand != null && hand.getType() != Material.AIR) {
			if (hand.hasItemMeta()) {
				
				ItemMeta meta = hand.getItemMeta();
				
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
		
		if (item.getType() == Material.BOW) return true;
		if (item.getType() == Material.CROSSBOW) return true;
		
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		if (enchant.equals(AiidorEnchant.SHORT_BOW)) return true;
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.BOW;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return "Long Bow";
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
		return 1;
	}
}
