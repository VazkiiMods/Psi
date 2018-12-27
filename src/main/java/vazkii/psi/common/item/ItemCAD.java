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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.PsiCreativeTab;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.crafting.recipe.AssemblyScavengeRecipe;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibItemNames;

public class ItemCAD extends ItemMod implements ICAD, ISpellSettable, IItemColorProvider, IPsiItem {

	private static final String TAG_COMPONENT_PREFIX = "component";
	private static final String TAG_STORED_PSI = "storedPsi";
	private static final String TAG_BULLET_PREFIX = "bullet";
	private static final String TAG_SELECTED_SLOT = "selectedSlot";
	private static final String TAG_TIME = "time";

	private static final String TAG_STORED_VECTOR_PREFIX = "storedVector";
	private static final String TAG_X = "x";
	private static final String TAG_Y = "y";
	private static final String TAG_Z = "z";
	
	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

	public ItemCAD() {
		super(LibItemNames.CAD);
		setMaxStackSize(1);

		new AssemblyScavengeRecipe();
		setCreativeTab(PsiCreativeTab.INSTANCE);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		Block block = worldIn.getBlockState(pos).getBlock(); 
		return block == ModBlocks.programmer ? ((BlockProgrammer) block).setSpell(worldIn, pos, playerIn, stack) : EnumActionResult.PASS;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		PlayerData data = PlayerDataHandler.get(playerIn);
		ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
		if(playerCad != itemStackIn) {
			if(!worldIn.isRemote)
				playerIn.sendMessage(new TextComponentTranslation("psimisc.multipleCads").setStyle(new Style().setColor(TextFormatting.RED)));
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}

		ItemStack bullet = getBulletInSocket(itemStackIn, getSelectedSlot(itemStackIn));
		boolean did = cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, null);

		if(!data.overflowed && bullet.isEmpty() && craft(playerIn, new ItemStack(Items.REDSTONE), new ItemStack(ModItems.material))) {
			if(!worldIn.isRemote)
				worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 0.5F, (float) (0.5 + Math.random() * 0.5));
			data.deductPsi(100, 60, true);

