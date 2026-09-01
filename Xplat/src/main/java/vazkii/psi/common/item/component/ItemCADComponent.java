/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item.component;

import com.mojang.logging.LogUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import vazkii.psi.api.cad.CADComponentLookup;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.internal.TooltipHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class ItemCADComponent extends Item implements ICADComponent {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * @deprecated Stats live in the {@code psi:cad_component} datapack registry; this map is
	 *             only read for items without a registry entry.
	 */
	@Deprecated
	private final HashMap<EnumCADStat, Integer> stats = new HashMap<>();

	public ItemCADComponent(Item.Properties properties) {
		super(properties.stacksTo(1));
	}

	/**
	 * @deprecated Define stats in {@code data/<ns>/psi/cad_component/<item>.json} instead.
	 */
	@Deprecated
	public static void addStatToStack(Item item, EnumCADStat stat, int value) {
		if(item instanceof ItemCADComponent) {
			((ItemCADComponent) item).addStat(stat, value);
		} else {
			LOGGER.error("Tried to add stats to non-component Item: {}", item.getDescription());
		}
	}

	/**
	 * Tooltip hook for items that are CAD components only through the datapack registry.
	 * Psi's own components render the same lines through {@link #appendHoverText}.
	 */
	public static void appendForeignHoverText(@Nullable HolderLookup.Provider registries, ItemStack stack, List<Component> tooltip) {
		if(stack.getItem() instanceof ItemCADComponent || CADComponentLookup.definition(registries, stack).isEmpty()) {
			return;
		}
		appendStatTooltip(registries, stack, tooltip);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag advanced) {
		appendStatTooltip(context == null ? null : context.registries(), stack, tooltip);
	}

	private static void appendStatTooltip(@Nullable HolderLookup.Provider registries, ItemStack stack, List<Component> tooltip) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			Optional<EnumCADComponent> componentType = CADComponentLookup.componentType(registries, stack);
			if(componentType.isEmpty()) {
				return;
			}

			Component componentName = Component.translatable(componentType.get().getName());
			tooltip.add(Component.translatable("psimisc.component_type", componentName));
			for(EnumCADStat stat : EnumCADStat.values()) {
				if(stat.getSourceType() != componentType.get()) {
					continue;
				}
				int statVal = CADComponentLookup.statValue(registries, stack, stat);
				String statValStr = statVal == -1 ? "∞" : "" + statVal;

				Component name = Component.translatable(stat.getName()).withStyle(ChatFormatting.AQUA);
				tooltip.add(Component.literal(" ").append(name).append(": " + statValStr));
			}
		});
	}

	/**
	 * @deprecated See {@link #addStatToStack}.
	 */
	@Deprecated
	public void addStat(EnumCADStat stat, int value) {
		stats.put(stat, value);
	}

	@Deprecated
	@Override
	public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
		return stats.getOrDefault(stat, 0);
	}

}
