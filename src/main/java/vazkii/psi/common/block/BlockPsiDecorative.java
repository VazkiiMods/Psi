/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [31/01/2016, 19:09:11 (GMT)]
 */
package vazkii.psi.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.psi.common.block.base.IPsiBlock;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.lib.LibBlockNames;

import java.util.Locale;

public class BlockPsiDecorative extends BlockMetaVariants<BlockPsiDecorative.Variants> implements IPsiBlock {

	public BlockPsiDecorative() {
		super(LibBlockNames.PSI_DECORATIVE, Material.IRON, Variants.class);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}

	@Override
	public int getLightValue(BlockState state, IBlockAccess world, BlockPos pos) {
		Variants variant = state.getValue(variantProp);
		return variant == Variants.PSIMETAL_PLATE_BLACK_LIGHT || variant == Variants.PSIMETAL_PLATE_WHITE_LIGHT ? 15 : 0;
	}

	public enum Variants implements IStringSerializable {
		PSIDUST_BLOCK,
		PSIMETAL_BLOCK,
		PSIGEM_BLOCK,
		PSIMETAL_PLATE_BLACK,
		PSIMETAL_PLATE_BLACK_LIGHT,
		PSIMETAL_PLATE_WHITE,
		PSIMETAL_PLATE_WHITE_LIGHT,
		EBONY_PSIMETAL_BLOCK,
		IVORY_PSIMETAL_BLOCK;


		@Override
		public String getName() {
			return this.name().toLowerCase(Locale.ENGLISH);
		}
	}

}
