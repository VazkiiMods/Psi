package vazkii.psi.client.gui;

import net.minecraft.world.item.ItemStack;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.common.item.ItemSpellDrive;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageFlashRingSync;
import vazkii.psi.common.spell.SpellCompiler;

import java.util.UUID;

public class GuiFlashRing extends GuiProgrammer {
	public GuiFlashRing(ItemStack stack) {
		super(null, ItemSpellDrive.getSpell(stack));
	}

	@Override
	public void onSpellChanged(boolean nameOnly) {
		spell.uuid = UUID.randomUUID();
		MessageRegister.sendToServer(new MessageFlashRingSync(spell));
		onSelectedChanged();
		spellNameField.setFocused(nameOnly);

		if(!nameOnly ||
				(compileResult.right().isPresent() && compileResult.right().get().getMessage().equals(SpellCompilationException.NO_NAME)) ||
				spell.name.isEmpty()) {
			compileResult = new SpellCompiler().compile(spell);
		}
	}
}
