package vazkii.psi.common.lib;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {

    public static final Tag<Item> DUST_PSI = forgeTag("dusts/psi");
    public static final Tag<Item> INGOT_PSI = forgeTag("ingots/psi");
    public static final Tag<Item> GEM_PSI = forgeTag("gems/psi");
    public static final Tag<Item> INGOT_EBONY_PSI = forgeTag("ingots/ebony_psi");
    public static final Tag<Item> INGOT_IVORY_PSI = forgeTag("ingots/ivory_psi");
    public static final Tag<Item> SUBSTANCE_IVORY = forgeTag("dusts/ivory_substance");
    public static final Tag<Item> SUBSTANCE_EBONY = forgeTag("dusts/ebony_susbtance");

    private static Tag<Item> tag(String name) {
        return new ItemTags.Wrapper(new ResourceLocation(LibMisc.MOD_ID, name));
    }

    private static Tag<Item> forgeTag(String name) {
        return new ItemTags.Wrapper(new ResourceLocation("forge", name));
    }
}
