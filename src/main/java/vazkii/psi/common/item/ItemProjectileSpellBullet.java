package vazkii.psi.common.item;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellProjectile;

public class ItemProjectileSpellBullet extends ItemSpellBullet {


    public ItemProjectileSpellBullet(String name, Properties properties) {
        super(name, properties);
    }

    @Override
    public void castSpell(ItemStack stack, SpellContext context) {
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
        EntitySpellProjectile projectile = new EntitySpellProjectile(context.caster.getEntityWorld(), context.caster);
        projectile.setInfo(context.caster, colorizer, stack);
        projectile.context = context;
        projectile.getEntityWorld().addEntity(projectile);
    }

    @Override
    public double getCostModifier(ItemStack stack) {
        return 1.02;
    }

    @Override
    public boolean isCADOnlyContainer(ItemStack stack) {
        return true;
    }

    @Override
    public String getBulletType() {
        return "projectile";
    }
}
