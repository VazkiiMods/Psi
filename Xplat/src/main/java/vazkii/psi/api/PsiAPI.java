/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.DummyMethodHandler;
import vazkii.psi.api.internal.IInternalMethodHandler;
import vazkii.psi.api.spell.SpellPieceGroup;
import vazkii.psi.api.spell.SpellPieceSettings;
import vazkii.psi.api.spell.SpellPieceType;
import vazkii.psi.common.item.SimpleTier;
import vazkii.psi.common.registry.PsiRegistries;

import java.util.Optional;

public final class PsiAPI {
	public static final String MOD_ID = "psi";
	public static final ResourceKey<Registry<SpellPieceType>> SPELL_PIECE_REGISTRY_TYPE_KEY = ResourceKey.createRegistryKey(PsiAPI.location("spell_piece_registry_type_key"));
	public static final Registry<SpellPieceType> SPELL_PIECE_REGISTRY = PsiRegistries.create(SPELL_PIECE_REGISTRY_TYPE_KEY, true);

	/**
	 * Datapack registry of {@link SpellPieceGroup}s. Only available through a {@link RegistryAccess}
	 * once a level is loaded.
	 */
	public static final ResourceKey<Registry<SpellPieceGroup>> SPELL_PIECE_GROUP_REGISTRY_KEY = ResourceKey.createRegistryKey(PsiAPI.location("spell_piece_group"));
	/**
	 * Datapack registry of {@link SpellPieceSettings}, keyed by spell piece id.
	 */
	public static final ResourceKey<Registry<SpellPieceSettings>> SPELL_PIECE_SETTINGS_REGISTRY_KEY = ResourceKey.createRegistryKey(PsiAPI.location("spell_piece_settings"));

	public static final Tier PSIMETAL_TOOL_MATERIAL = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 900, 7.8F,
			2F, 12, () -> Ingredient.of(BuiltInRegistries.ITEM.get(location("psimetal"))));
	/**
	 * The internal method handler in use. This object allows the API to interact with the mod.
	 * By default, this is a dummy. In the mod itself, this is replaced with an implementation that
	 * can handle all of its queries.<br>
	 * <br>
	 *
	 * <b>DO NOT EVER, EVER, OVERWRITE THIS VALUE</b>
	 */
	public static IInternalMethodHandler internalHandler = new DummyMethodHandler();

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

	/**
	 * Finds the group the given piece belongs to, if any.
	 */
	public static Optional<Holder.Reference<SpellPieceGroup>> getPieceGroup(RegistryAccess registries, ResourceLocation piece) {
		return registries.registryOrThrow(SPELL_PIECE_GROUP_REGISTRY_KEY).holders()
				.filter(holder -> holder.value().contains(piece))
				.findFirst();
	}

	/**
	 * Whether the given piece may be placed in the programmer and compiled. Pieces without a
	 * {@link SpellPieceSettings} entry are enabled.
	 */
	public static boolean isPieceEnabled(RegistryAccess registries, ResourceLocation piece) {
		return registries.registry(SPELL_PIECE_SETTINGS_REGISTRY_KEY)
				.flatMap(registry -> registry.getOptional(piece))
				.map(SpellPieceSettings::enabled)
				.orElse(true);
	}

	/**
	 * Whether the given piece is locked for the player: its group unlocks on an advancement the
	 * player has not completed. Groups that unlock by executing their main piece never lock,
	 * and nothing is locked for creative players.
	 */
	public static boolean isPieceLocked(Player player, RegistryAccess registries, ResourceLocation piece) {
		if(player.isCreative()) {
			return false;
		}

		Optional<Holder.Reference<SpellPieceGroup>> group = getPieceGroup(registries, piece);
		if(group.isEmpty() || !(group.get().value().unlock() instanceof SpellPieceGroup.Unlock.Advancement(ResourceLocation required))) {
			return false;
		}

		return !internalHandler.getDataForPlayer(player).hasAdvancement(required);
	}

	/**
	 * Whether the player may place the given piece in the programmer and compile it:
	 * {@link #isPieceEnabled(RegistryAccess, ResourceLocation) enabled} and not
	 * {@link #isPieceLocked(Player, RegistryAccess, ResourceLocation) locked}.
	 */
	public static boolean isPieceAvailable(Player player, RegistryAccess registries, ResourceLocation piece) {
		return isPieceEnabled(registries, piece) && !isPieceLocked(player, registries, piece);
	}

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
