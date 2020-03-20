/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [16/01/2016, 16:10:43 (GMT)]
 */
package vazkii.psi.common.spell.base;

import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.spell.constant.PieceConstantE;
import vazkii.psi.common.spell.constant.PieceConstantNumber;
import vazkii.psi.common.spell.constant.PieceConstantPi;
import vazkii.psi.common.spell.constant.PieceConstantWrapper;
import vazkii.psi.common.spell.operator.entity.*;
import vazkii.psi.common.spell.operator.number.*;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorAcos;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorAsin;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorCos;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorSin;
import vazkii.psi.common.spell.operator.vector.*;
import vazkii.psi.common.spell.other.PieceConnector;
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

public final class ModSpellPieces {

	public static PieceContainer selectorCaster;
	public static PieceContainer selectorFocalPoint;
	public static PieceContainer selectorNearbyItems;
	public static PieceContainer selectorNearbyLiving;
	public static PieceContainer selectorNearbyEnemies;
	public static PieceContainer selectorNearbyAnimals;
	public static PieceContainer selectorNearbyProjectiles;
	public static PieceContainer selectorNearbySmeltables;
	public static PieceContainer selectorLoopcastIndex;
	public static PieceContainer selectorBlockBroken;
	public static PieceContainer selectorBlockSideBroken;
	public static PieceContainer selectorAttackTarget;
	public static PieceContainer selectorSneakStatus;
	public static PieceContainer selectorTime;
	public static PieceContainer selectorAttacker;
	public static PieceContainer selectorDamageTaken;
	public static PieceContainer selectorRulerVector;
	public static PieceContainer selectorItemPresence;
	public static PieceContainer selectorBlockPresence;
	public static PieceContainer selectorSaveVector;
	public static PieceContainer selectorEidosChangelog;

	public static PieceContainer operatorSum;
	public static PieceContainer operatorSubtract;
	public static PieceContainer operatorMultiply;
	public static PieceContainer operatorDivide;
	public static PieceContainer operatorAbsolute;
	public static PieceContainer operatorInverse;
	public static PieceContainer operatorModulus;
	public static PieceContainer operatorRandom;
	public static PieceContainer operatorIntegerDivide;
	public static PieceContainer operatorSin;
	public static PieceContainer operatorCos;
	public static PieceContainer operatorAsin;
	public static PieceContainer operatorAcos;
	public static PieceContainer operatorMin;
	public static PieceContainer operatorMax;
	public static PieceContainer operatorSquare;
	public static PieceContainer operatorCube;
	public static PieceContainer operatorPower;
	public static PieceContainer operatorSquareRoot;
	public static PieceContainer operatorLog;
	public static PieceContainer operatorCeiling;
	public static PieceContainer operatorFloor;
	public static PieceContainer operatorRound;
	public static PieceContainer operatorEntityPosition;
	public static PieceContainer operatorEntityLook;
	public static PieceContainer operatorEntityMotion;
	public static PieceContainer operatorClosestToPoint;
	public static PieceContainer operatorRandomEntity;
	public static PieceContainer operatorFocusedEntity;
	public static PieceContainer operatorListAdd;
	public static PieceContainer operatorListRemove;
	public static PieceContainer operatorVectorRaycast;
	public static PieceContainer operatorVectorSum;
	public static PieceContainer operatorVectorSubtract;
	public static PieceContainer operatorVectorMultiply;
	public static PieceContainer operatorVectorDivide;
	public static PieceContainer operatorVectorCrossProduct;
	public static PieceContainer operatorVectorNormalize;
	public static PieceContainer operatorVectorNegate;
	public static PieceContainer operatorVectorMagnitude;
	public static PieceContainer operatorVectorConstruct;
	public static PieceContainer operatorVectorExtractX;
	public static PieceContainer operatorVectorExtractY;
	public static PieceContainer operatorVectorExtractZ;
	public static PieceContainer operatorVectorRaycastAxis;
	public static PieceContainer operatorVectorProject;
	public static PieceContainer operatorVectorDotProduct;

