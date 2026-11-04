/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.neoforge.compat.kubejs;

import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.util.thread.EffectiveSide;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.AssembleCADEvent;
import vazkii.psi.api.cad.CADStatEvent;
import vazkii.psi.api.cad.CADTakeEvent;
import vazkii.psi.api.cad.PostCADCraftEvent;
import vazkii.psi.api.cad.RegenPsiEvent;
import vazkii.psi.api.event.PsiEvent;
import vazkii.psi.api.event.PsiEvents;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.spell.EnumPieceType;
import vazkii.psi.api.spell.LoopcastEndEvent;
import vazkii.psi.api.spell.PieceExecutedEvent;
import vazkii.psi.api.spell.PieceGroupAdvancementComplete;
import vazkii.psi.api.spell.PieceKnowledgeEvent;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.SpellCastEvent;
import vazkii.psi.api.spell.detonator.DetonationEvent;
import vazkii.psi.api.spell.programmer.ProgrammerPopulateEvent;
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
import vazkii.psi.neoforge.compat.kubejs.piece.CraftingTrickPieceBuilder;
import vazkii.psi.neoforge.compat.kubejs.piece.OperatorPieceBuilder;
import vazkii.psi.neoforge.compat.kubejs.piece.TrickPieceBuilder;

import java.util.function.Function;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ScriptType;

public class PsiKubeJSPlugin implements KubeJSPlugin {

	@Override
	public void init() {
		bridge(AssembleCADEvent.class, PsiKubeEvents.ASSEMBLE_CAD, AssembleCADKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(CADStatEvent.class, PsiKubeEvents.CAD_STAT, CADStatKubeEvent::new, event -> currentSide());
		bridge(CADTakeEvent.class, PsiKubeEvents.CAD_TAKE, CADTakeKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(PostCADCraftEvent.class, PsiKubeEvents.POST_CAD_CRAFT, PostCADCraftKubeEvent::new, event -> currentSide());
		bridge(RegenPsiEvent.class, PsiKubeEvents.REGEN_PSI, RegenPsiKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(SpellCastEvent.class, PsiKubeEvents.SPELL_CAST, SpellCastKubeEvent::new, event -> sideOf(event.player));
		bridge(PreSpellCastEvent.class, PsiKubeEvents.PRE_SPELL_CAST, PreSpellCastKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(PieceExecutedEvent.class, PsiKubeEvents.PIECE_EXECUTED, PieceExecutedKubeEvent::new, event -> sideOf(event.getPlayerEntity()));
		bridge(PieceKnowledgeEvent.class, PsiKubeEvents.PIECE_KNOWLEDGE, PieceKnowledgeKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(PieceGroupAdvancementComplete.class, PsiKubeEvents.PIECE_GROUP_ADVANCEMENT_COMPLETE, PieceGroupAdvancementCompleteKubeEvent::new, event -> sideOf(event.getPlayerEntity()));
		bridge(LoopcastEndEvent.class, PsiKubeEvents.LOOPCAST_END, LoopcastEndKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(DetonationEvent.class, PsiKubeEvents.DETONATION, DetonationKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(ProgrammerPopulateEvent.class, PsiKubeEvents.PROGRAMMER_POPULATE, ProgrammerPopulateKubeEvent::new, event -> sideOf(event.getPlayer()));
		bridge(PsiArmorEvent.class, PsiKubeEvents.PSI_ARMOR, PsiArmorKubeEvent::new, event -> sideOf(event.getPlayer()));
	}

	@Override
	public void registerEvents(EventGroupRegistry registry) {
		registry.register(PsiKubeEvents.GROUP);
	}

	@Override
	public void registerBuilderTypes(BuilderTypeRegistry registry) {
		registry.of(PsiAPI.SPELL_PIECE_REGISTRY_TYPE_KEY, reg -> {
			reg.add(PsiAPI.location("crafting_trick"), CraftingTrickPieceBuilder.class, CraftingTrickPieceBuilder::new);
			reg.add(PsiAPI.location("trick"), TrickPieceBuilder.class, TrickPieceBuilder::new);
			reg.add(PsiAPI.location("operator"), OperatorPieceBuilder.class, id -> new OperatorPieceBuilder(id, EnumPieceType.OPERATOR));
			reg.add(PsiAPI.location("selector"), OperatorPieceBuilder.class, id -> new OperatorPieceBuilder(id, EnumPieceType.SELECTOR));
		});
	}

	@Override
	public void registerBindings(BindingRegistry bindings) {
		bindings.add("Psi", PsiSpellBinding.class);
	}

	@Override
	public void registerClasses(ClassFilter filter) {
		filter.deny("vazkii.psi.common");
		filter.deny("vazkii.psi.client");
		filter.deny("vazkii.psi.neoforge");
	}

	private static <E extends PsiEvent> void bridge(Class<E> type, EventHandler handler, Function<E, KubeEvent> wrapper, Function<E, ScriptType> side) {
		PsiEvents.register(type, event -> {
			if(!handler.hasListeners()) {
				return;
			}

			if(handler.post(side.apply(event), wrapper.apply(event)).interruptFalse()) {
				event.setCanceled(true);
			}
		});
	}

	private static ScriptType sideOf(Player player) {
		return player.level().isClientSide() ? ScriptType.CLIENT : ScriptType.SERVER;
	}

	private static ScriptType currentSide() {
		return EffectiveSide.get().isClient() ? ScriptType.CLIENT : ScriptType.SERVER;
	}

}
