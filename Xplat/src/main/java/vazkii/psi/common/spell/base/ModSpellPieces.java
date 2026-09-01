/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.base;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPieceType;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;
import vazkii.psi.common.spell.constant.*;
import vazkii.psi.common.spell.operator.block.*;
import vazkii.psi.common.spell.operator.entity.*;
import vazkii.psi.common.spell.operator.list.*;
import vazkii.psi.common.spell.operator.number.*;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorAcos;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorAsin;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorCos;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorSin;
import vazkii.psi.common.spell.operator.vector.*;
import vazkii.psi.common.spell.other.PieceConnector;
import vazkii.psi.common.spell.other.PieceCrossConnector;
import vazkii.psi.common.spell.other.PieceErrorCatch;
import vazkii.psi.common.spell.other.PieceErrorSuppressor;
import vazkii.psi.common.spell.selector.*;
import vazkii.psi.common.spell.selector.entity.*;
import vazkii.psi.common.spell.trick.*;
import vazkii.psi.common.spell.trick.block.*;
import vazkii.psi.common.spell.trick.entity.*;
import vazkii.psi.common.spell.trick.infusion.PieceTrickEbonyIvory;
import vazkii.psi.common.spell.trick.infusion.PieceTrickGreaterInfusion;
import vazkii.psi.common.spell.trick.infusion.PieceTrickInfusion;
import vazkii.psi.common.spell.trick.potion.*;

@SuppressWarnings("unused")
public final class ModSpellPieces {
	public static void register() {}