	public static PieceContainer constantNumber;
	public static PieceContainer constantPi;
	public static PieceContainer constantE;
	public static PieceContainer constantWrapper;

	public static PieceContainer connector;
	public static PieceContainer errorSuppressor;

	public static PieceContainer trickDebug;
	public static PieceContainer trickDelay;
	public static PieceContainer trickDie;
	public static PieceContainer trickEvaluate;
	public static PieceContainer trickAddMotion;
	public static PieceContainer trickExplode;
	public static PieceContainer trickBreakBlock;
	public static PieceContainer trickBreakInSequence;
	public static PieceContainer trickPlaceBlock;
	public static PieceContainer trickPlaceInSequence;
	public static PieceContainer trickInfusion;
	public static PieceContainer trickBlink;
	public static PieceContainer trickMassBlink;
	public static PieceContainer trickMassAddMotion;
	public static PieceContainer trickMassExodus;
	public static PieceContainer trickMoveBlock;
	public static PieceContainer trickCollapseBlock;
	public static PieceContainer trickSmite;
	public static PieceContainer trickBlaze;
	public static PieceContainer trickTorrent;
	public static PieceContainer trickOvergrow;
	public static PieceContainer trickGreaterInfusion;
	public static PieceContainer trickEbonyIvory;
	public static PieceContainer trickSpeed;
	public static PieceContainer trickHaste;
	public static PieceContainer trickStrength;
	public static PieceContainer trickJumpBoost;
	public static PieceContainer trickWaterBreathing;
	public static PieceContainer trickFireResistance;
	public static PieceContainer trickInvisibility;
	public static PieceContainer trickRegeneration;
	public static PieceContainer trickResistance;
	public static PieceContainer trickSlowness;
	public static PieceContainer trickWeakness;
	public static PieceContainer trickWither;
	public static PieceContainer trickIgnite;
	public static PieceContainer trickEidosAnchor;
	public static PieceContainer trickEidosReversal;
	public static PieceContainer trickSmeltBlock;
	public static PieceContainer trickSmeltItem;
	public static PieceContainer trickConjureBlock;
	public static PieceContainer trickConjureLight;
	public static PieceContainer trickConjureBlockSequence;
	public static PieceContainer trickSwitchTargetSlot;
	public static PieceContainer trickSaveVector;

