/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [31/01/2016, 19:09:11 (GMT)]
 */
package vazkii.psi.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.psi.common.block.base.BlockMetaVariants;
import vazkii.psi.common.lib.LibBlockNames;

public class BlockPsiDecorative extends BlockMetaVariants {
	
	public BlockPsiDecorative() {
		super(LibBlockNames.PSI_DECORATIVE, Material.iron, Variants.class);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		Variants variant = (Variants) world.getBlockState(pos).getValue(variantProp);
		return variant == Variants.PSIMETAL_PLATE_BLACK_LIGHT || variant == Variants.PSIMETAL_PLATE_WHITE_LIGHT ? 15 : 0;
	}
	
	private static enum Variants implements EnumBase {
		PSIDUST_BLOCK, 
		PSIMETAL_BLOCK,
		PSIGEM_BLOCK,
		PSIMETAL_PLATE_BLACK,
		PSIMETAL_PLATE_BLACK_LIGHT,
		PSIMETAL_PLATE_WHITE,
		PSIMETAL_PLATE_WHITE_LIGHT
	}

}
