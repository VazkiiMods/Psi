package vazkii.psi.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import vazkii.psi.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(new BlockTagProvider(event.getGenerator()));
        event.getGenerator().addProvider(new ItemTagProvider(event.getGenerator()));
        event.getGenerator().addProvider(new RecipeGenerator(event.getGenerator()));
    }
}
