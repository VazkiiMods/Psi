/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.client.render.spell;

import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;

import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

@SuppressWarnings("unused")
public final class SpellPieceMaterial {

	// ========== MEMORY MANAGEMENT ==========
	public static final RegistryEntry<Material> CROSS_CONNECTOR = register(LibPieceNames.CROSS_CONNECTOR);
	public static final RegistryEntry<Material> SELECTOR_SAVED_VECTOR = register(LibPieceNames.SELECTOR_SAVED_VECTOR);
	public static final RegistryEntry<Material> TRICK_DETONATE = register(LibPieceNames.TRICK_DETONATE);
	public static final RegistryEntry<Material> TRICK_SAVE_VECTOR = register(LibPieceNames.TRICK_SAVE_VECTOR);

	// ========== TUTORIAL_1 ==========
	public static final RegistryEntry<Material> TRICK_DEBUG = register(LibPieceNames.TRICK_DEBUG);
	public static final RegistryEntry<Material> TRICK_DEBUG_SPAMLESS = register(LibPieceNames.TRICK_DEBUG_SPAMLESS);

	// ========== TUTORIAL_2 ==========
	public static final RegistryEntry<Material> CONSTANT_NUMBER = register(LibPieceNames.CONSTANT_NUMBER);
	public static final RegistryEntry<Material> CONNECTOR = register(LibPieceNames.CONNECTOR);

	// ========== TUTORIAL_3 ==========
	public static final RegistryEntry<Material> OPERATOR_ENTITY_LOOK = register(LibPieceNames.OPERATOR_ENTITY_LOOK);
	public static final RegistryEntry<Material> TRICK_ADD_MOTION = register(LibPieceNames.TRICK_ADD_MOTION);

	// ========== TUTORIAL_4 ==========
	public static final RegistryEntry<Material> OPERATOR_ENTITY_POSITION = register(LibPieceNames.OPERATOR_ENTITY_POSITION);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_RAYCAST = register(LibPieceNames.OPERATOR_VECTOR_RAYCAST);
	public static final RegistryEntry<Material> TRICK_EXPLODE = register(LibPieceNames.TRICK_EXPLODE);
	public static final RegistryEntry<Material> ERROR_SUPPRESSOR = register(LibPieceNames.ERROR_SUPPRESSOR);
	public static final RegistryEntry<Material> ERROR_CATCH = register(LibPieceNames.ERROR_CATCH);

	// ========== PROJECTILES ==========
	public static final RegistryEntry<Material> SELECTOR_FOCAL_POINT = register(LibPieceNames.SELECTOR_FOCAL_POINT);
	public static final RegistryEntry<Material> SELECTOR_RULER_VECTOR = register(LibPieceNames.SELECTOR_RULER_VECTOR);

