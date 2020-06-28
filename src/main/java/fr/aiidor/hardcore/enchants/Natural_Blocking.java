package fr.aiidor.hardcore.enchants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Natural_Blocking extends AiidorEnchant  implements Listener {
	
	public Natural_Blocking(NamespacedKey id) {
		super(id);
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		
		if (e.getEntity() instanceof LivingEntity) {
			
			LivingEntity ent = (LivingEntity) e.getEntity();
			
			if (hasEnchant(ent)) {
				
				if (e.getDamage() > 0) {
					ItemStack hand = getShield(ent);
					
					int level = hand.getItemMeta().getEnchantLevel(this);
					double reduce = 1 * level;
					
					if (e.getDamage() <= 0) {
						e.setCancelled(true);
						return;
					}
					
					if (e.getDamage() - reduce < 0) e.setDamage(0);
					else e.setDamage(e.getDamage() - reduce);
					
					if (hand.getItemMeta() instanceof Damageable) {
						Damageable meta = (Damageable) hand.getItemMeta();
						 
						int unbreaking = hand.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
						int damage = (int) reduce * 2;
						
						damage = damage - unbreaking;
						
						if (damage <= 0) return;
						
						if (meta.getDamage() +  damage > hand.getType().getMaxDurability()) {
							hand.setAmount(hand.getAmount() - 1);
							ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 0.5f, 1f);
							return;
						}
						
						meta.setDamage(meta.getDamage() + damage);
						hand.setItemMeta((ItemMeta) meta);
						
					}
				}
			}
		}
	}
	
	private ItemStack getShield(LivingEntity ent) {
		
		if (ent.getEquipment() != null) {
			ItemStack mainhand = ent.getEquipment().getItemInMainHand();
			if (hasEnchant(mainhand)) return mainhand;
			
			ItemStack offhand = ent.getEquipment().getItemInOffHand();
			if (hasEnchant(offhand)) return offhand;
		}

		return null;
	}
	
	private Boolean hasEnchant(LivingEntity ent) {
		return getShield(ent) != null;
	}
	
	private Boolean hasEnchant(ItemStack item) {
		
		if (item != null && item.getType() != Material.AIR) {
			if (item.hasItemMeta()) {
				
				ItemMeta meta = item.getItemMeta();
				
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
		
		if (item.getType() == Material.SHIELD) return true;
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment enchant) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return EnchantmentTarget.WEAPON;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public String getName() {
		return "Natural Blocking";
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