	// ========== MEMORY MANAGEMENT ==========
	public static final RegistryEntry<SpellPieceType> CROSS_CONNECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CROSS_CONNECTOR), () -> SpellPieceType.ofClass(PieceCrossConnector.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_SAVED_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_SAVED_VECTOR), () -> SpellPieceType.ofClass(PieceSelectorSavedVector.class));
	public static final RegistryEntry<SpellPieceType> TRICK_DETONATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DETONATE), () -> SpellPieceType.ofClass(PieceTrickDetonate.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SAVE_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SAVE_VECTOR), () -> SpellPieceType.ofClass(PieceTrickSaveVector.class));

	// ========== TUTORIAL_1 ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_CASTER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_CASTER), () -> SpellPieceType.ofClass(PieceSelectorCaster.class));
	public static final RegistryEntry<SpellPieceType> TRICK_DEBUG =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DEBUG), () -> SpellPieceType.ofClass(PieceTrickDebug.class));
	public static final RegistryEntry<SpellPieceType> TRICK_DEBUG_SPAMLESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DEBUG_SPAMLESS), () -> SpellPieceType.ofClass(PieceTrickDebugSpamless.class));

	// ========== TUTORIAL_2 ==========
	public static final RegistryEntry<SpellPieceType> CONSTANT_NUMBER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_NUMBER), () -> SpellPieceType.ofClass(PieceConstantNumber.class));
	public static final RegistryEntry<SpellPieceType> CONNECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONNECTOR), () -> SpellPieceType.ofClass(PieceConnector.class));

	// ========== TUTORIAL_3 ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_LOOK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_LOOK), () -> SpellPieceType.ofClass(PieceOperatorEntityLook.class));
	public static final RegistryEntry<SpellPieceType> TRICK_ADD_MOTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_ADD_MOTION), () -> SpellPieceType.ofClass(PieceTrickAddMotion.class));

	// ========== TUTORIAL_4 ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_POSITION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_POSITION), () -> SpellPieceType.ofClass(PieceOperatorEntityPosition.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_RAYCAST =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_RAYCAST), () -> SpellPieceType.ofClass(PieceOperatorVectorRaycast.class));
	public static final RegistryEntry<SpellPieceType> TRICK_EXPLODE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EXPLODE), () -> SpellPieceType.ofClass(PieceTrickExplode.class));
	public static final RegistryEntry<SpellPieceType> ERROR_SUPPRESSOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.ERROR_SUPPRESSOR), () -> SpellPieceType.ofClass(PieceErrorSuppressor.class));
	public static final RegistryEntry<SpellPieceType> ERROR_CATCH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.ERROR_CATCH), () -> SpellPieceType.ofClass(PieceErrorCatch.class));

	// ========== PROJECTILES ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_FOCAL_POINT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_FOCAL_POINT), () -> SpellPieceType.ofClass(PieceSelectorFocalPoint.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_RULER_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_RULER_VECTOR), () -> SpellPieceType.ofClass(PieceSelectorRulerVector.class));

	// ========== ENTITIES_INTRO ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_ITEMS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_ITEMS), () -> SpellPieceType.ofClass(PieceSelectorNearbyItems.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_LIVING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_LIVING), () -> SpellPieceType.ofClass(PieceSelectorNearbyLiving.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_ENEMIES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_ENEMIES), () -> SpellPieceType.ofClass(PieceSelectorNearbyEnemies.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_ANIMALS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_ANIMALS), () -> SpellPieceType.ofClass(PieceSelectorNearbyAnimals.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_PROJECTILES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_PROJECTILES), () -> SpellPieceType.ofClass(PieceSelectorNearbyProjectiles.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_CHARGES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_CHARGES), () -> SpellPieceType.ofClass(PieceSelectorNearbyCharges.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_FALLING_BLOCKS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS), () -> SpellPieceType.ofClass(PieceSelectorNearbyFallingBlocks.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_GLOWING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_GLOWING), () -> SpellPieceType.ofClass(PieceSelectorNearbyGlowing.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_PLAYERS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_PLAYERS), () -> SpellPieceType.ofClass(PieceSelectorNearbyPlayers.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_VEHICLES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_VEHICLES), () -> SpellPieceType.ofClass(PieceSelectorNearbyVehicles.class));

	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_MOTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_MOTION), () -> SpellPieceType.ofClass(PieceOperatorEntityMotion.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_AXIAL_LOOK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK), () -> SpellPieceType.ofClass(PieceOperatorEntityAxialLook.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_CLOSEST_TO_POINT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CLOSEST_TO_POINT), () -> SpellPieceType.ofClass(PieceOperatorClosestToPoint.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_RANDOM_ENTITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_RANDOM_ENTITY), () -> SpellPieceType.ofClass(PieceOperatorRandomEntity.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_FOCUSED_ENTITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_FOCUSED_ENTITY), () -> SpellPieceType.ofClass(PieceOperatorFocusedEntity.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_ADD =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_ADD), () -> SpellPieceType.ofClass(PieceOperatorListAdd.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_REMOVE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_REMOVE), () -> SpellPieceType.ofClass(PieceOperatorListRemove.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_CLOSEST_TO_LINE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CLOSEST_TO_LINE), () -> SpellPieceType.ofClass(PieceOperatorClosestToLine.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_HEALTH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_HEALTH), () -> SpellPieceType.ofClass(PieceOperatorEntityHealth.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_RAYCAST =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_RAYCAST), () -> SpellPieceType.ofClass(PieceOperatorEntityRaycast.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ENTITY_HEIGHT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_HEIGHT), () -> SpellPieceType.ofClass(PieceOperatorEntityHeight.class));

	// ========== TOOL_CASTING ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_BLOCK_BROKEN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_BLOCK_BROKEN), () -> SpellPieceType.ofClass(PieceSelectorBlockBroken.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_BLOCK_SIDE_BROKEN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN), () -> SpellPieceType.ofClass(PieceSelectorBlockSideBroken.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_ATTACK_TARGET =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ATTACK_TARGET), () -> SpellPieceType.ofClass(PieceSelectorAttackTarget.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_ITEM_COUNT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ITEM_COUNT), () -> SpellPieceType.ofClass(PieceSelectorItemCount.class));

	// ========== LOOPCASTING ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_LOOPCAST_INDEX =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_LOOPCAST_INDEX), () -> SpellPieceType.ofClass(PieceSelectorLoopcastIndex.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_MODULUS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MODULUS), () -> SpellPieceType.ofClass(PieceOperatorModulus.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_INTEGER_DIVIDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_INTEGER_DIVIDE), () -> SpellPieceType.ofClass(PieceOperatorIntegerDivide.class));

	// ========== FLOW_CONTROL ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_SNEAK_STATUS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_SNEAK_STATUS), () -> SpellPieceType.ofClass(PieceSelectorSneakStatus.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_TICK_TIME =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_TICK_TIME), () -> SpellPieceType.ofClass(PieceSelectorTickTime.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_TPS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_TPS), () -> SpellPieceType.ofClass(PieceSelectorTps.class));
	public static final RegistryEntry<SpellPieceType> TRICK_DELAY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DELAY), () -> SpellPieceType.ofClass(PieceTrickDelay.class));
	public static final RegistryEntry<SpellPieceType> TRICK_DIE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DIE), () -> SpellPieceType.ofClass(PieceTrickDie.class));
	public static final RegistryEntry<SpellPieceType> TRICK_EVALUATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EVALUATE), () -> SpellPieceType.ofClass(PieceTrickEvaluate.class));
	public static final RegistryEntry<SpellPieceType> TRICK_BREAK_LOOP =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BREAK_LOOP), () -> SpellPieceType.ofClass(PieceTrickBreakLoop.class));
	public static final RegistryEntry<SpellPieceType> CONSTANT_WRAPPER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_WRAPPER), () -> SpellPieceType.ofClass(PieceConstantWrapper.class));

	// ========== NUMBERS_INTRO ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_SUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SUM), () -> SpellPieceType.ofClass(PieceOperatorSum.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_SUBTRACT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SUBTRACT), () -> SpellPieceType.ofClass(PieceOperatorSubtract.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_MULTIPLY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MULTIPLY), () -> SpellPieceType.ofClass(PieceOperatorMultiply.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_DIVIDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_DIVIDE), () -> SpellPieceType.ofClass(PieceOperatorDivide.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ABSOLUTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ABSOLUTE), () -> SpellPieceType.ofClass(PieceOperatorAbsolute.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_INVERSE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_INVERSE), () -> SpellPieceType.ofClass(PieceOperatorInverse.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ROOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ROOT), () -> SpellPieceType.ofClass(PieceOperatorRoot.class));

	// ========== SECONDARY_OPERATORS ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_SQUARE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SQUARE), () -> SpellPieceType.ofClass(PieceOperatorSquare.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_CUBE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CUBE), () -> SpellPieceType.ofClass(PieceOperatorCube.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_POWER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_POWER), () -> SpellPieceType.ofClass(PieceOperatorPower.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_SQUARE_ROOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SQUARE_ROOT), () -> SpellPieceType.ofClass(PieceOperatorSquareRoot.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LOG =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LOG), () -> SpellPieceType.ofClass(PieceOperatorLog.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_CEILING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CEILING), () -> SpellPieceType.ofClass(PieceOperatorCeiling.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_FLOOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_FLOOR), () -> SpellPieceType.ofClass(PieceOperatorFloor.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ROUND =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ROUND), () -> SpellPieceType.ofClass(PieceOperatorRound.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_MAX =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MAX), () -> SpellPieceType.ofClass(PieceOperatorMax.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_MIN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MIN), () -> SpellPieceType.ofClass(PieceOperatorMin.class));
	public static final RegistryEntry<SpellPieceType> CONSTANT_E =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_E), () -> SpellPieceType.ofClass(PieceConstantE.class));

	// ========== TRIGONOMETRY ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_SIN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SIN), () -> SpellPieceType.ofClass(PieceOperatorSin.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_COS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_COS), () -> SpellPieceType.ofClass(PieceOperatorCos.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ASIN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ASIN), () -> SpellPieceType.ofClass(PieceOperatorAsin.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_ACOS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ACOS), () -> SpellPieceType.ofClass(PieceOperatorAcos.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_DOT_PRODUCT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT), () -> SpellPieceType.ofClass(PieceOperatorVectorDotProduct.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_GAMMA_FUNCTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_GAMMA_FUNCTION), () -> SpellPieceType.ofClass(PieceOperatorGammaFunc.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_PLANAR_NORMAL_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR), () -> SpellPieceType.ofClass(PieceOperatorPlanarNormalVector.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_ROTATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_ROTATE), () -> SpellPieceType.ofClass(PieceOperatorVectorRotate.class));
	public static final RegistryEntry<SpellPieceType> CONSTANT_PI =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_PI), () -> SpellPieceType.ofClass(PieceConstantPi.class));
	public static final RegistryEntry<SpellPieceType> CONSTANT_TAU =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_TAU), () -> SpellPieceType.ofClass(PieceConstantTau.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_SIGNUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SIGNUM), () -> SpellPieceType.ofClass(PieceOperatorSignum.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_ABSOLUTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_ABSOLUTE), () -> SpellPieceType.ofClass(PieceOperatorVectorAbsolute.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_SIGNUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_SIGNUM), () -> SpellPieceType.ofClass(PieceOperatorVectorSignum.class));

	// ========== VECTORS_INTRO ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_SUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_SUM), () -> SpellPieceType.ofClass(PieceOperatorVectorSum.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_SUBTRACT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_SUBTRACT), () -> SpellPieceType.ofClass(PieceOperatorVectorSubtract.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_MULTIPLY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MULTIPLY), () -> SpellPieceType.ofClass(PieceOperatorVectorMultiply.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_DIVIDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_DIVIDE), () -> SpellPieceType.ofClass(PieceOperatorVectorDivide.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_CROSS_PRODUCT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT), () -> SpellPieceType.ofClass(PieceOperatorVectorCrossProduct.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_NORMALIZE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_NORMALIZE), () -> SpellPieceType.ofClass(PieceOperatorVectorNormalize.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_NEGATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_NEGATE), () -> SpellPieceType.ofClass(PieceOperatorVectorNegate.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_MAGNITUDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MAGNITUDE), () -> SpellPieceType.ofClass(PieceOperatorVectorMagnitude.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_CONSTRUCT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_CONSTRUCT), () -> SpellPieceType.ofClass(PieceOperatorVectorConstruct.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_EXTRACT_X =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_EXTRACT_X), () -> SpellPieceType.ofClass(PieceOperatorVectorExtractX.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_EXTRACT_Y =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y), () -> SpellPieceType.ofClass(PieceOperatorVectorExtractY.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_EXTRACT_Z =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z), () -> SpellPieceType.ofClass(PieceOperatorVectorExtractZ.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_MAXIMUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MAXIMUM), () -> SpellPieceType.ofClass(PieceOperatorVectorMaximum.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_MINIMUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MINIMUM), () -> SpellPieceType.ofClass(PieceOperatorVectorMinimum.class));

	// ========== BLOCK_WORKS ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_RAYCAST_AXIS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS), () -> SpellPieceType.ofClass(PieceOperatorVectorRaycastAxis.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_VECTOR_PROJECT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_PROJECT), () -> SpellPieceType.ofClass(PieceOperatorVectorProject.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_BLOCK_LIGHT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_LIGHT), () -> SpellPieceType.ofClass(PieceOperatorBlockLightLevel.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_BLOCK_HARDNESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_HARDNESS), () -> SpellPieceType.ofClass(PieceOperatorBlockHardness.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_BLOCK_COMPARATOR_STRENGTH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_COMPARATOR_STRENGTH), () -> SpellPieceType.ofClass(PieceOperatorBlockComparatorStrength.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_BLOCK_SIDE_SOLIDITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_SIDE_SOLIDITY), () -> SpellPieceType.ofClass(PieceOperatorBlockSideSolidity.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_BLOCK_MINING_LEVEL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_MINING_LEVEL), () -> SpellPieceType.ofClass(PieceOperatorBlockMiningLevel.class));
	public static final RegistryEntry<SpellPieceType> TRICK_BREAK_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BREAK_BLOCK), () -> SpellPieceType.ofClass(PieceTrickBreakBlock.class));
	public static final RegistryEntry<SpellPieceType> TRICK_BREAK_IN_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BREAK_IN_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickBreakInSequence.class));
	public static final RegistryEntry<SpellPieceType> TRICK_PLACE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PLACE_BLOCK), () -> SpellPieceType.ofClass(PieceTrickPlaceBlock.class));
	public static final RegistryEntry<SpellPieceType> TRICK_PLACE_IN_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PLACE_IN_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickPlaceInSequence.class));

	// ========== BLOCK_MOVEMENT ==========
	public static final RegistryEntry<SpellPieceType> TRICK_MOVE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MOVE_BLOCK), () -> SpellPieceType.ofClass(PieceTrickMoveBlock.class));
	public static final RegistryEntry<SpellPieceType> TRICK_COLLAPSE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_COLLAPSE_BLOCK), () -> SpellPieceType.ofClass(PieceTrickCollapseBlock.class));
	public static final RegistryEntry<SpellPieceType> TRICK_MOVE_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickMoveBlockSequence.class));
	public static final RegistryEntry<SpellPieceType> TRICK_COLLAPSE_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_COLLAPSE_BLOCK_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickCollapseBlockSequence.class));

	// ========== BLOCK_CONJURATION ==========
	public static final RegistryEntry<SpellPieceType> TRICK_CONJURE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_BLOCK), () -> SpellPieceType.ofClass(PieceTrickConjureBlock.class));
	public static final RegistryEntry<SpellPieceType> TRICK_CONJURE_LIGHT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_LIGHT), () -> SpellPieceType.ofClass(PieceTrickConjureLight.class));
	public static final RegistryEntry<SpellPieceType> TRICK_CONJURE_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickConjureBlockSequence.class));
	public static final RegistryEntry<SpellPieceType> TRICK_PARTICLE_TRAIL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PARTICLE_TRAIL), () -> SpellPieceType.ofClass(PieceTrickParticleTrail.class));

	// ========== MOVEMENT ==========
	public static final RegistryEntry<SpellPieceType> TRICK_BLINK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BLINK), () -> SpellPieceType.ofClass(PieceTrickBlink.class));
	public static final RegistryEntry<SpellPieceType> TRICK_MASS_BLINK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MASS_BLINK), () -> SpellPieceType.ofClass(PieceTrickMassBlink.class));
	public static final RegistryEntry<SpellPieceType> TRICK_MASS_ADD_MOTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MASS_ADD_MOTION), () -> SpellPieceType.ofClass(PieceTrickMassAddMotion.class));
	public static final RegistryEntry<SpellPieceType> TRICK_MASS_EXODUS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MASS_EXODUS), () -> SpellPieceType.ofClass(PieceTrickMassExodus.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_IS_ELYTRA_FLYING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_IS_ELYTRA_FLYING), () -> SpellPieceType.ofClass(PieceSelectorIsElytraFlying.class));

	// ========== ELEMENTAL_ARTS ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_RANDOM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_RANDOM), () -> SpellPieceType.ofClass(PieceOperatorRandom.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SMITE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMITE), () -> SpellPieceType.ofClass(PieceTrickSmite.class));
	public static final RegistryEntry<SpellPieceType> TRICK_BLAZE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BLAZE), () -> SpellPieceType.ofClass(PieceTrickBlaze.class));
	public static final RegistryEntry<SpellPieceType> TRICK_TORRENT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_TORRENT), () -> SpellPieceType.ofClass(PieceTrickTorrent.class));
	public static final RegistryEntry<SpellPieceType> TRICK_OVERGROW =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_OVERGROW), () -> SpellPieceType.ofClass(PieceTrickOvergrow.class));

	// ========== POSITIVE_EFFECTS ==========
	public static final RegistryEntry<SpellPieceType> TRICK_SPEED =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SPEED), () -> SpellPieceType.ofClass(PieceTrickSpeed.class));
	public static final RegistryEntry<SpellPieceType> TRICK_HASTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_HASTE), () -> SpellPieceType.ofClass(PieceTrickHaste.class));
	public static final RegistryEntry<SpellPieceType> TRICK_STRENGTH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_STRENGTH), () -> SpellPieceType.ofClass(PieceTrickStrength.class));
	public static final RegistryEntry<SpellPieceType> TRICK_JUMP_BOOST =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_JUMP_BOOST), () -> SpellPieceType.ofClass(PieceTrickJumpBoost.class));
	public static final RegistryEntry<SpellPieceType> TRICK_WATER_BREATHING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_WATER_BREATHING), () -> SpellPieceType.ofClass(PieceTrickWaterBreathing.class));
	public static final RegistryEntry<SpellPieceType> TRICK_FIRE_RESISTANCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_FIRE_RESISTANCE), () -> SpellPieceType.ofClass(PieceTrickFireResistance.class));
	public static final RegistryEntry<SpellPieceType> TRICK_INVISIBILITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_INVISIBILITY), () -> SpellPieceType.ofClass(PieceTrickInvisibility.class));
	public static final RegistryEntry<SpellPieceType> TRICK_REGENERATION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_REGENERATION), () -> SpellPieceType.ofClass(PieceTrickRegeneration.class));
	public static final RegistryEntry<SpellPieceType> TRICK_RESISTANCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_RESISTANCE), () -> SpellPieceType.ofClass(PieceTrickResistance.class));
	public static final RegistryEntry<SpellPieceType> TRICK_NIGHT_VISION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_NIGHT_VISION), () -> SpellPieceType.ofClass(PieceTrickNightVision.class));

	// ========== NEGATIVE_EFFECTS ==========
	public static final RegistryEntry<SpellPieceType> TRICK_WITHER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_WITHER), () -> SpellPieceType.ofClass(PieceTrickWither.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SLOWNESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SLOWNESS), () -> SpellPieceType.ofClass(PieceTrickSlowness.class));
	public static final RegistryEntry<SpellPieceType> TRICK_WEAKNESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_WEAKNESS), () -> SpellPieceType.ofClass(PieceTrickWeakness.class));
	public static final RegistryEntry<SpellPieceType> TRICK_IGNITE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_IGNITE), () -> SpellPieceType.ofClass(PieceTrickIgnite.class));

	// ========== EIDOS_REVERSAL ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_EIDOS_CHANGELOG =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_EIDOS_CHANGELOG), () -> SpellPieceType.ofClass(PieceSelectorEidosChangelog.class));
	public static final RegistryEntry<SpellPieceType> TRICK_EIDOS_ANCHOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EIDOS_ANCHOR), () -> SpellPieceType.ofClass(PieceTrickEidosAnchor.class));
	public static final RegistryEntry<SpellPieceType> TRICK_EIDOS_REVERSAL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EIDOS_REVERSAL), () -> SpellPieceType.ofClass(PieceTrickEidosReversal.class));

	// ========== EXOSUIT_CASTING ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_TIME =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_TIME), () -> SpellPieceType.ofClass(PieceSelectorTime.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_ATTACKER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ATTACKER), () -> SpellPieceType.ofClass(PieceSelectorAttacker.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_DAMAGE_TAKEN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_DAMAGE_TAKEN), () -> SpellPieceType.ofClass(PieceSelectorDamageTaken.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_SUCCESS_COUNTER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_SUCCESS_COUNTER), () -> SpellPieceType.ofClass(PieceSelectorSuccessCounter.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_CASTER_BATTERY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_CASTER_BATTERY), () -> SpellPieceType.ofClass(PieceSelectorCasterBattery.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_CASTER_ENERGY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_CASTER_ENERGY), () -> SpellPieceType.ofClass(PieceSelectorCasterEnergy.class));

	// ========== DETECTION_DYNAMICS ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_ITEM_PRESENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ITEM_PRESENCE), () -> SpellPieceType.ofClass(PieceSelectorItemPresence.class));
	public static final RegistryEntry<SpellPieceType> SELECTOR_BLOCK_PRESENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_BLOCK_PRESENCE), () -> SpellPieceType.ofClass(PieceSelectorBlockPresence.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SWITCH_TARGET_SLOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SWITCH_TARGET_SLOT), () -> SpellPieceType.ofClass(PieceTrickSwitchTargetSlot.class));
	public static final RegistryEntry<SpellPieceType> TRICK_CHANGE_SLOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CHANGE_SLOT), () -> SpellPieceType.ofClass(PieceTrickChangeSlot.class));

	// ========== SMELTERY ==========
	public static final RegistryEntry<SpellPieceType> SELECTOR_NEARBY_SMELTABLES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_SMELTABLES), () -> SpellPieceType.ofClass(PieceSelectorNearbySmeltables.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SMELT_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMELT_BLOCK), () -> SpellPieceType.ofClass(PieceTrickSmeltBlock.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SMELT_ITEM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMELT_ITEM), () -> SpellPieceType.ofClass(PieceTrickSmeltItem.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SMELT_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickSmeltBlockSequence.class));

	// ========== INFUSION / GREATER_INFUSION ==========
	public static final RegistryEntry<SpellPieceType> TRICK_INFUSION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_INFUSION), () -> SpellPieceType.ofClass(PieceTrickInfusion.class));
	public static final RegistryEntry<SpellPieceType> TRICK_GREATER_INFUSION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_GREATER_INFUSION), () -> SpellPieceType.ofClass(PieceTrickGreaterInfusion.class));
	public static final RegistryEntry<SpellPieceType> TRICK_EBONY_IVORY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EBONY_IVORY), () -> SpellPieceType.ofClass(PieceTrickEbonyIvory.class));

	// ========== LIST_OPERATIONS ==========
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_EXCLUSION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_EXCLUSION), () -> SpellPieceType.ofClass(PieceOperatorListExclusion.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_INTERSECTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_INTERSECTION), () -> SpellPieceType.ofClass(PieceOperatorListIntersection.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_SIZE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_SIZE), () -> SpellPieceType.ofClass(PieceOperatorListSize.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_UNION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_UNION), () -> SpellPieceType.ofClass(PieceOperatorListUnion.class));
	public static final RegistryEntry<SpellPieceType> OPERATOR_LIST_INDEX =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_INDEX), () -> SpellPieceType.ofClass(PieceOperatorListIndex.class));

	// ========== EIDOS / MISC / RUSSIAN ROULETTE & OTHERS ==========
	public static final RegistryEntry<SpellPieceType> TRICK_PLAY_SOUND =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PLAY_SOUND), () -> SpellPieceType.ofClass(PieceTrickPlaySound.class));
	public static final RegistryEntry<SpellPieceType> TRICK_TILL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_TILL), () -> SpellPieceType.ofClass(PieceTrickTill.class));
	public static final RegistryEntry<SpellPieceType> TRICK_TILL_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_TILL_SEQUENCE), () -> SpellPieceType.ofClass(PieceTrickTillSequence.class));
	public static final RegistryEntry<SpellPieceType> TRICK_SPIN_CHAMBER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SPIN_CHAMBER), () -> SpellPieceType.ofClass(PieceTrickSpinChamber.class));
	public static final RegistryEntry<SpellPieceType> TRICK_RUSSIAN_ROULETTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_RUSSIAN_ROULETTE), () -> SpellPieceType.ofClass(PieceTrickRussianRoulette.class));
	public static final RegistryEntry<SpellPieceType> TRICK_CONJURE_CIRCLE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_CIRCLE), () -> SpellPieceType.ofClass(PieceTrickConjureCircle.class));

}
