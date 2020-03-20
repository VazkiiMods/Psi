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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.apache.logging.log4j.Level;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketableCapability;
import vazkii.psi.api.internal.DummyMethodHandler;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.material.PsimetalArmorMaterial;
import vazkii.psi.api.material.PsimetalToolMaterial;
import vazkii.psi.api.recipe.TrickRecipe;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibMisc;

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

	@CapabilityInject(ISpellImmune.class)
	public static Capability<ISpellImmune> SPELL_IMMUNE_CAPABILITY = null;

	@CapabilityInject(IDetonationHandler.class)
	public static Capability<IDetonationHandler> DETONATION_HANDLER_CAPABILITY = null;

	@CapabilityInject(IPsiBarDisplay.class)
	public static Capability<IPsiBarDisplay> PSI_BAR_DISPLAY_CAPABILITY = null;

	@CapabilityInject(ISpellAcceptor.class)
	public static Capability<ISpellAcceptor> SPELL_ACCEPTOR_CAPABILITY = null;

	@CapabilityInject(ICADData.class)
	public static Capability<ICADData> CAD_DATA_CAPABILITY = null;

	@CapabilityInject(ISocketableCapability.class)
	public static Capability<ISocketableCapability> SOCKETABLE_CAPABILITY = null;


	public static final SimpleRegistry<Class<? extends SpellPiece>> spellPieceRegistry = new SimpleRegistry<>();
	public static final HashMap<String, ResourceLocation> simpleSpellTextures = new HashMap<>();
	public static final Multimap<ResourceLocation, Class<? extends SpellPiece>> advancementGroups = HashMultimap.create();
	public static final HashMap<Class<? extends SpellPiece>, ResourceLocation> advancementGroupsInverse = new HashMap<>();
	public static final HashMap<ResourceLocation, Class<? extends SpellPiece>> mainPieceForGroup = new HashMap<>();

	public static final List<TrickRecipe> trickRecipes = new ArrayList<>();


	public static final PsimetalArmorMaterial PSIMETAL_ARMOR_MATERIAL = new PsimetalArmorMaterial("psimetal", 18, new int[]{2, 6, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F, null);
	public static final PsimetalToolMaterial PSIMETAL_TOOL_MATERIAL = new PsimetalToolMaterial();


	/**
	 * Registers a Spell Piece given its class, by which, it puts it in the registry.
	 */
	public static void registerSpellPiece(ResourceLocation resourceLocation, Class<? extends SpellPiece> clazz) {
		PsiAPI.spellPieceRegistry.register(resourceLocation, clazz);
	}

	/**
	 * Registers a spell piece and tries to create its relative texture given the current loading mod.
	 * The spell texture should be in /assets/(yourmod)/textures/spell/(key).png.<br>
	 * If you want to put the spell piece elsewhere or use some other type of resource location, feel free to map
	 * the texture directly through {@link #simpleSpellTextures}.<br>
	 * As SpellPiece objects can have custom renders, depending on how you wish to handle yours, you might
	 * not even need to use this. In that case use {@link #registerSpellPiece(ResourceLocation, Class)}
	 */
	public static void registerSpellPieceAndTexture(ResourceLocation resourceLocation, Class<? extends SpellPiece> clazz) {
		registerSpellPiece(resourceLocation, clazz);
		PsiAPI.simpleSpellTextures.put(resourceLocation.getPath(), new ResourceLocation(resourceLocation.getNamespace(), String.format("textures/spell/%s.png", resourceLocation.getPath())));
	}


	/**
	 * Adds a piece to a group. This must be done for every piece, or it'll not be selectable in the programmer
	 * interface. The "main" parameter defines whether this piece is to be set as the main piece of the respective
	 * group. The main piece is the one that has to be used for level-up to be registered.
	 */
	public static void addPieceToGroup(Class<? extends SpellPiece> clazz, ResourceLocation resLoc, boolean main) {
		advancementGroups.put(resLoc, clazz);
		advancementGroupsInverse.put(clazz, resLoc);

		if (main) {
			if (mainPieceForGroup.containsKey(resLoc))
				Psi.logger.log(Level.INFO, "Group " + resLoc + " already has a main piece!");
			mainPieceForGroup.put(resLoc, clazz);
		}
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

	public static void registerTrickRecipe(ResourceLocation trick, Ingredient input, ItemStack output, ItemStack minAssembly) {
		Class<? extends SpellPiece> pieceClass = spellPieceRegistry.getValue(trick).orElse(null);
		SpellPiece piece = null;
		if (pieceClass != null && PieceTrick.class.isAssignableFrom(pieceClass))
			piece = SpellPiece.create(pieceClass, new Spell());
		trickRecipes.add(new TrickRecipe((PieceTrick) piece, input, output, minAssembly));
	}
	
	public static void registerTrickRecipe(String trick, Ingredient input, ItemStack output, ItemStack minAssembly) {
		registerTrickRecipe(new ResourceLocation(LibMisc.MOD_ID, trick.toLowerCase()), input, output, minAssembly);
	}

}
