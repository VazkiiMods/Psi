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
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.client.core.handler.ModelHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageDataSync;

public class ItemCAD extends ItemMod implements ICAD {

	private static final String TAG_COMPONENT_PREFIX = "component";
	private static final String TAG_STORED_PSI = "storedPsi";
	private static final String TAG_BULLET_PREFIX = "bullet";
	private static final String TAG_SELECTED_SLOT = "selectedSlot";
	
	public ItemCAD() {
		super(LibItemNames.CAD);
		setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		PlayerData data = PlayerDataHandler.get(playerIn);

		if(data.getAvailablePsi() > 0) {
			ItemStack bullet = getBulletInSocket(itemStackIn, getSelectedSlot(itemStackIn));
			if(bullet != null && bullet.getItem() instanceof ISpellContainer) {
				ISpellContainer spellContainer = (ISpellContainer) bullet.getItem();
				if(spellContainer.containsSpell(bullet)) {
					Spell spell = spellContainer.getSpell(bullet);
					SpellContext context = new SpellContext().setPlayer(playerIn).setSpell(spell);
					if(context.isValid()) {
						if(context.cspell.metadata.evaluateAgainst(itemStackIn)) {
							int cost = getRealCost(itemStackIn, context.cspell.metadata.stats.get(EnumSpellStat.COST)); 
							if(cost > 0)
								data.deductPsi(cost, 40, true);
							
							spellContainer.castSpell(bullet, context);
							
							if(data.level == 0 && playerIn instanceof EntityPlayerMP) {
								data.levelUp();
								NetworkHandler.INSTANCE.sendTo(new MessageDataSync(data), (EntityPlayerMP) playerIn);
							}
						} else if(!playerIn.worldObj.isRemote)
							playerIn.addChatComponentMessage(new ChatComponentTranslation("psimisc.weakCad").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					}
				}
			}
		}
		
		return itemStackIn;
	}
	
	public static int getRealCost(ItemStack stack, int cost) {
		if(stack != null && stack.getItem() instanceof ICAD) {
			int eff = ((ICAD) stack.getItem()).getStatValue(stack, EnumCADStat.EFFICIENCY);
			if(eff == 0)
				return cost;
			
			double effPercentile = (double) eff / 100;
			return cost / eff;
		}
		
		return cost;
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
	public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
		int sockets = getStatValue(stack, EnumCADStat.SOCKETS);
		return slot < sockets;
	}

	@Override
	public ItemStack getBulletInSocket(ItemStack stack, int slot) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, name, true);
		
		if(cmp == null)
			return null;
		
		return ItemStack.loadItemStackFromNBT(cmp);
	}

	@Override
	public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = new NBTTagCompound();
		
		if(bullet != null)
			bullet.writeToNBT(cmp);
		
		ItemNBTHelper.setCompound(stack, name, cmp);
	}
	
	@Override
	public int getSelectedSlot(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
	}

	@Override
	public void setSelectedSlot(ItemStack stack, int slot) {
		ItemNBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
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
		tooltipIfShift(tooltip, () -> {
			String componentName = local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
			addToTooltip(tooltip, "psimisc.spellSelected", componentName);
			
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
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
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
