package fr.aiidor.hardcore.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.aiidor.hardcore.Plugin;

public class LockManager {
	
	private static BlockFace[] faces = {BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST};
	
	private Block b;
	private Player player;
	
	public LockManager(Block b, Player player) {
		this.b = b;
		this.player = player;
	}
	
	public Boolean canOpen() {
		GuildManager gm = Plugin.getInstance().getGuildManager();
		
		if (hasPermissions()) {
			if (!getPermissions().contains(player.getName().toLowerCase())) {
				
				if (gm.hasGuild(player) && getPermissions().contains(gm.getPlayerGuild(player).getName().toLowerCase())) {
					return true;
				}
				
				return false;
			}
		}
		return true;
	}
	
	public Block getLockBlock() {
		
		if (b.getBlockData() instanceof WallSign) {
			WallSign s = (WallSign) b.getBlockData();
			
			for (BlockFace f : faces) {
				Block b1 = b.getRelative(f);
				if (new LockManager(b1, player).hasPermissions()) {
					if (s.getFacing() == f.getOppositeFace()) {
						return b1;
					}
				}
			}
		}
		return null;
	}
	
	public Boolean hasLockBlock() {
		return getLockBlock() != null;
	}
	
	public void removeSigns() {
		for (Sign s : getSigns(b)) {
			
			s.getWorld().playEffect(s.getLocation(), Effect.STEP_SOUND, s.getType());
			s.getBlock().breakNaturally();
		}
		
		if (hasDoubleChest()) {
			for (Sign s : getSigns(getDoubleChest())) {
				
				s.getWorld().playEffect(s.getLocation(), Effect.STEP_SOUND, s.getType());
				s.getBlock().breakNaturally();
			}
		}
	}
	
	public Boolean hasInventory() {
		return getInventory() != null;
	}
	
	public Inventory getInventory() {
		
		if (b.getType() == Material.SMOKER || b.getType() == Material.BLAST_FURNACE) return null;
				
		if (b.getState() instanceof Container) {
			Inventory inv;
			Inventory c = ((Container) b.getState()).getInventory();
			
			if (hasDoubleChest()) {
				
				inv = Bukkit.createInventory(c.getHolder(), c.getSize(), "§cLock");
				inv.setContents(c.getContents());
				
			} else {
				inv = Bukkit.createInventory(c.getHolder(), c.getType(), "§cLock");
				inv.setContents(c.getContents());
			}

			
			return inv;
		}
		
		return null;
	}
	
	public Boolean hasPermissions() {
		return !getPermissions().isEmpty();
	}
	
	public List<String> getPermissions() {
		List<String> perms = new ArrayList<String>();
		
		if (b.getState() instanceof Container) {
			for (Sign s : getSigns(b)) for (String l : s.getLines()) perms.add(l.toLowerCase());
			
			if (hasDoubleChest()) {
				for (Sign s : getSigns(getDoubleChest())) for (String l : s.getLines()) perms.add(l.toLowerCase());
			}
		}
		
		if (!perms.stream().anyMatch(s-> s.equals("[private]") || s.equals("[privé]"))) perms.clear();
		
		return perms;
	}
	
	private List<Sign> getSigns(Block b) {
		List<Sign> signs = new ArrayList<Sign>();
		
		for (BlockFace f : faces) {
			Block b1 = b.getRelative(f);
			
			if (b1.getBlockData() instanceof WallSign) {
				if (((WallSign) b1.getBlockData()).getFacing() == f) {
					signs.add((Sign) b1);
				}
			}
		}
		
		return signs;
	}
	
	private Boolean hasDoubleChest() {
		return getDoubleChest() != null;
	}
	
	private Block getDoubleChest() {
		
		if (b.getState() instanceof Chest) {
			Chest c = (Chest) b.getState();
			
			if (c.getInventory().getHolder() instanceof DoubleChest) {
				DoubleChest d = (DoubleChest) c.getInventory().getHolder();
				
				for (BlockFace f : faces) {
					Block b1 = b.getRelative(f);
					
					if (b1.getState() instanceof Chest) {
						Chest c1 = (Chest) b.getState();
						
						if (c1.getInventory().getHolder() instanceof DoubleChest) {
							DoubleChest d1 = (DoubleChest) c1.getInventory().getHolder();
							if (d.getLocation().equals(d1.getLocation())) return b1;
						}
					}
				}
			}
		}
		return null;
	}
	
}
