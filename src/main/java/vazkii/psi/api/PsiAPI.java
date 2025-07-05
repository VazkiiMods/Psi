/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.common.SimpleTier;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADData;
import vazkii.psi.api.cad.IPsiBarDisplay;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.DummyMethodHandler;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.item.base.ModItems;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class PsiAPI {

	public static final String MOD_ID = "psi";
	public static final EntityCapability<ISpellImmune, Void> SPELL_IMMUNE_CAPABILITY = EntityCapability.createVoid(PsiAPI.location("spell_immune"), ISpellImmune.class);
	public static final EntityCapability<IDetonationHandler, Void> DETONATION_HANDLER_CAPABILITY = EntityCapability.createVoid(PsiAPI.location("detonation_handler"), IDetonationHandler.class);
	public static final ItemCapability<IPsiBarDisplay, Void> PSI_BAR_DISPLAY_CAPABILITY = ItemCapability.createVoid(PsiAPI.location("psi_bar_display"), IPsiBarDisplay.class);
	public static final ItemCapability<ISpellAcceptor, Void> SPELL_ACCEPTOR_CAPABILITY = ItemCapability.createVoid(PsiAPI.location("spell_acceptor"), ISpellAcceptor.class);
	public static final ItemCapability<ICADData, Void> CAD_DATA_CAPABILITY = ItemCapability.createVoid(PsiAPI.location("cad_data"), ICADData.class);
	public static final ItemCapability<ISocketable, Void> SOCKETABLE_CAPABILITY = ItemCapability.createVoid(PsiAPI.location("socketable"), ISocketable.class);

	public static final ResourceKey<Registry<Class<? extends SpellPiece>>> SPELL_PIECE_REGISTRY_TYPE_KEY = ResourceKey.createRegistryKey(PsiAPI.location("spell_piece_registry_type_key"));

	private static final MappedRegistry<Class<? extends SpellPiece>> spellPieceRegistry = new MappedRegistry<>(SPELL_PIECE_REGISTRY_TYPE_KEY, Lifecycle.stable()); //TODO (circa 1.18.2): un-duct-tape this
	public static final Tier PSIMETAL_TOOL_MATERIAL = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 900, 7.8F, 2F, 12, () -> Ingredient.of(ModItems.psimetal));
	private static final Multimap<ResourceLocation, Class<? extends SpellPiece>> advancementGroups = HashMultimap.create();
	private static final Map<Class<? extends SpellPiece>, ResourceLocation> advancementGroupsInverse = new HashMap<>();
	private static final Map<ResourceLocation, Class<? extends SpellPiece>> mainPieceForGroup = new HashMap<>();
	/**
	 * The internal method handler in use. This object allows the API to interact with the mod.
	 * By default this is a dummy. In the mod itself, this is replaced with an implementation that
	 * can handle all of its queries.<br>
	 * <br>
	 *
	 * <b>DO NOT EVER, EVER, OVERWRITE THIS VALUE</b>
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

	/**
	 * Registers a Spell Piece.
	 */
	public static void registerSpellPiece(ResourceLocation resourceLocation, Class<? extends SpellPiece> clazz) {
		synchronized (PsiAPI.spellPieceRegistry) {
			PsiAPI.spellPieceRegistry.register(ResourceKey.create(SPELL_PIECE_REGISTRY_TYPE_KEY, resourceLocation), clazz, RegistrationInfo.BUILT_IN);
		}
	}

	/**
	 * Registers a spell piece and its texture.
	 * On Forge, call this at any time before registry events finish (e.g. during item registration).
	 * Note that common setup event is <em>too late</em>!
	 * The spell texture will be set to <code>/assets/(namespace)/textures/spell/(path).png</code>,
	 * and will be stitched to an atlas for render.<br />
	 * To use a different path, see {@link ClientPsiAPI#registerPieceTexture}.<br />
	 * To use custom rendering entirely, call {@link #registerSpellPiece} and override {@link SpellPiece#drawBackground}
	 * to do your own rendering.
	 */
	public static void registerSpellPieceAndTexture(ResourceLocation id, Class<? extends SpellPiece> clazz) {
		registerSpellPiece(id, clazz);
		if(FMLEnvironment.dist.isClient()) {
			ClientPsiAPI.registerPieceTexture(id, ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "spell/" + id.getPath()));
		}
	}

	/**
	 * Adds a piece to a group. This must be done for every piece, or it'll not be selectable in the programmer
	 * interface. The "main" parameter defines whether this piece is to be set as the main piece of the respective
	 * group. The main piece is the one that has to be used for level-up to be registered.
	 */
	public static void addPieceToGroup(Class<? extends SpellPiece> clazz, ResourceLocation resLoc, boolean main) {
		advancementGroups.put(resLoc, clazz);
		advancementGroupsInverse.put(clazz, resLoc);

		if(main) {
			if(mainPieceForGroup.containsKey(resLoc)) {
                LogManager.getLogger(MOD_ID).info("Group {} already has a main piece!", resLoc);
			}
			mainPieceForGroup.put(resLoc, clazz);
		}
	}

	/**
	 * Gets the CAD the passed PlayerEntity is using. As a player can only have one CAD, if there's
	 * more than one, this will return null.
	 */
	public static ItemStack getPlayerCAD(Player player) {
		if(player == null) {
			return ItemStack.EMPTY;
		}

		ItemStack cad = ItemStack.EMPTY;
		for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stackAt = player.getInventory().getItem(i);
			if(!stackAt.isEmpty() && stackAt.getItem() instanceof ICAD) {
				if(!cad.isEmpty()) {
					return ItemStack.EMPTY; // Player can only have one CAD
				}

				cad = stackAt;
			}
		}

		return cad;
	}

	public static int getPlayerCADSlot(Player player) {
		if(player == null) {
			return -1;
		}

		int slot = -1;
		for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stackAt = player.getInventory().getItem(i);
			if(!stackAt.isEmpty() && stackAt.getItem() instanceof ICAD) {
				if(slot != -1) {
					return -1; // Player can only have one CAD
				}

				slot = i;
			}
		}

		return slot;
	}

	public static boolean canCADBeUpdated(Player player) {
		if(player == null) {
			return false;
		}

		if(player.containerMenu == null) {
			return true;
		}

		int cadSlot = getPlayerCADSlot(player);
		return cadSlot < 9 || cadSlot == 40;
	}

	public static Class<? extends SpellPiece> getSpellPiece(ResourceLocation key) {
		return spellPieceRegistry.get(key);
	}

	public static ResourceLocation getSpellPieceKey(Class<? extends SpellPiece> clazz) {
		return spellPieceRegistry.getKey(clazz);
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
		return spellPieceRegistry.keySet().contains(key);
	}

	public static Collection<Class<? extends SpellPiece>> getAllRegisteredSpellPieces() {
		return spellPieceRegistry.stream().collect(Collectors.toList());
	}

	public static Collection<ResourceLocation> getAllPieceKeys() {
		return spellPieceRegistry.keySet();
	}

	public static MappedRegistry<Class<? extends SpellPiece>> getSpellPieceRegistry() {
		return spellPieceRegistry;
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
