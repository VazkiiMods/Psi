package vazkii.psi.api.cad;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;
import vazkii.psi.common.block.tile.TileCADAssembler;

/**
 * Posted after a CAD is crafted in the assembler
 */
public class PostCADCraftEvent extends Event {

    private final ItemStack cad;
    private final TileCADAssembler assembler;

    public PostCADCraftEvent(ItemStack cad, TileCADAssembler assembler) {
        this.cad = cad;
        this.assembler = assembler;
    }

    public TileCADAssembler getAssembler() {
        return assembler;
    }

    public ItemStack getCad() {
        return cad;
    }
}
