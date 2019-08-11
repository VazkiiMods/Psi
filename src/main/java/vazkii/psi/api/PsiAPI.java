/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [13/01/2016, 16:08:02 (GMT)]
 */
package vazkii.psi.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.DummyMethodHandler;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.api.recipe.TrickRecipe;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.SpellPiece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class PsiAPI {

	/**
	 * The internal method handler in use. This object allows the API to interact with the mod.
	 * By default this is a dummy. In the mod itself, this is replaced with an implementation that
	 * can handle all of its queries.<br><br>
	 *
	 * <b>DO NOT EVER, EVER, OVERWRITE THIS VALUE</b>
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();


	public static final SimpleRegistry<Class<? extends SpellPiece>> spellPieceRegistry = new SimpleRegistry<>();
	public static final HashMap<String, ResourceLocation> simpleSpellTextures = new HashMap<>();
	public static final HashMap<Class<? extends SpellPiece>, PieceGroup> groupsForPiece = new HashMap<>();
	public static final HashMap<Class<? extends SpellPiece>, String> pieceMods = new HashMap<>();
	public static final HashMap<String, PieceGroup> groupsForName = new HashMap<>();

	public static final List<TrickRecipe> trickRecipes = new ArrayList<>();


	public static final PsimetalArmorMaterial PSIMETAL_ARMOR_MATERIAL = new PsimetalArmorMaterial("psimetal", 18, new int[]{2, 6, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F, null);

	public static int levelCap = 1;

	private static String getCurrentModId() {
        ModContainer activeModContainer = ModLoadingContext.get().getActiveContainer();
		if (activeModContainer != null)
            return activeModContainer.getModId();
		return "minecraft";
	}

	/**
	 * Registers a Spell Piece given its class, by which, it puts it in the registry.
	 */
	public static void registerSpellPiece(String key, Class<? extends SpellPiece> clazz) {
		spellPieceRegistry.register(new ResourceLocation(key), clazz);
		pieceMods.put(clazz, getCurrentModId());
	}

	/**
	 * Registers a spell piece and tries to create its relative texture given the current loading mod.
	 * The spell texture should be in /assets/(yourmod)/textures/spell/(key).png.<br>
	 * If you want to put the spell piece elsewhere or use some other type of resource location, feel free to map
	 * the texture directly through {@link #simpleSpellTextures}.<br>
	 * As SpellPiece objects can have custom renders, depending on how you wish to handle yours, you might
	 * not even need to use this. In that case use {@link #registerSpellPiece(String, Class)}
	 */
	public static void registerSpellPieceAndTexture(String key, Class<? extends SpellPiece> clazz) {
		registerSpellPieceAndTexture(key, getCurrentModId(), clazz);
	}

	private static void registerSpellPieceAndTexture(String key, String mod, Class<? extends SpellPiece> clazz) {
		registerSpellPiece(key, clazz);
		
		String textureName = key.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
		simpleSpellTextures.put(key, new ResourceLocation(mod, String.format("textures/spell/%s.png", textureName)));
	}

	/**
	 * Adds a piece to a group. This must be done for every piece, or it'll not be selectable in the programmer
	 * interface. The "main" parameter defines whether this piece is to be set as the main piece of the respective
	 * group. The main piece is the one that has to be used for level-up to be registered.
	 */
	public static void addPieceToGroup(Class<? extends SpellPiece> clazz, String groupName, boolean main) {
		if(!groupsForName.containsKey(groupName))
			addGroup(groupName);

		PieceGroup group = groupsForName.get(groupName);
		group.addPiece(clazz, main);
		groupsForPiece.put(clazz, group);
	}

	/**
	 * Sets the required groups for a group to be unlocked.
	 */
	public static void setGroupRequirements(String groupName, int level, String... reqs) {
		if(!groupsForName.containsKey(groupName))
			addGroup(groupName);

		PieceGroup group = groupsForName.get(groupName);
		group.setRequirements(level, reqs);
	}

	private static void addGroup(String groupName) {
		groupsForName.put(groupName, new PieceGroup(groupName));
		levelCap++;
	}

	/**
	 * Gets the CAD the passed PlayerEntity is using. As a player can only have one CAD, if there's
	 * more than one, this will return null.
	 */
	public static ItemStack getPlayerCAD(PlayerEntity player) {
		if(player == null)
			return ItemStack.EMPTY;

		ItemStack cad = ItemStack.EMPTY;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if(!stackAt.isEmpty() && stackAt.getItem() instanceof ICAD) {
				if(!cad.isEmpty())
					return ItemStack.EMPTY; // Player can only have one CAD

				cad = stackAt;
			}
		}

		return cad;
	}
	
	public static int getPlayerCADSlot(PlayerEntity player) {
		if(player == null)
			return -1;

		int slot = -1;
		for(int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stackAt = player.inventory.getStackInSlot(i);
			if(!stackAt.isEmpty() && stackAt.getItem() instanceof ICAD) {
				if(slot != -1)
					return -1; // Player can only have one CAD

				slot = i;
			}
		}

		return slot;
	}

	public static boolean canCADBeUpdated(PlayerEntity player) {
		if(player == null)
			return false;

		if(player.openContainer == null)
			return true;

		int cadSlot = getPlayerCADSlot(player);
		return cadSlot < 9 || cadSlot == 40;
	}

	public static void registerTrickRecipe(String trick, Object input, ItemStack output, ItemStack minAssembly) {
		//TODO: Someone check if this is correct
		trickRecipes.add(new TrickRecipe(trick, Ingredient.fromItems((IItemProvider) input), output, minAssembly));
	}

}
