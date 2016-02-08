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

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.common.item.base.ItemMod;

public abstract class ItemCADComponent extends ItemMod implements ICADComponent {

	private final HashMap<Pair<EnumCADStat, Integer>, Integer> stats;
	
	public ItemCADComponent(String name, String... variants) {
		super(name, variants);
		setMaxStackSize(1);
		stats = new HashMap();
		registerStats();
	}
	
	public void registerStats() {
		// NO-OP
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltipIfShift(tooltip, () -> {
			EnumCADComponent componentType = getComponentType(stack);
			
			String componentName = local(componentType.getName());
			addToTooltip(tooltip, "psimisc.componentType", componentName);
			for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
				if(stat.getSourceType() == componentType) {
					int statVal = getCADStatValue(stack, stat);
					String statValStr = statVal == -1 ?	"\u221E" : ""+statVal; 
					
					String name = local(stat.getName());
					addToTooltip(tooltip, " " + EnumChatFormatting.AQUA + name + EnumChatFormatting.GRAY + ": " + statValStr);
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
