/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/VazkiiMods/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;

import vazkii.psi.data.PsiBlockModelGenerator;
import vazkii.psi.data.PsiBlockTagProvider;
import vazkii.psi.data.PsiDamageTypeTagsProvider;
import vazkii.psi.data.PsiDataRegistries;
import vazkii.psi.data.PsiItemModelGenerator;
import vazkii.psi.data.PsiItemTagProvider;
import vazkii.psi.data.PsiRecipeGenerator;

public class FabricPsiDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(FabricPsiDynamicRegistryProvider::new);
		PsiBlockTagProvider blockTags = pack.addProvider(PsiBlockTagProvider::new);
		pack.addProvider(PsiDamageTypeTagsProvider::new);
		pack.addProvider((output, registries) -> new PsiItemTagProvider(output, registries, blockTags.contentsGetter()));
		pack.addProvider(PsiRecipeGenerator::new);
		pack.addProvider(FabricPsiLootTableProvider::new);
		pack.addProvider((PackOutput output) -> new PsiBlockModelGenerator(output));
		pack.addProvider((PackOutput output) -> new PsiItemModelGenerator(output));
	}

	@Override
	public void buildRegistry(RegistrySetBuilder builder) {
		PsiDataRegistries.configure(builder);
	}
}
