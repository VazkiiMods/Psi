/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.spell.base;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.lib.LibPieceNames;
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

	// ========== REGISTRIES ==========
	public static final DeferredRegister<Class<? extends SpellPiece>> SPELL_PIECES =
			DeferredRegister.create(PsiAPI.SPELL_PIECE_REGISTRY_TYPE_KEY, LibMisc.MOD_ID);
	public static final DeferredRegister<Collection<Class<? extends SpellPiece>>> ADVANCEMENT_GROUPS =
			DeferredRegister.create(PsiAPI.ADVANCEMENT_GROUP_REGISTRY_KEY, LibMisc.MOD_ID);

	// ========== MEMORY MANAGEMENT ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceCrossConnector>> CROSS_CONNECTOR =
			SPELL_PIECES.register(LibPieceNames.CROSS_CONNECTOR, () -> PieceCrossConnector.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorSavedVector>> SELECTOR_SAVED_VECTOR =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_SAVED_VECTOR, () -> PieceSelectorSavedVector.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickDetonate>> TRICK_DETONATE =
			SPELL_PIECES.register(LibPieceNames.TRICK_DETONATE, () -> PieceTrickDetonate.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSaveVector>> TRICK_SAVE_VECTOR =
			SPELL_PIECES.register(LibPieceNames.TRICK_SAVE_VECTOR, () -> PieceTrickSaveVector.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> MEMORY_MANAGEMENT =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.MEMORY_MANAGEMENT,
					() -> Arrays.asList(
							PieceTrickSaveVector.class,
							PieceTrickDetonate.class,
							PieceSelectorSavedVector.class,
							PieceCrossConnector.class
					));

	// ========== TUTORIAL_1 ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorCaster>> SELECTOR_CASTER =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_CASTER, () -> PieceSelectorCaster.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickDebug>> TRICK_DEBUG =
			SPELL_PIECES.register(LibPieceNames.TRICK_DEBUG, () -> PieceTrickDebug.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickDebugSpamless>> TRICK_DEBUG_SPAMLESS =
			SPELL_PIECES.register(LibPieceNames.TRICK_DEBUG_SPAMLESS, () -> PieceTrickDebugSpamless.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> TUTORIAL_1 =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.TUTORIAL_1,
					() -> Arrays.asList(
							PieceSelectorCaster.class,
							PieceTrickDebug.class,
							PieceTrickDebugSpamless.class
					));

	// ========== TUTORIAL_2 ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceConstantNumber>> CONSTANT_NUMBER =
			SPELL_PIECES.register(LibPieceNames.CONSTANT_NUMBER, () -> PieceConstantNumber.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceConnector>> CONNECTOR =
			SPELL_PIECES.register(LibPieceNames.CONNECTOR, () -> PieceConnector.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> TUTORIAL_2 =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.TUTORIAL_2,
					() -> Arrays.asList(
							PieceConstantNumber.class,
							PieceConnector.class
					));

	// ========== TUTORIAL_3 ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityLook>> OPERATOR_ENTITY_LOOK =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_LOOK, () -> PieceOperatorEntityLook.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickAddMotion>> TRICK_ADD_MOTION =
			SPELL_PIECES.register(LibPieceNames.TRICK_ADD_MOTION, () -> PieceTrickAddMotion.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> TUTORIAL_3 =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.TUTORIAL_3,
					() -> Arrays.asList(
							PieceTrickAddMotion.class,
							PieceOperatorEntityLook.class
					));

	// ========== TUTORIAL_4 ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityPosition>> OPERATOR_ENTITY_POSITION =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_POSITION, () -> PieceOperatorEntityPosition.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorRaycast>> OPERATOR_VECTOR_RAYCAST =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_RAYCAST, () -> PieceOperatorVectorRaycast.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickExplode>> TRICK_EXPLODE =
			SPELL_PIECES.register(LibPieceNames.TRICK_EXPLODE, () -> PieceTrickExplode.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceErrorSuppressor>> ERROR_SUPPRESSOR =
			SPELL_PIECES.register(LibPieceNames.ERROR_SUPPRESSOR, () -> PieceErrorSuppressor.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceErrorCatch>> ERROR_CATCH =
			SPELL_PIECES.register(LibPieceNames.ERROR_CATCH, () -> PieceErrorCatch.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> TUTORIAL_4 =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.TUTORIAL_4,
					() -> Arrays.asList(
							PieceTrickExplode.class,
							PieceOperatorEntityPosition.class,
							PieceOperatorVectorRaycast.class,
							PieceErrorSuppressor.class,
							PieceErrorCatch.class
					));

	// ========== PROJECTILES ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorFocalPoint>> SELECTOR_FOCAL_POINT =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_FOCAL_POINT, () -> PieceSelectorFocalPoint.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorRulerVector>> SELECTOR_RULER_VECTOR =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_RULER_VECTOR, () -> PieceSelectorRulerVector.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> PROJECTILES =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.PROJECTILES,
					() -> Arrays.asList(
							PieceSelectorFocalPoint.class,
							PieceSelectorRulerVector.class
					));

	// ========== ENTITIES_INTRO ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyItems>> SELECTOR_NEARBY_ITEMS =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_ITEMS, () -> PieceSelectorNearbyItems.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyLiving>> SELECTOR_NEARBY_LIVING =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_LIVING, () -> PieceSelectorNearbyLiving.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyEnemies>> SELECTOR_NEARBY_ENEMIES =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_ENEMIES, () -> PieceSelectorNearbyEnemies.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyAnimals>> SELECTOR_NEARBY_ANIMALS =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_ANIMALS, () -> PieceSelectorNearbyAnimals.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyProjectiles>> SELECTOR_NEARBY_PROJECTILES =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_PROJECTILES, () -> PieceSelectorNearbyProjectiles.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyCharges>> SELECTOR_NEARBY_CHARGES =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_CHARGES, () -> PieceSelectorNearbyCharges.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyFallingBlocks>> SELECTOR_NEARBY_FALLING_BLOCKS =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_FALLING_BLOCKS, () -> PieceSelectorNearbyFallingBlocks.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyGlowing>> SELECTOR_NEARBY_GLOWING =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_GLOWING, () -> PieceSelectorNearbyGlowing.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyPlayers>> SELECTOR_NEARBY_PLAYERS =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_PLAYERS, () -> PieceSelectorNearbyPlayers.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbyVehicles>> SELECTOR_NEARBY_VEHICLES =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_VEHICLES, () -> PieceSelectorNearbyVehicles.class);

	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityMotion>> OPERATOR_ENTITY_MOTION =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_MOTION, () -> PieceOperatorEntityMotion.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityAxialLook>> OPERATOR_ENTITY_AXIAL_LOOK =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK, () -> PieceOperatorEntityAxialLook.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorClosestToPoint>> OPERATOR_CLOSEST_TO_POINT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_CLOSEST_TO_POINT, () -> PieceOperatorClosestToPoint.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorRandomEntity>> OPERATOR_RANDOM_ENTITY =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_RANDOM_ENTITY, () -> PieceOperatorRandomEntity.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorFocusedEntity>> OPERATOR_FOCUSED_ENTITY =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_FOCUSED_ENTITY, () -> PieceOperatorFocusedEntity.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListAdd>> OPERATOR_LIST_ADD =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_ADD, () -> PieceOperatorListAdd.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListRemove>> OPERATOR_LIST_REMOVE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_REMOVE, () -> PieceOperatorListRemove.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorClosestToLine>> OPERATOR_CLOSEST_TO_LINE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_CLOSEST_TO_LINE, () -> PieceOperatorClosestToLine.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityHealth>> OPERATOR_ENTITY_HEALTH =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_HEALTH, () -> PieceOperatorEntityHealth.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityRaycast>> OPERATOR_ENTITY_RAYCAST =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_RAYCAST, () -> PieceOperatorEntityRaycast.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorEntityHeight>> OPERATOR_ENTITY_HEIGHT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ENTITY_HEIGHT, () -> PieceOperatorEntityHeight.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> ENTITIES_INTRO =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.ENTITIES_INTRO,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorBlockBroken>> SELECTOR_BLOCK_BROKEN =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_BLOCK_BROKEN, () -> PieceSelectorBlockBroken.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorBlockSideBroken>> SELECTOR_BLOCK_SIDE_BROKEN =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN, () -> PieceSelectorBlockSideBroken.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorAttackTarget>> SELECTOR_ATTACK_TARGET =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_ATTACK_TARGET, () -> PieceSelectorAttackTarget.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorItemCount>> SELECTOR_ITEM_COUNT =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_ITEM_COUNT, () -> PieceSelectorItemCount.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> TOOL_CASTING =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.TOOL_CASTING,
					() -> Arrays.asList(
							PieceSelectorBlockBroken.class,
							PieceSelectorBlockSideBroken.class,
							PieceSelectorAttackTarget.class,
							PieceSelectorItemCount.class
					));

	// ========== LOOPCASTING ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorLoopcastIndex>> SELECTOR_LOOPCAST_INDEX =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_LOOPCAST_INDEX, () -> PieceSelectorLoopcastIndex.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorModulus>> OPERATOR_MODULUS =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_MODULUS, () -> PieceOperatorModulus.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorIntegerDivide>> OPERATOR_INTEGER_DIVIDE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_INTEGER_DIVIDE, () -> PieceOperatorIntegerDivide.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> LOOPCASTING =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.LOOPCASTING,
					() -> Arrays.asList(
							PieceSelectorLoopcastIndex.class,
							PieceOperatorModulus.class,
							PieceOperatorIntegerDivide.class
					));

	// ========== FLOW_CONTROL ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorSneakStatus>> SELECTOR_SNEAK_STATUS =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_SNEAK_STATUS, () -> PieceSelectorSneakStatus.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorTickTime>> SELECTOR_TICK_TIME =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_TICK_TIME, () -> PieceSelectorTickTime.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorTps>> SELECTOR_TPS =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_TPS, () -> PieceSelectorTps.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickDelay>> TRICK_DELAY =
			SPELL_PIECES.register(LibPieceNames.TRICK_DELAY, () -> PieceTrickDelay.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickDie>> TRICK_DIE =
			SPELL_PIECES.register(LibPieceNames.TRICK_DIE, () -> PieceTrickDie.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickEvaluate>> TRICK_EVALUATE =
			SPELL_PIECES.register(LibPieceNames.TRICK_EVALUATE, () -> PieceTrickEvaluate.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickBreakLoop>> TRICK_BREAK_LOOP =
			SPELL_PIECES.register(LibPieceNames.TRICK_BREAK_LOOP, () -> PieceTrickBreakLoop.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceConstantWrapper>> CONSTANT_WRAPPER =
			SPELL_PIECES.register(LibPieceNames.CONSTANT_WRAPPER, () -> PieceConstantWrapper.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> FLOW_CONTROL =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.FLOW_CONTROL,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorSum>> OPERATOR_SUM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_SUM, () -> PieceOperatorSum.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorSubtract>> OPERATOR_SUBTRACT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_SUBTRACT, () -> PieceOperatorSubtract.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorMultiply>> OPERATOR_MULTIPLY =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_MULTIPLY, () -> PieceOperatorMultiply.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorDivide>> OPERATOR_DIVIDE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_DIVIDE, () -> PieceOperatorDivide.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorAbsolute>> OPERATOR_ABSOLUTE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ABSOLUTE, () -> PieceOperatorAbsolute.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorInverse>> OPERATOR_INVERSE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_INVERSE, () -> PieceOperatorInverse.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorRoot>> OPERATOR_ROOT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ROOT, () -> PieceOperatorRoot.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> NUMBERS_INTRO =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.NUMBERS_INTRO,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorSquare>> OPERATOR_SQUARE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_SQUARE, () -> PieceOperatorSquare.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorCube>> OPERATOR_CUBE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_CUBE, () -> PieceOperatorCube.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorPower>> OPERATOR_POWER =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_POWER, () -> PieceOperatorPower.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorSquareRoot>> OPERATOR_SQUARE_ROOT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_SQUARE_ROOT, () -> PieceOperatorSquareRoot.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorLog>> OPERATOR_LOG =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LOG, () -> PieceOperatorLog.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorCeiling>> OPERATOR_CEILING =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_CEILING, () -> PieceOperatorCeiling.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorFloor>> OPERATOR_FLOOR =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_FLOOR, () -> PieceOperatorFloor.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorRound>> OPERATOR_ROUND =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ROUND, () -> PieceOperatorRound.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorMax>> OPERATOR_MAX =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_MAX, () -> PieceOperatorMax.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorMin>> OPERATOR_MIN =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_MIN, () -> PieceOperatorMin.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceConstantE>> CONSTANT_E =
			SPELL_PIECES.register(LibPieceNames.CONSTANT_E, () -> PieceConstantE.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> SECONDARY_OPERATORS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.SECONDARY_OPERATORS,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorSin>> OPERATOR_SIN =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_SIN, () -> PieceOperatorSin.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorCos>> OPERATOR_COS =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_COS, () -> PieceOperatorCos.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorAsin>> OPERATOR_ASIN =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ASIN, () -> PieceOperatorAsin.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorAcos>> OPERATOR_ACOS =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_ACOS, () -> PieceOperatorAcos.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorDotProduct>> OPERATOR_VECTOR_DOT_PRODUCT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT, () -> PieceOperatorVectorDotProduct.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorGammaFunc>> OPERATOR_GAMMA_FUNCTION =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_GAMMA_FUNCTION, () -> PieceOperatorGammaFunc.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorPlanarNormalVector>> OPERATOR_PLANAR_NORMAL_VECTOR =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_PLANAR_NORMAL_VECTOR, () -> PieceOperatorPlanarNormalVector.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorRotate>> OPERATOR_VECTOR_ROTATE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_ROTATE, () -> PieceOperatorVectorRotate.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceConstantPi>> CONSTANT_PI =
			SPELL_PIECES.register(LibPieceNames.CONSTANT_PI, () -> PieceConstantPi.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceConstantTau>> CONSTANT_TAU =
			SPELL_PIECES.register(LibPieceNames.CONSTANT_TAU, () -> PieceConstantTau.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorSignum>> OPERATOR_SIGNUM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_SIGNUM, () -> PieceOperatorSignum.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorAbsolute>> OPERATOR_VECTOR_ABSOLUTE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_ABSOLUTE, () -> PieceOperatorVectorAbsolute.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorSignum>> OPERATOR_VECTOR_SIGNUM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_SIGNUM, () -> PieceOperatorVectorSignum.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> TRIGONOMETRY =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.TRIGONOMETRY,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorSum>> OPERATOR_VECTOR_SUM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_SUM, () -> PieceOperatorVectorSum.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorSubtract>> OPERATOR_VECTOR_SUBTRACT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_SUBTRACT, () -> PieceOperatorVectorSubtract.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorMultiply>> OPERATOR_VECTOR_MULTIPLY =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_MULTIPLY, () -> PieceOperatorVectorMultiply.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorDivide>> OPERATOR_VECTOR_DIVIDE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_DIVIDE, () -> PieceOperatorVectorDivide.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorCrossProduct>> OPERATOR_VECTOR_CROSS_PRODUCT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT, () -> PieceOperatorVectorCrossProduct.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorNormalize>> OPERATOR_VECTOR_NORMALIZE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_NORMALIZE, () -> PieceOperatorVectorNormalize.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorNegate>> OPERATOR_VECTOR_NEGATE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_NEGATE, () -> PieceOperatorVectorNegate.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorMagnitude>> OPERATOR_VECTOR_MAGNITUDE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_MAGNITUDE, () -> PieceOperatorVectorMagnitude.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorConstruct>> OPERATOR_VECTOR_CONSTRUCT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_CONSTRUCT, () -> PieceOperatorVectorConstruct.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorExtractX>> OPERATOR_VECTOR_EXTRACT_X =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_X, () -> PieceOperatorVectorExtractX.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorExtractY>> OPERATOR_VECTOR_EXTRACT_Y =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y, () -> PieceOperatorVectorExtractY.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorExtractZ>> OPERATOR_VECTOR_EXTRACT_Z =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z, () -> PieceOperatorVectorExtractZ.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorMaximum>> OPERATOR_VECTOR_MAXIMUM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_MAXIMUM, () -> PieceOperatorVectorMaximum.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorMinimum>> OPERATOR_VECTOR_MINIMUM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_MINIMUM, () -> PieceOperatorVectorMinimum.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> VECTORS_INTRO =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.VECTORS_INTRO,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorRaycastAxis>> OPERATOR_VECTOR_RAYCAST_AXIS =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS, () -> PieceOperatorVectorRaycastAxis.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorVectorProject>> OPERATOR_VECTOR_PROJECT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_VECTOR_PROJECT, () -> PieceOperatorVectorProject.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorBlockLightLevel>> OPERATOR_BLOCK_LIGHT =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_BLOCK_LIGHT, () -> PieceOperatorBlockLightLevel.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorBlockHardness>> OPERATOR_BLOCK_HARDNESS =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_BLOCK_HARDNESS, () -> PieceOperatorBlockHardness.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorBlockComparatorStrength>> OPERATOR_BLOCK_COMPARATOR_STRENGTH =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_BLOCK_COMPARATOR_STRENGTH, () -> PieceOperatorBlockComparatorStrength.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorBlockSideSolidity>> OPERATOR_BLOCK_SIDE_SOLIDITY =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_BLOCK_SIDE_SOLIDITY, () -> PieceOperatorBlockSideSolidity.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorBlockMiningLevel>> OPERATOR_BLOCK_MINING_LEVEL =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_BLOCK_MINING_LEVEL, () -> PieceOperatorBlockMiningLevel.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickBreakBlock>> TRICK_BREAK_BLOCK =
			SPELL_PIECES.register(LibPieceNames.TRICK_BREAK_BLOCK, () -> PieceTrickBreakBlock.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickBreakInSequence>> TRICK_BREAK_IN_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_BREAK_IN_SEQUENCE, () -> PieceTrickBreakInSequence.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickPlaceBlock>> TRICK_PLACE_BLOCK =
			SPELL_PIECES.register(LibPieceNames.TRICK_PLACE_BLOCK, () -> PieceTrickPlaceBlock.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickPlaceInSequence>> TRICK_PLACE_IN_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_PLACE_IN_SEQUENCE, () -> PieceTrickPlaceInSequence.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> BLOCK_WORKS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.BLOCK_WORKS,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickMoveBlock>> TRICK_MOVE_BLOCK =
			SPELL_PIECES.register(LibPieceNames.TRICK_MOVE_BLOCK, () -> PieceTrickMoveBlock.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickCollapseBlock>> TRICK_COLLAPSE_BLOCK =
			SPELL_PIECES.register(LibPieceNames.TRICK_COLLAPSE_BLOCK, () -> PieceTrickCollapseBlock.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickMoveBlockSequence>> TRICK_MOVE_BLOCK_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_MOVE_BLOCK_SEQUENCE, () -> PieceTrickMoveBlockSequence.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickCollapseBlockSequence>> TRICK_COLLAPSE_BLOCK_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_COLLAPSE_BLOCK_SEQUENCE, () -> PieceTrickCollapseBlockSequence.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> BLOCK_MOVEMENT =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.BLOCK_MOVEMENT,
					() -> Arrays.asList(
							PieceTrickMoveBlock.class,
							PieceTrickCollapseBlock.class,
							PieceTrickMoveBlockSequence.class,
							PieceTrickCollapseBlockSequence.class
					));

	// ========== BLOCK_CONJURATION ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickConjureBlock>> TRICK_CONJURE_BLOCK =
			SPELL_PIECES.register(LibPieceNames.TRICK_CONJURE_BLOCK, () -> PieceTrickConjureBlock.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickConjureLight>> TRICK_CONJURE_LIGHT =
			SPELL_PIECES.register(LibPieceNames.TRICK_CONJURE_LIGHT, () -> PieceTrickConjureLight.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickConjureBlockSequence>> TRICK_CONJURE_BLOCK_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE, () -> PieceTrickConjureBlockSequence.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickParticleTrail>> TRICK_PARTICLE_TRAIL =
			SPELL_PIECES.register(LibPieceNames.TRICK_PARTICLE_TRAIL, () -> PieceTrickParticleTrail.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> BLOCK_CONJURATION =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.BLOCK_CONJURATION,
					() -> Arrays.asList(
							PieceTrickConjureBlock.class,
							PieceTrickConjureLight.class,
							PieceTrickConjureBlockSequence.class,
							PieceTrickParticleTrail.class
					));

	// ========== MOVEMENT ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickBlink>> TRICK_BLINK =
			SPELL_PIECES.register(LibPieceNames.TRICK_BLINK, () -> PieceTrickBlink.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickMassBlink>> TRICK_MASS_BLINK =
			SPELL_PIECES.register(LibPieceNames.TRICK_MASS_BLINK, () -> PieceTrickMassBlink.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickMassAddMotion>> TRICK_MASS_ADD_MOTION =
			SPELL_PIECES.register(LibPieceNames.TRICK_MASS_ADD_MOTION, () -> PieceTrickMassAddMotion.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickMassExodus>> TRICK_MASS_EXODUS =
			SPELL_PIECES.register(LibPieceNames.TRICK_MASS_EXODUS, () -> PieceTrickMassExodus.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorIsElytraFlying>> SELECTOR_IS_ELYTRA_FLYING =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_IS_ELYTRA_FLYING, () -> PieceSelectorIsElytraFlying.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> MOVEMENT =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.MOVEMENT,
					() -> Arrays.asList(
							PieceTrickBlink.class,
							PieceTrickMassBlink.class,
							PieceTrickMassAddMotion.class,
							PieceTrickMassExodus.class,
							PieceSelectorIsElytraFlying.class
					));

	// ========== ELEMENTAL_ARTS ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorRandom>> OPERATOR_RANDOM =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_RANDOM, () -> PieceOperatorRandom.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSmite>> TRICK_SMITE =
			SPELL_PIECES.register(LibPieceNames.TRICK_SMITE, () -> PieceTrickSmite.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickBlaze>> TRICK_BLAZE =
			SPELL_PIECES.register(LibPieceNames.TRICK_BLAZE, () -> PieceTrickBlaze.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickTorrent>> TRICK_TORRENT =
			SPELL_PIECES.register(LibPieceNames.TRICK_TORRENT, () -> PieceTrickTorrent.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickOvergrow>> TRICK_OVERGROW =
			SPELL_PIECES.register(LibPieceNames.TRICK_OVERGROW, () -> PieceTrickOvergrow.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> ELEMENTAL_ARTS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.ELEMENTAL_ARTS,
					() -> Arrays.asList(
							PieceTrickSmite.class,
							PieceOperatorRandom.class,
							PieceTrickBlaze.class,
							PieceTrickTorrent.class,
							PieceTrickOvergrow.class
					));

	// ========== POSITIVE_EFFECTS ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSpeed>> TRICK_SPEED =
			SPELL_PIECES.register(LibPieceNames.TRICK_SPEED, () -> PieceTrickSpeed.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickHaste>> TRICK_HASTE =
			SPELL_PIECES.register(LibPieceNames.TRICK_HASTE, () -> PieceTrickHaste.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickStrength>> TRICK_STRENGTH =
			SPELL_PIECES.register(LibPieceNames.TRICK_STRENGTH, () -> PieceTrickStrength.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickJumpBoost>> TRICK_JUMP_BOOST =
			SPELL_PIECES.register(LibPieceNames.TRICK_JUMP_BOOST, () -> PieceTrickJumpBoost.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickWaterBreathing>> TRICK_WATER_BREATHING =
			SPELL_PIECES.register(LibPieceNames.TRICK_WATER_BREATHING, () -> PieceTrickWaterBreathing.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickFireResistance>> TRICK_FIRE_RESISTANCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_FIRE_RESISTANCE, () -> PieceTrickFireResistance.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickInvisibility>> TRICK_INVISIBILITY =
			SPELL_PIECES.register(LibPieceNames.TRICK_INVISIBILITY, () -> PieceTrickInvisibility.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickRegeneration>> TRICK_REGENERATION =
			SPELL_PIECES.register(LibPieceNames.TRICK_REGENERATION, () -> PieceTrickRegeneration.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickResistance>> TRICK_RESISTANCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_RESISTANCE, () -> PieceTrickResistance.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickNightVision>> TRICK_NIGHT_VISION =
			SPELL_PIECES.register(LibPieceNames.TRICK_NIGHT_VISION, () -> PieceTrickNightVision.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> POSITIVE_EFFECTS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.POSITIVE_EFFECTS,
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
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickWither>> TRICK_WITHER =
			SPELL_PIECES.register(LibPieceNames.TRICK_WITHER, () -> PieceTrickWither.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSlowness>> TRICK_SLOWNESS =
			SPELL_PIECES.register(LibPieceNames.TRICK_SLOWNESS, () -> PieceTrickSlowness.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickWeakness>> TRICK_WEAKNESS =
			SPELL_PIECES.register(LibPieceNames.TRICK_WEAKNESS, () -> PieceTrickWeakness.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickIgnite>> TRICK_IGNITE =
			SPELL_PIECES.register(LibPieceNames.TRICK_IGNITE, () -> PieceTrickIgnite.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> NEGATIVE_EFFECTS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.NEGATIVE_EFFECTS,
					() -> Arrays.asList(
							PieceTrickWither.class,
							PieceTrickSlowness.class,
							PieceTrickWeakness.class,
							PieceTrickIgnite.class
					));

	// ========== EIDOS_REVERSAL ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorEidosChangelog>> SELECTOR_EIDOS_CHANGELOG =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_EIDOS_CHANGELOG, () -> PieceSelectorEidosChangelog.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickEidosAnchor>> TRICK_EIDOS_ANCHOR =
			SPELL_PIECES.register(LibPieceNames.TRICK_EIDOS_ANCHOR, () -> PieceTrickEidosAnchor.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickEidosReversal>> TRICK_EIDOS_REVERSAL =
			SPELL_PIECES.register(LibPieceNames.TRICK_EIDOS_REVERSAL, () -> PieceTrickEidosReversal.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> EIDOS_REVERSAL =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.EIDOS_REVERSAL,
					() -> Arrays.asList(
							PieceTrickEidosReversal.class,
							PieceSelectorEidosChangelog.class,
							PieceTrickEidosAnchor.class
					));

	// ========== EXOSUIT_CASTING ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorTime>> SELECTOR_TIME =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_TIME, () -> PieceSelectorTime.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorAttacker>> SELECTOR_ATTACKER =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_ATTACKER, () -> PieceSelectorAttacker.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorDamageTaken>> SELECTOR_DAMAGE_TAKEN =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_DAMAGE_TAKEN, () -> PieceSelectorDamageTaken.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorSuccessCounter>> SELECTOR_SUCCESS_COUNTER =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_SUCCESS_COUNTER, () -> PieceSelectorSuccessCounter.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorCasterBattery>> SELECTOR_CASTER_BATTERY =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_CASTER_BATTERY, () -> PieceSelectorCasterBattery.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorCasterEnergy>> SELECTOR_CASTER_ENERGY =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_CASTER_ENERGY, () -> PieceSelectorCasterEnergy.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> EXOSUIT_CASTING =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.EXOSUIT_CASTING,
					() -> Arrays.asList(
							PieceSelectorTime.class,
							PieceSelectorAttacker.class,
							PieceSelectorDamageTaken.class,
							PieceSelectorSuccessCounter.class,
							PieceSelectorCasterBattery.class,
							PieceSelectorCasterEnergy.class
					));

	// ========== DETECTION_DYNAMICS ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorItemPresence>> SELECTOR_ITEM_PRESENCE =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_ITEM_PRESENCE, () -> PieceSelectorItemPresence.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorBlockPresence>> SELECTOR_BLOCK_PRESENCE =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_BLOCK_PRESENCE, () -> PieceSelectorBlockPresence.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSwitchTargetSlot>> TRICK_SWITCH_TARGET_SLOT =
			SPELL_PIECES.register(LibPieceNames.TRICK_SWITCH_TARGET_SLOT, () -> PieceTrickSwitchTargetSlot.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickChangeSlot>> TRICK_CHANGE_SLOT =
			SPELL_PIECES.register(LibPieceNames.TRICK_CHANGE_SLOT, () -> PieceTrickChangeSlot.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> DETECTION_DYNAMICS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.DETECTION_DYNAMICS,
					() -> Arrays.asList(
							PieceTrickSwitchTargetSlot.class,
							PieceSelectorItemPresence.class,
							PieceSelectorBlockPresence.class,
							PieceTrickChangeSlot.class
					));

	// ========== SMELTERY ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceSelectorNearbySmeltables>> SELECTOR_NEARBY_SMELTABLES =
			SPELL_PIECES.register(LibPieceNames.SELECTOR_NEARBY_SMELTABLES, () -> PieceSelectorNearbySmeltables.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSmeltBlock>> TRICK_SMELT_BLOCK =
			SPELL_PIECES.register(LibPieceNames.TRICK_SMELT_BLOCK, () -> PieceTrickSmeltBlock.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSmeltItem>> TRICK_SMELT_ITEM =
			SPELL_PIECES.register(LibPieceNames.TRICK_SMELT_ITEM, () -> PieceTrickSmeltItem.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSmeltBlockSequence>> TRICK_SMELT_BLOCK_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_SMELT_BLOCK_SEQUENCE, () -> PieceTrickSmeltBlockSequence.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> SMELTERY =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.SMELTERY,
					() -> Arrays.asList(
							PieceTrickSmeltItem.class,
							PieceSelectorNearbySmeltables.class,
							PieceTrickSmeltBlock.class,
							PieceTrickSmeltBlockSequence.class
					));

	// ========== INFUSION / GREATER_INFUSION ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickInfusion>> TRICK_INFUSION =
			SPELL_PIECES.register(LibPieceNames.TRICK_INFUSION, () -> PieceTrickInfusion.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickGreaterInfusion>> TRICK_GREATER_INFUSION =
			SPELL_PIECES.register(LibPieceNames.TRICK_GREATER_INFUSION, () -> PieceTrickGreaterInfusion.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickEbonyIvory>> TRICK_EBONY_IVORY =
			SPELL_PIECES.register(LibPieceNames.TRICK_EBONY_IVORY, () -> PieceTrickEbonyIvory.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> INFUSION =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.INFUSION,
					() -> List.of(
							PieceTrickInfusion.class
					));

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> GREATER_INFUSION =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.GREATER_INFUSION,
					() -> Arrays.asList(
							PieceTrickGreaterInfusion.class,
							PieceTrickEbonyIvory.class
					));

	// ========== LIST_OPERATIONS ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListExclusion>> OPERATOR_LIST_EXCLUSION =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_EXCLUSION, () -> PieceOperatorListExclusion.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListIntersection>> OPERATOR_LIST_INTERSECTION =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_INTERSECTION, () -> PieceOperatorListIntersection.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListSize>> OPERATOR_LIST_SIZE =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_SIZE, () -> PieceOperatorListSize.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListUnion>> OPERATOR_LIST_UNION =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_UNION, () -> PieceOperatorListUnion.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceOperatorListIndex>> OPERATOR_LIST_INDEX =
			SPELL_PIECES.register(LibPieceNames.OPERATOR_LIST_INDEX, () -> PieceOperatorListIndex.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> LIST_OPERATIONS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.LIST_OPERATIONS,
					() -> Arrays.asList(
							PieceOperatorListExclusion.class,
							PieceOperatorListIntersection.class,
							PieceOperatorListSize.class,
							PieceOperatorListUnion.class,
							PieceOperatorListIndex.class
					));

	// ========== EIDOS / MISC / RUSSIAN ROULETTE & OTHERS ==========
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickPlaySound>> TRICK_PLAY_SOUND =
			SPELL_PIECES.register(LibPieceNames.TRICK_PLAY_SOUND, () -> PieceTrickPlaySound.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickTill>> TRICK_TILL =
			SPELL_PIECES.register(LibPieceNames.TRICK_TILL, () -> PieceTrickTill.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickTillSequence>> TRICK_TILL_SEQUENCE =
			SPELL_PIECES.register(LibPieceNames.TRICK_TILL_SEQUENCE, () -> PieceTrickTillSequence.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickSpinChamber>> TRICK_SPIN_CHAMBER =
			SPELL_PIECES.register(LibPieceNames.TRICK_SPIN_CHAMBER, () -> PieceTrickSpinChamber.class);
	public static final DeferredHolder<Class<? extends SpellPiece>, Class<PieceTrickRussianRoulette>> TRICK_RUSSIAN_ROULETTE =
			SPELL_PIECES.register(LibPieceNames.TRICK_RUSSIAN_ROULETTE, () -> PieceTrickRussianRoulette.class);

	public static final DeferredHolder<Collection<Class<? extends SpellPiece>>, Collection<Class<? extends SpellPiece>>> MISC_TRICKS =
			ADVANCEMENT_GROUPS.register(LibPieceGroups.MISC_TRICKS,
					() -> Arrays.asList(
							PieceTrickPlaySound.class,
							PieceTrickTill.class,
							PieceTrickTillSequence.class,
							PieceTrickSpinChamber.class,
							PieceTrickRussianRoulette.class
					));
}
