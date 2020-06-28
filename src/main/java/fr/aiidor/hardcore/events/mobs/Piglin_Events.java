package fr.aiidor.hardcore.events.mobs;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Piglin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.EntityEquipment;

public class Piglin_Events implements Listener {
	
	@EventHandler
	public void entitySpawnEvent(EntitySpawnEvent e) {
		
		if (e.getEntity() instanceof Piglin) {
			Piglin pig = (Piglin) e.getEntity();
			
			pig.setCanPickupItems(true);
			EntityEquipment ee = pig.getEquipment();
			
			pig.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5);
			pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.35);
			
			if (ee.getItemInMainHand() != null) {
				
			}
		}
	}
}
