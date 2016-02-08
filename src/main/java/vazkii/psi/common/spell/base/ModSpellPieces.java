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

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.spell.constant.PieceConstantNumber;
import vazkii.psi.common.spell.operator.entity.PieceOperatorClosestToPoint;
import vazkii.psi.common.spell.operator.entity.PieceOperatorEntityAxialLook;
import vazkii.psi.common.spell.operator.entity.PieceOperatorEntityLook;
import vazkii.psi.common.spell.operator.entity.PieceOperatorEntityMotion;
import vazkii.psi.common.spell.operator.entity.PieceOperatorEntityPosition;
import vazkii.psi.common.spell.operator.entity.PieceOperatorRandomEntity;
import vazkii.psi.common.spell.operator.number.PieceOperatorAbsolute;
import vazkii.psi.common.spell.operator.number.PieceOperatorDivide;
import vazkii.psi.common.spell.operator.number.PieceOperatorIntegerDivide;
import vazkii.psi.common.spell.operator.number.PieceOperatorInverse;
import vazkii.psi.common.spell.operator.number.PieceOperatorModulus;
import vazkii.psi.common.spell.operator.number.PieceOperatorMultiply;
import vazkii.psi.common.spell.operator.number.PieceOperatorRandom;
import vazkii.psi.common.spell.operator.number.PieceOperatorSubtract;
import vazkii.psi.common.spell.operator.number.PieceOperatorSum;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorConstruct;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorCrossProduct;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorDivide;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorExtractX;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorExtractY;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorExtractZ;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorMagnitude;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorMultiply;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorNegate;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorNormalize;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorProject;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycastAxis;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorSubtract;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorSum;
import vazkii.psi.common.spell.other.PieceConnector;
import vazkii.psi.common.spell.other.PieceErrorSuppressor;
import vazkii.psi.common.spell.selector.PieceSelectorAttackTarget;
import vazkii.psi.common.spell.selector.PieceSelectorBlockBroken;
import vazkii.psi.common.spell.selector.PieceSelectorBlockSideBroken;
import vazkii.psi.common.spell.selector.PieceSelectorCaster;
import vazkii.psi.common.spell.selector.PieceSelectorFocalPoint;
import vazkii.psi.common.spell.selector.PieceSelectorLoopcastIndex;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbyAnimals;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbyEnemies;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbyItems;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbyLiving;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearbyProjectiles;
import vazkii.psi.common.spell.trick.PieceTrickBlaze;
import vazkii.psi.common.spell.trick.PieceTrickDebug;
import vazkii.psi.common.spell.trick.PieceTrickExplode;
import vazkii.psi.common.spell.trick.PieceTrickOvergrow;
import vazkii.psi.common.spell.trick.PieceTrickSmite;
import vazkii.psi.common.spell.trick.PieceTrickTorrent;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakInSequence;
import vazkii.psi.common.spell.trick.block.PieceTrickCollapseBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickMoveBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock;
import vazkii.psi.common.spell.trick.block.PieceTrickPlaceInSequence;
import vazkii.psi.common.spell.trick.entity.PieceTrickAddMotion;
import vazkii.psi.common.spell.trick.entity.PieceTrickBlink;
import vazkii.psi.common.spell.trick.entity.PieceTrickMassAddMotion;
import vazkii.psi.common.spell.trick.entity.PieceTrickMassBlink;
import vazkii.psi.common.spell.trick.entity.PieceTrickMassExodus;
import vazkii.psi.common.spell.trick.infusion.PieceTrickEbonyIvory;
import vazkii.psi.common.spell.trick.infusion.PieceTrickGreaterInfusion;
import vazkii.psi.common.spell.trick.infusion.PieceTrickInfusion;
import vazkii.psi.common.spell.trick.potion.PieceTrickFireResistance;
import vazkii.psi.common.spell.trick.potion.PieceTrickHaste;
import vazkii.psi.common.spell.trick.potion.PieceTrickInvisibility;
import vazkii.psi.common.spell.trick.potion.PieceTrickJumpBoost;
import vazkii.psi.common.spell.trick.potion.PieceTrickRegeneration;
import vazkii.psi.common.spell.trick.potion.PieceTrickResistance;
import vazkii.psi.common.spell.trick.potion.PieceTrickSpeed;
import vazkii.psi.common.spell.trick.potion.PieceTrickStrength;
import vazkii.psi.common.spell.trick.potion.PieceTrickWaterBreathing;

public final class ModSpellPieces {

	public static PieceContainer selectorCaster;
	public static PieceContainer selectorFocalPoint;
	public static PieceContainer selectorNearbyItems;
	public static PieceContainer selectorNearbyLiving;
	public static PieceContainer selectorNearbyEnemies;
	public static PieceContainer selectorNearbyAnimals;
	public static PieceContainer selectorNearbyProjectiles;
	public static PieceContainer selectorLoopcastIndex;
	public static PieceContainer selectorBlockBroken;
	public static PieceContainer selectorBlockSideBroken;
	public static PieceContainer selectorAttackTarget;

