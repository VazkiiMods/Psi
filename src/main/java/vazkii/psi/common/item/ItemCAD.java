/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [09/01/2016, 17:04:30 (GMT)]
 */
package vazkii.psi.common.item;

import java.util.List;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.client.core.handler.ModelHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageDataSync;
import vazkii.psi.common.network.message.TestMessage;

public class ItemCAD extends ItemMod implements ICAD {

	private static final String TAG_COMPONENT_PREFIX = "component";
	private static final String TAG_STORED_PSI = "storedPsi";
	
	public ItemCAD() {
		super(LibItemNames.CAD);
		setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		PlayerData data = PlayerDataHandler.get(playerIn);
		data.deductPsi(playerIn.isSneaking() ? 50 : 200, 40, true); // TODO DEBUG
		if(data.level == 0 && playerIn instanceof EntityPlayerMP) {
			data.levelUp();
			NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) playerIn);
		}
		
		return itemStackIn;
	}
	
	public static void setComponent(ItemStack stack, ItemStack componentStack) {
		if(componentStack != null && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			EnumCADComponent componentType = component.getComponentType(componentStack);
			String name = TAG_COMPONENT_PREFIX + componentType.name();
			
			NBTTagCompound cmp = new NBTTagCompound();
			componentStack.writeToNBT(cmp);
			ItemNBTHelper.setCompound(stack, name, cmp);
		}
	}
	
	public static ItemStack makeCAD(ItemStack... components) {
		ItemStack stack = new ItemStack(ModItems.cad);
		for(ItemStack component : components)
			setComponent(stack, component);
		return stack;
	}

	@Override
	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
		String name = TAG_COMPONENT_PREFIX + type.name();
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);
		
		if(cmp == null)
			return null;
		
		return ItemStack.loadItemStackFromNBT(cmp);
	}

	@Override
	public int getStatValue(ItemStack stack, EnumCADStat stat) {
		ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
		if(componentStack != null && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			return component.getCADStatValue(componentStack, stat);
		}

		return 0;
	}
	
	@Override
	public int getStoredPsi(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_STORED_PSI, 0);
	}

	@Override
	public void regenPsi(ItemStack stack, int psi) {
		int maxPsi = getStatValue(stack, EnumCADStat.OVERFLOW);
		int currPsi = getStoredPsi(stack);
		int endPsi = Math.min(currPsi + psi, maxPsi);
		
		if(endPsi != currPsi)
			ItemNBTHelper.setInt(stack, TAG_STORED_PSI, endPsi);
	}

	@Override
	public int consumePsi(ItemStack stack, int psi) {
		int currPsi = getStoredPsi(stack);
		if(currPsi >= psi) {
			ItemNBTHelper.setInt(stack, TAG_STORED_PSI, currPsi - psi);
			return 0;
		}
		
		ItemNBTHelper.setInt(stack, TAG_STORED_PSI, 0);
		return psi - currPsi;
	}
	
	@Override
	public int getSpellColor(ItemStack stack) {
		ItemStack dye = getComponentInSlot(stack, EnumCADComponent.DYE);
		if(dye != null && dye.getItem() instanceof ICADColorizer)
			return ((ICADColorizer) dye.getItem()).getColor(dye);
		
		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged;
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		// Basic Iron CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 0)));
		
		// Iron CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 0), 
				new ItemStack(ModItems.cadCore, 1, 0), 
				new ItemStack(ModItems.cadSocket, 1, 0), 
				new ItemStack(ModItems.cadBattery, 1, 0)));		

		
		// Gold CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 1), 
				new ItemStack(ModItems.cadCore, 1, 0), 
				new ItemStack(ModItems.cadSocket, 1, 0), 
				new ItemStack(ModItems.cadBattery, 1, 0)));	
		
		// Psimetal CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 2), 
				new ItemStack(ModItems.cadCore, 1, 0), 
				new ItemStack(ModItems.cadSocket, 1, 0), 
				new ItemStack(ModItems.cadBattery, 1, 0)));	
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltipIfShift(playerIn, tooltip, () -> {
			for(EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
				ItemStack componentStack = getComponentInSlot(stack, componentType);
				String name = "psimisc.none";
				if(componentStack != null)
					name = componentStack.getDisplayName();
				
				name = local(name);
				String line = EnumChatFormatting.GREEN + local(componentType.getName()) + EnumChatFormatting.GRAY + ": " + name;
				addToTooltip(tooltip, line);
				
				line = "";
				for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
					if(stat.getSourceType() == componentType) {
						String shrt = stat.getName();
						int statVal = getStatValue(stack, stat);
						line = " " + EnumChatFormatting.AQUA + local(shrt) + EnumChatFormatting.GRAY + ": " + statVal;
						if(!line.isEmpty())
							addToTooltip(tooltip, line);
					}
				}
			}
		});
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ItemMeshDefinition getCustomMeshDefinition() {
		return (ItemStack stack) -> {
			ICAD cad = (ICAD) stack.getItem();
			ItemStack assembly = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
			ModelResourceLocation loc = ModelHandler.getModelLocation(assembly); 
			return loc;
		};
	}

}