	// ========== ENTITIES_INTRO ==========
	public static final RegistryEntry<Material> SELECTOR_NEARBY_ITEMS = register(LibPieceNames.SELECTOR_NEARBY_ITEMS);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_LIVING = register(LibPieceNames.SELECTOR_NEARBY_LIVING);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_ENEMIES = register(LibPieceNames.SELECTOR_NEARBY_ENEMIES);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_ANIMALS = register(LibPieceNames.SELECTOR_NEARBY_ANIMALS);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_PROJECTILES = register(LibPieceNames.SELECTOR_NEARBY_PROJECTILES);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_CHARGES = register(LibPieceNames.SELECTOR_NEARBY_CHARGES);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_FALLING_BLOCKS = register(LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_GLOWING = register(LibPieceNames.SELECTOR_NEARBY_GLOWING);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_PLAYERS = register(LibPieceNames.SELECTOR_NEARBY_PLAYERS);
	public static final RegistryEntry<Material> SELECTOR_NEARBY_VEHICLES = register(LibPieceNames.SELECTOR_NEARBY_VEHICLES);

	public static final RegistryEntry<Material> OPERATOR_ENTITY_MOTION = register(LibPieceNames.OPERATOR_ENTITY_MOTION);
	public static final RegistryEntry<Material> OPERATOR_ENTITY_AXIAL_LOOK = register(LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK);
	public static final RegistryEntry<Material> OPERATOR_CLOSEST_TO_POINT = register(LibPieceNames.OPERATOR_CLOSEST_TO_POINT);
	public static final RegistryEntry<Material> OPERATOR_RANDOM_ENTITY = register(LibPieceNames.OPERATOR_RANDOM_ENTITY);
	public static final RegistryEntry<Material> OPERATOR_FOCUSED_ENTITY = register(LibPieceNames.OPERATOR_FOCUSED_ENTITY);
	public static final RegistryEntry<Material> OPERATOR_LIST_ADD = register(LibPieceNames.OPERATOR_LIST_ADD);
	public static final RegistryEntry<Material> OPERATOR_LIST_REMOVE = register(LibPieceNames.OPERATOR_LIST_REMOVE);
	public static final RegistryEntry<Material> OPERATOR_CLOSEST_TO_LINE = register(LibPieceNames.OPERATOR_CLOSEST_TO_LINE);
	public static final RegistryEntry<Material> OPERATOR_ENTITY_HEALTH = register(LibPieceNames.OPERATOR_ENTITY_HEALTH);
	public static final RegistryEntry<Material> OPERATOR_ENTITY_RAYCAST = register(LibPieceNames.OPERATOR_ENTITY_RAYCAST);
	public static final RegistryEntry<Material> OPERATOR_ENTITY_HEIGHT = register(LibPieceNames.OPERATOR_ENTITY_HEIGHT);

	// ========== TOOL_CASTING ==========
	public static final RegistryEntry<Material> SELECTOR_BLOCK_BROKEN = register(LibPieceNames.SELECTOR_BLOCK_BROKEN);
	public static final RegistryEntry<Material> SELECTOR_BLOCK_SIDE_BROKEN = register(LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN);
	public static final RegistryEntry<Material> SELECTOR_ATTACK_TARGET = register(LibPieceNames.SELECTOR_ATTACK_TARGET);
	public static final RegistryEntry<Material> SELECTOR_ITEM_COUNT = register(LibPieceNames.SELECTOR_ITEM_COUNT);

	// ========== LOOPCASTING ==========
	public static final RegistryEntry<Material> SELECTOR_LOOPCAST_INDEX = register(LibPieceNames.SELECTOR_LOOPCAST_INDEX);
	public static final RegistryEntry<Material> OPERATOR_MODULUS = register(LibPieceNames.OPERATOR_MODULUS);
	public static final RegistryEntry<Material> OPERATOR_INTEGER_DIVIDE = register(LibPieceNames.OPERATOR_INTEGER_DIVIDE);

	// ========== FLOW_CONTROL ==========
	public static final RegistryEntry<Material> SELECTOR_SNEAK_STATUS = register(LibPieceNames.SELECTOR_SNEAK_STATUS);
	public static final RegistryEntry<Material> SELECTOR_TICK_TIME = register(LibPieceNames.SELECTOR_TICK_TIME);
	public static final RegistryEntry<Material> SELECTOR_TPS = register(LibPieceNames.SELECTOR_TPS);
	public static final RegistryEntry<Material> TRICK_DELAY = register(LibPieceNames.TRICK_DELAY);
	public static final RegistryEntry<Material> TRICK_DIE = register(LibPieceNames.TRICK_DIE);
	public static final RegistryEntry<Material> TRICK_EVALUATE = register(LibPieceNames.TRICK_EVALUATE);
	public static final RegistryEntry<Material> TRICK_BREAK_LOOP = register(LibPieceNames.TRICK_BREAK_LOOP);
	public static final RegistryEntry<Material> CONSTANT_WRAPPER = register(LibPieceNames.CONSTANT_WRAPPER);

	// ========== NUMBERS_INTRO ==========
	public static final RegistryEntry<Material> OPERATOR_SUM = register(LibPieceNames.OPERATOR_SUM);
	public static final RegistryEntry<Material> OPERATOR_SUBTRACT = register(LibPieceNames.OPERATOR_SUBTRACT);
	public static final RegistryEntry<Material> OPERATOR_MULTIPLY = register(LibPieceNames.OPERATOR_MULTIPLY);
	public static final RegistryEntry<Material> OPERATOR_DIVIDE = register(LibPieceNames.OPERATOR_DIVIDE);
	public static final RegistryEntry<Material> OPERATOR_ABSOLUTE = register(LibPieceNames.OPERATOR_ABSOLUTE);
	public static final RegistryEntry<Material> OPERATOR_INVERSE = register(LibPieceNames.OPERATOR_INVERSE);
	public static final RegistryEntry<Material> OPERATOR_ROOT = register(LibPieceNames.OPERATOR_ROOT);

	// ========== SECONDARY_OPERATORS ==========
	public static final RegistryEntry<Material> OPERATOR_SQUARE = register(LibPieceNames.OPERATOR_SQUARE);
	public static final RegistryEntry<Material> OPERATOR_CUBE = register(LibPieceNames.OPERATOR_CUBE);
	public static final RegistryEntry<Material> OPERATOR_POWER = register(LibPieceNames.OPERATOR_POWER);
	public static final RegistryEntry<Material> OPERATOR_SQUARE_ROOT = register(LibPieceNames.OPERATOR_SQUARE_ROOT);
	public static final RegistryEntry<Material> OPERATOR_LOG = register(LibPieceNames.OPERATOR_LOG);
	public static final RegistryEntry<Material> OPERATOR_CEILING = register(LibPieceNames.OPERATOR_CEILING);
	public static final RegistryEntry<Material> OPERATOR_FLOOR = register(LibPieceNames.OPERATOR_FLOOR);
	public static final RegistryEntry<Material> OPERATOR_ROUND = register(LibPieceNames.OPERATOR_ROUND);
	public static final RegistryEntry<Material> OPERATOR_MAX = register(LibPieceNames.OPERATOR_MAX);
	public static final RegistryEntry<Material> OPERATOR_MIN = register(LibPieceNames.OPERATOR_MIN);
	public static final RegistryEntry<Material> CONSTANT_E = register(LibPieceNames.CONSTANT_E);

	// ========== TRIGONOMETRY ==========
	public static final RegistryEntry<Material> OPERATOR_SIN = register(LibPieceNames.OPERATOR_SIN);
	public static final RegistryEntry<Material> OPERATOR_COS = register(LibPieceNames.OPERATOR_COS);
	public static final RegistryEntry<Material> OPERATOR_ASIN = register(LibPieceNames.OPERATOR_ASIN);
	public static final RegistryEntry<Material> OPERATOR_ACOS = register(LibPieceNames.OPERATOR_ACOS);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_DOT_PRODUCT = register(LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT);
	public static final RegistryEntry<Material> OPERATOR_GAMMA_FUNCTION = register(LibPieceNames.OPERATOR_GAMMA_FUNCTION);
	public static final RegistryEntry<Material> OPERATOR_PLANAR_NORMAL_VECTOR = register(LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_ROTATE = register(LibPieceNames.OPERATOR_VECTOR_ROTATE);
	public static final RegistryEntry<Material> CONSTANT_PI = register(LibPieceNames.CONSTANT_PI);
	public static final RegistryEntry<Material> CONSTANT_TAU = register(LibPieceNames.CONSTANT_TAU);
	public static final RegistryEntry<Material> OPERATOR_SIGNUM = register(LibPieceNames.OPERATOR_SIGNUM);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_ABSOLUTE = register(LibPieceNames.OPERATOR_VECTOR_ABSOLUTE);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_SIGNUM = register(LibPieceNames.OPERATOR_VECTOR_SIGNUM);

	// ========== VECTORS_INTRO ==========
	public static final RegistryEntry<Material> OPERATOR_VECTOR_SUM = register(LibPieceNames.OPERATOR_VECTOR_SUM);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_SUBTRACT = register(LibPieceNames.OPERATOR_VECTOR_SUBTRACT);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_MULTIPLY = register(LibPieceNames.OPERATOR_VECTOR_MULTIPLY);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_DIVIDE = register(LibPieceNames.OPERATOR_VECTOR_DIVIDE);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_CROSS_PRODUCT = register(LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_NORMALIZE = register(LibPieceNames.OPERATOR_VECTOR_NORMALIZE);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_NEGATE = register(LibPieceNames.OPERATOR_VECTOR_NEGATE);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_MAGNITUDE = register(LibPieceNames.OPERATOR_VECTOR_MAGNITUDE);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_CONSTRUCT = register(LibPieceNames.OPERATOR_VECTOR_CONSTRUCT);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_EXTRACT_X = register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_X);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_EXTRACT_Y = register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_EXTRACT_Z = register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_MAXIMUM = register(LibPieceNames.OPERATOR_VECTOR_MAXIMUM);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_MINIMUM = register(LibPieceNames.OPERATOR_VECTOR_MINIMUM);

	// ========== BLOCK_WORKS ==========
	public static final RegistryEntry<Material> OPERATOR_VECTOR_RAYCAST_AXIS = register(LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS);
	public static final RegistryEntry<Material> OPERATOR_VECTOR_PROJECT = register(LibPieceNames.OPERATOR_VECTOR_PROJECT);
	public static final RegistryEntry<Material> OPERATOR_BLOCK_LIGHT = register(LibPieceNames.OPERATOR_BLOCK_LIGHT);
	public static final RegistryEntry<Material> OPERATOR_BLOCK_HARDNESS = register(LibPieceNames.OPERATOR_BLOCK_HARDNESS);
	public static final RegistryEntry<Material> OPERATOR_BLOCK_COMPARATOR_STRENGTH = register(LibPieceNames.OPERATOR_BLOCK_COMPARATOR_STRENGTH);
	public static final RegistryEntry<Material> OPERATOR_BLOCK_SIDE_SOLIDITY = register(LibPieceNames.OPERATOR_BLOCK_SIDE_SOLIDITY);
	public static final RegistryEntry<Material> OPERATOR_BLOCK_MINING_LEVEL = register(LibPieceNames.OPERATOR_BLOCK_MINING_LEVEL);
	public static final RegistryEntry<Material> TRICK_BREAK_BLOCK = register(LibPieceNames.TRICK_BREAK_BLOCK);
	public static final RegistryEntry<Material> TRICK_BREAK_IN_SEQUENCE = register(LibPieceNames.TRICK_BREAK_IN_SEQUENCE);
	public static final RegistryEntry<Material> TRICK_PLACE_BLOCK = register(LibPieceNames.TRICK_PLACE_BLOCK);
	public static final RegistryEntry<Material> TRICK_PLACE_IN_SEQUENCE = register(LibPieceNames.TRICK_PLACE_IN_SEQUENCE);

	// ========== BLOCK_MOVEMENT ==========
	public static final RegistryEntry<Material> TRICK_MOVE_BLOCK = register(LibPieceNames.TRICK_MOVE_BLOCK);
	public static final RegistryEntry<Material> TRICK_COLLAPSE_BLOCK = register(LibPieceNames.TRICK_COLLAPSE_BLOCK);
	public static final RegistryEntry<Material> TRICK_MOVE_BLOCK_SEQUENCE = register(LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE);
	public static final RegistryEntry<Material> TRICK_COLLAPSE_BLOCK_SEQUENCE = register(LibPieceNames.TRICK_COLLAPSE_BLOCK_SEQUENCE);

	// ========== BLOCK_CONJURATION ==========
	public static final RegistryEntry<Material> TRICK_CONJURE_BLOCK = register(LibPieceNames.TRICK_CONJURE_BLOCK);
	public static final RegistryEntry<Material> TRICK_CONJURE_LIGHT = register(LibPieceNames.TRICK_CONJURE_LIGHT);
	public static final RegistryEntry<Material> TRICK_CONJURE_BLOCK_SEQUENCE = register(LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE);
	public static final RegistryEntry<Material> TRICK_PARTICLE_TRAIL = register(LibPieceNames.TRICK_PARTICLE_TRAIL);

	// ========== MOVEMENT ==========
	public static final RegistryEntry<Material> TRICK_BLINK = register(LibPieceNames.TRICK_BLINK);
	public static final RegistryEntry<Material> TRICK_MASS_BLINK = register(LibPieceNames.TRICK_MASS_BLINK);
	public static final RegistryEntry<Material> TRICK_MASS_ADD_MOTION = register(LibPieceNames.TRICK_MASS_ADD_MOTION);
	public static final RegistryEntry<Material> TRICK_MASS_EXODUS = register(LibPieceNames.TRICK_MASS_EXODUS);
	public static final RegistryEntry<Material> SELECTOR_IS_ELYTRA_FLYING = register(LibPieceNames.SELECTOR_IS_ELYTRA_FLYING);

	// ========== ELEMENTAL_ARTS ==========
	public static final RegistryEntry<Material> OPERATOR_RANDOM = register(LibPieceNames.OPERATOR_RANDOM);
	public static final RegistryEntry<Material> TRICK_SMITE = register(LibPieceNames.TRICK_SMITE);
	public static final RegistryEntry<Material> TRICK_BLAZE = register(LibPieceNames.TRICK_BLAZE);
	public static final RegistryEntry<Material> TRICK_TORRENT = register(LibPieceNames.TRICK_TORRENT);
	public static final RegistryEntry<Material> TRICK_OVERGROW = register(LibPieceNames.TRICK_OVERGROW);

	// ========== POSITIVE_EFFECTS ==========
	public static final RegistryEntry<Material> TRICK_SPEED = register(LibPieceNames.TRICK_SPEED);
	public static final RegistryEntry<Material> TRICK_HASTE = register(LibPieceNames.TRICK_HASTE);
	public static final RegistryEntry<Material> TRICK_STRENGTH = register(LibPieceNames.TRICK_STRENGTH);
	public static final RegistryEntry<Material> TRICK_JUMP_BOOST = register(LibPieceNames.TRICK_JUMP_BOOST);
	public static final RegistryEntry<Material> TRICK_WATER_BREATHING = register(LibPieceNames.TRICK_WATER_BREATHING);
	public static final RegistryEntry<Material> TRICK_FIRE_RESISTANCE = register(LibPieceNames.TRICK_FIRE_RESISTANCE);
	public static final RegistryEntry<Material> TRICK_INVISIBILITY = register(LibPieceNames.TRICK_INVISIBILITY);
	public static final RegistryEntry<Material> TRICK_REGENERATION = register(LibPieceNames.TRICK_REGENERATION);
	public static final RegistryEntry<Material> TRICK_RESISTANCE = register(LibPieceNames.TRICK_RESISTANCE);
	public static final RegistryEntry<Material> TRICK_NIGHT_VISION = register(LibPieceNames.TRICK_NIGHT_VISION);

	// ========== NEGATIVE_EFFECTS ==========
	public static final RegistryEntry<Material> TRICK_WITHER = register(LibPieceNames.TRICK_WITHER);
	public static final RegistryEntry<Material> TRICK_SLOWNESS = register(LibPieceNames.TRICK_SLOWNESS);
	public static final RegistryEntry<Material> TRICK_WEAKNESS = register(LibPieceNames.TRICK_WEAKNESS);
	public static final RegistryEntry<Material> TRICK_IGNITE = register(LibPieceNames.TRICK_IGNITE);

	// ========== EIDOS_REVERSAL ==========
	public static final RegistryEntry<Material> SELECTOR_EIDOS_CHANGELOG = register(LibPieceNames.SELECTOR_EIDOS_CHANGELOG);
	public static final RegistryEntry<Material> TRICK_EIDOS_ANCHOR = register(LibPieceNames.TRICK_EIDOS_ANCHOR);
	public static final RegistryEntry<Material> TRICK_EIDOS_REVERSAL = register(LibPieceNames.TRICK_EIDOS_REVERSAL);

	// ========== EXOSUIT_CASTING ==========
	public static final RegistryEntry<Material> SELECTOR_TIME = register(LibPieceNames.SELECTOR_TIME);
	public static final RegistryEntry<Material> SELECTOR_ATTACKER = register(LibPieceNames.SELECTOR_ATTACKER);
	public static final RegistryEntry<Material> SELECTOR_DAMAGE_TAKEN = register(LibPieceNames.SELECTOR_DAMAGE_TAKEN);
	public static final RegistryEntry<Material> SELECTOR_SUCCESS_COUNTER = register(LibPieceNames.SELECTOR_SUCCESS_COUNTER);
	public static final RegistryEntry<Material> SELECTOR_CASTER_BATTERY = register(LibPieceNames.SELECTOR_CASTER_BATTERY);
	public static final RegistryEntry<Material> SELECTOR_CASTER_ENERGY = register(LibPieceNames.SELECTOR_CASTER_ENERGY);

	// ========== DETECTION_DYNAMICS ==========
	public static final RegistryEntry<Material> SELECTOR_ITEM_PRESENCE = register(LibPieceNames.SELECTOR_ITEM_PRESENCE);
	public static final RegistryEntry<Material> SELECTOR_BLOCK_PRESENCE = register(LibPieceNames.SELECTOR_BLOCK_PRESENCE);
	public static final RegistryEntry<Material> TRICK_SWITCH_TARGET_SLOT = register(LibPieceNames.TRICK_SWITCH_TARGET_SLOT);
	public static final RegistryEntry<Material> TRICK_CHANGE_SLOT = register(LibPieceNames.TRICK_CHANGE_SLOT);

	// ========== SMELTERY ==========
	public static final RegistryEntry<Material> SELECTOR_NEARBY_SMELTABLES = register(LibPieceNames.SELECTOR_NEARBY_SMELTABLES);
	public static final RegistryEntry<Material> TRICK_SMELT_BLOCK = register(LibPieceNames.TRICK_SMELT_BLOCK);
	public static final RegistryEntry<Material> TRICK_SMELT_ITEM = register(LibPieceNames.TRICK_SMELT_ITEM);
	public static final RegistryEntry<Material> TRICK_SMELT_BLOCK_SEQUENCE = register(LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE);

	// ========== INFUSION / GREATER_INFUSION ==========
	public static final RegistryEntry<Material> TRICK_INFUSION = register(LibPieceNames.TRICK_INFUSION);
	public static final RegistryEntry<Material> TRICK_GREATER_INFUSION = register(LibPieceNames.TRICK_GREATER_INFUSION);
	public static final RegistryEntry<Material> TRICK_EBONY_IVORY = register(LibPieceNames.TRICK_EBONY_IVORY);

	// ========== LIST_OPERATIONS ==========
	public static final RegistryEntry<Material> OPERATOR_LIST_EXCLUSION = register(LibPieceNames.OPERATOR_LIST_EXCLUSION);
	public static final RegistryEntry<Material> OPERATOR_LIST_INTERSECTION = register(LibPieceNames.OPERATOR_LIST_INTERSECTION);
	public static final RegistryEntry<Material> OPERATOR_LIST_SIZE = register(LibPieceNames.OPERATOR_LIST_SIZE);
	public static final RegistryEntry<Material> OPERATOR_LIST_UNION = register(LibPieceNames.OPERATOR_LIST_UNION);
	public static final RegistryEntry<Material> OPERATOR_LIST_INDEX = register(LibPieceNames.OPERATOR_LIST_INDEX);

	// ========== EIDOS / MISC / RUSSIAN ROULETTE & OTHERS ==========
	public static final RegistryEntry<Material> SELECTOR_CASTER = register(LibPieceNames.SELECTOR_CASTER);
	public static final RegistryEntry<Material> TRICK_PLAY_SOUND = register(LibPieceNames.TRICK_PLAY_SOUND);
	public static final RegistryEntry<Material> TRICK_TILL = register(LibPieceNames.TRICK_TILL);
	public static final RegistryEntry<Material> TRICK_TILL_SEQUENCE = register(LibPieceNames.TRICK_TILL_SEQUENCE);
	public static final RegistryEntry<Material> TRICK_SPIN_CHAMBER = register(LibPieceNames.TRICK_SPIN_CHAMBER);
	public static final RegistryEntry<Material> TRICK_RUSSIAN_ROULETTE = register(LibPieceNames.TRICK_RUSSIAN_ROULETTE);
	public static final RegistryEntry<Material> TRICK_CONJURE_CIRCLE = register(LibPieceNames.TRICK_CONJURE_CIRCLE);

	private SpellPieceMaterial() {}

	public static void init() {}

	private static RegistryEntry<Material> register(String name) {
		return PsiRegistries.register(ClientPsiAPI.SPELL_PIECE_MATERIAL_REGISTRY, PsiAPI.location(name),
				() -> new Material(InventoryMenu.BLOCK_ATLAS, PsiAPI.location("spell/" + name)));
	}
}