	public static void init() {
		selectorCaster = register(PieceSelectorCaster.class, LibPieceNames.SELECTOR_CASTER, LibPieceGroups.TUTORIAL_1);
		selectorFocalPoint = register(PieceSelectorFocalPoint.class, LibPieceNames.SELECTOR_FOCAL_POINT, LibPieceGroups.PROJECTILES, true);
		selectorNearbyItems = register(PieceSelectorNearbyItems.class, LibPieceNames.SELECTOR_NEARBY_ITEMS, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyLiving = register(PieceSelectorNearbyLiving.class, LibPieceNames.SELECTOR_NEARBY_LIVING, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyEnemies = register(PieceSelectorNearbyEnemies.class, LibPieceNames.SELECTOR_NEARBY_ENEMIES, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyAnimals = register(PieceSelectorNearbyAnimals.class, LibPieceNames.SELECTOR_NEARBY_ANIMALS, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyProjectiles = register(PieceSelectorNearbyProjectiles.class, LibPieceNames.SELECTOR_NEARBY_PROJECTILES, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbySmeltables = register(PieceSelectorNearbySmeltables.class, LibPieceNames.SELECTOR_NEARBY_SMELTABLES, LibPieceGroups.SMELTERY);
		selectorLoopcastIndex = register(PieceSelectorLoopcastIndex.class, LibPieceNames.SELECTOR_LOOPCAST_INDEX, LibPieceGroups.LOOPCASTING, true);
		selectorBlockBroken = register(PieceSelectorBlockBroken.class, LibPieceNames.SELECTOR_BLOCK_BROKEN, LibPieceGroups.TOOL_CASTING, true);
		selectorBlockSideBroken = register(PieceSelectorBlockSideBroken.class, LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN, LibPieceGroups.TOOL_CASTING);
		selectorAttackTarget = register(PieceSelectorAttackTarget.class, LibPieceNames.SELECTOR_ATTACK_TARGET, LibPieceGroups.TOOL_CASTING);
		selectorSneakStatus = register(PieceSelectorSneakStatus.class, LibPieceNames.SELECTOR_SNEAK_STATUS, LibPieceGroups.FLOW_CONTROL);
		selectorTime = register(PieceSelectorTime.class, LibPieceNames.SELECTOR_TIME, LibPieceGroups.EXOSUIT_CASTING, true);
		selectorAttacker = register(PieceSelectorAttacker.class, LibPieceNames.SELECTOR_ATTACKER, LibPieceGroups.EXOSUIT_CASTING);
		selectorDamageTaken = register(PieceSelectorDamageTaken.class, LibPieceNames.SELECTOR_DAMAGE_TAKEN, LibPieceGroups.EXOSUIT_CASTING);
		selectorRulerVector = register(PieceSelectorRulerVector.class, LibPieceNames.SELECTOR_RULER_VECTOR, LibPieceGroups.PROJECTILES);
		selectorItemPresence = register(PieceSelectorItemPresence.class, LibPieceNames.SELECTOR_ITEM_PRESENCE, LibPieceGroups.DETECTION_DYNAMICS);
		selectorBlockPresence = register(PieceSelectorBlockPresence.class, LibPieceNames.SELECTOR_BLOCK_PRESENCE, LibPieceGroups.DETECTION_DYNAMICS);
		selectorSaveVector = register(PieceSelectorSavedVector.class, LibPieceNames.SELECTOR_SAVED_VECTOR, LibPieceGroups.MEMORY_MANAGEMENT);
		selectorEidosChangelog = register(PieceSelectorEidosChangelog.class, LibPieceNames.SELECTOR_EIDOS_CHANGELOG, LibPieceGroups.EIDOS_REVERSAL);

		operatorSum = register(PieceOperatorSum.class, LibPieceNames.OPERATOR_SUM, LibPieceGroups.NUMBERS_INTRO, true);
		operatorSubtract = register(PieceOperatorSubtract.class, LibPieceNames.OPERATOR_SUBTRACT, LibPieceGroups.NUMBERS_INTRO);
		operatorMultiply = register(PieceOperatorMultiply.class, LibPieceNames.OPERATOR_MULTIPLY, LibPieceGroups.NUMBERS_INTRO);
		operatorDivide = register(PieceOperatorDivide.class, LibPieceNames.OPERATOR_DIVIDE, LibPieceGroups.NUMBERS_INTRO);
		operatorAbsolute = register(PieceOperatorAbsolute.class, LibPieceNames.OPERATOR_ABSOLUTE, LibPieceGroups.NUMBERS_INTRO);
		operatorInverse = register(PieceOperatorInverse.class, LibPieceNames.OPERATOR_INVERSE, LibPieceGroups.NUMBERS_INTRO);
		operatorModulus = register(PieceOperatorModulus.class, LibPieceNames.OPERATOR_MODULUS, LibPieceGroups.LOOPCASTING);
		operatorRandom = register(PieceOperatorRandom.class, LibPieceNames.OPERATOR_RANDOM, LibPieceGroups.ELEMENTAL_ARTS);
		operatorIntegerDivide = register(PieceOperatorIntegerDivide.class, LibPieceNames.OPERATOR_INTEGER_DIVIDE, LibPieceGroups.LOOPCASTING);
		operatorSin = register(PieceOperatorSin.class, LibPieceNames.OPERATOR_SIN, LibPieceGroups.TRIGNOMETRY);
		operatorCos = register(PieceOperatorCos.class, LibPieceNames.OPERATOR_COS, LibPieceGroups.TRIGNOMETRY);
		operatorAsin = register(PieceOperatorAsin.class, LibPieceNames.OPERATOR_ASIN, LibPieceGroups.TRIGNOMETRY);
		operatorAcos = register(PieceOperatorAcos.class, LibPieceNames.OPERATOR_ACOS, LibPieceGroups.TRIGNOMETRY);
		operatorMin = register(PieceOperatorMin.class, LibPieceNames.OPERATOR_MIN, LibPieceGroups.SECONDARY_OPERATORS);
		operatorMax = register(PieceOperatorMax.class, LibPieceNames.OPERATOR_MAX, LibPieceGroups.SECONDARY_OPERATORS);
		operatorSquare = register(PieceOperatorSquare.class, LibPieceNames.OPERATOR_SQUARE, LibPieceGroups.SECONDARY_OPERATORS, true);
		operatorCube = register(PieceOperatorCube.class, LibPieceNames.OPERATOR_CUBE, LibPieceGroups.SECONDARY_OPERATORS);
		operatorPower = register(PieceOperatorPower.class, LibPieceNames.OPERATOR_POWER, LibPieceGroups.SECONDARY_OPERATORS);
		operatorSquareRoot = register(PieceOperatorSquareRoot.class, LibPieceNames.OPERATOR_SQUARE_ROOT, LibPieceGroups.SECONDARY_OPERATORS);
		operatorLog = register(PieceOperatorLog.class, LibPieceNames.OPERATOR_LOG, LibPieceGroups.SECONDARY_OPERATORS);
		operatorCeiling = register(PieceOperatorCeiling.class, LibPieceNames.OPERATOR_CEILING, LibPieceGroups.SECONDARY_OPERATORS);
		operatorFloor = register(PieceOperatorFloor.class, LibPieceNames.OPERATOR_FLOOR, LibPieceGroups.SECONDARY_OPERATORS);
		operatorRound = register(PieceOperatorRound.class, LibPieceNames.OPERATOR_ROUND, LibPieceGroups.SECONDARY_OPERATORS);
		operatorEntityPosition = register(PieceOperatorEntityPosition.class, LibPieceNames.OPERATOR_ENTITY_POSITION, LibPieceGroups.TUTORIAL_4);
		operatorEntityLook = register(PieceOperatorEntityLook.class, LibPieceNames.OPERATOR_ENTITY_LOOK, LibPieceGroups.TUTORIAL_3);
		operatorEntityMotion = register(PieceOperatorEntityMotion.class, LibPieceNames.OPERATOR_ENTITY_MOTION, LibPieceGroups.ENTITIES_INTRO);
		operatorEntityMotion = register(PieceOperatorEntityAxialLook.class, LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK, LibPieceGroups.BLOCK_WORKS);
		operatorClosestToPoint = register(PieceOperatorClosestToPoint.class, LibPieceNames.OPERATOR_CLOSEST_TO_POINT, LibPieceGroups.ENTITIES_INTRO, true);
		operatorRandomEntity = register(PieceOperatorRandomEntity.class, LibPieceNames.OPERATOR_RANDOM_ENTITY, LibPieceGroups.ENTITIES_INTRO);
		operatorFocusedEntity = register(PieceOperatorFocusedEntity.class, LibPieceNames.OPERATOR_FOCUSED_ENTITY, LibPieceGroups.ENTITIES_INTRO);
		operatorListAdd = register(PieceOperatorListAdd.class, LibPieceNames.OPERATOR_LIST_ADD, LibPieceGroups.ENTITIES_INTRO);
		operatorListRemove = register(PieceOperatorListRemove.class, LibPieceNames.OPERATOR_LIST_REMOVE, LibPieceGroups.ENTITIES_INTRO);
		operatorVectorRaycast = register(PieceOperatorVectorRaycast.class, LibPieceNames.OPERATOR_VECTOR_RAYCAST, LibPieceGroups.TUTORIAL_4);
		operatorVectorSum = register(PieceOperatorVectorSum.class, LibPieceNames.OPERATOR_VECTOR_SUM, LibPieceGroups.VECTORS_INTRO);
		operatorVectorSubtract = register(PieceOperatorVectorSubtract.class, LibPieceNames.OPERATOR_VECTOR_SUBTRACT, LibPieceGroups.VECTORS_INTRO);
		operatorVectorMultiply = register(PieceOperatorVectorMultiply.class, LibPieceNames.OPERATOR_VECTOR_MULTIPLY, LibPieceGroups.VECTORS_INTRO);
		operatorVectorDivide = register(PieceOperatorVectorDivide.class, LibPieceNames.OPERATOR_VECTOR_DIVIDE, LibPieceGroups.VECTORS_INTRO);
		operatorVectorCrossProduct = register(PieceOperatorVectorCrossProduct.class, LibPieceNames.OPERATOR_VECTOR_CROSS_PRODUCT, LibPieceGroups.VECTORS_INTRO);
		operatorVectorNormalize = register(PieceOperatorVectorNormalize.class, LibPieceNames.OPERATOR_VECTOR_NORMALIZE, LibPieceGroups.VECTORS_INTRO);
		operatorVectorNegate = register(PieceOperatorVectorNegate.class, LibPieceNames.OPERATOR_VECTOR_NEGATE, LibPieceGroups.VECTORS_INTRO);
		operatorVectorMagnitude = register(PieceOperatorVectorMagnitude.class, LibPieceNames.OPERATOR_VECTOR_MAGNITUDE, LibPieceGroups.VECTORS_INTRO);
		operatorVectorConstruct = register(PieceOperatorVectorConstruct.class, LibPieceNames.OPERATOR_VECTOR_CONSTRUCT, LibPieceGroups.VECTORS_INTRO, true);
		operatorVectorExtractX = register(PieceOperatorVectorExtractX.class, LibPieceNames.OPERATOR_VECTOR_EXTRACT_X, LibPieceGroups.VECTORS_INTRO);
		operatorVectorExtractY = register(PieceOperatorVectorExtractY.class, LibPieceNames.OPERATOR_VECTOR_EXTRACT_Y, LibPieceGroups.VECTORS_INTRO);
		operatorVectorExtractZ = register(PieceOperatorVectorExtractZ.class, LibPieceNames.OPERATOR_VECTOR_EXTRACT_Z, LibPieceGroups.VECTORS_INTRO);
		operatorVectorRaycastAxis = register(PieceOperatorVectorRaycastAxis.class, LibPieceNames.OPERATOR_VECTOR_RAYCAST_AXIS, LibPieceGroups.BLOCK_WORKS);
		operatorVectorProject = register(PieceOperatorVectorProject.class, LibPieceNames.OPERATOR_VECTOR_PROJECT, LibPieceGroups.BLOCK_WORKS);
		operatorVectorDotProduct = register(PieceOperatorVectorDotProduct.class, LibPieceNames.OPERATOR_VECTOR_DOT_PRODUCT, LibPieceGroups.TRIGNOMETRY);

		constantNumber = register(PieceConstantNumber.class, LibPieceNames.CONSTANT_NUMBER, LibPieceGroups.TUTORIAL_2, true);
		constantPi = register(PieceConstantPi.class, LibPieceNames.CONSTANT_PI, LibPieceGroups.TRIGNOMETRY, true);
		constantE = register(PieceConstantE.class, LibPieceNames.CONSTANT_E, LibPieceGroups.SECONDARY_OPERATORS);

		constantWrapper = register(PieceConstantWrapper.class, LibPieceNames.CONSTANT_WRAPPER, LibPieceGroups.FLOW_CONTROL);

		connector = register(PieceConnector.class, LibPieceNames.CONNECTOR, LibPieceGroups.TUTORIAL_2);
		errorSuppressor = register(PieceErrorSuppressor.class, LibPieceNames.ERROR_SUPPRESSOR, LibPieceGroups.TUTORIAL_4);

		trickDebug = register(PieceTrickDebug.class, LibPieceNames.TRICK_DEBUG, LibPieceGroups.TUTORIAL_1, true);
		trickDelay = register(PieceTrickDelay.class, LibPieceNames.TRICK_DELAY, LibPieceGroups.FLOW_CONTROL, true);
		trickDie = register(PieceTrickDie.class, LibPieceNames.TRICK_DIE, LibPieceGroups.FLOW_CONTROL);
		trickEvaluate = register(PieceTrickEvaluate.class, LibPieceNames.TRICK_EVALUATE, LibPieceGroups.FLOW_CONTROL);
		trickAddMotion = register(PieceTrickAddMotion.class, LibPieceNames.TRICK_ADD_MOTION, LibPieceGroups.TUTORIAL_3, true);
		trickExplode = register(PieceTrickExplode.class, LibPieceNames.TRICK_EXPLODE, LibPieceGroups.TUTORIAL_4, true);
		trickBreakBlock = register(PieceTrickBreakBlock.class, LibPieceNames.TRICK_BREAK_BLOCK, LibPieceGroups.BLOCK_WORKS);
		trickBreakInSequence = register(PieceTrickBreakInSequence.class, LibPieceNames.TRICK_BREAK_IN_SEQUENCE, LibPieceGroups.BLOCK_WORKS, true);
		trickPlaceBlock = register(PieceTrickPlaceBlock.class, LibPieceNames.TRICK_PLACE_BLOCK, LibPieceGroups.BLOCK_WORKS);
		trickPlaceInSequence = register(PieceTrickPlaceInSequence.class, LibPieceNames.TRICK_PLACE_IN_SEQUENCE, LibPieceGroups.BLOCK_WORKS);
		trickInfusion = register(PieceTrickInfusion.class, LibPieceNames.TRICK_INFUSION, LibPieceGroups.INFUSION, true);
		trickBlink = register(PieceTrickBlink.class, LibPieceNames.TRICK_BLINK, LibPieceGroups.MOVEMENT, true);
		trickMassBlink = register(PieceTrickMassBlink.class, LibPieceNames.TRICK_MASS_BLINK, LibPieceGroups.MOVEMENT);
		trickMassAddMotion = register(PieceTrickMassAddMotion.class, LibPieceNames.TRICK_MASS_ADD_MOTION, LibPieceGroups.MOVEMENT);
		trickMassExodus = register(PieceTrickMassExodus.class, LibPieceNames.TRICK_MASS_EXODUS, LibPieceGroups.MOVEMENT);
		trickMoveBlock = register(PieceTrickMoveBlock.class, LibPieceNames.TRICK_MOVE_BLOCK, LibPieceGroups.BLOCK_MOVEMENT, true);
		trickCollapseBlock = register(PieceTrickCollapseBlock.class, LibPieceNames.TRICK_COLLAPSE_BLOCK, LibPieceGroups.BLOCK_MOVEMENT);
		trickSmite = register(PieceTrickSmite.class, LibPieceNames.TRICK_SMITE, LibPieceGroups.ELEMENTAL_ARTS, true);
		trickBlaze = register(PieceTrickBlaze.class, LibPieceNames.TRICK_BLAZE, LibPieceGroups.ELEMENTAL_ARTS);
		trickTorrent = register(PieceTrickTorrent.class, LibPieceNames.TRICK_TORRENT, LibPieceGroups.ELEMENTAL_ARTS);
		trickOvergrow = register(PieceTrickOvergrow.class, LibPieceNames.TRICK_OVERGROW, LibPieceGroups.ELEMENTAL_ARTS);
		trickGreaterInfusion = register(PieceTrickGreaterInfusion.class, LibPieceNames.TRICK_GREATER_INFUSION, LibPieceGroups.GREATER_INFUSION, true);
		trickEbonyIvory = register(PieceTrickEbonyIvory.class, LibPieceNames.TRICK_EBONY_IVORY, LibPieceGroups.GREATER_INFUSION);
		trickSpeed = register(PieceTrickSpeed.class, LibPieceNames.TRICK_SPEED, LibPieceGroups.POSITIVE_EFFECTS, true);
		trickHaste = register(PieceTrickHaste.class, LibPieceNames.TRICK_HASTE, LibPieceGroups.POSITIVE_EFFECTS);
		trickStrength = register(PieceTrickStrength.class, LibPieceNames.TRICK_STRENGTH, LibPieceGroups.POSITIVE_EFFECTS);
		trickJumpBoost = register(PieceTrickJumpBoost.class, LibPieceNames.TRICK_JUMP_BOOST, LibPieceGroups.POSITIVE_EFFECTS);
		trickWaterBreathing = register(PieceTrickWaterBreathing.class, LibPieceNames.TRICK_WATER_BREATHING, LibPieceGroups.POSITIVE_EFFECTS);
		trickFireResistance = register(PieceTrickFireResistance.class, LibPieceNames.TRICK_FIRE_RESISTANCE, LibPieceGroups.POSITIVE_EFFECTS);
		trickInvisibility = register(PieceTrickInvisibility.class, LibPieceNames.TRICK_INVISIBILITY, LibPieceGroups.POSITIVE_EFFECTS);
		trickRegeneration = register(PieceTrickRegeneration.class, LibPieceNames.TRICK_REGENERATION, LibPieceGroups.POSITIVE_EFFECTS);
		trickResistance = register(PieceTrickResistance.class, LibPieceNames.TRICK_RESISTANCE, LibPieceGroups.POSITIVE_EFFECTS);
		trickSlowness = register(PieceTrickSlowness.class, LibPieceNames.TRICK_SLOWNESS, LibPieceGroups.NEGATIVE_EFFECTS);
		trickWeakness = register(PieceTrickWeakness.class, LibPieceNames.TRICK_WEAKNESS, LibPieceGroups.NEGATIVE_EFFECTS);
		trickWither = register(PieceTrickWither.class, LibPieceNames.TRICK_WITHER, LibPieceGroups.NEGATIVE_EFFECTS, true);
		trickIgnite = register(PieceTrickIgnite.class, LibPieceNames.TRICK_IGNITE, LibPieceGroups.NEGATIVE_EFFECTS);
		trickEidosAnchor = register(PieceTrickEidosAnchor.class, LibPieceNames.TRICK_EIDOS_ANCHOR, LibPieceGroups.EIDOS_REVERSAL);
		trickEidosReversal = register(PieceTrickEidosReversal.class, LibPieceNames.TRICK_EIDOS_REVERSAL, LibPieceGroups.EIDOS_REVERSAL, true);
		trickSmeltBlock = register(PieceTrickSmeltBlock.class, LibPieceNames.TRICK_SMELT_BLOCK, LibPieceGroups.SMELTERY);
		trickSmeltItem = register(PieceTrickSmeltItem.class, LibPieceNames.TRICK_SMELT_ITEM, LibPieceGroups.SMELTERY, true);
		trickConjureBlock = register(PieceTrickConjureBlock.class, LibPieceNames.TRICK_CONJURE_BLOCK, LibPieceGroups.BLOCK_CONJURATION, true);
		trickConjureLight = register(PieceTrickConjureLight.class, LibPieceNames.TRICK_CONJURE_LIGHT, LibPieceGroups.BLOCK_CONJURATION);
		trickConjureBlockSequence = register(PieceTrickConjureBlockSequence.class, LibPieceNames.TRICK_CONJURE_BLOCK_SEQUENCE, LibPieceGroups.BLOCK_CONJURATION);
		trickSwitchTargetSlot = register(PieceTrickSwitchTargetSlot.class, LibPieceNames.TRICK_SWITCH_TARGET_SLOT, LibPieceGroups.DETECTION_DYNAMICS, true);
		trickSaveVector = register(PieceTrickSaveVector.class, LibPieceNames.TRICK_SAVE_VECTOR, LibPieceGroups.MEMORY_MANAGEMENT, true);
	}

	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group) {
		return register(clazz, name, group, false);
	}

	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group, boolean main) {
		PsiAPI.registerSpellPieceAndTexture(new ResourceLocation("psi", name), clazz);
		PsiAPI.addPieceToGroup(clazz, new ResourceLocation("psi", group), main);
		return (Spell s) -> SpellPiece.create(clazz, s);
	}

	public interface PieceContainer {
		SpellPiece get(Spell s);
	}

}
