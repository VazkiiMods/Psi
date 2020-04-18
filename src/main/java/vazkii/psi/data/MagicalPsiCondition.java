/*
 * This class is distributed as a part of the Psi Mod.
 * Get the Source Code on GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.data;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

import vazkii.psi.common.Psi;
import vazkii.psi.common.lib.LibMisc;

public class MagicalPsiCondition implements ICondition {
	public static final MagicalPsiCondition INSTANCE = new MagicalPsiCondition();
	public static final ResourceLocation NAME = new ResourceLocation(LibMisc.MOD_ID, "magipsi_enabled");

	@Override
	public ResourceLocation getID() {
		return NAME;
	}

	@Override
	public boolean test() {
		return Psi.magical;
	}

	public MagicalPsiCondition() {
		//NOOP
	}

	@Override
	public String toString() {
		return "magipsi_enabled";
	}

	public static class Serializer implements IConditionSerializer<MagicalPsiCondition> {
		public static final MagicalPsiCondition.Serializer INSTANCE = new MagicalPsiCondition.Serializer();

		@Override
		public void write(JsonObject json, MagicalPsiCondition value) {
			//NOOP
		}

		@Override
		public MagicalPsiCondition read(JsonObject json) {
			return MagicalPsiCondition.INSTANCE;
		}

		@Override
		public ResourceLocation getID() {
			return MagicalPsiCondition.NAME;
		}
	}
}
