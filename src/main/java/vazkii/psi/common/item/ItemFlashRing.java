package vazkii.psi.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;

import org.jetbrains.annotations.NotNull;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.gui.GuiFlashRing;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemFlashRing extends Item {

	public ItemFlashRing(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	public void verifyComponentsAfterLoad(ItemStack pStack) {
		if(pStack.has(DataComponents.CUSTOM_DATA)) {
			CustomData patch = pStack.get(DataComponents.CUSTOM_DATA);
			CompoundTag compound = patch.copyTag();

			if(compound.contains("has_spell")) {
				pStack.set(ModItems.HAS_SPELL, compound.getBoolean("has_spell"));
				pStack.set(DataComponents.RARITY, compound.getBoolean("has_spell") ? Rarity.RARE : Rarity.COMMON);
				compound.remove("has_spell");
			}
			if(compound.contains("spell")) {
				pStack.set(ModItems.TAG_SPELL, compound.getCompound("spell"));
				compound.remove("spell");
			}
			CustomData.set(DataComponents.CUSTOM_DATA, pStack, compound);
		}
	}

	@Nonnull
	@Override
	public Component getName(@Nonnull ItemStack stack) {
		if(!ISpellAcceptor.hasSpell(stack)) {
			return super.getName(stack);
		}

		CompoundTag cmp = stack.getOrDefault(ModItems.TAG_SPELL, new CompoundTag());
		String name = cmp.getString(Spell.TAG_SPELL_NAME);

		if(name.isEmpty()) {
			return super.getName(stack);
		}

		return Component.literal(name);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			tooltip.add(Component.translatable("psimisc.bullet_cost", (int) (ISpellAcceptor.acceptor(stack).getCostModifier() * 100)));
		});
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack held = player.getItemInHand(usedHand);
		boolean isSneaking = player.isShiftKeyDown();

		// Open GUI if sneaking
		if(isSneaking && level.isClientSide) {
			Minecraft.getInstance().setScreen(new GuiFlashRing(held));

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, held);
		}

		// Cast spell if not sneaking
		if(!isSneaking && ISpellAcceptor.hasSpell(held)) {
			PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
			ItemStack cad = data.getCAD();

			if(cad.isEmpty()) {
				return new InteractionResultHolder<>(InteractionResult.PASS, held);
			}

			boolean casted = ItemCAD.cast(level, player, data, held, cad, 100, 25, 0.5f, ctx -> ctx.castFrom = usedHand).isPresent();

			return new InteractionResultHolder<>(casted ? InteractionResult.SUCCESS : InteractionResult.PASS, held);
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, held);
	}

	public static class SpellAcceptor implements ICapabilityProvider<ItemCapability<?, Void>, Void, SpellAcceptor>, ISpellAcceptor {
		protected final ItemStack stack;

		public SpellAcceptor(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public SpellAcceptor getCapability(ItemCapability<?, Void> capability, Void facing) {
			return capability == PsiAPI.SPELL_ACCEPTOR_CAPABILITY ? this : null;
		}

		@Override
		public void setSpell(Player player, Spell spell) {
			ItemSpellDrive.setSpell(stack, spell);
		}

		@Override
		public Spell getSpell() {
			return ItemSpellDrive.getSpell(stack);
		}

		@Override
		public boolean containsSpell() {
			return stack.getOrDefault(ModItems.HAS_SPELL, false);
		}

		@Override
		public ArrayList<Entity> castSpell(SpellContext context) {
			context.cspell.safeExecute(context);
			return new ArrayList<>();
		}

		@Override
		public boolean loopcastSpell(SpellContext context) {
			return false;
		}

		@Override
		public double getCostModifier() {
			return 2;
		}

		@Override
		public boolean requiresSneakForSpellSet() {
			return true;
		}
	}

}
