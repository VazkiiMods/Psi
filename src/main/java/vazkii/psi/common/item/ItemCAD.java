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

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.item.BasicItem;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageCADDataSync;
import vazkii.psi.common.network.message.MessageVisualEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemCAD extends BasicItem implements ICAD, ISpellSettable, IItemColorProvider {

	private static final String TAG_BULLET_PREFIX = "bullet";
	private static final String TAG_SELECTED_SLOT = "selectedSlot";

	// Legacy tags
	private static final String TAG_TIME_LEGACY = "time";
	private static final String TAG_STORED_PSI_LEGACY = "storedPsi";

	private static final String TAG_X_LEGACY = "x";
	private static final String TAG_Y_LEGACY = "y";
	private static final String TAG_Z_LEGACY = "z";
	private static final Pattern VECTOR_PREFIX_PATTERN = Pattern.compile("^storedVector(\\d+)$");

	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*])|(?:ComputerCraft)$");

	public ItemCAD(String name, Item.Properties properties) {
		super(name, properties.maxStackSize(1));
	}

	private ICADData getCADData(ItemStack stack) {
		return stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).orElseGet(CADData::new);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		CADData data = new CADData();
		if (nbt != null && nbt.contains("Parent", Constants.NBT.TAG_COMPOUND))
			data.deserializeNBT(nbt.getCompound("Parent"));
		return data;
	}


	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
		CompoundNBT compound = ItemNBTHelper.getNBT(stack);


		stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> {
			if (compound.contains(TAG_TIME_LEGACY, Constants.NBT.TAG_ANY_NUMERIC)) {
				data.setTime(compound.getInt(TAG_TIME_LEGACY));
				data.markDirty(true);
				compound.remove(TAG_TIME_LEGACY);
			}

			if (compound.contains(TAG_STORED_PSI_LEGACY, Constants.NBT.TAG_ANY_NUMERIC)) {
				data.setBattery(compound.getInt(TAG_STORED_PSI_LEGACY));
				data.markDirty(true);
				compound.remove(TAG_STORED_PSI_LEGACY);
			}

			Set<String> keys = new HashSet<>(compound.keySet());

			for (String key : keys) {
				Matcher matcher = VECTOR_PREFIX_PATTERN.matcher(key);
				if (matcher.find()) {
					CompoundNBT vec = compound.getCompound(key);
					compound.remove(key);
					int memory = Integer.parseInt(matcher.group(1));
					Vector3 vector = new Vector3(vec.getDouble(TAG_X_LEGACY),
							vec.getDouble(TAG_Y_LEGACY),
							vec.getDouble(TAG_Z_LEGACY));
					data.setSavedVector(memory, vector);
				}
			}

			if (entityIn instanceof ServerPlayerEntity && data.isDirty()) {
				MessageRegister.HANDLER.sendToPlayer(new MessageCADDataSync(data), (ServerPlayerEntity) entityIn);
				data.markDirty(false);
			}
		});
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World worldIn = ctx.getWorld();
		Hand hand = ctx.getHand();
		BlockPos pos = ctx.getPos();
		PlayerEntity playerIn = ctx.getPlayer();
		ItemStack stack = playerIn.getHeldItem(hand);
		Block block = worldIn.getBlockState(pos).getBlock();
		return block == ModBlocks.programmer ? ((BlockProgrammer) block).setSpell(worldIn, pos, playerIn, stack) : ActionResultType.PASS;
	}


	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		PlayerData data = PlayerDataHandler.get(playerIn);
		ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
		if (playerCad != itemStackIn) {
			if (!worldIn.isRemote)
				playerIn.sendMessage(new TranslationTextComponent("psimisc.multiple_cads").setStyle(new Style().setColor(TextFormatting.RED)));
			return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
		}

		ItemStack bullet = getBulletInSocket(itemStackIn, getSelectedSlot(itemStackIn));
		boolean did = cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, ctx -> ctx.castFrom = hand);

		if (!data.overflowed && bullet.isEmpty() && craft(playerIn, Tags.Items.DUSTS_REDSTONE, new ItemStack(ModItems.psidust))) {
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 0.5F, (float) (0.5 + Math.random() * 0.5));
			data.deductPsi(100, 60, true);

			if (!data.hasAdvancement(LibPieceGroups.FAKE_LEVEL_PSIDUST))
				data.tutorialComplete(LibPieceGroups.FAKE_LEVEL_PSIDUST);
			did = true;
		}

		return new ActionResult<>(did ? ActionResultType.PASS : ActionResultType.PASS, itemStackIn);
	}

	@Override
	public void setSpell(PlayerEntity player, ItemStack stack, Spell spell) {
		int slot = getSelectedSlot(stack);
		ItemStack bullet = getBulletInSocket(stack, slot);
		if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
			ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
			setBulletInSocket(stack, slot, bullet);
			player.getCooldownTracker().setCooldown(stack.getItem(), 10);
		}
	}

	public static boolean cast(World world, PlayerEntity player, PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate) {
		if (!data.overflowed && data.getAvailablePsi() > 0 && !cad.isEmpty() && !bullet.isEmpty() && ISpellAcceptor.hasSpell(bullet) && isTruePlayer(player)) {
			ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(bullet);
			Spell spell = spellContainer.getSpell();
			SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
			if (predicate != null)
				predicate.accept(context);

			if (context.isValid()) {
				if (context.cspell.metadata.evaluateAgainst(cad)) {
					int cost = getRealCost(cad, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST));
					PreSpellCastEvent event = new PreSpellCastEvent(cost, sound, particles, cd, spell, context, player, data, cad, bullet);
					if (MinecraftForge.EVENT_BUS.post(event)) {
						String cancelMessage = event.getCancellationMessage();
						if (cancelMessage != null && !cancelMessage.isEmpty())
							player.sendMessage(new TranslationTextComponent(cancelMessage).setStyle(new Style().setColor(TextFormatting.RED)));
						return false;
					}

					cd = event.getCooldown();
					particles = event.getParticles();
					sound = event.getSound();
					cost = event.getCost();

					spell = event.getSpell();
					context = event.getContext();

					if (cost > 0)
						data.deductPsi(cost, cd, true);

					if (cost != 0 && sound > 0) {
						if (!world.isRemote)
							world.playSound(null, player.getX(), player.getY(), player.getZ(), PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, sound, (float) (0.5 + Math.random() * 0.5));
						else {
							int color = Psi.proxy.getColorForCAD(cad);
							float r = PsiRenderHelper.r(color) / 255F;
							float g = PsiRenderHelper.g(color) / 255F;
							float b = PsiRenderHelper.b(color) / 255F;
							for (int i = 0; i < particles; i++) {
								double x = player.getX() + (Math.random() - 0.5) * 2.1 * player.getWidth();
								double y = player.getY() - player.getYOffset();
								double z = player.getZ() + (Math.random() - 0.5) * 2.1 * player.getWidth();
								float grav = -0.15F - (float) Math.random() * 0.03F;
								Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
							}

							double x = player.getX();
							double y = player.getY() + player.getEyeHeight() - 0.1;
							double z = player.getZ();
							Vector3 lookOrig = new Vector3(player.getLookVec());
							for (int i = 0; i < 25; i++) {
								Vector3 look = lookOrig.copy();
								double spread = 0.25;
								look.x += (Math.random() - 0.5) * spread;
								look.y += (Math.random() - 0.5) * spread;
								look.z += (Math.random() - 0.5) * spread;
								look.normalize().multiply(0.15);

								Psi.proxy.sparkleFX(x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 0.3F, 5);
							}
						}
					}

					if (!world.isRemote)
						spellContainer.castSpell(context);
					MinecraftForge.EVENT_BUS.post(new SpellCastEvent(spell, context, player, data, cad, bullet));
					return true;
				} else if (!world.isRemote)
					player.sendMessage(new TranslationTextComponent("psimisc.weak_cad").setStyle(new Style().setColor(TextFormatting.RED)));
			}
		}

		return false;
	}

	public static boolean craft(PlayerEntity player, ItemStack in, ItemStack out) {
		return craft(player, Ingredient.fromStacks(in), out);
	}

	public static boolean craft(PlayerEntity player, Tag<Item> in, ItemStack out) {
		return craft(player, Ingredient.fromTag(in), out);
	}

	public static boolean craft(PlayerEntity player, Ingredient in, ItemStack out) {
		if (player.world.isRemote)
			return false;

		List<ItemEntity> items = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class,
				player.getBoundingBox().grow(8),
				entity -> entity != null && entity.getDistanceSq(player) <= 8 * 8);

		boolean did = false;
		for(ItemEntity item : items) {
			ItemStack stack = item.getItem();
			if(in.test(stack)) {
				ItemStack outCopy = out.copy();
				outCopy.setCount(stack.getCount());
				item.setItem(outCopy);
				did = true;
				MessageRegister.sendToAllAround(new MessageVisualEffect(ICADColorizer.DEFAULT_SPELL_COLOR,
								item.getX(), item.getY(), item.getZ(), item.getWidth(), item.getHeight(), item.getYOffset(),
								MessageVisualEffect.TYPE_CRAFT)
						, item.getPosition(), item.getEntityWorld(), 32);
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
			if(!bullet.isEmpty() && ISpellAcceptor.isContainer(bullet))
				procCost *= ISpellAcceptor.acceptor(bullet).getCostModifier();

			return (int) procCost;
		}

		return cost;
	}

	public static boolean isTruePlayer(Entity e) {
		if (!(e instanceof PlayerEntity))
			return false;

		PlayerEntity player = (PlayerEntity) e;

		String name = player.getName().getString();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}

	public static void setComponent(ItemStack stack, ItemStack componentStack) {
		if (stack.getItem() instanceof ICAD)
			((ICAD) stack.getItem()).setCADComponent(stack, componentStack);
	}

	public static ItemStack makeCAD(ItemStack... components) {
		return makeCAD(Arrays.asList(components));
	}

	public static ItemStack makeCADWithAssembly(ItemStack assembly, List<ItemStack> components) {
		ItemStack cad = assembly.getItem() instanceof ICADAssembly ?
				((ICADAssembly) assembly.getItem()).createCADStack(assembly, components) :
				new ItemStack(ModItems.cad);

		return makeCAD(cad, components);
	}

	public static ItemStack makeCAD(List<ItemStack> components) {
		return makeCAD(new ItemStack(ModItems.cad), components);
	}

	public static ItemStack makeCAD(ItemStack base, List<ItemStack> components) {
		ItemStack stack = base.copy();
		for(ItemStack component : components)
			setComponent(stack, component);
		return stack;
	}

	@Override
	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
		String name = TAG_COMPONENT_PREFIX + type.name();
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, name, true);

		if (cmp == null)
			return ItemStack.EMPTY;

		return ItemStack.read(cmp);
	}

	@Override
	public int getStatValue(ItemStack stack, EnumCADStat stat) {
		int statValue = 0;
		ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
		if(!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
			ICADComponent component = (ICADComponent) componentStack.getItem();
			statValue = component.getCADStatValue(componentStack, stat);
		}

		CADStatEvent event = new CADStatEvent(stat, stack, componentStack, statValue);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getStatValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public int getSpellColor(ItemStack stack) {
		ItemStack dye = getComponentInSlot(stack, EnumCADComponent.DYE);
		if(!dye.isEmpty() && dye.getItem() instanceof ICADColorizer)
			return ((ICADColorizer) dye.getItem()).getColor(dye);

		return ICADColorizer.DEFAULT_SPELL_COLOR;
	}

	@Override
	public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
		int sockets = getStatValue(stack, EnumCADStat.SOCKETS);
		if (sockets == -1 || sockets > ItemCADSocket.MAX_SOCKETS)
			sockets = ItemCADSocket.MAX_SOCKETS;
		return slot < sockets;
	}

	@Override
	public ItemStack getBulletInSocket(ItemStack stack, int slot) {
		String name = TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = ItemNBTHelper.getCompound(stack, name, true);

		if (cmp == null)
			return ItemStack.EMPTY;

		return ItemStack.read(cmp);
	}

	@Override
	public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
		String name = TAG_BULLET_PREFIX + slot;
		CompoundNBT cmp = new CompoundNBT();

		if (!bullet.isEmpty())
			bullet.write(cmp);

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
	@OnlyIn(Dist.CLIENT)
	public IItemColor getItemColor() {
		return (stack, tintIndex) -> tintIndex == 1 ? getSpellColor(stack) : 0xFFFFFF;
	}
	
	@Override
	public int getTime(ItemStack stack) {
		return getCADData(stack).getTime();
	}
	
	@Override
	public void incrementTime(ItemStack stack) {
		ICADData data = getCADData(stack);
		data.setTime(data.getTime() + 1);
	}

	@Override
	public int getStoredPsi(ItemStack stack) {
		int maxPsi = getStatValue(stack, EnumCADStat.OVERFLOW);

		return Math.min(getCADData(stack).getBattery(), maxPsi);
	}

	@Override
	public void regenPsi(ItemStack stack, int psi) {
		int maxPsi = getStatValue(stack, EnumCADStat.OVERFLOW);
		if (maxPsi == -1)
			return;

		int currPsi = getStoredPsi(stack);
		int endPsi = Math.min(currPsi + psi, maxPsi);

		if(endPsi != currPsi) {
			ICADData data = getCADData(stack);
			data.setBattery(endPsi);
			data.markDirty(true);
		}
	}

	@Override
	public int consumePsi(ItemStack stack, int psi) {
		if (psi == 0)
			return 0;

		int currPsi = getStoredPsi(stack);

		if (currPsi == -1)
			return 0;

		ICADData data = getCADData(stack);

		if (currPsi >= psi) {
			data.setBattery(currPsi - psi);
			data.markDirty(true);
			return 0;
		}

		data.setBattery(0);
		data.markDirty(true);
		return psi - currPsi;
	}

	@Override
	public int getMemorySize(ItemStack stack) {
		int sockets = getStatValue(stack, EnumCADStat.SOCKETS);
		if (sockets == -1)
			return 0xFF;
		return sockets / 3;
	}
	
	@Override
	public void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException {
		int size = getMemorySize(stack);
		if(memorySlot < 0 || memorySlot >= size)
			throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
		getCADData(stack).setSavedVector(memorySlot, vec);
	}

	@Override
	public Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException {
		int size = getMemorySize(stack);
		if(memorySlot < 0 || memorySlot >= size)
			throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
		return getCADData(stack).getSavedVector(memorySlot);
	}


	@Override
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> subItems) {
		if (!isInGroup(tab))
			return;


		// Basic Iron CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyIron)));

		// Iron CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyIron),
				new ItemStack(ModItems.cadCoreBasic),
				new ItemStack(ModItems.cadSocketBasic),
				new ItemStack(ModItems.cadBatteryBasic)));

		// Gold CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyGold),
				new ItemStack(ModItems.cadCoreBasic),
				new ItemStack(ModItems.cadSocketBasic),
				new ItemStack(ModItems.cadBatteryBasic)));


		// Psimetal CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyPsimetal),
				new ItemStack(ModItems.cadCoreOverclocked),
				new ItemStack(ModItems.cadSocketSignaling),
				new ItemStack(ModItems.cadBatteryExtended)));

		// Ebony Psimetal CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyEbony),
				new ItemStack(ModItems.cadCoreHyperClocked),
				new ItemStack(ModItems.cadSocketTransmissive),
				new ItemStack(ModItems.cadBatteryUltradense)));

		// Ivory Psimetal CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyIvory),
				new ItemStack(ModItems.cadCoreHyperClocked),
				new ItemStack(ModItems.cadSocketTransmissive),
				new ItemStack(ModItems.cadBatteryUltradense)));


		// Creative CAD
		subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyCreative),
				new ItemStack(ModItems.cadCoreHyperClocked),
				new ItemStack(ModItems.cadSocketTransmissive),
				new ItemStack(ModItems.cadBatteryUltradense)));

	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World playerin, List<ITextComponent> tooltip, ITooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
			tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));

			for (EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
				ItemStack componentStack = getComponentInSlot(stack, componentType);
				ITextComponent name = new TranslationTextComponent("psimisc.none");
				if (!componentStack.isEmpty())
					name = componentStack.getDisplayName();

				ITextComponent componentTypeName = new TranslationTextComponent(componentType.getName()).applyTextStyle(TextFormatting.GREEN);
				tooltip.add(componentTypeName.appendText(": ").appendSibling(name));

				for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
					if (stat.getSourceType() == componentType) {
						String shrt = stat.getName();
						int statVal = getStatValue(stack, stat);
						String statValStr = statVal == -1 ? "\u221E" : "" + statVal;

						tooltip.add(new TranslationTextComponent(shrt).applyTextStyle(TextFormatting.AQUA).appendText(": " + statValStr));
					}
				}
			}
		});
	}

	@Override
	public boolean requiresSneakForSpellSet(ItemStack stack) {
		return true;
	}

	@Nonnull
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.RARE;
	}


}
