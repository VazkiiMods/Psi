package vazkii.psi.mixin.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.psi.common.item.ItemSpellBullet;

@Mixin(ServerPlaceRecipe.class)
public class ServerPlaceRecipeMixin {
    @WrapOperation(method = "moveItemToGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingUnusedItem(Lnet/minecraft/world/item/ItemStack;)I"))
    public int findBullets(Inventory instance, ItemStack stack, Operation<Integer> original) {
        if (stack.getItem() instanceof ItemSpellBullet) {
            for (int i = 0; i < instance.items.size(); i++) {
                ItemStack itemstack = instance.items.get(i);
                if (!itemstack.isEmpty() && ItemStack.isSameItem(stack, itemstack)) {
                    return i;
                }
            }

            return -1;
        }

        return original.call(instance, stack);
    }
}
