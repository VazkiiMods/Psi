package vazkii.psi.api.material;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class PsimetalToolMaterial implements IItemTier {
    @Override
    public int getMaxUses() {
        return 900;
    }

    @Override
    public float getEfficiency() {
        return 7.8F;
    }

    @Override
    public float getAttackDamage() {
        return 2F;
    }

    @Override
    public int getHarvestLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 12;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromTag(ItemTags.getCollection().getOrCreate(new ResourceLocation("forge", "ingots/psimetal")));
    }
}
