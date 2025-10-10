/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.spell;

import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibPieceNames;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class SpellPieceMaterial {

	// ========== REGISTRIES ==========
	public static final DeferredRegister<Material> SPELL_PIECE_MATERIAL =
			DeferredRegister.create(ClientPsiAPI.SPELL_PIECE_MATERIAL, LibMisc.MOD_ID);

	// ========== MEMORY MANAGEMENT ==========
	public static final DeferredHolder<Material, Material> CROSS_CONNECTOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CROSS_CONNECTOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CROSS_CONNECTOR)));
	public static final DeferredHolder<Material, Material> SELECTOR_SAVED_VECTOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_SAVED_VECTOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_SAVED_VECTOR)));
	public static final DeferredHolder<Material, Material> TRICK_DETONATE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_DETONATE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_DETONATE)));
	public static final DeferredHolder<Material, Material> TRICK_SAVE_VECTOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SAVE_VECTOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SAVE_VECTOR)));

	// ========== TUTORIAL_1 ==========
	public static final DeferredHolder<Material, Material> TRICK_DEBUG =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_DEBUG, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_DEBUG)));
	public static final DeferredHolder<Material, Material> TRICK_DEBUG_SPAMLESS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_DEBUG_SPAMLESS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_DEBUG_SPAMLESS)));

	// ========== TUTORIAL_2 ==========
	public static final DeferredHolder<Material, Material> CONSTANT_NUMBER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CONSTANT_NUMBER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CONSTANT_NUMBER)));
	public static final DeferredHolder<Material, Material> CONNECTOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CONNECTOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CONNECTOR)));

	// ========== TUTORIAL_3 ==========
	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_LOOK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_LOOK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_LOOK)));
	public static final DeferredHolder<Material, Material> TRICK_ADD_MOTION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_ADD_MOTION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_ADD_MOTION)));

	// ========== TUTORIAL_4 ==========
	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_POSITION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_POSITION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_POSITION)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_RAYCAST =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_RAYCAST, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_RAYCAST)));
	public static final DeferredHolder<Material, Material> TRICK_EXPLODE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_EXPLODE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_EXPLODE)));
	public static final DeferredHolder<Material, Material> ERROR_SUPPRESSOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.ERROR_SUPPRESSOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.ERROR_SUPPRESSOR)));
	public static final DeferredHolder<Material, Material> ERROR_CATCH =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.ERROR_CATCH, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.ERROR_CATCH)));

	// ========== PROJECTILES ==========
	public static final DeferredHolder<Material, Material> SELECTOR_FOCAL_POINT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_FOCAL_POINT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_FOCAL_POINT)));
	public static final DeferredHolder<Material, Material> SELECTOR_RULER_VECTOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_RULER_VECTOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_RULER_VECTOR)));

	// ========== ENTITIES_INTRO ==========
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_ITEMS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_ITEMS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_ITEMS)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_LIVING =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_LIVING, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_LIVING)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_ENEMIES =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_ENEMIES, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_ENEMIES)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_ANIMALS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_ANIMALS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_ANIMALS)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_PROJECTILES =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_PROJECTILES, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_PROJECTILES)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_CHARGES =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_CHARGES, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_CHARGES)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_FALLING_BLOCKS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_GLOWING =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_GLOWING, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_GLOWING)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_PLAYERS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_PLAYERS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_PLAYERS)));
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_VEHICLES =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_VEHICLES, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_VEHICLES)));

	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_MOTION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_MOTION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_MOTION)));
	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_AXIAL_LOOK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK)));
	public static final DeferredHolder<Material, Material> OPERATOR_CLOSEST_TO_POINT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_CLOSEST_TO_POINT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_CLOSEST_TO_POINT)));
	public static final DeferredHolder<Material, Material> OPERATOR_RANDOM_ENTITY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_RANDOM_ENTITY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_RANDOM_ENTITY)));
	public static final DeferredHolder<Material, Material> OPERATOR_FOCUSED_ENTITY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_FOCUSED_ENTITY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_FOCUSED_ENTITY)));
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_ADD =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_ADD, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_ADD)));
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_REMOVE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_REMOVE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_REMOVE)));
	public static final DeferredHolder<Material, Material> OPERATOR_CLOSEST_TO_LINE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_CLOSEST_TO_LINE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_CLOSEST_TO_LINE)));
	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_HEALTH =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_HEALTH, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_HEALTH)));
	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_RAYCAST =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_RAYCAST, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_RAYCAST)));
	public static final DeferredHolder<Material, Material> OPERATOR_ENTITY_HEIGHT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ENTITY_HEIGHT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ENTITY_HEIGHT)));

	// ========== TOOL_CASTING ==========
	public static final DeferredHolder<Material, Material> SELECTOR_BLOCK_BROKEN =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_BLOCK_BROKEN, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_BLOCK_BROKEN)));
	public static final DeferredHolder<Material, Material> SELECTOR_BLOCK_SIDE_BROKEN =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN)));
	public static final DeferredHolder<Material, Material> SELECTOR_ATTACK_TARGET =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_ATTACK_TARGET, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_ATTACK_TARGET)));
	public static final DeferredHolder<Material, Material> SELECTOR_ITEM_COUNT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_ITEM_COUNT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_ITEM_COUNT)));

	// ========== LOOPCASTING ==========
	public static final DeferredHolder<Material, Material> SELECTOR_LOOPCAST_INDEX =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_LOOPCAST_INDEX, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_LOOPCAST_INDEX)));
	public static final DeferredHolder<Material, Material> OPERATOR_MODULUS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_MODULUS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_MODULUS)));
	public static final DeferredHolder<Material, Material> OPERATOR_INTEGER_DIVIDE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_INTEGER_DIVIDE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_INTEGER_DIVIDE)));

	// ========== FLOW_CONTROL ==========
	public static final DeferredHolder<Material, Material> SELECTOR_SNEAK_STATUS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_SNEAK_STATUS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_SNEAK_STATUS)));
	public static final DeferredHolder<Material, Material> SELECTOR_TICK_TIME =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_TICK_TIME, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_TICK_TIME)));
	public static final DeferredHolder<Material, Material> SELECTOR_TPS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_TPS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_TPS)));
	public static final DeferredHolder<Material, Material> TRICK_DELAY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_DELAY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_DELAY)));
	public static final DeferredHolder<Material, Material> TRICK_DIE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_DIE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_DIE)));
	public static final DeferredHolder<Material, Material> TRICK_EVALUATE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_EVALUATE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_EVALUATE)));
	public static final DeferredHolder<Material, Material> TRICK_BREAK_LOOP =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_BREAK_LOOP, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_BREAK_LOOP)));
	public static final DeferredHolder<Material, Material> CONSTANT_WRAPPER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CONSTANT_WRAPPER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CONSTANT_WRAPPER)));

	// ========== NUMBERS_INTRO ==========
	public static final DeferredHolder<Material, Material> OPERATOR_SUM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_SUM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_SUM)));
	public static final DeferredHolder<Material, Material> OPERATOR_SUBTRACT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_SUBTRACT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_SUBTRACT)));
	public static final DeferredHolder<Material, Material> OPERATOR_MULTIPLY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_MULTIPLY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_MULTIPLY)));
	public static final DeferredHolder<Material, Material> OPERATOR_DIVIDE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_DIVIDE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_DIVIDE)));
	public static final DeferredHolder<Material, Material> OPERATOR_ABSOLUTE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ABSOLUTE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ABSOLUTE)));
	public static final DeferredHolder<Material, Material> OPERATOR_INVERSE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_INVERSE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_INVERSE)));
	public static final DeferredHolder<Material, Material> OPERATOR_ROOT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ROOT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ROOT)));

	// ========== SECONDARY_OPERATORS ==========
	public static final DeferredHolder<Material, Material> OPERATOR_SQUARE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_SQUARE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_SQUARE)));
	public static final DeferredHolder<Material, Material> OPERATOR_CUBE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_CUBE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_CUBE)));
	public static final DeferredHolder<Material, Material> OPERATOR_POWER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_POWER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_POWER)));
	public static final DeferredHolder<Material, Material> OPERATOR_SQUARE_ROOT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_SQUARE_ROOT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_SQUARE_ROOT)));
	public static final DeferredHolder<Material, Material> OPERATOR_LOG =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LOG, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LOG)));
	public static final DeferredHolder<Material, Material> OPERATOR_CEILING =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_CEILING, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_CEILING)));
	public static final DeferredHolder<Material, Material> OPERATOR_FLOOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_FLOOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_FLOOR)));
	public static final DeferredHolder<Material, Material> OPERATOR_ROUND =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ROUND, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ROUND)));
	public static final DeferredHolder<Material, Material> OPERATOR_MAX =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_MAX, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_MAX)));
	public static final DeferredHolder<Material, Material> OPERATOR_MIN =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_MIN, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_MIN)));
	public static final DeferredHolder<Material, Material> CONSTANT_E =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CONSTANT_E, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CONSTANT_E)));

	// ========== TRIGONOMETRY ==========
	public static final DeferredHolder<Material, Material> OPERATOR_SIN =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_SIN, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_SIN)));
	public static final DeferredHolder<Material, Material> OPERATOR_COS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_COS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_COS)));
	public static final DeferredHolder<Material, Material> OPERATOR_ASIN =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ASIN, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ASIN)));
	public static final DeferredHolder<Material, Material> OPERATOR_ACOS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_ACOS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_ACOS)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_DOT_PRODUCT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT)));
	public static final DeferredHolder<Material, Material> OPERATOR_GAMMA_FUNCTION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_GAMMA_FUNCTION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_GAMMA_FUNCTION)));
	public static final DeferredHolder<Material, Material> OPERATOR_PLANAR_NORMAL_VECTOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_ROTATE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_ROTATE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_ROTATE)));
	public static final DeferredHolder<Material, Material> CONSTANT_PI =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CONSTANT_PI, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CONSTANT_PI)));
	public static final DeferredHolder<Material, Material> CONSTANT_TAU =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.CONSTANT_TAU, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.CONSTANT_TAU)));
	public static final DeferredHolder<Material, Material> OPERATOR_SIGNUM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_SIGNUM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_SIGNUM)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_ABSOLUTE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_ABSOLUTE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_ABSOLUTE)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_SIGNUM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_SIGNUM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_SIGNUM)));

	// ========== VECTORS_INTRO ==========
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_SUM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_SUM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_SUM)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_SUBTRACT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_SUBTRACT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_SUBTRACT)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_MULTIPLY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_MULTIPLY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_MULTIPLY)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_DIVIDE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_DIVIDE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_DIVIDE)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_CROSS_PRODUCT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_NORMALIZE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_NORMALIZE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_NORMALIZE)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_NEGATE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_NEGATE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_NEGATE)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_MAGNITUDE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_MAGNITUDE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_MAGNITUDE)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_CONSTRUCT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_CONSTRUCT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_CONSTRUCT)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_EXTRACT_X =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_X, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_EXTRACT_X)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_EXTRACT_Y =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_EXTRACT_Z =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_MAXIMUM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_MAXIMUM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_MAXIMUM)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_MINIMUM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_MINIMUM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_MINIMUM)));

	// ========== BLOCK_WORKS ==========
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_RAYCAST_AXIS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS)));
	public static final DeferredHolder<Material, Material> OPERATOR_VECTOR_PROJECT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_VECTOR_PROJECT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_VECTOR_PROJECT)));
	public static final DeferredHolder<Material, Material> OPERATOR_BLOCK_LIGHT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_BLOCK_LIGHT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_BLOCK_LIGHT)));
	public static final DeferredHolder<Material, Material> OPERATOR_BLOCK_HARDNESS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_BLOCK_HARDNESS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_BLOCK_HARDNESS)));
	public static final DeferredHolder<Material, Material> OPERATOR_BLOCK_COMPARATOR_STRENGTH =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_BLOCK_COMPARATOR_STRENGTH, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_BLOCK_COMPARATOR_STRENGTH)));
	public static final DeferredHolder<Material, Material> OPERATOR_BLOCK_SIDE_SOLIDITY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_BLOCK_SIDE_SOLIDITY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_BLOCK_SIDE_SOLIDITY)));
	public static final DeferredHolder<Material, Material> OPERATOR_BLOCK_MINING_LEVEL =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_BLOCK_MINING_LEVEL, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_BLOCK_MINING_LEVEL)));
	public static final DeferredHolder<Material, Material> TRICK_BREAK_BLOCK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_BREAK_BLOCK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_BREAK_BLOCK)));
	public static final DeferredHolder<Material, Material> TRICK_BREAK_IN_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_BREAK_IN_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_BREAK_IN_SEQUENCE)));
	public static final DeferredHolder<Material, Material> TRICK_PLACE_BLOCK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_PLACE_BLOCK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_PLACE_BLOCK)));
	public static final DeferredHolder<Material, Material> TRICK_PLACE_IN_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_PLACE_IN_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_PLACE_IN_SEQUENCE)));

	// ========== BLOCK_MOVEMENT ==========
	public static final DeferredHolder<Material, Material> TRICK_MOVE_BLOCK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_MOVE_BLOCK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_MOVE_BLOCK)));
	public static final DeferredHolder<Material, Material> TRICK_COLLAPSE_BLOCK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_COLLAPSE_BLOCK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_COLLAPSE_BLOCK)));
	public static final DeferredHolder<Material, Material> TRICK_MOVE_BLOCK_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE)));
	public static final DeferredHolder<Material, Material> TRICK_COLLAPSE_BLOCK_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_COLLAPSE_BLOCK_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_COLLAPSE_BLOCK_SEQUENCE)));

	// ========== BLOCK_CONJURATION ==========
	public static final DeferredHolder<Material, Material> TRICK_CONJURE_BLOCK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_CONJURE_BLOCK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_CONJURE_BLOCK)));
	public static final DeferredHolder<Material, Material> TRICK_CONJURE_LIGHT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_CONJURE_LIGHT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_CONJURE_LIGHT)));
	public static final DeferredHolder<Material, Material> TRICK_CONJURE_BLOCK_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE)));
	public static final DeferredHolder<Material, Material> TRICK_PARTICLE_TRAIL =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_PARTICLE_TRAIL, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_PARTICLE_TRAIL)));

	// ========== MOVEMENT ==========
	public static final DeferredHolder<Material, Material> TRICK_BLINK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_BLINK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_BLINK)));
	public static final DeferredHolder<Material, Material> TRICK_MASS_BLINK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_MASS_BLINK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_MASS_BLINK)));
	public static final DeferredHolder<Material, Material> TRICK_MASS_ADD_MOTION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_MASS_ADD_MOTION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_MASS_ADD_MOTION)));
	public static final DeferredHolder<Material, Material> TRICK_MASS_EXODUS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_MASS_EXODUS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_MASS_EXODUS)));
	public static final DeferredHolder<Material, Material> SELECTOR_IS_ELYTRA_FLYING =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_IS_ELYTRA_FLYING, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_IS_ELYTRA_FLYING)));

	// ========== ELEMENTAL_ARTS ==========
	public static final DeferredHolder<Material, Material> OPERATOR_RANDOM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_RANDOM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_RANDOM)));
	public static final DeferredHolder<Material, Material> TRICK_SMITE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SMITE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SMITE)));
	public static final DeferredHolder<Material, Material> TRICK_BLAZE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_BLAZE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_BLAZE)));
	public static final DeferredHolder<Material, Material> TRICK_TORRENT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_TORRENT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_TORRENT)));
	public static final DeferredHolder<Material, Material> TRICK_OVERGROW =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_OVERGROW, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_OVERGROW)));

	// ========== POSITIVE_EFFECTS ==========
	public static final DeferredHolder<Material, Material> TRICK_SPEED =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SPEED, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SPEED)));
	public static final DeferredHolder<Material, Material> TRICK_HASTE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_HASTE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_HASTE)));
	public static final DeferredHolder<Material, Material> TRICK_STRENGTH =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_STRENGTH, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_STRENGTH)));
	public static final DeferredHolder<Material, Material> TRICK_JUMP_BOOST =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_JUMP_BOOST, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_JUMP_BOOST)));
	public static final DeferredHolder<Material, Material> TRICK_WATER_BREATHING =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_WATER_BREATHING, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_WATER_BREATHING)));
	public static final DeferredHolder<Material, Material> TRICK_FIRE_RESISTANCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_FIRE_RESISTANCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_FIRE_RESISTANCE)));
	public static final DeferredHolder<Material, Material> TRICK_INVISIBILITY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_INVISIBILITY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_INVISIBILITY)));
	public static final DeferredHolder<Material, Material> TRICK_REGENERATION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_REGENERATION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_REGENERATION)));
	public static final DeferredHolder<Material, Material> TRICK_RESISTANCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_RESISTANCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_RESISTANCE)));
	public static final DeferredHolder<Material, Material> TRICK_NIGHT_VISION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_NIGHT_VISION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_NIGHT_VISION)));

	// ========== NEGATIVE_EFFECTS ==========
	public static final DeferredHolder<Material, Material> TRICK_WITHER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_WITHER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_WITHER)));
	public static final DeferredHolder<Material, Material> TRICK_SLOWNESS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SLOWNESS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SLOWNESS)));
	public static final DeferredHolder<Material, Material> TRICK_WEAKNESS =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_WEAKNESS, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_WEAKNESS)));
	public static final DeferredHolder<Material, Material> TRICK_IGNITE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_IGNITE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_IGNITE)));

	// ========== EIDOS_REVERSAL ==========
	public static final DeferredHolder<Material, Material> SELECTOR_EIDOS_CHANGELOG =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_EIDOS_CHANGELOG, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_EIDOS_CHANGELOG)));
	public static final DeferredHolder<Material, Material> TRICK_EIDOS_ANCHOR =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_EIDOS_ANCHOR, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_EIDOS_ANCHOR)));
	public static final DeferredHolder<Material, Material> TRICK_EIDOS_REVERSAL =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_EIDOS_REVERSAL, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_EIDOS_REVERSAL)));

	// ========== EXOSUIT_CASTING ==========
	public static final DeferredHolder<Material, Material> SELECTOR_TIME =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_TIME, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_TIME)));
	public static final DeferredHolder<Material, Material> SELECTOR_ATTACKER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_ATTACKER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_ATTACKER)));
	public static final DeferredHolder<Material, Material> SELECTOR_DAMAGE_TAKEN =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_DAMAGE_TAKEN, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_DAMAGE_TAKEN)));
	public static final DeferredHolder<Material, Material> SELECTOR_SUCCESS_COUNTER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_SUCCESS_COUNTER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_SUCCESS_COUNTER)));
	public static final DeferredHolder<Material, Material> SELECTOR_CASTER_BATTERY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_CASTER_BATTERY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_CASTER_BATTERY)));
	public static final DeferredHolder<Material, Material> SELECTOR_CASTER_ENERGY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_CASTER_ENERGY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_CASTER_ENERGY)));

	// ========== DETECTION_DYNAMICS ==========
	public static final DeferredHolder<Material, Material> SELECTOR_ITEM_PRESENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_ITEM_PRESENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_ITEM_PRESENCE)));
	public static final DeferredHolder<Material, Material> SELECTOR_BLOCK_PRESENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_BLOCK_PRESENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_BLOCK_PRESENCE)));
	public static final DeferredHolder<Material, Material> TRICK_SWITCH_TARGET_SLOT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SWITCH_TARGET_SLOT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SWITCH_TARGET_SLOT)));
	public static final DeferredHolder<Material, Material> TRICK_CHANGE_SLOT =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_CHANGE_SLOT, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_CHANGE_SLOT)));

	// ========== SMELTERY ==========
	public static final DeferredHolder<Material, Material> SELECTOR_NEARBY_SMELTABLES =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_NEARBY_SMELTABLES, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_NEARBY_SMELTABLES)));
	public static final DeferredHolder<Material, Material> TRICK_SMELT_BLOCK =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SMELT_BLOCK, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SMELT_BLOCK)));
	public static final DeferredHolder<Material, Material> TRICK_SMELT_ITEM =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SMELT_ITEM, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SMELT_ITEM)));
	public static final DeferredHolder<Material, Material> TRICK_SMELT_BLOCK_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE)));

	// ========== INFUSION / GREATER_INFUSION ==========
	public static final DeferredHolder<Material, Material> TRICK_INFUSION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_INFUSION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_INFUSION)));
	public static final DeferredHolder<Material, Material> TRICK_GREATER_INFUSION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_GREATER_INFUSION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_GREATER_INFUSION)));
	public static final DeferredHolder<Material, Material> TRICK_EBONY_IVORY =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_EBONY_IVORY, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_EBONY_IVORY)));

	// ========== LIST_OPERATIONS ==========
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_EXCLUSION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_EXCLUSION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_EXCLUSION)));
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_INTERSECTION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_INTERSECTION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_INTERSECTION)));
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_SIZE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_SIZE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_SIZE)));
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_UNION =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_UNION, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_UNION)));
	public static final DeferredHolder<Material, Material> OPERATOR_LIST_INDEX =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.OPERATOR_LIST_INDEX, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.OPERATOR_LIST_INDEX)));

	// ========== EIDOS / MISC / RUSSIAN ROULETTE & OTHERS ==========
	public static final DeferredHolder<Material, Material> SELECTOR_CASTER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.SELECTOR_CASTER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.SELECTOR_CASTER)));
	public static final DeferredHolder<Material, Material> TRICK_PLAY_SOUND =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_PLAY_SOUND, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_PLAY_SOUND)));
	public static final DeferredHolder<Material, Material> TRICK_TILL =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_TILL, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_TILL)));
	public static final DeferredHolder<Material, Material> TRICK_TILL_SEQUENCE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_TILL_SEQUENCE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_TILL_SEQUENCE)));
	public static final DeferredHolder<Material, Material> TRICK_SPIN_CHAMBER =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_SPIN_CHAMBER, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_SPIN_CHAMBER)));
	public static final DeferredHolder<Material, Material> TRICK_RUSSIAN_ROULETTE =
			SPELL_PIECE_MATERIAL.register(LibPieceNames.TRICK_RUSSIAN_ROULETTE, () -> new Material(InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(PsiAPI.MOD_ID, "spell/" + LibPieceNames.TRICK_RUSSIAN_ROULETTE)));
}
