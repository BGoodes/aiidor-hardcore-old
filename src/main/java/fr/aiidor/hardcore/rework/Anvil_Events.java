package fr.aiidor.hardcore.rework;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.managers.EnchantManager;
import fr.aiidor.hardcore.tools.WorldSound;

public class Anvil_Events implements Listener {
	
	public EnchantManager em;
	
	public Anvil_Events() {
		em = Plugin.getInstance().getEnchantManager();
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (em.anvils.containsKey(e.getPlayer())) {
			e.getPlayer().closeInventory();
			em.anvils.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {
		
		if (e.getBlock().getType() == Material.ANVIL || e.getBlock().getType() == Material.CHIPPED_ANVIL || e.getBlock().getType() == Material.DAMAGED_ANVIL) {
			
			Location loc = e.getBlock().getLocation();
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getOpenInventory() != null) {

					if (p.getOpenInventory().getTitle().equals("§bEnclume")) {
						if (!em.anvils.containsKey(p)) {
							p.closeInventory();
							return;
						}
						
						if (loc.equals(em.anvils.get(p).getAnvil().getLocation())) {
							p.closeInventory();
						}
					}
				}
			}
		}
	}
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.ANVIL || e.getClickedBlock().getType() == Material.CHIPPED_ANVIL || e.getClickedBlock().getType() == Material.DAMAGED_ANVIL) {
				
				e.setCancelled(true);
				
				//OPEN INVENTORY
				Anvil_Inv anvil = new Anvil_Inv(e.getPlayer(), e.getClickedBlock());
				em.anvils.put(e.getPlayer(), anvil);
				e.getPlayer().openInventory(anvil.getInventory());
				return;
			}
		}
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		if (e.getCurrentItem() == null) return;
		
		InventoryView inv = e.getView();
		Player p = (Player) e.getWhoClicked();	
		
		if (e.getInventory().getHolder() != null) return;
		
		if (inv.getTitle().equals("§bEnclume")) {
			
			if (!em.anvils.containsKey(p)) {
				p.closeInventory();
				return;
			}
			
			if (!e.getInventory().equals(e.getClickedInventory())) {
				return;
			}
			
			Anvil_Inv enclume = em.anvils.get(p);
			
			if (!e.getInventory().equals(enclume.getInventory())) {
				
				p.closeInventory();
				em.anvils.remove(p);
				return;
			}
			
			ItemStack item = e.getCurrentItem();
			
			if (enclume.getAnvil() == null) {
				em.anvils.remove(p);
				p.closeInventory();
				return;
			}
			
			if ((enclume.getAnvil().getType() != Material.ANVIL && enclume.getAnvil().getType() != Material.CHIPPED_ANVIL && enclume.getAnvil().getType() != Material.DAMAGED_ANVIL)) {
				em.anvils.remove(p);
				p.closeInventory();
				return;
			}
			
			
			if (item.getType() == Material.BARRIER) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7 ")) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (item.getType() == Material.EXPERIENCE_BOTTLE) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(" §bniveaux !") || e.getCurrentItem().getItemMeta().getDisplayName().contains(" �bniveau !")) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (item.getType() == Material.GRAY_STAINED_GLASS_PANE || item.getType() == Material.RED_STAINED_GLASS_PANE || item.getType() == Material.GREEN_STAINED_GLASS_PANE) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§7 ")) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (item.getType() == Material.NAME_TAG) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aRenommer ?")) {
					e.setCancelled(true);
					
					//RENAME
					//A FAIRE 
				}
			}
			
			if (e.getSlot() == 13) {
				
				if (e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getType() == Material.BARRIER) {
					e.setCancelled(true);
					return;
				}
				
				if (p.getGameMode() != GameMode.CREATIVE) {

					int level = enclume.getLevelPrice();
					
					if (p.getLevel() < level) {
						p.closeInventory();
						p.sendMessage(Plugin.prefix + "§cVous n'avez assez de niveaux pour faire ça !");
						return;
					}
					
					p.setLevel(p.getLevel() - level);
					
					if (new Random().nextInt(100) <= 12) {
						
						if (enclume.getAnvil().getType() == Material.ANVIL) {
							enclume.getAnvil().setType(Material.CHIPPED_ANVIL);
						}
						
						else if (enclume.getAnvil().getType() == Material.CHIPPED_ANVIL) {
							enclume.getAnvil().setType(Material.DAMAGED_ANVIL);
						}
						
						else if (enclume.getAnvil().getType() == Material.DAMAGED_ANVIL) {
							
							enclume.getAnvil().setType(Material.AIR);
							enclume.getAnvil().getWorld().playEffect(enclume.getAnvil().getLocation(), Effect.STEP_SOUND, enclume.getAnvil().getType());
							new WorldSound(enclume.getAnvil().getLocation()).PlaySound(Sound.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS);
							
							e.setCancelled(true);
							Location loc = enclume.getAnvil().getLocation().clone().add(0.5, 1, 0.5);
							loc.getWorld().dropItem(loc, item);
							
							enclume.setBaseItem(new ItemStack(Material.AIR));
							enclume.setOtherItem(new ItemStack(Material.AIR));
							p.closeInventory();
							return;
						}
						

					}
				} 
				
				enclume.setBaseItem(new ItemStack(Material.AIR));
				enclume.setOtherItem(new ItemStack(Material.AIR));
				
				
				Bukkit.getScheduler().runTaskLater(Plugin.getInstance(), new Runnable() {
					public void run() {
						p.updateInventory();
					}
				}, 1);
				
				new WorldSound(enclume.getAnvil().getLocation()).PlaySound(Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS);
			}
			
		}
	}
	
	public void anvilDropItem() {
		
	}
		
	@EventHandler
	public void playerCloseEvent(InventoryCloseEvent e) {
			
		if (e.getPlayer() instanceof Player) {
				
			if (e.getView().getTitle().equals("�bEnclume")) {
					
				Player player = (Player) e.getPlayer();
					
				if (em.anvils == null || em.anvils.isEmpty()) return;
						
				if (!em.anvils.containsKey(player)) {
					player.closeInventory();
					return;
				}
					
				Anvil_Inv anvil = em.anvils.get(player);
				em.anvils.remove(player);
				
				Location loc = anvil.getAnvil().getLocation().clone().add(0.5, 1, 0.5);
				
				if (anvil.getBaseItem() != null) {
					if (anvil.getBaseItem().getType() != Material.AIR) {
						loc.getWorld().dropItem(loc, anvil.getBaseItem() );
					}
				}
				
				if (anvil.getOtherItem() != null) {
					if (anvil.getOtherItem().getType() != Material.AIR) {
						loc.getWorld().dropItem(loc, anvil.getOtherItem() );
					}
				}
			}
		}
	}
}
