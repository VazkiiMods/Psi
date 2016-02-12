/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 * 
 * File Created @ [09/01/2016, 17:04:30 (GMT)]
 */
package vazkii.psi.common.item;

import java.awt.Color;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.ISpellContainer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.helper.ItemNBTHelper;
import vazkii.psi.common.crafting.recipe.AssemblyScavengeRecipe;
import vazkii.psi.common.item.base.ItemMod;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCAD extends ItemMod implements ICAD {

	private static final String TAG_COMPONENT_PREFIX = "component";
	private static final String TAG_STORED_PSI = "storedPsi";
	private static final String TAG_BULLET_PREFIX = "bullet";
	private static final String TAG_SELECTED_SLOT = "selectedSlot";
	
	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

	public ItemCAD() {
		super(LibItemNames.CAD);
		setMaxStackSize(1);
		
		GameRegistry.addRecipe(new AssemblyScavengeRecipe());
		RecipeSorter.register("psi:assemblyScavenge", AssemblyScavengeRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		PlayerData data = PlayerDataHandler.get(playerIn);
		ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
		if(playerCad != itemStackIn) {
			if(!worldIn.isRemote)
				playerIn.addChatComponentMessage(new ChatComponentTranslation("psimisc.multipleCads").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			return itemStackIn;
		}

		ItemStack bullet = getBulletInSocket(itemStackIn, getSelectedSlot(itemStackIn));
		cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, null);
		
		if(bullet == null && craft(playerIn, new ItemStack(Items.redstone), new ItemStack(ModItems.material))) {
			if(!worldIn.isRemote)
				worldIn.playSoundAtEntity(playerIn, "psi:cadShoot", 0.5F, (float) (0.5 + Math.random() * 0.5));
			data.deductPsi(100, 60, true);

			if(data.level == 0)
				data.levelUp();
		}

		return itemStackIn;
	}

	public static void cast(World world, EntityPlayer player, PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate) {
		if(data.getAvailablePsi() > 0 && cad != null && bullet != null && bullet.getItem() instanceof ISpellContainer && isTruePlayer(player)) {
			ISpellContainer spellContainer = (ISpellContainer) bullet.getItem();
			if(spellContainer.containsSpell(bullet)) {
				Spell spell = spellContainer.getSpell(bullet);
				SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
				if(predicate != null)
					predicate.accept(context);
				
				if(context.isValid()) {
					if(context.cspell.metadata.evaluateAgainst(cad)) {
						int cost = getRealCost(cad, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST)); 
						if(cost > 0 || cost == -1) {
							if(cost != -1)
								data.deductPsi(cost, cd, true);

							if(!world.isRemote)
								world.playSoundAtEntity(player, "psi:cadShoot", sound, (float) (0.5 + Math.random() * 0.5));

							Color color = Psi.proxy.getCADColor(cad);
							float r = (float) color.getRed() / 255F;
							float g = (float) color.getGreen() / 255F;
							float b = (float) color.getBlue() / 255F;
							for(int i = 0; i < particles; i++) {
								double x = player.posX + ((Math.random() - 0.5) * 2.1) * player.width;
								double y = player.posY - player.getYOffset();
								double z = player.posZ + ((Math.random() - 0.5) * 2.1) * player.width;
								float grav = -0.15F - (float) Math.random() * 0.03F;
								Psi.proxy.sparkleFX(world, x, y, z, r, g, b, grav, 0.25F, 15);
							}

							double x = player.posX;
							double y = player.posY + player.getEyeHeight() - 0.1;
							double z = player.posZ;
							Vector3 lookOrig = new Vector3(player.getLookVec());
							for(int i = 0; i < 25; i++) {
								Vector3 look = lookOrig.copy();
								double spread = 0.25;
								look.x += (Math.random() - 0.5) * spread;
								look.y += (Math.random() - 0.5) * spread;
								look.z += (Math.random() - 0.5) * spread;
								look.normalize().multiply(0.15);

								Psi.proxy.sparkleFX(world, x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 0.3F, 5);
							}
						}

						spellContainer.castSpell(bullet, context);
					} else if(!world.isRemote)
						player.addChatComponentMessage(new ChatComponentTranslation("psimisc.weakCad").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
				}
			}
		}
	}
	
	public static boolean craft(EntityPlayer player, ItemStack in, ItemStack out) {
		List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.posX - 8, player.posY - 8, player.posZ - 8, player.posX + 8, player.posY + 8, player.posZ + 8));

		Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);
		float r = (float) color.getRed() / 255F;
		float g = (float) color.getGreen() / 255F;
		float b = (float) color.getBlue() / 255F;

		
		boolean did = false;
		for(EntityItem item : items) {
			ItemStack stack = item.getEntityItem();
			if(stack != null && ItemStack.areItemsEqual(stack, in)) {
				ItemStack outCopy = out.copy();
				outCopy.stackSize = stack.stackSize;
				item.setEntityItemStack(outCopy);
				did = true;
				
				for(int i = 0; i < 5; i++) {
					double x = item.posX + ((Math.random() - 0.5) * 2.1) * item.width;
					double y = item.posY - item.getYOffset();
					double z = item.posZ + ((Math.random() - 0.5) * 2.1) * item.width;
					float grav = -0.05F - (float) Math.random() * 0.01F;
					Psi.proxy.sparkleFX(item.worldObj, x, y, z, r, g, b, grav, 3.5F, 15);
					
					double m = 0.01;
					double d3 = 10.0D;
					for(int j = 0; j < 3; j++) {
						double d0 = item.worldObj.rand.nextGaussian() * m;
						double d1 = item.worldObj.rand.nextGaussian() * m;
						double d2 = item.worldObj.rand.nextGaussian() * m;
						
						item.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, item.posX + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d0 * d3, item.posY + item.worldObj.rand.nextFloat() * item.height - d1 * d3, item.posZ + item.worldObj.rand.nextFloat() * item.width * 2.0F - item.width - d2 * d3, d0, d1, d2);
					}
				}
			}
		}

		return did;
	}

	public static int getRealCost(ItemStack stack, ItemStack bullet, int cost) {
		if(stack != null && stack.getItem() instanceof ICAD) {
			int eff = ((ICAD) stack.getItem()).getStatValue(stack, EnumCADStat.EFFICIENCY);
			if(eff == -1)
				return -1;
			if(eff == 0)
				return cost;

			double effPercentile = (double) eff / 100;
			double procCost = (double) cost / effPercentile;
			if(bullet != null)
				procCost *= ((ISpellContainer) bullet.getItem()).getCostModifier(bullet);
			
			return (int) procCost;
		}

		return cost;
	}
	
	public static boolean isTruePlayer(Entity e) {
		if(!(e instanceof EntityPlayer))
			return false;

		EntityPlayer player = (EntityPlayer) e;

		String name = player.getName();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
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
	@SideOnly(Side.CLIENT)
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
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if(renderPass == 1)
			return getSpellColor(stack);
		return 0xFFFFFF;
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
				new ItemStack(ModItems.cadCore, 1, 1), 
				new ItemStack(ModItems.cadSocket, 1, 1), 
				new ItemStack(ModItems.cadBattery, 1, 1)));

		// Ebony Psimetal CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 3), 
				new ItemStack(ModItems.cadCore, 1, 3), 
				new ItemStack(ModItems.cadSocket, 1, 3), 
				new ItemStack(ModItems.cadBattery, 1, 2)));

		// Ivory Psimetal CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 4), 
				new ItemStack(ModItems.cadCore, 1, 3), 
				new ItemStack(ModItems.cadSocket, 1, 3), 
				new ItemStack(ModItems.cadBattery, 1, 2)));
		
		// Creative CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 5), 
				new ItemStack(ModItems.cadCore, 1, 3), 
				new ItemStack(ModItems.cadSocket, 1, 3), 
				new ItemStack(ModItems.cadBattery, 1, 2)));
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
						String statValStr = statVal == -1 ?	"\u221E" : ""+statVal; 
						
						line = " " + EnumChatFormatting.AQUA + local(shrt) + EnumChatFormatting.GRAY + ": " + statValStr;
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
		return new ItemMeshDefinition() { // This isn't a Lambda because of a ForgeGradle bug that messes them up
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				ICAD cad = (ICAD) stack.getItem();
				ItemStack assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
				if(assemblyStack == null)
					return null;
				ICADAssembly assembly = (ICADAssembly) assemblyStack.getItem();
				return assembly.getCADModel(assemblyStack, stack);
			}
		};
	}

}
