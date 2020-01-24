package vazkii.psi.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.ModTags;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(DataGenerator p_i48255_1_) {
        super(p_i48255_1_);
    }

    @Override
    protected void registerTags() {
        getBuilder(ModTags.DUST_PSI).add(ModItems.psidust);
        getBuilder(ModTags.INGOT_PSI).add(ModItems.psimetal);
        getBuilder(ModTags.GEM_PSI).add(ModItems.psigem);
        getBuilder(ModTags.INGOT_EBONY_PSI).add(ModItems.ebonyPsimetal);
        getBuilder(ModTags.INGOT_IVORY_PSI).add(ModItems.ivoryPsimetal);
        getBuilder(ModTags.SUBSTANCE_EBONY).add(ModItems.ebonySubstance);
        getBuilder(ModTags.SUBSTANCE_IVORY).add(ModItems.ivorySubstance);

    }

    @Override
    public String getName() {
        return "Psi item tags";
    }
}
