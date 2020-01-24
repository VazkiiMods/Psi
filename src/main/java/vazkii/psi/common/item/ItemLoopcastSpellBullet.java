package vazkii.psi.common.item;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.LoopcastTrackingHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class ItemLoopcastSpellBullet extends ItemSpellBullet {

    public ItemLoopcastSpellBullet(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public void castSpell(ItemStack stack, SpellContext context) {
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
        if (!data.loopcasting || context.castFrom != data.loopcastHand) {
            context.cspell.safeExecute(context);
            data.loopcasting = true;
            data.loopcastHand = context.castFrom;
            data.lastTickLoopcastStack = null;
            if (context.caster instanceof ServerPlayerEntity)
                LoopcastTrackingHandler.syncForTrackers((ServerPlayerEntity) context.caster);
        }
    }

    @Override
    public String getBulletType() {
        return "loopcast";
    }

    @Override
    public boolean isCADOnlyContainer(ItemStack stack) {
        return true;
    }

    @Override
    public double getCostModifier(ItemStack stack) {
        return 1.0;
    }
}
