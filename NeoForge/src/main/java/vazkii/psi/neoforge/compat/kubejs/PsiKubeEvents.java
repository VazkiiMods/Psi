/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs;

import vazkii.psi.neoforge.compat.kubejs.event.AssembleCADKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.CADStatKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.CADTakeKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.DetonationKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.LoopcastEndKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.PieceExecutedKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.PieceGroupAdvancementCompleteKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.PieceKnowledgeKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.PostCADCraftKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.PreSpellCastKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.ProgrammerPopulateKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.PsiArmorKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.RegenPsiKubeEvent;
import vazkii.psi.neoforge.compat.kubejs.event.SpellCastKubeEvent;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface PsiKubeEvents {

	EventGroup GROUP = EventGroup.of("PsiEvents");

	EventHandler ASSEMBLE_CAD = GROUP.common("assembleCad", () -> AssembleCADKubeEvent.class).hasResult();
	EventHandler CAD_STAT = GROUP.common("cadStat", () -> CADStatKubeEvent.class).hasResult();
	EventHandler CAD_TAKE = GROUP.common("cadTake", () -> CADTakeKubeEvent.class).hasResult();
	EventHandler POST_CAD_CRAFT = GROUP.common("postCadCraft", () -> PostCADCraftKubeEvent.class).hasResult();
	EventHandler REGEN_PSI = GROUP.common("regenPsi", () -> RegenPsiKubeEvent.class).hasResult();
	EventHandler SPELL_CAST = GROUP.common("spellCast", () -> SpellCastKubeEvent.class).hasResult();
	EventHandler PRE_SPELL_CAST = GROUP.common("preSpellCast", () -> PreSpellCastKubeEvent.class).hasResult();
	EventHandler PIECE_EXECUTED = GROUP.common("pieceExecuted", () -> PieceExecutedKubeEvent.class).hasResult();
	EventHandler PIECE_KNOWLEDGE = GROUP.common("pieceKnowledge", () -> PieceKnowledgeKubeEvent.class).hasResult();
	EventHandler PIECE_GROUP_ADVANCEMENT_COMPLETE = GROUP.common("pieceGroupAdvancementComplete", () -> PieceGroupAdvancementCompleteKubeEvent.class).hasResult();
	EventHandler LOOPCAST_END = GROUP.common("loopcastEnd", () -> LoopcastEndKubeEvent.class).hasResult();
	EventHandler DETONATION = GROUP.common("detonation", () -> DetonationKubeEvent.class).hasResult();
	EventHandler PROGRAMMER_POPULATE = GROUP.common("programmerPopulate", () -> ProgrammerPopulateKubeEvent.class).hasResult();
	EventHandler PSI_ARMOR = GROUP.common("psiArmor", () -> PsiArmorKubeEvent.class).hasResult();

}
