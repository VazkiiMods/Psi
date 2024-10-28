/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler.capability;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ComponentItemHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.common.core.capability.CapabilityTriggerSensor;
import vazkii.psi.common.entity.ModEntities;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.armor.ItemPsimetalArmor;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.ToolSocketable;
import vazkii.psi.common.lib.LibMisc;

@EventBusSubscriber(modid = LibMisc.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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
			if(instance instanceof INBTSerializable) {
				return ((INBTSerializable<?>) instance).serializeNBT();
			}
			return null;
		}
	
		@Override
		@SuppressWarnings("unchecked")
		public void readNBT(Capability<T> capability, T instance, Direction side, Tag nbt) {
			if(nbt instanceof CompoundTag) {
				((INBTSerializable<Tag>) instance).deserializeNBT(nbt);
			}
		}
	
	}
	*/
    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                PsiAPI.SPELL_IMMUNE_CAPABILITY,
                ModEntities.spellCircle,
                (entity, ctx) -> entity);

        event.registerEntity(
                PsiAPI.DETONATION_HANDLER_CAPABILITY,
                EntityType.PLAYER,
                (player, ctx) -> new CapabilityTriggerSensor(player));
        event.registerEntity(
                PsiAPI.DETONATION_HANDLER_CAPABILITY,
                ModEntities.spellCharge,
                (entity, ctx) -> entity);

        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (itemStack, context) -> new ComponentItemHandler(itemStack, ModItems.COMPONENTS.get(), EnumCADComponent.values().length),
                ModItems.cad);

        event.registerItem(
                PsiAPI.PSI_BAR_DISPLAY_CAPABILITY,
                (cad, ctx) -> new CADData(cad),
                ModItems.cad);
        event.registerItem(
                PsiAPI.PSI_BAR_DISPLAY_CAPABILITY,
                (tool, ctx) -> new ToolSocketable(tool, 3),
                ModItems.psimetalShovel,
                ModItems.psimetalPickaxe,
                ModItems.psimetalAxe,
                ModItems.psimetalSword);
        event.registerItem(
                PsiAPI.PSI_BAR_DISPLAY_CAPABILITY,
                (tool, ctx) -> new ItemPsimetalArmor.ArmorSocketable(tool, 3),
                ModItems.psimetalExosuitHelmet,
                ModItems.psimetalExosuitChestplate,
                ModItems.psimetalExosuitLeggings,
                ModItems.psimetalExosuitBoots);

        event.registerItem(
                PsiAPI.SPELL_ACCEPTOR_CAPABILITY,
                (cad, ctx) -> new CADData(cad),
                ModItems.cad);
        event.registerItem(
                PsiAPI.SPELL_ACCEPTOR_CAPABILITY,
                (tool, ctx) -> new ToolSocketable(tool, 3),
                ModItems.psimetalShovel,
                ModItems.psimetalPickaxe,
                ModItems.psimetalAxe,
                ModItems.psimetalSword);
        event.registerItem(
                PsiAPI.SPELL_ACCEPTOR_CAPABILITY,
                (tool, ctx) -> new ItemPsimetalArmor.ArmorSocketable(tool, 3),
                ModItems.psimetalExosuitHelmet,
                ModItems.psimetalExosuitChestplate,
                ModItems.psimetalExosuitLeggings,
                ModItems.psimetalExosuitBoots);
        event.registerItem(
                PsiAPI.SPELL_ACCEPTOR_CAPABILITY,
                (stack, ctx) -> new ItemSpellBullet.SpellAcceptor(stack),
                ModItems.spellBullet,
                ModItems.projectileSpellBullet,
                ModItems.loopSpellBullet,
                ModItems.circleSpellBullet,
                ModItems.grenadeSpellBullet,
                ModItems.chargeSpellBullet,
                ModItems.mineSpellBullet);

        event.registerItem(
                PsiAPI.CAD_DATA_CAPABILITY,
                (cad, ctx) -> new CADData(cad),
                ModItems.cad);

        event.registerItem(
                PsiAPI.SOCKETABLE_CAPABILITY,
                (cad, ctx) -> new CADData(cad),
                ModItems.cad);
        event.registerItem(
                PsiAPI.SOCKETABLE_CAPABILITY,
                (tool, ctx) -> new ToolSocketable(tool, 3),
                ModItems.psimetalShovel,
                ModItems.psimetalPickaxe,
                ModItems.psimetalAxe,
                ModItems.psimetalSword);
        event.registerItem(
                PsiAPI.SOCKETABLE_CAPABILITY,
                (tool, ctx) -> new ItemPsimetalArmor.ArmorSocketable(tool, 3),
                ModItems.psimetalExosuitHelmet,
                ModItems.psimetalExosuitChestplate,
                ModItems.psimetalExosuitLeggings,
                ModItems.psimetalExosuitBoots);
    }

}
