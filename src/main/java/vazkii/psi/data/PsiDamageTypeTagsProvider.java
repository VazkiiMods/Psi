package vazkii.psi.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.ModTags;

import java.util.concurrent.CompletableFuture;

public class PsiDamageTypeTagsProvider extends DamageTypeTagsProvider {

	public PsiDamageTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, ExistingFileHelper existingFileHelper) {
		super(pOutput, pLookupProvider, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		this.tag(DamageTypeTags.BYPASSES_ARMOR).addTag(ModTags.PSI_OVERLOAD);
		this.tag(DamageTypeTags.BYPASSES_SHIELD).addTag(ModTags.PSI_OVERLOAD);
		this.tag(DamageTypeTags.BYPASSES_RESISTANCE).addTag(ModTags.PSI_OVERLOAD);
		this.tag(DamageTypeTags.BYPASSES_EFFECTS).addTag(ModTags.PSI_OVERLOAD);
		this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).addTag(ModTags.PSI_OVERLOAD);
	}

	@Override
	public String getName() {
		return "Psi damage type tags";
	}
}
