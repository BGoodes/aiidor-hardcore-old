package fr.aiidor.mch;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.mch.task.Zombie_Boomer;

public class CommandTest implements CommandExecutor {

	public Main main;
	
	public CommandTest(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
		
		Location loc = player.getLocation();
		
		Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
		
		zombie.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(64);
		zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
		zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);
		zombie.setCanPickupItems(false);
		
		EntityEquipment ee = zombie.getEquipment();
		ee.setHelmet(new ItemStack(Material.TNT));
			
		new Zombie_Boomer(zombie).runTaskTimer(main, 0, 20);
		zombie.addScoreboardTag("élite");
		
		return true;
	}
}
	