			if(data.level == 0)
				data.levelUp();
			did = true;
		}

		return new ActionResult<ItemStack>(did ? EnumActionResult.SUCCESS : EnumActionResult.PASS, itemStackIn);
	}

	@Override
	public void setSpell(EntityPlayer player, ItemStack stack, Spell spell) {
		int slot = getSelectedSlot(stack);
		ItemStack bullet = getBulletInSocket(stack, slot);
		if(!bullet.isEmpty() && bullet.getItem() instanceof ISpellSettable) {
			((ISpellSettable) bullet.getItem()).setSpell(player, bullet, spell);
			setBulletInSocket(stack, slot, bullet);
			player.getCooldownTracker().setCooldown(stack.getItem(), 10);
		}
	}

	public static boolean cast(World world, EntityPlayer player, PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate) {
		if(!data.overflowed && data.getAvailablePsi() > 0 && !cad.isEmpty() && !bullet.isEmpty() && bullet.getItem() instanceof ISpellContainer && isTruePlayer(player)) {
			ISpellContainer spellContainer = (ISpellContainer) bullet.getItem();
			if(spellContainer.containsSpell(bullet)) {
				Spell spell = spellContainer.getSpell(bullet);
				SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
				if(predicate != null)
					predicate.accept(context);

				if(context.isValid()) {
					if(context.cspell.metadata.evaluateAgainst(cad)) {
						int cost = getRealCost(cad, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST));
						PreSpellCastEvent event = new PreSpellCastEvent(cost, sound, particles, cd, spell, context, player, data, cad, bullet);
						if(!MinecraftForge.EVENT_BUS.post(event)) {
							String cancelMessage = event.getCancellationMessage();
							if(cancelMessage != null && !cancelMessage.isEmpty())
								player.sendMessage(new TextComponentTranslation(cancelMessage).setStyle(new Style().setColor(TextFormatting.RED)));
							return false;
						}

						cd = event.getCooldown();
						particles = event.getParticles();
						sound = event.getSound();
						cost = event.getCost();

						spell = event.getSpell();
						context = event.getContext();

						if(cost > 0)
							data.deductPsi(cost, cd, true);

						if(cost != 0 && sound > 0) {
							if(!world.isRemote)
								world.playSound(null, player.posX, player.posY, player.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, sound, (float) (0.5 + Math.random() * 0.5));
							else {
								Color color = Psi.proxy.getCADColor(cad);
								float r = color.getRed() / 255F;
								float g = color.getGreen() / 255F;
								float b = color.getBlue() / 255F;
								for(int i = 0; i < particles; i++) {
									double x = player.posX + (Math.random() - 0.5) * 2.1 * player.width;
									double y = player.posY - player.getYOffset();
									double z = player.posZ + (Math.random() - 0.5) * 2.1 * player.width;
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
						}

						spellContainer.castSpell(bullet, context);
						MinecraftForge.EVENT_BUS.post(new SpellCastEvent(spell, context, player, data, cad, bullet));
						return true;
					} else if(!world.isRemote)
						player.sendMessage(new TextComponentTranslation("psimisc.weakCad").setStyle(new Style().setColor(TextFormatting.RED)));
				}
			}
		}
		
		return false;
	}

	public static boolean craft(EntityPlayer player, ItemStack in, ItemStack out) {
		List<EntityItem> items = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.posX - 8, player.posY - 8, player.posZ - 8, player.posX + 8, player.posY + 8, player.posZ + 8));

		Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);
		float r = color.getRed() / 255F;
		float g = color.getGreen() / 255F;
		float b = color.getBlue() / 255F;


		boolean did = false;
		for(EntityItem item : items) {
			ItemStack stack = item.getItem();
			if(!stack.isEmpty() && ItemStack.areItemsEqual(stack, in)) {
				ItemStack outCopy = out.copy();
				outCopy.setCount(stack.getCount());
				item.setItem(outCopy);
				did = true;

				for(int i = 0; i < 5; i++) {
					double x = item.posX + (Math.random() - 0.5) * 2.1 * item.width;
					double y = item.posY - item.getYOffset();
					double z = item.posZ + (Math.random() - 0.5) * 2.1 * item.width;
					float grav = -0.05F - (float) Math.random() * 0.01F;
					Psi.proxy.sparkleFX(item.getEntityWorld(), x, y, z, r, g, b, grav, 3.5F, 15);

					double m = 0.01;
					double d3 = 10.0D;
					for(int j = 0; j < 3; j++) {
						double d0 = item.getEntityWorld().rand.nextGaussian() * m;
						double d1 = item.getEntityWorld().rand.nextGaussian() * m;
						double d2 = item.getEntityWorld().rand.nextGaussian() * m;

						item.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, item.posX + item.getEntityWorld().rand.nextFloat() * item.width * 2.0F - item.width - d0 * d3, item.posY + item.getEntityWorld().rand.nextFloat() * item.height - d1 * d3, item.posZ + item.getEntityWorld().rand.nextFloat() * item.width * 2.0F - item.width - d2 * d3, d0, d1, d2);
					}
				}
			}
		}

		return did;
	}

	public static int getRealCost(ItemStack stack, ItemStack bullet, int cost) {
		if(!stack.isEmpty() && stack.getItem() instanceof ICAD) {
			int eff = ((ICAD) stack.getItem()).getStatValue(stack, EnumCADStat.EFFICIENCY);
			if(eff == -1)
				return -1;
			if(eff == 0)
				return cost;

			double effPercentile = (double) eff / 100;
			double procCost = cost / effPercentile;
			if(!bullet.isEmpty())
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
		if(!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			EnumCADComponent componentType = component.getComponentType(componentStack);
			String name = TAG_COMPONENT_PREFIX + componentType.name();

			NBTTagCompound cmp = new NBTTagCompound();
			componentStack.writeToNBT(cmp);
			ItemNBTHelper.setCompound(stack, name, cmp);
		}
	}

	public static ItemStack makeCAD(ItemStack... components) {
		return makeCAD(Arrays.asList(components));
	}
	
	public static ItemStack makeCAD(List<ItemStack> components) {
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
			return ItemStack.EMPTY;

		return new ItemStack(cmp);
	}

	@Override
	public int getStatValue(ItemStack stack, EnumCADStat stat) {
		ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
		if(!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
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
		if(!dye.isEmpty() && dye.getItem() instanceof ICADColorizer)
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
			return ItemStack.EMPTY;

		return new ItemStack(cmp);
	}

	@Override
	public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		String name = TAG_BULLET_PREFIX + slot;
		NBTTagCompound cmp = new NBTTagCompound();

		if(!bullet.isEmpty())
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
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColor() {
		return new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				return tintIndex == 1 ? getSpellColor(stack) : 0xFFFFFF;
			}
		};
	}
	
	@Override
	public int getTime(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_TIME, 0);
	}
	
	@Override
	public void incrementTime(ItemStack stack) {
		int time = getTime(stack);
		ItemNBTHelper.setInt(stack, TAG_TIME, time + 1);
	}
	
	@Override
	public int getMemorySize(ItemStack stack) {
		return getStatValue(stack, EnumCADStat.SOCKETS) / 3;
	}
	
	@Override
	public void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException {
		int size = getMemorySize(stack);
		if(memorySlot < 0 || memorySlot >= size)
			throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
		
		NBTTagCompound cmp = new NBTTagCompound();
		cmp.setDouble(TAG_X, vec.x);
		cmp.setDouble(TAG_Y, vec.y);
		cmp.setDouble(TAG_Z, vec.z);
		ItemNBTHelper.setCompound(stack, TAG_STORED_VECTOR_PREFIX + memorySlot, cmp);
	}

	@Override
	public Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException {
		int size = getMemorySize(stack);
		if(memorySlot < 0 || memorySlot >= size)
			throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
		
		NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, TAG_STORED_VECTOR_PREFIX + memorySlot, false);
		double x = cmp.getDouble(TAG_X);
		double y = cmp.getDouble(TAG_Y);
		double z = cmp.getDouble(TAG_Z);
		return new Vector3(x, y, z);
	}
	
	@Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
    	return ConfigHandler.cadHarvestLevel;
    }
	
	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return ImmutableSet.of("pickaxe", "axe", "shovel");
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		Block block = state.getBlock();
		int level = block.getHarvestLevel(state);
		return getHarvestLevel(stack, "", null, null) >= level; 
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if(!isInCreativeTab(tab))
			return;
		
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
	public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
		tooltipIfShift(tooltip, () -> {
			String componentName = local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
			addToTooltip(tooltip, "psimisc.spellSelected", componentName);

			for(EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
				ItemStack componentStack = getComponentInSlot(stack, componentType);
				String name = "psimisc.none";
				if(!componentStack.isEmpty())
					name = componentStack.getDisplayName();

				name = local(name);
				String line = TextFormatting.GREEN + local(componentType.getName()) + TextFormatting.GRAY + ": " + name;
				addToTooltip(tooltip, line);

				line = "";
				for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
					if(stat.getSourceType() == componentType) {
						String shrt = stat.getName();
						int statVal = getStatValue(stack, stat);
						String statValStr = statVal == -1 ?	"\u221E" : ""+statVal;

						line = " " + TextFormatting.AQUA + local(shrt) + TextFormatting.GRAY + ": " + statValStr;
						if(!line.isEmpty())
							addToTooltip(tooltip, line);
					}
				}
			}
		});
	}

	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return true;
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
				if(assemblyStack.isEmpty())
					return null;
				ICADAssembly assembly = (ICADAssembly) assemblyStack.getItem();
				return assembly.getCADModel(assemblyStack, stack);
			}
		};
	}

}
