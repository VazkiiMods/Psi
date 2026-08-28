/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.api;

import net.minecraft.core.Registry;
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
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.item.SimpleTier;
import vazkii.psi.common.registry.PsiRegistries;

import java.util.Collection;

public final class PsiAPI {
	public static final String MOD_ID = "psi";
	public static final ResourceKey<Registry<Class<? extends SpellPiece>>> SPELL_PIECE_REGISTRY_TYPE_KEY = ResourceKey.createRegistryKey(PsiAPI.location("spell_piece_registry_type_key"));
	public static final Registry<Class<? extends SpellPiece>> SPELL_PIECE_REGISTRY = PsiRegistries.create(SPELL_PIECE_REGISTRY_TYPE_KEY, true);

	// The main piece for the group is the one that comes first.
	public static final ResourceKey<Registry<Collection<Class<? extends SpellPiece>>>> ADVANCEMENT_GROUP_REGISTRY_KEY = ResourceKey.createRegistryKey(PsiAPI.location("advancement_group_registry_key"));
	public static final Registry<Collection<Class<? extends SpellPiece>>> ADVANCEMENT_GROUP_REGISTRY = PsiRegistries.create(ADVANCEMENT_GROUP_REGISTRY_KEY, true);

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

	public static ResourceLocation location(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
