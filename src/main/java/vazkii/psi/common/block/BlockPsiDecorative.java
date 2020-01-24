package vazkii.psi.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import vazkii.arl.block.BasicBlock;
import vazkii.arl.interf.IBlockItemProvider;

public class BlockPsiDecorative extends BasicBlock implements IBlockItemProvider {

    Item.Properties itemProps;

    public BlockPsiDecorative(String regname, Properties properties, Item.Properties itemProps) {
        super(regname, properties);
        this.itemProps = itemProps;
    }

    @Override
    public BlockItem provideItemBlock(Block block, Item.Properties properties) {
        return new BlockItem(block, itemProps);
    }
}