	public static PieceContainer operatorSum;
	public static PieceContainer operatorSubtract;
	public static PieceContainer operatorMultiply;
	public static PieceContainer operatorDivide;
	public static PieceContainer operatorAbsolute;
	public static PieceContainer operatorInverse;
	public static PieceContainer operatorModulus;
	public static PieceContainer operatorRandom;
	public static PieceContainer operatorIntegerDivide;
	public static PieceContainer operatorEntityPosition;
	public static PieceContainer operatorEntityLook;
	public static PieceContainer operatorEntityMotion;
	public static PieceContainer operatorClosestToPoint;
	public static PieceContainer operatorRandomEntity;
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

	public static PieceContainer constantNumber;
	
	public static PieceContainer connector;
	public static PieceContainer errorSuppressor;

	public static PieceContainer trickDebug;
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

	public static void init() {
		selectorCaster = register(PieceSelectorCaster.class, LibPieceNames.SELECTOR_CASTER, LibPieceGroups.TUTORIAL_1);
		selectorFocalPoint = register(PieceSelectorFocalPoint.class, LibPieceNames.SELECTOR_FOCAL_POINT, LibPieceGroups.PROJECTILES, true);
		selectorNearbyItems = register(PieceSelectorNearbyItems.class, LibPieceNames.SELECTOR_NEARBY_ITEMS, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyLiving = register(PieceSelectorNearbyLiving.class, LibPieceNames.SELECTOR_NEARBY_LIVING, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyEnemies = register(PieceSelectorNearbyEnemies.class, LibPieceNames.SELECTOR_NEARBY_ENEMIES, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyAnimals = register(PieceSelectorNearbyAnimals.class, LibPieceNames.SELECTOR_NEARBY_ANIMALS, LibPieceGroups.ENTITIES_INTRO);
		selectorNearbyProjectiles = register(PieceSelectorNearbyProjectiles.class, LibPieceNames.SELECTOR_NEARBY_PROJECTILES, LibPieceGroups.ENTITIES_INTRO);
		selectorLoopcastIndex = register(PieceSelectorLoopcastIndex.class, LibPieceNames.SELECTOR_LOOPCAST_INDEX, LibPieceGroups.LOOPCASTING, true);
		selectorBlockBroken = register(PieceSelectorBlockBroken.class, LibPieceNames.SELECTOR_BLOCK_BROKEN, LibPieceGroups.TOOL_CASTING, true);
		selectorBlockSideBroken = register(PieceSelectorBlockSideBroken.class, LibPieceNames.SELECTOR_BLOCK_SIDE_BROKEN, LibPieceGroups.TOOL_CASTING);
		selectorAttackTarget = register(PieceSelectorAttackTarget.class, LibPieceNames.SELECTOR_ATTACK_TARGET, LibPieceGroups.TOOL_CASTING);
		
		operatorSum = register(PieceOperatorSum.class, LibPieceNames.OPERATOR_SUM, LibPieceGroups.NUMBERS_INTRO, true);
		operatorSubtract = register(PieceOperatorSubtract.class, LibPieceNames.OPERATOR_SUBTRACT, LibPieceGroups.NUMBERS_INTRO);
		operatorMultiply = register(PieceOperatorMultiply.class, LibPieceNames.OPERATOR_MULTIPLY, LibPieceGroups.NUMBERS_INTRO);
		operatorDivide = register(PieceOperatorDivide.class, LibPieceNames.OPERATOR_DIVIDE, LibPieceGroups.NUMBERS_INTRO);
		operatorAbsolute = register(PieceOperatorAbsolute.class, LibPieceNames.OPERATOR_ABSOLUTE, LibPieceGroups.NUMBERS_INTRO);
		operatorInverse = register(PieceOperatorInverse.class, LibPieceNames.OPERATOR_INVERSE, LibPieceGroups.NUMBERS_INTRO);
		operatorModulus = register(PieceOperatorModulus.class, LibPieceNames.OPERATOR_MODULUS, LibPieceGroups.LOOPCASTING);
		operatorRandom = register(PieceOperatorRandom.class, LibPieceNames.OPERATOR_RANDOM, LibPieceGroups.ELEMENTAL_ARTS);
		operatorIntegerDivide = register(PieceOperatorIntegerDivide.class, LibPieceNames.OPERATOR_INTEGER_DIVIDE, LibPieceGroups.LOOPCASTING);
		operatorEntityPosition = register(PieceOperatorEntityPosition.class, LibPieceNames.OPERATOR_ENTITY_POSITION, LibPieceGroups.TUTORIAL_4);
		operatorEntityLook = register(PieceOperatorEntityLook.class, LibPieceNames.OPERATOR_ENTITY_LOOK, LibPieceGroups.TUTORIAL_3);
		operatorEntityMotion = register(PieceOperatorEntityMotion.class, LibPieceNames.OPERATOR_ENTITY_MOTION, LibPieceGroups.ENTITIES_INTRO);
		operatorEntityMotion = register(PieceOperatorEntityAxialLook.class, LibPieceNames.OPERATOR_ENTITY_AXIAL_LOOK, LibPieceGroups.BLOCK_WORKS);
		operatorClosestToPoint = register(PieceOperatorClosestToPoint.class, LibPieceNames.OPERATOR_CLOSEST_TO_POINT, LibPieceGroups.ENTITIES_INTRO, true);
		operatorRandomEntity = register(PieceOperatorRandomEntity.class, LibPieceNames.OPERATOR_RANDOM_ENTITY, LibPieceGroups.ENTITIES_INTRO);
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

		constantNumber = register(PieceConstantNumber.class, LibPieceNames.CONSTANT_NUMBER, LibPieceGroups.TUTORIAL_2, true);
		
		connector = register(PieceConnector.class, LibPieceNames.CONNECTOR, LibPieceGroups.TUTORIAL_2);
		errorSuppressor = register(PieceErrorSuppressor.class, LibPieceNames.ERROR_SUPPRESSOR, LibPieceGroups.TUTORIAL_4);
		
		trickDebug = register(PieceTrickDebug.class, LibPieceNames.TRICK_DEBUG, LibPieceGroups.TUTORIAL_1, true);
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

		PsiAPI.setGroupRequirements(LibPieceGroups.TUTORIAL_1, 1);
		PsiAPI.setGroupRequirements(LibPieceGroups.TUTORIAL_2, 2, LibPieceGroups.TUTORIAL_1);
		PsiAPI.setGroupRequirements(LibPieceGroups.TUTORIAL_3, 3, LibPieceGroups.TUTORIAL_2);
		PsiAPI.setGroupRequirements(LibPieceGroups.TUTORIAL_4, 4, LibPieceGroups.TUTORIAL_3);
		PsiAPI.setGroupRequirements(LibPieceGroups.NUMBERS_INTRO, 5, LibPieceGroups.TUTORIAL_4);
		PsiAPI.setGroupRequirements(LibPieceGroups.VECTORS_INTRO, 5, LibPieceGroups.TUTORIAL_4);
		PsiAPI.setGroupRequirements(LibPieceGroups.ENTITIES_INTRO, 5, LibPieceGroups.TUTORIAL_4);
		PsiAPI.setGroupRequirements(LibPieceGroups.PROJECTILES, 6, LibPieceGroups.ENTITIES_INTRO);
		PsiAPI.setGroupRequirements(LibPieceGroups.BLOCK_WORKS, 6, LibPieceGroups.VECTORS_INTRO);
		PsiAPI.setGroupRequirements(LibPieceGroups.INFUSION, 10, LibPieceGroups.VECTORS_INTRO, LibPieceGroups.ENTITIES_INTRO, LibPieceGroups.NUMBERS_INTRO);
		PsiAPI.setGroupRequirements(LibPieceGroups.MOVEMENT, 11, LibPieceGroups.ENTITIES_INTRO);
		PsiAPI.setGroupRequirements(LibPieceGroups.BLOCK_MOVEMENT, 11, LibPieceGroups.BLOCK_WORKS);
		PsiAPI.setGroupRequirements(LibPieceGroups.ELEMENTAL_ARTS, 11, LibPieceGroups.VECTORS_INTRO);
		PsiAPI.setGroupRequirements(LibPieceGroups.LOOPCASTING, 12, LibPieceGroups.INFUSION);
		PsiAPI.setGroupRequirements(LibPieceGroups.GREATER_INFUSION, 15, LibPieceGroups.INFUSION);
		PsiAPI.setGroupRequirements(LibPieceGroups.TOOL_CASTING, 16, LibPieceGroups.GREATER_INFUSION);
		PsiAPI.setGroupRequirements(LibPieceGroups.POSITIVE_EFFECTS, 16, LibPieceGroups.GREATER_INFUSION);
	}
	
	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group) {
		return register(clazz, name, group, false);
	}
	
	public static PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group, boolean main) {
		PsiAPI.registerSpellPieceAndTexture(name, clazz);
		PsiAPI.addPieceToGroup(clazz, group, main);
		return (Spell s) -> { return SpellPiece.create(clazz, s); };
	}
	
	public static interface PieceContainer {
		public SpellPiece get(Spell s);
	}
	
}
