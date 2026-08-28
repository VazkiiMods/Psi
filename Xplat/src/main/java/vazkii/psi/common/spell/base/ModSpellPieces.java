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
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public final class ModSpellPieces {
	public static void register() {}

	// ========== MEMORY MANAGEMENT ==========
	public static final RegistryEntry<Class<PieceCrossConnector>> CROSS_CONNECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CROSS_CONNECTOR), () -> PieceCrossConnector.class);
	public static final RegistryEntry<Class<PieceSelectorSavedVector>> SELECTOR_SAVED_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_SAVED_VECTOR), () -> PieceSelectorSavedVector.class);
	public static final RegistryEntry<Class<PieceTrickDetonate>> TRICK_DETONATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DETONATE), () -> PieceTrickDetonate.class);
	public static final RegistryEntry<Class<PieceTrickSaveVector>> TRICK_SAVE_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SAVE_VECTOR), () -> PieceTrickSaveVector.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> MEMORY_MANAGEMENT =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.MEMORY_MANAGEMENT),
					() -> Arrays.asList(
							PieceTrickSaveVector.class,
							PieceTrickDetonate.class,
							PieceSelectorSavedVector.class,
							PieceCrossConnector.class
					));

	// ========== TUTORIAL_1 ==========
	public static final RegistryEntry<Class<PieceSelectorCaster>> SELECTOR_CASTER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_CASTER), () -> PieceSelectorCaster.class);
	public static final RegistryEntry<Class<PieceTrickDebug>> TRICK_DEBUG =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DEBUG), () -> PieceTrickDebug.class);
	public static final RegistryEntry<Class<PieceTrickDebugSpamless>> TRICK_DEBUG_SPAMLESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DEBUG_SPAMLESS), () -> PieceTrickDebugSpamless.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> TUTORIAL_1 =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.TUTORIAL_1),
					() -> Arrays.asList(
							PieceSelectorCaster.class,
							PieceTrickDebug.class,
							PieceTrickDebugSpamless.class
					));

	// ========== TUTORIAL_2 ==========
	public static final RegistryEntry<Class<PieceConstantNumber>> CONSTANT_NUMBER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_NUMBER), () -> PieceConstantNumber.class);
	public static final RegistryEntry<Class<PieceConnector>> CONNECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONNECTOR), () -> PieceConnector.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> TUTORIAL_2 =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.TUTORIAL_2),
					() -> Arrays.asList(
							PieceConstantNumber.class,
							PieceConnector.class
					));

	// ========== TUTORIAL_3 ==========
	public static final RegistryEntry<Class<PieceOperatorEntityLook>> OPERATOR_ENTITY_LOOK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_LOOK), () -> PieceOperatorEntityLook.class);
	public static final RegistryEntry<Class<PieceTrickAddMotion>> TRICK_ADD_MOTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_ADD_MOTION), () -> PieceTrickAddMotion.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> TUTORIAL_3 =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.TUTORIAL_3),
					() -> Arrays.asList(
							PieceTrickAddMotion.class,
							PieceOperatorEntityLook.class
					));

	// ========== TUTORIAL_4 ==========
	public static final RegistryEntry<Class<PieceOperatorEntityPosition>> OPERATOR_ENTITY_POSITION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_POSITION), () -> PieceOperatorEntityPosition.class);
	public static final RegistryEntry<Class<PieceOperatorVectorRaycast>> OPERATOR_VECTOR_RAYCAST =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_RAYCAST), () -> PieceOperatorVectorRaycast.class);
	public static final RegistryEntry<Class<PieceTrickExplode>> TRICK_EXPLODE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EXPLODE), () -> PieceTrickExplode.class);
	public static final RegistryEntry<Class<PieceErrorSuppressor>> ERROR_SUPPRESSOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.ERROR_SUPPRESSOR), () -> PieceErrorSuppressor.class);
	public static final RegistryEntry<Class<PieceErrorCatch>> ERROR_CATCH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.ERROR_CATCH), () -> PieceErrorCatch.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> TUTORIAL_4 =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.TUTORIAL_4),
					() -> Arrays.asList(
							PieceTrickExplode.class,
							PieceOperatorEntityPosition.class,
							PieceOperatorVectorRaycast.class,
							PieceErrorSuppressor.class,
							PieceErrorCatch.class
					));

	// ========== PROJECTILES ==========
	public static final RegistryEntry<Class<PieceSelectorFocalPoint>> SELECTOR_FOCAL_POINT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_FOCAL_POINT), () -> PieceSelectorFocalPoint.class);
	public static final RegistryEntry<Class<PieceSelectorRulerVector>> SELECTOR_RULER_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_RULER_VECTOR), () -> PieceSelectorRulerVector.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> PROJECTILES =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.PROJECTILES),
					() -> Arrays.asList(
							PieceSelectorFocalPoint.class,
							PieceSelectorRulerVector.class
					));

	// ========== ENTITIES_INTRO ==========
	public static final RegistryEntry<Class<PieceSelectorNearbyItems>> SELECTOR_NEARBY_ITEMS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_ITEMS), () -> PieceSelectorNearbyItems.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyLiving>> SELECTOR_NEARBY_LIVING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_LIVING), () -> PieceSelectorNearbyLiving.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyEnemies>> SELECTOR_NEARBY_ENEMIES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_ENEMIES), () -> PieceSelectorNearbyEnemies.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyAnimals>> SELECTOR_NEARBY_ANIMALS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_ANIMALS), () -> PieceSelectorNearbyAnimals.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyProjectiles>> SELECTOR_NEARBY_PROJECTILES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_PROJECTILES), () -> PieceSelectorNearbyProjectiles.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyCharges>> SELECTOR_NEARBY_CHARGES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_CHARGES), () -> PieceSelectorNearbyCharges.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyFallingBlocks>> SELECTOR_NEARBY_FALLING_BLOCKS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS), () -> PieceSelectorNearbyFallingBlocks.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyGlowing>> SELECTOR_NEARBY_GLOWING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_GLOWING), () -> PieceSelectorNearbyGlowing.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyPlayers>> SELECTOR_NEARBY_PLAYERS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_PLAYERS), () -> PieceSelectorNearbyPlayers.class);
	public static final RegistryEntry<Class<PieceSelectorNearbyVehicles>> SELECTOR_NEARBY_VEHICLES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_VEHICLES), () -> PieceSelectorNearbyVehicles.class);

	public static final RegistryEntry<Class<PieceOperatorEntityMotion>> OPERATOR_ENTITY_MOTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_MOTION), () -> PieceOperatorEntityMotion.class);
	public static final RegistryEntry<Class<PieceOperatorEntityAxialLook>> OPERATOR_ENTITY_AXIAL_LOOK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK), () -> PieceOperatorEntityAxialLook.class);
	public static final RegistryEntry<Class<PieceOperatorClosestToPoint>> OPERATOR_CLOSEST_TO_POINT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CLOSEST_TO_POINT), () -> PieceOperatorClosestToPoint.class);
	public static final RegistryEntry<Class<PieceOperatorRandomEntity>> OPERATOR_RANDOM_ENTITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_RANDOM_ENTITY), () -> PieceOperatorRandomEntity.class);
	public static final RegistryEntry<Class<PieceOperatorFocusedEntity>> OPERATOR_FOCUSED_ENTITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_FOCUSED_ENTITY), () -> PieceOperatorFocusedEntity.class);
	public static final RegistryEntry<Class<PieceOperatorListAdd>> OPERATOR_LIST_ADD =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_ADD), () -> PieceOperatorListAdd.class);
	public static final RegistryEntry<Class<PieceOperatorListRemove>> OPERATOR_LIST_REMOVE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_REMOVE), () -> PieceOperatorListRemove.class);
	public static final RegistryEntry<Class<PieceOperatorClosestToLine>> OPERATOR_CLOSEST_TO_LINE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CLOSEST_TO_LINE), () -> PieceOperatorClosestToLine.class);
	public static final RegistryEntry<Class<PieceOperatorEntityHealth>> OPERATOR_ENTITY_HEALTH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_HEALTH), () -> PieceOperatorEntityHealth.class);
	public static final RegistryEntry<Class<PieceOperatorEntityRaycast>> OPERATOR_ENTITY_RAYCAST =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_RAYCAST), () -> PieceOperatorEntityRaycast.class);
	public static final RegistryEntry<Class<PieceOperatorEntityHeight>> OPERATOR_ENTITY_HEIGHT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ENTITY_HEIGHT), () -> PieceOperatorEntityHeight.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> ENTITIES_INTRO =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.ENTITIES_INTRO),
					() -> Arrays.asList(
							PieceOperatorClosestToPoint.class,
							PieceSelectorNearbyItems.class,
							PieceSelectorNearbyLiving.class,
							PieceSelectorNearbyEnemies.class,
							PieceSelectorNearbyAnimals.class,
							PieceSelectorNearbyProjectiles.class,
							PieceSelectorNearbyCharges.class,
							PieceSelectorNearbyFallingBlocks.class,
							PieceSelectorNearbyGlowing.class,
							PieceSelectorNearbyPlayers.class,
							PieceSelectorNearbyVehicles.class,
							PieceOperatorEntityMotion.class,
							PieceOperatorEntityAxialLook.class,
							PieceOperatorRandomEntity.class,
							PieceOperatorFocusedEntity.class,
							PieceOperatorListAdd.class,
							PieceOperatorListRemove.class,
							PieceOperatorClosestToLine.class,
							PieceOperatorEntityHealth.class,
							PieceOperatorEntityRaycast.class,
							PieceOperatorEntityHeight.class
					));

	// ========== TOOL_CASTING ==========
	public static final RegistryEntry<Class<PieceSelectorBlockBroken>> SELECTOR_BLOCK_BROKEN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_BLOCK_BROKEN), () -> PieceSelectorBlockBroken.class);
	public static final RegistryEntry<Class<PieceSelectorBlockSideBroken>> SELECTOR_BLOCK_SIDE_BROKEN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN), () -> PieceSelectorBlockSideBroken.class);
	public static final RegistryEntry<Class<PieceSelectorAttackTarget>> SELECTOR_ATTACK_TARGET =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ATTACK_TARGET), () -> PieceSelectorAttackTarget.class);
	public static final RegistryEntry<Class<PieceSelectorItemCount>> SELECTOR_ITEM_COUNT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ITEM_COUNT), () -> PieceSelectorItemCount.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> TOOL_CASTING =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.TOOL_CASTING),
					() -> Arrays.asList(
							PieceSelectorBlockBroken.class,
							PieceSelectorBlockSideBroken.class,
							PieceSelectorAttackTarget.class,
							PieceSelectorItemCount.class
					));

	// ========== LOOPCASTING ==========
	public static final RegistryEntry<Class<PieceSelectorLoopcastIndex>> SELECTOR_LOOPCAST_INDEX =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_LOOPCAST_INDEX), () -> PieceSelectorLoopcastIndex.class);
	public static final RegistryEntry<Class<PieceOperatorModulus>> OPERATOR_MODULUS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MODULUS), () -> PieceOperatorModulus.class);
	public static final RegistryEntry<Class<PieceOperatorIntegerDivide>> OPERATOR_INTEGER_DIVIDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_INTEGER_DIVIDE), () -> PieceOperatorIntegerDivide.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> LOOPCASTING =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.LOOPCASTING),
					() -> Arrays.asList(
							PieceSelectorLoopcastIndex.class,
							PieceOperatorModulus.class,
							PieceOperatorIntegerDivide.class
					));

	// ========== FLOW_CONTROL ==========
	public static final RegistryEntry<Class<PieceSelectorSneakStatus>> SELECTOR_SNEAK_STATUS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_SNEAK_STATUS), () -> PieceSelectorSneakStatus.class);
	public static final RegistryEntry<Class<PieceSelectorTickTime>> SELECTOR_TICK_TIME =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_TICK_TIME), () -> PieceSelectorTickTime.class);
	public static final RegistryEntry<Class<PieceSelectorTps>> SELECTOR_TPS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_TPS), () -> PieceSelectorTps.class);
	public static final RegistryEntry<Class<PieceTrickDelay>> TRICK_DELAY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DELAY), () -> PieceTrickDelay.class);
	public static final RegistryEntry<Class<PieceTrickDie>> TRICK_DIE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_DIE), () -> PieceTrickDie.class);
	public static final RegistryEntry<Class<PieceTrickEvaluate>> TRICK_EVALUATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EVALUATE), () -> PieceTrickEvaluate.class);
	public static final RegistryEntry<Class<PieceTrickBreakLoop>> TRICK_BREAK_LOOP =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BREAK_LOOP), () -> PieceTrickBreakLoop.class);
	public static final RegistryEntry<Class<PieceConstantWrapper>> CONSTANT_WRAPPER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_WRAPPER), () -> PieceConstantWrapper.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> FLOW_CONTROL =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.FLOW_CONTROL),
					() -> Arrays.asList(
							PieceTrickDelay.class,
							PieceSelectorSneakStatus.class,
							PieceSelectorTickTime.class,
							PieceSelectorTps.class,
							PieceTrickDie.class,
							PieceTrickEvaluate.class,
							PieceTrickBreakLoop.class,
							PieceConstantWrapper.class
					));

	// ========== NUMBERS_INTRO ==========
	public static final RegistryEntry<Class<PieceOperatorSum>> OPERATOR_SUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SUM), () -> PieceOperatorSum.class);
	public static final RegistryEntry<Class<PieceOperatorSubtract>> OPERATOR_SUBTRACT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SUBTRACT), () -> PieceOperatorSubtract.class);
	public static final RegistryEntry<Class<PieceOperatorMultiply>> OPERATOR_MULTIPLY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MULTIPLY), () -> PieceOperatorMultiply.class);
	public static final RegistryEntry<Class<PieceOperatorDivide>> OPERATOR_DIVIDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_DIVIDE), () -> PieceOperatorDivide.class);
	public static final RegistryEntry<Class<PieceOperatorAbsolute>> OPERATOR_ABSOLUTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ABSOLUTE), () -> PieceOperatorAbsolute.class);
	public static final RegistryEntry<Class<PieceOperatorInverse>> OPERATOR_INVERSE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_INVERSE), () -> PieceOperatorInverse.class);
	public static final RegistryEntry<Class<PieceOperatorRoot>> OPERATOR_ROOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ROOT), () -> PieceOperatorRoot.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> NUMBERS_INTRO =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.NUMBERS_INTRO),
					() -> Arrays.asList(
							PieceOperatorSum.class,
							PieceOperatorSubtract.class,
							PieceOperatorMultiply.class,
							PieceOperatorDivide.class,
							PieceOperatorAbsolute.class,
							PieceOperatorInverse.class,
							PieceOperatorRoot.class
					));

	// ========== SECONDARY_OPERATORS ==========
	public static final RegistryEntry<Class<PieceOperatorSquare>> OPERATOR_SQUARE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SQUARE), () -> PieceOperatorSquare.class);
	public static final RegistryEntry<Class<PieceOperatorCube>> OPERATOR_CUBE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CUBE), () -> PieceOperatorCube.class);
	public static final RegistryEntry<Class<PieceOperatorPower>> OPERATOR_POWER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_POWER), () -> PieceOperatorPower.class);
	public static final RegistryEntry<Class<PieceOperatorSquareRoot>> OPERATOR_SQUARE_ROOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SQUARE_ROOT), () -> PieceOperatorSquareRoot.class);
	public static final RegistryEntry<Class<PieceOperatorLog>> OPERATOR_LOG =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LOG), () -> PieceOperatorLog.class);
	public static final RegistryEntry<Class<PieceOperatorCeiling>> OPERATOR_CEILING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_CEILING), () -> PieceOperatorCeiling.class);
	public static final RegistryEntry<Class<PieceOperatorFloor>> OPERATOR_FLOOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_FLOOR), () -> PieceOperatorFloor.class);
	public static final RegistryEntry<Class<PieceOperatorRound>> OPERATOR_ROUND =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ROUND), () -> PieceOperatorRound.class);
	public static final RegistryEntry<Class<PieceOperatorMax>> OPERATOR_MAX =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MAX), () -> PieceOperatorMax.class);
	public static final RegistryEntry<Class<PieceOperatorMin>> OPERATOR_MIN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_MIN), () -> PieceOperatorMin.class);
	public static final RegistryEntry<Class<PieceConstantE>> CONSTANT_E =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_E), () -> PieceConstantE.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> SECONDARY_OPERATORS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.SECONDARY_OPERATORS),
					() -> Arrays.asList(
							PieceOperatorSquare.class,
							PieceOperatorCube.class,
							PieceOperatorPower.class,
							PieceOperatorSquareRoot.class,
							PieceOperatorLog.class,
							PieceOperatorCeiling.class,
							PieceOperatorFloor.class,
							PieceOperatorRound.class,
							PieceOperatorMax.class,
							PieceOperatorMin.class,
							PieceConstantE.class
					));

	// ========== TRIGONOMETRY ==========
	public static final RegistryEntry<Class<PieceOperatorSin>> OPERATOR_SIN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SIN), () -> PieceOperatorSin.class);
	public static final RegistryEntry<Class<PieceOperatorCos>> OPERATOR_COS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_COS), () -> PieceOperatorCos.class);
	public static final RegistryEntry<Class<PieceOperatorAsin>> OPERATOR_ASIN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ASIN), () -> PieceOperatorAsin.class);
	public static final RegistryEntry<Class<PieceOperatorAcos>> OPERATOR_ACOS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_ACOS), () -> PieceOperatorAcos.class);
	public static final RegistryEntry<Class<PieceOperatorVectorDotProduct>> OPERATOR_VECTOR_DOT_PRODUCT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT), () -> PieceOperatorVectorDotProduct.class);
	public static final RegistryEntry<Class<PieceOperatorGammaFunc>> OPERATOR_GAMMA_FUNCTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_GAMMA_FUNCTION), () -> PieceOperatorGammaFunc.class);
	public static final RegistryEntry<Class<PieceOperatorPlanarNormalVector>> OPERATOR_PLANAR_NORMAL_VECTOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR), () -> PieceOperatorPlanarNormalVector.class);
	public static final RegistryEntry<Class<PieceOperatorVectorRotate>> OPERATOR_VECTOR_ROTATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_ROTATE), () -> PieceOperatorVectorRotate.class);
	public static final RegistryEntry<Class<PieceConstantPi>> CONSTANT_PI =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_PI), () -> PieceConstantPi.class);
	public static final RegistryEntry<Class<PieceConstantTau>> CONSTANT_TAU =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.CONSTANT_TAU), () -> PieceConstantTau.class);
	public static final RegistryEntry<Class<PieceOperatorSignum>> OPERATOR_SIGNUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_SIGNUM), () -> PieceOperatorSignum.class);
	public static final RegistryEntry<Class<PieceOperatorVectorAbsolute>> OPERATOR_VECTOR_ABSOLUTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_ABSOLUTE), () -> PieceOperatorVectorAbsolute.class);
	public static final RegistryEntry<Class<PieceOperatorVectorSignum>> OPERATOR_VECTOR_SIGNUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_SIGNUM), () -> PieceOperatorVectorSignum.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> TRIGONOMETRY =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.TRIGONOMETRY),
					() -> Arrays.asList(
							PieceConstantPi.class,
							PieceOperatorSin.class,
							PieceOperatorCos.class,
							PieceOperatorAsin.class,
							PieceOperatorAcos.class,
							PieceOperatorVectorDotProduct.class,
							PieceOperatorGammaFunc.class,
							PieceOperatorPlanarNormalVector.class,
							PieceOperatorVectorRotate.class,
							PieceConstantTau.class,
							PieceOperatorSignum.class,
							PieceOperatorVectorAbsolute.class,
							PieceOperatorVectorSignum.class
					));

	// ========== VECTORS_INTRO ==========
	public static final RegistryEntry<Class<PieceOperatorVectorSum>> OPERATOR_VECTOR_SUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_SUM), () -> PieceOperatorVectorSum.class);
	public static final RegistryEntry<Class<PieceOperatorVectorSubtract>> OPERATOR_VECTOR_SUBTRACT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_SUBTRACT), () -> PieceOperatorVectorSubtract.class);
	public static final RegistryEntry<Class<PieceOperatorVectorMultiply>> OPERATOR_VECTOR_MULTIPLY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MULTIPLY), () -> PieceOperatorVectorMultiply.class);
	public static final RegistryEntry<Class<PieceOperatorVectorDivide>> OPERATOR_VECTOR_DIVIDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_DIVIDE), () -> PieceOperatorVectorDivide.class);
	public static final RegistryEntry<Class<PieceOperatorVectorCrossProduct>> OPERATOR_VECTOR_CROSS_PRODUCT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT), () -> PieceOperatorVectorCrossProduct.class);
	public static final RegistryEntry<Class<PieceOperatorVectorNormalize>> OPERATOR_VECTOR_NORMALIZE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_NORMALIZE), () -> PieceOperatorVectorNormalize.class);
	public static final RegistryEntry<Class<PieceOperatorVectorNegate>> OPERATOR_VECTOR_NEGATE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_NEGATE), () -> PieceOperatorVectorNegate.class);
	public static final RegistryEntry<Class<PieceOperatorVectorMagnitude>> OPERATOR_VECTOR_MAGNITUDE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MAGNITUDE), () -> PieceOperatorVectorMagnitude.class);
	public static final RegistryEntry<Class<PieceOperatorVectorConstruct>> OPERATOR_VECTOR_CONSTRUCT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_CONSTRUCT), () -> PieceOperatorVectorConstruct.class);
	public static final RegistryEntry<Class<PieceOperatorVectorExtractX>> OPERATOR_VECTOR_EXTRACT_X =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_EXTRACT_X), () -> PieceOperatorVectorExtractX.class);
	public static final RegistryEntry<Class<PieceOperatorVectorExtractY>> OPERATOR_VECTOR_EXTRACT_Y =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y), () -> PieceOperatorVectorExtractY.class);
	public static final RegistryEntry<Class<PieceOperatorVectorExtractZ>> OPERATOR_VECTOR_EXTRACT_Z =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z), () -> PieceOperatorVectorExtractZ.class);
	public static final RegistryEntry<Class<PieceOperatorVectorMaximum>> OPERATOR_VECTOR_MAXIMUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MAXIMUM), () -> PieceOperatorVectorMaximum.class);
	public static final RegistryEntry<Class<PieceOperatorVectorMinimum>> OPERATOR_VECTOR_MINIMUM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_MINIMUM), () -> PieceOperatorVectorMinimum.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> VECTORS_INTRO =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.VECTORS_INTRO),
					() -> Arrays.asList(
							PieceOperatorVectorConstruct.class,
							PieceOperatorVectorSum.class,
							PieceOperatorVectorSubtract.class,
							PieceOperatorVectorMultiply.class,
							PieceOperatorVectorDivide.class,
							PieceOperatorVectorCrossProduct.class,
							PieceOperatorVectorNormalize.class,
							PieceOperatorVectorNegate.class,
							PieceOperatorVectorMagnitude.class,
							PieceOperatorVectorExtractX.class,
							PieceOperatorVectorExtractY.class,
							PieceOperatorVectorExtractZ.class,
							PieceOperatorVectorMaximum.class,
							PieceOperatorVectorMinimum.class
					));

	// ========== BLOCK_WORKS ==========
	public static final RegistryEntry<Class<PieceOperatorVectorRaycastAxis>> OPERATOR_VECTOR_RAYCAST_AXIS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS), () -> PieceOperatorVectorRaycastAxis.class);
	public static final RegistryEntry<Class<PieceOperatorVectorProject>> OPERATOR_VECTOR_PROJECT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_VECTOR_PROJECT), () -> PieceOperatorVectorProject.class);
	public static final RegistryEntry<Class<PieceOperatorBlockLightLevel>> OPERATOR_BLOCK_LIGHT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_LIGHT), () -> PieceOperatorBlockLightLevel.class);
	public static final RegistryEntry<Class<PieceOperatorBlockHardness>> OPERATOR_BLOCK_HARDNESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_HARDNESS), () -> PieceOperatorBlockHardness.class);
	public static final RegistryEntry<Class<PieceOperatorBlockComparatorStrength>> OPERATOR_BLOCK_COMPARATOR_STRENGTH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_COMPARATOR_STRENGTH), () -> PieceOperatorBlockComparatorStrength.class);
	public static final RegistryEntry<Class<PieceOperatorBlockSideSolidity>> OPERATOR_BLOCK_SIDE_SOLIDITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_SIDE_SOLIDITY), () -> PieceOperatorBlockSideSolidity.class);
	public static final RegistryEntry<Class<PieceOperatorBlockMiningLevel>> OPERATOR_BLOCK_MINING_LEVEL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_BLOCK_MINING_LEVEL), () -> PieceOperatorBlockMiningLevel.class);
	public static final RegistryEntry<Class<PieceTrickBreakBlock>> TRICK_BREAK_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BREAK_BLOCK), () -> PieceTrickBreakBlock.class);
	public static final RegistryEntry<Class<PieceTrickBreakInSequence>> TRICK_BREAK_IN_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BREAK_IN_SEQUENCE), () -> PieceTrickBreakInSequence.class);
	public static final RegistryEntry<Class<PieceTrickPlaceBlock>> TRICK_PLACE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PLACE_BLOCK), () -> PieceTrickPlaceBlock.class);
	public static final RegistryEntry<Class<PieceTrickPlaceInSequence>> TRICK_PLACE_IN_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PLACE_IN_SEQUENCE), () -> PieceTrickPlaceInSequence.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> BLOCK_WORKS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.BLOCK_WORKS),
					() -> Arrays.asList(
							PieceTrickBreakInSequence.class,
							PieceOperatorVectorRaycastAxis.class,
							PieceOperatorVectorProject.class,
							PieceOperatorBlockLightLevel.class,
							PieceOperatorBlockHardness.class,
							PieceOperatorBlockComparatorStrength.class,
							PieceOperatorBlockSideSolidity.class,
							PieceOperatorBlockMiningLevel.class,
							PieceTrickBreakBlock.class,
							PieceTrickPlaceBlock.class,
							PieceTrickPlaceInSequence.class
					));

	// ========== BLOCK_MOVEMENT ==========
	public static final RegistryEntry<Class<PieceTrickMoveBlock>> TRICK_MOVE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MOVE_BLOCK), () -> PieceTrickMoveBlock.class);
	public static final RegistryEntry<Class<PieceTrickCollapseBlock>> TRICK_COLLAPSE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_COLLAPSE_BLOCK), () -> PieceTrickCollapseBlock.class);
	public static final RegistryEntry<Class<PieceTrickMoveBlockSequence>> TRICK_MOVE_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE), () -> PieceTrickMoveBlockSequence.class);
	public static final RegistryEntry<Class<PieceTrickCollapseBlockSequence>> TRICK_COLLAPSE_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_COLLAPSE_BLOCK_SEQUENCE), () -> PieceTrickCollapseBlockSequence.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> BLOCK_MOVEMENT =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.BLOCK_MOVEMENT),
					() -> Arrays.asList(
							PieceTrickMoveBlock.class,
							PieceTrickCollapseBlock.class,
							PieceTrickMoveBlockSequence.class,
							PieceTrickCollapseBlockSequence.class
					));

	// ========== BLOCK_CONJURATION ==========
	public static final RegistryEntry<Class<PieceTrickConjureBlock>> TRICK_CONJURE_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_BLOCK), () -> PieceTrickConjureBlock.class);
	public static final RegistryEntry<Class<PieceTrickConjureLight>> TRICK_CONJURE_LIGHT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_LIGHT), () -> PieceTrickConjureLight.class);
	public static final RegistryEntry<Class<PieceTrickConjureBlockSequence>> TRICK_CONJURE_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE), () -> PieceTrickConjureBlockSequence.class);
	public static final RegistryEntry<Class<PieceTrickParticleTrail>> TRICK_PARTICLE_TRAIL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PARTICLE_TRAIL), () -> PieceTrickParticleTrail.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> BLOCK_CONJURATION =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.BLOCK_CONJURATION),
					() -> Arrays.asList(
							PieceTrickConjureBlock.class,
							PieceTrickConjureLight.class,
							PieceTrickConjureBlockSequence.class,
							PieceTrickParticleTrail.class
					));

	// ========== MOVEMENT ==========
	public static final RegistryEntry<Class<PieceTrickBlink>> TRICK_BLINK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BLINK), () -> PieceTrickBlink.class);
	public static final RegistryEntry<Class<PieceTrickMassBlink>> TRICK_MASS_BLINK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MASS_BLINK), () -> PieceTrickMassBlink.class);
	public static final RegistryEntry<Class<PieceTrickMassAddMotion>> TRICK_MASS_ADD_MOTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MASS_ADD_MOTION), () -> PieceTrickMassAddMotion.class);
	public static final RegistryEntry<Class<PieceTrickMassExodus>> TRICK_MASS_EXODUS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_MASS_EXODUS), () -> PieceTrickMassExodus.class);
	public static final RegistryEntry<Class<PieceSelectorIsElytraFlying>> SELECTOR_IS_ELYTRA_FLYING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_IS_ELYTRA_FLYING), () -> PieceSelectorIsElytraFlying.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> MOVEMENT =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.MOVEMENT),
					() -> Arrays.asList(
							PieceTrickBlink.class,
							PieceTrickMassBlink.class,
							PieceTrickMassAddMotion.class,
							PieceTrickMassExodus.class,
							PieceSelectorIsElytraFlying.class
					));

	// ========== ELEMENTAL_ARTS ==========
	public static final RegistryEntry<Class<PieceOperatorRandom>> OPERATOR_RANDOM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_RANDOM), () -> PieceOperatorRandom.class);
	public static final RegistryEntry<Class<PieceTrickSmite>> TRICK_SMITE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMITE), () -> PieceTrickSmite.class);
	public static final RegistryEntry<Class<PieceTrickBlaze>> TRICK_BLAZE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_BLAZE), () -> PieceTrickBlaze.class);
	public static final RegistryEntry<Class<PieceTrickTorrent>> TRICK_TORRENT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_TORRENT), () -> PieceTrickTorrent.class);
	public static final RegistryEntry<Class<PieceTrickOvergrow>> TRICK_OVERGROW =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_OVERGROW), () -> PieceTrickOvergrow.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> ELEMENTAL_ARTS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.ELEMENTAL_ARTS),
					() -> Arrays.asList(
							PieceTrickSmite.class,
							PieceOperatorRandom.class,
							PieceTrickBlaze.class,
							PieceTrickTorrent.class,
							PieceTrickOvergrow.class
					));

	// ========== POSITIVE_EFFECTS ==========
	public static final RegistryEntry<Class<PieceTrickSpeed>> TRICK_SPEED =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SPEED), () -> PieceTrickSpeed.class);
	public static final RegistryEntry<Class<PieceTrickHaste>> TRICK_HASTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_HASTE), () -> PieceTrickHaste.class);
	public static final RegistryEntry<Class<PieceTrickStrength>> TRICK_STRENGTH =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_STRENGTH), () -> PieceTrickStrength.class);
	public static final RegistryEntry<Class<PieceTrickJumpBoost>> TRICK_JUMP_BOOST =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_JUMP_BOOST), () -> PieceTrickJumpBoost.class);
	public static final RegistryEntry<Class<PieceTrickWaterBreathing>> TRICK_WATER_BREATHING =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_WATER_BREATHING), () -> PieceTrickWaterBreathing.class);
	public static final RegistryEntry<Class<PieceTrickFireResistance>> TRICK_FIRE_RESISTANCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_FIRE_RESISTANCE), () -> PieceTrickFireResistance.class);
	public static final RegistryEntry<Class<PieceTrickInvisibility>> TRICK_INVISIBILITY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_INVISIBILITY), () -> PieceTrickInvisibility.class);
	public static final RegistryEntry<Class<PieceTrickRegeneration>> TRICK_REGENERATION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_REGENERATION), () -> PieceTrickRegeneration.class);
	public static final RegistryEntry<Class<PieceTrickResistance>> TRICK_RESISTANCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_RESISTANCE), () -> PieceTrickResistance.class);
	public static final RegistryEntry<Class<PieceTrickNightVision>> TRICK_NIGHT_VISION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_NIGHT_VISION), () -> PieceTrickNightVision.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> POSITIVE_EFFECTS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.POSITIVE_EFFECTS),
					() -> Arrays.asList(
							PieceTrickSpeed.class,
							PieceTrickHaste.class,
							PieceTrickStrength.class,
							PieceTrickJumpBoost.class,
							PieceTrickWaterBreathing.class,
							PieceTrickFireResistance.class,
							PieceTrickInvisibility.class,
							PieceTrickRegeneration.class,
							PieceTrickResistance.class,
							PieceTrickNightVision.class
					));

	// ========== NEGATIVE_EFFECTS ==========
	public static final RegistryEntry<Class<PieceTrickWither>> TRICK_WITHER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_WITHER), () -> PieceTrickWither.class);
	public static final RegistryEntry<Class<PieceTrickSlowness>> TRICK_SLOWNESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SLOWNESS), () -> PieceTrickSlowness.class);
	public static final RegistryEntry<Class<PieceTrickWeakness>> TRICK_WEAKNESS =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_WEAKNESS), () -> PieceTrickWeakness.class);
	public static final RegistryEntry<Class<PieceTrickIgnite>> TRICK_IGNITE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_IGNITE), () -> PieceTrickIgnite.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> NEGATIVE_EFFECTS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.NEGATIVE_EFFECTS),
					() -> Arrays.asList(
							PieceTrickWither.class,
							PieceTrickSlowness.class,
							PieceTrickWeakness.class,
							PieceTrickIgnite.class
					));

	// ========== EIDOS_REVERSAL ==========
	public static final RegistryEntry<Class<PieceSelectorEidosChangelog>> SELECTOR_EIDOS_CHANGELOG =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_EIDOS_CHANGELOG), () -> PieceSelectorEidosChangelog.class);
	public static final RegistryEntry<Class<PieceTrickEidosAnchor>> TRICK_EIDOS_ANCHOR =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EIDOS_ANCHOR), () -> PieceTrickEidosAnchor.class);
	public static final RegistryEntry<Class<PieceTrickEidosReversal>> TRICK_EIDOS_REVERSAL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EIDOS_REVERSAL), () -> PieceTrickEidosReversal.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> EIDOS_REVERSAL =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.EIDOS_REVERSAL),
					() -> Arrays.asList(
							PieceTrickEidosReversal.class,
							PieceSelectorEidosChangelog.class,
							PieceTrickEidosAnchor.class
					));

	// ========== EXOSUIT_CASTING ==========
	public static final RegistryEntry<Class<PieceSelectorTime>> SELECTOR_TIME =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_TIME), () -> PieceSelectorTime.class);
	public static final RegistryEntry<Class<PieceSelectorAttacker>> SELECTOR_ATTACKER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ATTACKER), () -> PieceSelectorAttacker.class);
	public static final RegistryEntry<Class<PieceSelectorDamageTaken>> SELECTOR_DAMAGE_TAKEN =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_DAMAGE_TAKEN), () -> PieceSelectorDamageTaken.class);
	public static final RegistryEntry<Class<PieceSelectorSuccessCounter>> SELECTOR_SUCCESS_COUNTER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_SUCCESS_COUNTER), () -> PieceSelectorSuccessCounter.class);
	public static final RegistryEntry<Class<PieceSelectorCasterBattery>> SELECTOR_CASTER_BATTERY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_CASTER_BATTERY), () -> PieceSelectorCasterBattery.class);
	public static final RegistryEntry<Class<PieceSelectorCasterEnergy>> SELECTOR_CASTER_ENERGY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_CASTER_ENERGY), () -> PieceSelectorCasterEnergy.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> EXOSUIT_CASTING =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.EXOSUIT_CASTING),
					() -> Arrays.asList(
							PieceSelectorTime.class,
							PieceSelectorAttacker.class,
							PieceSelectorDamageTaken.class,
							PieceSelectorSuccessCounter.class,
							PieceSelectorCasterBattery.class,
							PieceSelectorCasterEnergy.class
					));

	// ========== DETECTION_DYNAMICS ==========
	public static final RegistryEntry<Class<PieceSelectorItemPresence>> SELECTOR_ITEM_PRESENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_ITEM_PRESENCE), () -> PieceSelectorItemPresence.class);
	public static final RegistryEntry<Class<PieceSelectorBlockPresence>> SELECTOR_BLOCK_PRESENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_BLOCK_PRESENCE), () -> PieceSelectorBlockPresence.class);
	public static final RegistryEntry<Class<PieceTrickSwitchTargetSlot>> TRICK_SWITCH_TARGET_SLOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SWITCH_TARGET_SLOT), () -> PieceTrickSwitchTargetSlot.class);
	public static final RegistryEntry<Class<PieceTrickChangeSlot>> TRICK_CHANGE_SLOT =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CHANGE_SLOT), () -> PieceTrickChangeSlot.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> DETECTION_DYNAMICS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.DETECTION_DYNAMICS),
					() -> Arrays.asList(
							PieceTrickSwitchTargetSlot.class,
							PieceSelectorItemPresence.class,
							PieceSelectorBlockPresence.class,
							PieceTrickChangeSlot.class
					));

	// ========== SMELTERY ==========
	public static final RegistryEntry<Class<PieceSelectorNearbySmeltables>> SELECTOR_NEARBY_SMELTABLES =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.SELECTOR_NEARBY_SMELTABLES), () -> PieceSelectorNearbySmeltables.class);
	public static final RegistryEntry<Class<PieceTrickSmeltBlock>> TRICK_SMELT_BLOCK =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMELT_BLOCK), () -> PieceTrickSmeltBlock.class);
	public static final RegistryEntry<Class<PieceTrickSmeltItem>> TRICK_SMELT_ITEM =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMELT_ITEM), () -> PieceTrickSmeltItem.class);
	public static final RegistryEntry<Class<PieceTrickSmeltBlockSequence>> TRICK_SMELT_BLOCK_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE), () -> PieceTrickSmeltBlockSequence.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> SMELTERY =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.SMELTERY),
					() -> Arrays.asList(
							PieceTrickSmeltItem.class,
							PieceSelectorNearbySmeltables.class,
							PieceTrickSmeltBlock.class,
							PieceTrickSmeltBlockSequence.class
					));

	// ========== INFUSION / GREATER_INFUSION ==========
	public static final RegistryEntry<Class<PieceTrickInfusion>> TRICK_INFUSION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_INFUSION), () -> PieceTrickInfusion.class);
	public static final RegistryEntry<Class<PieceTrickGreaterInfusion>> TRICK_GREATER_INFUSION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_GREATER_INFUSION), () -> PieceTrickGreaterInfusion.class);
	public static final RegistryEntry<Class<PieceTrickEbonyIvory>> TRICK_EBONY_IVORY =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_EBONY_IVORY), () -> PieceTrickEbonyIvory.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> INFUSION =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.INFUSION),
					() -> List.of(
							PieceTrickInfusion.class
					));

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> GREATER_INFUSION =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.GREATER_INFUSION),
					() -> Arrays.asList(
							PieceTrickGreaterInfusion.class,
							PieceTrickEbonyIvory.class
					));

	// ========== LIST_OPERATIONS ==========
	public static final RegistryEntry<Class<PieceOperatorListExclusion>> OPERATOR_LIST_EXCLUSION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_EXCLUSION), () -> PieceOperatorListExclusion.class);
	public static final RegistryEntry<Class<PieceOperatorListIntersection>> OPERATOR_LIST_INTERSECTION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_INTERSECTION), () -> PieceOperatorListIntersection.class);
	public static final RegistryEntry<Class<PieceOperatorListSize>> OPERATOR_LIST_SIZE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_SIZE), () -> PieceOperatorListSize.class);
	public static final RegistryEntry<Class<PieceOperatorListUnion>> OPERATOR_LIST_UNION =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_UNION), () -> PieceOperatorListUnion.class);
	public static final RegistryEntry<Class<PieceOperatorListIndex>> OPERATOR_LIST_INDEX =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.OPERATOR_LIST_INDEX), () -> PieceOperatorListIndex.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> LIST_OPERATIONS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.LIST_OPERATIONS),
					() -> Arrays.asList(
							PieceOperatorListExclusion.class,
							PieceOperatorListIntersection.class,
							PieceOperatorListSize.class,
							PieceOperatorListUnion.class,
							PieceOperatorListIndex.class
					));

	// ========== EIDOS / MISC / RUSSIAN ROULETTE & OTHERS ==========
	public static final RegistryEntry<Class<PieceTrickPlaySound>> TRICK_PLAY_SOUND =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_PLAY_SOUND), () -> PieceTrickPlaySound.class);
	public static final RegistryEntry<Class<PieceTrickTill>> TRICK_TILL =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_TILL), () -> PieceTrickTill.class);
	public static final RegistryEntry<Class<PieceTrickTillSequence>> TRICK_TILL_SEQUENCE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_TILL_SEQUENCE), () -> PieceTrickTillSequence.class);
	public static final RegistryEntry<Class<PieceTrickSpinChamber>> TRICK_SPIN_CHAMBER =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_SPIN_CHAMBER), () -> PieceTrickSpinChamber.class);
	public static final RegistryEntry<Class<PieceTrickRussianRoulette>> TRICK_RUSSIAN_ROULETTE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_RUSSIAN_ROULETTE), () -> PieceTrickRussianRoulette.class);
	public static final RegistryEntry<Class<PieceTrickConjureCircle>> TRICK_CONJURE_CIRCLE =
			PsiRegistries.register(PsiAPI.SPELL_PIECE_REGISTRY, PsiAPI.location(LibPieceNames.TRICK_CONJURE_CIRCLE), () -> PieceTrickConjureCircle.class);

	public static final RegistryEntry<Collection<Class<? extends SpellPiece>>> MISC_TRICKS =
			PsiRegistries.register(PsiAPI.ADVANCEMENT_GROUP_REGISTRY, PsiAPI.location(LibPieceGroups.MISC_TRICKS),
					() -> Arrays.asList(
							PieceTrickPlaySound.class,
							PieceTrickTill.class,
							PieceTrickTillSequence.class,
							PieceTrickSpinChamber.class,
							PieceTrickRussianRoulette.class,
							PieceTrickConjureCircle.class
					));
}
