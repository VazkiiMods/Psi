package vazkii.psi.common.attribute.base;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.lib.LibAttributeNames;
import vazkii.psi.common.platform.PsiAttributes;
import vazkii.psi.common.registry.PsiRegistries;
import vazkii.psi.common.registry.RegistryEntry;

public final class ModAttributes {
	public static final RegistryEntry<Attribute> TOTAL_PSI = PsiRegistries.register(
			BuiltInRegistries.ATTRIBUTE, PsiAPI.location(LibAttributeNames.TOTAL_PSI),
			() -> new RangedAttribute(
					"attribute.psi.total_psi",
					5000,
					0,
					Integer.MAX_VALUE
			).setSyncable(true)
	);

	public static final RegistryEntry<Attribute> REGEN = PsiRegistries.register(
			BuiltInRegistries.ATTRIBUTE, PsiAPI.location(LibAttributeNames.REGEN),
			() -> new RangedAttribute(
					"attribute.psi.regen",
					25,
					0,
					Integer.MAX_VALUE
			).setSyncable(true)
	);

	private ModAttributes() {}

	public static void register() {
		PsiAttributes.addToPlayer(TOTAL_PSI.holder(), REGEN.holder());
	}
}
