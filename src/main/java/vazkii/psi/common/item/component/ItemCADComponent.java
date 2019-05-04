/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [08/01/2016, 21:53:45 (GMT)]
 */
package vazkii.psi.common.item.component;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.item.base.IPsiItem;

import java.util.HashMap;
import java.util.List;

public abstract class ItemCADComponent extends ItemMod implements ICADComponent, IPsiItem {

	private final HashMap<Pair<EnumCADStat, Integer>, Integer> stats = new HashMap<>();

	public ItemCADComponent(String name, String... variants) {
		super(name, variants);
		setMaxStackSize(1);
		registerStats();
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	public void registerStats() {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			EnumCADComponent componentType = getComponentType(stack);

			String componentName = local(componentType.getName());
			TooltipHelper.addToTooltip(tooltip, "psimisc.componentType", componentName);
			for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				if(stat.getSourceType() == componentType) {
					int statVal = getCADStatValue(stack, stat);
					String statValStr = statVal == -1 ?	"\u221E" : ""+statVal;

					String name = local(stat.getName());
					tooltip.add(" " + TextFormatting.AQUA + name + TextFormatting.GRAY + ": " + statValStr);
				}
			}
		});
	}

	public void addStat(EnumCADStat stat, int meta, int value) {
		stats.put(Pair.of(stat, meta), value);
	}

	@Override
	public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
		Pair p = Pair.of(stat, stack.getItemDamage());
		if(stats.containsKey(p))
			return stats.get(p);

		return 0;
	}

}
