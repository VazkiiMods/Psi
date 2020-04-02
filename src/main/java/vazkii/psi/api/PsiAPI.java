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
import net.minecraft.client.renderer.model.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
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

import java.util.*;
import java.util.stream.Collectors;

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


	public static final String MOD_ID = "psi";
	public static final ResourceLocation PSI_PIECE_TEXTURE_ATLAS = new ResourceLocation(MOD_ID, "spell_pieces");


	private static final SimpleRegistry<Class<? extends SpellPiece>> spellPieceRegistry = new SimpleRegistry<>();
	private static final Map<ResourceLocation, Material> simpleSpellTextures = new HashMap<>();
	private static final Multimap<ResourceLocation, Class<? extends SpellPiece>> advancementGroups = HashMultimap.create();
	private static final Map<Class<? extends SpellPiece>, ResourceLocation> advancementGroupsInverse = new HashMap<>();
	private static final Map<ResourceLocation, Class<? extends SpellPiece>> mainPieceForGroup = new HashMap<>();

	public static final List<TrickRecipe> trickRecipes = new ArrayList<>();


	public static final PsimetalArmorMaterial PSIMETAL_ARMOR_MATERIAL = new PsimetalArmorMaterial("psimetal", 18, new int[]{2, 6, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0F, () -> Ingredient.fromTag(ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "ingots/psimetal"))));
	public static final PsimetalToolMaterial PSIMETAL_TOOL_MATERIAL = new PsimetalToolMaterial();


	/**
	 * Registers a Spell Piece.
	 */
	public static void registerSpellPiece(ResourceLocation resourceLocation, Class<? extends SpellPiece> clazz) {
		PsiAPI.spellPieceRegistry.register(resourceLocation, clazz);
	}

	/**
	 * Registers a spell piece and its texture.
	 * The spell texture will be set to <code>/assets/(namespace)/textures/spell/(path).png</code>,
	 * and will be stitched to an atlas for render.<br />
	 * To use a different path, see {@link #registerPieceTexture}.<br />
	 * To use custom rendering entirely, call {@link #registerSpellPiece} and override {@link SpellPiece#drawBackground} to do your own rendering.
	 */
	public static void registerSpellPieceAndTexture(ResourceLocation id, Class<? extends SpellPiece> clazz) {
		registerSpellPiece(id, clazz);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> registerPieceTexture(id, new ResourceLocation(id.getNamespace(), "spell/" + id.getPath())));
	}

	/**
	 * Register the texture of a piece
	 * @param pieceId ID of the piece whose texture to register
	 * @param texture Path to the piece's texture, where <code>domain:foo/bar</code> translates to <code>/assets/domain/textures/foo/bar.png</code>.
	 *                In other words, do <b>not</b> prefix with textures/ nor suffix with .png.
	 */
	@OnlyIn(Dist.CLIENT)
	public static void registerPieceTexture(ResourceLocation pieceId, ResourceLocation texture) {
		PsiAPI.simpleSpellTextures.put(pieceId, new Material(PSI_PIECE_TEXTURE_ATLAS, texture));
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
				LogManager.getLogger(MOD_ID).info("Group " + resLoc + " already has a main piece!");
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
		registerTrickRecipe(new ResourceLocation(MOD_ID, trick.toLowerCase()), input, output, minAssembly);
	}

	public static Class<? extends SpellPiece> getSpellPiece(ResourceLocation key) {
		return spellPieceRegistry.getValue(key).orElse(null);
	}

	public static ResourceLocation getSpellPieceKey(Class<? extends SpellPiece> clazz) {
		return spellPieceRegistry.getKey(clazz);
	}

	public static Material getSpellPieceMaterial(ResourceLocation key) {
		return simpleSpellTextures.get(key);
	}

	public static Collection<Class<? extends SpellPiece>> getPiecesInAdvancementGroup(ResourceLocation group) {
		return advancementGroups.get(group);
	}

	public static ResourceLocation getGroupForPiece(Class<? extends SpellPiece> piece) {
		return advancementGroupsInverse.get(piece);
	}

	public static Class<? extends SpellPiece> getMainPieceForGroup(ResourceLocation group) {
		return mainPieceForGroup.get(group);
	}

	public static boolean isPieceRegistered(ResourceLocation key) {
		return spellPieceRegistry.getValue(key).isPresent();
	}

	public static Collection<Material> getAllSpellPieceMaterial() {
		return simpleSpellTextures.values();
	}

	public static Collection<Class<? extends SpellPiece>> getAllRegisteredSpellPieces() {
		return spellPieceRegistry.stream().collect(Collectors.toList());
	}

	public static Collection<ResourceLocation> getAllPieceKeys() {
		return spellPieceRegistry.keySet();
	}

	public static SimpleRegistry<Class<? extends SpellPiece>> getSpellPieceRegistry() {
		return spellPieceRegistry;
	}
}
