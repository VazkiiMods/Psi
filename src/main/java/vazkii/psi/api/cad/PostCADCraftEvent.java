package vazkii.psi.api.cad;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Posted after a CAD is crafted in the assembler.
 * Editing the ItemStack does not and cannot guarantee the output item is changed.
 */
public class PostCADCraftEvent extends Event {

	private final ItemStack cad;
	private final ITileCADAssembler assembler;

	public PostCADCraftEvent(ItemStack cad, ITileCADAssembler assembler) {
		this.cad = cad;
		this.assembler = assembler;
	}

	public ITileCADAssembler getAssembler() {
		return assembler;
	}

	public ItemStack getCad() {
		return cad;
	}
}
