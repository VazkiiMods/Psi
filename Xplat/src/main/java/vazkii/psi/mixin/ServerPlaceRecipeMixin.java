package vazkii.psi.mixin;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import vazkii.psi.common.item.ItemSpellBullet;

@Mixin(ServerPlaceRecipe.class)
public class ServerPlaceRecipeMixin {
	@Redirect(method = "moveItemToGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingUnusedItem(Lnet/minecraft/world/item/ItemStack;)I"))
	public int findBullets(Inventory instance, ItemStack stack) {
		if(stack.getItem() instanceof ItemSpellBullet) {
			for(int i = 0; i < instance.items.size(); i++) {
				ItemStack itemstack = instance.items.get(i);
				if(!itemstack.isEmpty() && ItemStack.isSameItem(stack, itemstack)) {
					return i;
				}
			}

			return -1;
		}

		return instance.findSlotMatchingUnusedItem(stack);
	}
}
