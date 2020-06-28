package fr.aiidor.hardcore.managers;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.hardcore.Plugin;
import fr.aiidor.hardcore.tools.DurabilityManager;

public class CraftsManager {

	public void load() {
		
		Plugin pl = Plugin.getInstance();
		pl.getServer().removeRecipe(NamespacedKey.minecraft("glistering_melon_slice"));
		pl.getServer().removeRecipe(NamespacedKey.minecraft("golden_carrot"));
		
		//NERF ET BUFF ------------------------------------------------
		ShapedRecipe glistering_melon_slice = new ShapedRecipe(NamespacedKey.minecraft("glistering_melon_slice"), new ItemStack(Material.GLISTERING_MELON_SLICE));
		glistering_melon_slice.shape(new String[] {" G ", "GMG", " G "});
		glistering_melon_slice.setIngredient('G', Material.GOLD_INGOT);
		glistering_melon_slice.setIngredient('M', Material.MELON_SLICE);
		
		pl.getServer().addRecipe(glistering_melon_slice);
		
		ShapedRecipe golden_carrot = new ShapedRecipe(NamespacedKey.minecraft("golden_carrot"), new ItemStack(Material.GOLDEN_CARROT));
		golden_carrot.shape(new String[] {" G ", "GMG", " G "});
		golden_carrot.setIngredient('G', Material.GOLD_INGOT);
		golden_carrot.setIngredient('M', Material.CARROT);
		
		pl.getServer().addRecipe(golden_carrot);
		
		
		//CHAINMAIL ARMOR ------------------------------------------------
		ShapedRecipe chainmail_helmet = new ShapedRecipe(new NamespacedKey(pl, "chainmail_helmet"), new ItemStack(Material.CHAINMAIL_HELMET));
		chainmail_helmet.shape(new String[] {"FFF", "F F"});
		chainmail_helmet.setIngredient('F', Material.IRON_BARS);
		
		pl.getServer().addRecipe(chainmail_helmet);
		
		
		
		ShapedRecipe chainmail_boots = new ShapedRecipe(new NamespacedKey(pl, "chainmail_boots"), new ItemStack(Material.CHAINMAIL_BOOTS));
		chainmail_boots.shape(new String[] {"F F", "F F"});
		chainmail_boots.setIngredient('F', Material.IRON_BARS);
		
		pl.getServer().addRecipe(chainmail_boots);
		
		
		
		ShapedRecipe chainmail_chestplate = new ShapedRecipe(new NamespacedKey(pl, "chainmail_chestplate"), new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		chainmail_chestplate.shape(new String[] {"F F", "FFF", "FFF"});
		chainmail_chestplate.setIngredient('F', Material.IRON_BARS);
		
		pl.getServer().addRecipe(chainmail_chestplate);
		
		
		
		ShapedRecipe chainmail_leggings = new ShapedRecipe(new NamespacedKey(pl, "chainmail_leggings"), new ItemStack(Material.CHAINMAIL_LEGGINGS));
		chainmail_leggings.shape(new String[] {"FFF", "F F", "F F"});
		chainmail_leggings.setIngredient('F', Material.IRON_BARS);
		
		pl.getServer().addRecipe(chainmail_leggings);
		
		
		//NETHERITE STUFF ------------------------------------------------
		
		
		ShapedRecipe netherite_sword = new ShapedRecipe(new NamespacedKey(pl, "netherite_sword"), new ItemStack(Material.NETHERITE_SWORD));
		
		netherite_sword.shape(new String[] {"N", "N", "S"});
		netherite_sword.setIngredient('N', Material.NETHERITE_INGOT);
		netherite_sword.setIngredient('S', Material.STICK);
		
		pl.getServer().addRecipe(netherite_sword);
		
		
		ShapedRecipe netherite_shovel = new ShapedRecipe(new NamespacedKey(pl, "netherite_shovel"), new ItemStack(Material.NETHERITE_SHOVEL));
		
		netherite_shovel.shape(new String[] {"N", "S", "S"});
		netherite_shovel.setIngredient('N', Material.NETHERITE_INGOT);
		netherite_shovel.setIngredient('S', Material.STICK);
		
		pl.getServer().addRecipe(netherite_shovel);
		
		
		ShapedRecipe netherite_pickaxe = new ShapedRecipe(new NamespacedKey(pl, "netherite_pickaxe"), new ItemStack(Material.NETHERITE_PICKAXE));
		
		netherite_pickaxe.shape(new String[] {"NNN", " S ", " S "});
		netherite_pickaxe.setIngredient('N', Material.NETHERITE_INGOT);
		netherite_pickaxe.setIngredient('S', Material.STICK);
		
		pl.getServer().addRecipe(netherite_pickaxe);
		
		
		ShapedRecipe netherite_axe = new ShapedRecipe(new NamespacedKey(pl, "netherite_axe"), new ItemStack(Material.NETHERITE_AXE));
		
		netherite_axe.shape(new String[] {"NN ", "NS ", " S "});
		netherite_axe.setIngredient('N', Material.NETHERITE_INGOT);
		netherite_axe.setIngredient('S', Material.STICK);
		
		pl.getServer().addRecipe(netherite_axe);
		
		
		ShapedRecipe netherite_hoe = new ShapedRecipe(new NamespacedKey(pl, "netherite_hoe"), new ItemStack(Material.NETHERITE_HOE));
		netherite_hoe.shape(new String[] {"NN ", " S ", " S "});
		
		netherite_hoe.setIngredient('N', Material.NETHERITE_INGOT);
		netherite_hoe.setIngredient('S', Material.STICK);
		
		pl.getServer().addRecipe(netherite_hoe);
		
		ShapedRecipe netherite_helmet = new ShapedRecipe(new NamespacedKey(pl, "netherite_helmet"), new ItemStack(Material.NETHERITE_HELMET));
		netherite_helmet.shape(new String[] {"NNN", "N N"});
		netherite_helmet.setIngredient('N', Material.NETHERITE_INGOT);
		
		pl.getServer().addRecipe(netherite_helmet);
		
		
		
		ShapedRecipe netherite_boots = new ShapedRecipe(new NamespacedKey(pl, "netherite_boots"), new ItemStack(Material.NETHERITE_BOOTS));
		netherite_boots.shape(new String[] {"N N", "N N"});
		netherite_boots.setIngredient('N', Material.NETHERITE_INGOT);
		
		pl.getServer().addRecipe(netherite_boots);
		
		
		
		ShapedRecipe netherite_chestplate = new ShapedRecipe(new NamespacedKey(pl, "netherite_chestplate"), new ItemStack(Material.NETHERITE_CHESTPLATE));
		netherite_chestplate.shape(new String[] {"N N", "NNN", "NNN"});
		netherite_chestplate.setIngredient('N', Material.NETHERITE_INGOT);
		
		pl.getServer().addRecipe(netherite_chestplate);
		
		
		
		ShapedRecipe netherite_leggings = new ShapedRecipe(new NamespacedKey(pl, "netherite_leggings"), new ItemStack(Material.NETHERITE_LEGGINGS));
		netherite_leggings.shape(new String[] {"NNN", "N N", "N N"});
		netherite_leggings.setIngredient('N', Material.NETHERITE_INGOT);
		
		pl.getServer().addRecipe(netherite_leggings);
		
		
		//OTHER ------------------------------------------------
		ShapedRecipe grass_block = new ShapedRecipe(new NamespacedKey(pl, "grass_block"), new ItemStack(Material.GRASS_BLOCK));
		grass_block.shape(new String[] {"B", "D"});
		grass_block.setIngredient('B', Material.BONE_MEAL);
		grass_block.setIngredient('D', Material.DIRT);
		
		pl.getServer().addRecipe(grass_block);
		
		ShapedRecipe notch_apple = new ShapedRecipe(new NamespacedKey(pl, "notch_apple"), new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
		notch_apple.shape(new String[] {"GGG", "GAG", "GGG"});
		notch_apple.setIngredient('G', Material.GOLD_BLOCK);
		notch_apple.setIngredient('A', Material.GOLDEN_APPLE);
		
		pl.getServer().addRecipe(notch_apple);
		
		ShapelessRecipe name_Tag = new ShapelessRecipe(new NamespacedKey(pl, "name_tag"), new ItemStack(Material.NAME_TAG));
		name_Tag.addIngredient(Material.STRING);
		name_Tag.addIngredient(Material.PAPER);
		name_Tag.addIngredient(Material.INK_SAC);
		
		pl.getServer().addRecipe(name_Tag);
		
		ShapedRecipe end_portal = new ShapedRecipe(new NamespacedKey(pl, "end_portal"), new ItemStack(Material.END_PORTAL_FRAME));
		end_portal.shape(new String[] {" P ", "OOO", "EEE"});
		
		end_portal.setIngredient('P', Material.ENDER_PEARL);
		end_portal.setIngredient('O', Material.OBSIDIAN);
		end_portal.setIngredient('E', Material.END_STONE);
		
		pl.getServer().addRecipe(end_portal);
		
		ShapelessRecipe blaze_rod = new ShapelessRecipe(new NamespacedKey(pl, "blaze_rod"), new ItemStack(Material.BLAZE_ROD));
		blaze_rod.addIngredient(Material.BLAZE_POWDER);
		blaze_rod.addIngredient(Material.BLAZE_POWDER);
		
		pl.getServer().addRecipe(blaze_rod);
		
		
		
		//CUSTOM ITEM
		ItemStack slime_stick = new ItemStack(new ItemStack(Material.STONE_SWORD));
		ItemMeta slime_stick_meta = slime_stick.getItemMeta();
		
		slime_stick_meta.setUnbreakable(true);
		slime_stick_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		slime_stick_meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			
		slime_stick_meta.setCustomModelData(1);
		slime_stick_meta.setDisplayName("§fSlime Stick");

		slime_stick_meta.setLore(Arrays.asList("", "§aPropulse... Loin !"));
		
		slime_stick_meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
		
		slime_stick.setItemMeta(slime_stick_meta);
		new DurabilityManager(slime_stick).setDamageable(1, 1);
		
		ShapelessRecipe slime_stick_recipe = new ShapelessRecipe(new NamespacedKey(pl, "slime_stick"), slime_stick);
		slime_stick_recipe.addIngredient(Material.STICK);
		slime_stick_recipe.addIngredient(Material.STRING);
		slime_stick_recipe.addIngredient(Material.SLIME_BLOCK);
		
		pl.getServer().addRecipe(slime_stick_recipe);
		
		
		// ----------------------
		
		ItemStack diamond_apple = new ItemStack(new ItemStack(Material.GOLDEN_APPLE));
		ItemMeta diamond_apple_meta = diamond_apple.getItemMeta();
		
		diamond_apple_meta.setCustomModelData(1);
		diamond_apple_meta.setDisplayName("§bDiamond Apple");
		
		diamond_apple.setItemMeta(diamond_apple_meta);
		
		ShapedRecipe diamond_apple_recipe = new ShapedRecipe(new NamespacedKey(pl, "diamond_apple"), diamond_apple);
		diamond_apple_recipe.shape(new String[] {"DDD", "DAD", "DDD"});
		
		diamond_apple_recipe.setIngredient('A', Material.APPLE);
		diamond_apple_recipe.setIngredient('D', Material.DIAMOND);

		pl.getServer().addRecipe(diamond_apple_recipe);
		
		// ----------------------
		
		ItemStack netherite_apple = new ItemStack(new ItemStack(Material.GOLDEN_APPLE));
		ItemMeta netherite_apple_meta = netherite_apple.getItemMeta();
		
		netherite_apple_meta.setCustomModelData(2);
		netherite_apple_meta.setDisplayName("§7Netherite §8Apple");
		
		netherite_apple.setItemMeta(netherite_apple_meta);
		
		ShapedRecipe netherite_apple_recipe = new ShapedRecipe(new NamespacedKey(pl, "netherite_apple"), netherite_apple);
		netherite_apple_recipe.shape(new String[] {"NNN", "NAN", "NNN"});
		
		netherite_apple_recipe.setIngredient('A', Material.APPLE);
		netherite_apple_recipe.setIngredient('N', Material.NETHERITE_INGOT);

		pl.getServer().addRecipe(netherite_apple_recipe);
		
		// ----------------------
		
		ItemStack diamond_carrot = new ItemStack(new ItemStack(Material.GOLDEN_CARROT));
		ItemMeta diamond_carrot_meta = diamond_carrot.getItemMeta();
		
		diamond_carrot_meta.setCustomModelData(1);
		diamond_carrot_meta.setDisplayName("§fDiamond Carrot");
		
		diamond_carrot.setItemMeta(diamond_carrot_meta);
		
		ShapedRecipe diamond_carrot_recipe = new ShapedRecipe(new NamespacedKey(pl, "diamond_carrot"), diamond_carrot);
		diamond_carrot_recipe.shape(new String[] {" D ", "DCD", " D "});
		
		diamond_carrot_recipe.setIngredient('C', Material.CARROT);
		diamond_carrot_recipe.setIngredient('D', Material.DIAMOND);

		pl.getServer().addRecipe(diamond_carrot_recipe);
		
	}
}
