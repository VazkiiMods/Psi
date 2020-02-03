package vazkii.psi.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.ModTags;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        getBuilder(ModTags.PSIDUST).add(ModItems.psidust);
        getBuilder(ModTags.EBONY_SUBSTANCE).add(ModItems.ebonySubstance);
        getBuilder(ModTags.IVORY_SUBSTANCE).add(ModItems.ivorySubstance);
        
        getBuilder(ModTags.INGOT_PSIMETAL).add(ModItems.psimetal);
        copy(ModTags.Blocks.BLOCK_PSIMETAL, ModTags.BLOCK_PSIMETAL);
        
        getBuilder(ModTags.GEM_PSIGEM).add(ModItems.psigem);
        copy(ModTags.Blocks.BLOCK_PSIGEM, ModTags.BLOCK_PSIGEM);
        
        getBuilder(ModTags.INGOT_EBONY_PSIMETAL).add(ModItems.ebonyPsimetal);
        copy(ModTags.Blocks.BLOCK_EBONY_PSIMETAL, ModTags.BLOCK_EBONY_PSIMETAL);
        getBuilder(ModTags.INGOT_IVORY_PSIMETAL).add(ModItems.ivoryPsimetal);
        copy(ModTags.Blocks.BLOCK_IVORY_PSIMETAL, ModTags.BLOCK_IVORY_PSIMETAL);
    }

    @Override
    public String getName() {
        return "Psi item tags";
    }
}
