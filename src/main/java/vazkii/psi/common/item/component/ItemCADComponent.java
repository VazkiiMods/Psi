/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.common.Psi;

import java.util.HashMap;
import java.util.List;

public abstract class ItemCADComponent extends Item implements ICADComponent {

	private final HashMap<EnumCADStat, Integer> stats = new HashMap<>();

	public ItemCADComponent(Item.Properties properties) {
		super(properties.stacksTo(1));
		registerStats();
	}

	public static void addStatToStack(ItemStack stack, EnumCADStat stat, int value) {
		if(stack.getItem() instanceof ItemCADComponent) {
			((ItemCADComponent) stack.getItem()).addStat(stat, value);
		} else {
			Psi.logger.error("Tried to add stats to non-component ItemStack: {}", stack.getItem().getDescription());
		}
	}

	public static void addStatToStack(Item item, EnumCADStat stat, int value) {
		if(item instanceof ItemCADComponent) {
			((ItemCADComponent) item).addStat(stat, value);
		} else {
			Psi.logger.error("Tried to add stats to non-component Item: {}", item.getDescription());
		}
	}

	public void registerStats() {
		// NO-OP
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			EnumCADComponent componentType = getComponentType(stack);

			Component componentName = Component.translatable(componentType.getName());
			tooltip.add(Component.translatable("psimisc.component_type", componentName));
			for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				if(stat.getSourceType() == componentType) {
					int statVal = getCADStatValue(stack, stat);
					String statValStr = statVal == -1 ? "\u221E" : "" + statVal;

					Component name = Component.translatable(stat.getName()).withStyle(ChatFormatting.AQUA);
					tooltip.add(Component.literal(" ").append(name).append(": " + statValStr));
				}
			}
		});
	}

	public void addStat(HashMap<EnumCADStat, Integer> stats) {
		stats.forEach(this::addStat);
	}

	public void addStat(EnumCADStat stat, int value) {
		stats.put(stat, value);
	}

	@Override
	public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
		if(stats.containsKey(stat)) {
			return stats.get(stat);
		}

		return 0;
	}

}
