/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.core.capability.CapabilityTriggerSensor;
import vazkii.psi.common.core.handler.capability.wrappers.SimpleProvider;
import vazkii.psi.common.lib.LibMisc;

import static vazkii.psi.api.PsiAPI.DETONATION_HANDLER_CAPABILITY;
import static vazkii.psi.api.PsiAPI.SPELL_IMMUNE_CAPABILITY;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
public class CapabilityHandler {
	/* //TODO Does this need reimplimentation?
	public static void register() {
		register(ICADData.class, CapabilityHandler::noDefault);
		register(ISocketable.class, SocketWheel::new);
		register(ISpellAcceptor.class, SpellHolder::new);
	
		registerSingleDefault(IDetonationHandler.class, () -> {});
		registerSingleDefault(IPsiBarDisplay.class, data -> false);
		registerSingleDefault(ISpellImmune.class, () -> false);
	}
	
	private static <T> void registerSingleDefault(Class<T> clazz, T provided) {
		register(clazz, () -> provided);
	}
	
	private static <T> void register(Class<T> clazz, Callable<T> provider) {
		CapabilityManager.INSTANCE.register(clazz, new CapabilityFactory<>(), provider);
	}
	
	private static <T> T noDefault() {
		throw new UnsupportedOperationException("No default instance!");
	}
	
	private static class CapabilityFactory<T> implements Capability.IStorage<T> {
	
		@Override
		public Tag writeNBT(Capability<T> capability, T instance, Direction side) {
			if (instance instanceof INBTSerializable) {
				return ((INBTSerializable<?>) instance).serializeNBT();
			}
			return null;
		}
	
		@Override
		@SuppressWarnings("unchecked")
		public void readNBT(Capability<T> capability, T instance, Direction side, Tag nbt) {
			if (nbt instanceof CompoundTag) {
				((INBTSerializable<Tag>) instance).deserializeNBT(nbt);
			}
		}
	
	}
	*/
	private static final ResourceLocation SPELL_IMMUNE = new ResourceLocation(LibMisc.MOD_ID, "immune");
	private static final ResourceLocation DETONATOR = new ResourceLocation(LibMisc.MOD_ID, "detonator");
	public static final ResourceLocation TRIGGER_SENSOR = new ResourceLocation(LibMisc.MOD_ID, "trigger_sensor");

	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof ISpellImmune) {
			event.addCapability(SPELL_IMMUNE, new SimpleProvider<>(SPELL_IMMUNE_CAPABILITY,
					(ISpellImmune) event.getObject()));
		}
		if (event.getObject() instanceof Player) {
			event.addCapability(TRIGGER_SENSOR, new CapabilityTriggerSensor((Player) event.getObject()));
		}
		if (event.getObject() instanceof IDetonationHandler) {
			event.addCapability(DETONATOR, new SimpleProvider<>(DETONATION_HANDLER_CAPABILITY,
					(IDetonationHandler) event.getObject()));
		}
	}

}
