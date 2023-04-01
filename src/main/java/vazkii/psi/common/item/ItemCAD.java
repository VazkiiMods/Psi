/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.network.PacketDistributor;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.PieceGroupAdvancementComplete;
import vazkii.psi.api.spell.PreSpellCastEvent;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCastEvent;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageCADDataSync;
import vazkii.psi.common.network.message.MessageVisualEffect;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemCAD extends Item implements ICAD {

	// Legacy tags
	private static final String TAG_TIME_LEGACY = "time";
	private static final String TAG_STORED_PSI_LEGACY = "storedPsi";

	private static final String TAG_X_LEGACY = "x";
	private static final String TAG_Y_LEGACY = "y";
	private static final String TAG_Z_LEGACY = "z";
	private static final Pattern VECTOR_PREFIX_PATTERN = Pattern.compile("^storedVector(\\d+)$");

	private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*])|(?:ComputerCraft)$");

	public ItemCAD(Item.Properties properties) {
		super(properties
				.stacksTo(1)
		);
	}

	private ICADData getCADData(ItemStack stack) {
		return stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).orElseGet(() -> new CADData(stack));
	}

	private ISocketable getSocketable(ItemStack stack) {
		return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseGet(() -> new CADData(stack));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		CADData data = new CADData(stack);
		if (nbt != null && nbt.contains("Parent", Tag.TAG_COMPOUND)) {
			data.deserializeNBT(nbt.getCompound("Parent"));
		}
		return data;
	}

	@Override
	public @Nullable CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> nbt.put("CapabalityData", data.serializeNBT()));
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		super.readShareTag(stack, nbt);

		if (nbt != null) {
			stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> data.deserializeNBT(nbt.getCompound("CapabilityData")));
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entityIn, int itemSlot, boolean isSelected) {
		CompoundTag compound = stack.getOrCreateTag();

		stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> {
			if (compound.contains(TAG_TIME_LEGACY, Tag.TAG_ANY_NUMERIC)) {
				data.setTime(compound.getInt(TAG_TIME_LEGACY));
				data.markDirty(true);
				compound.remove(TAG_TIME_LEGACY);
			}

			if (compound.contains(TAG_STORED_PSI_LEGACY, Tag.TAG_ANY_NUMERIC)) {
				data.setBattery(compound.getInt(TAG_STORED_PSI_LEGACY));
				data.markDirty(true);
				compound.remove(TAG_STORED_PSI_LEGACY);
			}

			Set<String> keys = new HashSet<>(compound.getAllKeys());

			for (String key : keys) {
				Matcher matcher = VECTOR_PREFIX_PATTERN.matcher(key);
				if (matcher.find()) {
					CompoundTag vec = compound.getCompound(key);
					compound.remove(key);
					int memory = Integer.parseInt(matcher.group(1));
					Vector3 vector = new Vector3(vec.getDouble(TAG_X_LEGACY),
							vec.getDouble(TAG_Y_LEGACY),
							vec.getDouble(TAG_Z_LEGACY));
					data.setSavedVector(memory, vector);
				}
			}

			if (entityIn instanceof ServerPlayer && data.isDirty()) {
				ServerPlayer player = (ServerPlayer) entityIn;
				MessageRegister.sendToPlayer(new MessageCADDataSync(data), player);
				data.markDirty(false);
			}
		});
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level worldIn = ctx.getLevel();
		InteractionHand hand = ctx.getHand();
		BlockPos pos = ctx.getClickedPos();
		Player playerIn = ctx.getPlayer();
		ItemStack stack = playerIn.getItemInHand(hand);
		Block block = worldIn.getBlockState(pos).getBlock();
		return block == ModBlocks.programmer ? ((BlockProgrammer) block).setSpell(worldIn, pos, playerIn, stack) : InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand hand) {
		ItemStack itemStackIn = playerIn.getItemInHand(hand);
		PlayerData data = PlayerDataHandler.get(playerIn);
		ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
		if (playerCad != itemStackIn) {
			if (!worldIn.isClientSide) {
				playerIn.sendSystemMessage(Component.translatable("psimisc.multiple_cads").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
			}
			return new InteractionResultHolder<>(InteractionResult.CONSUME, itemStackIn);
		}
		ISocketable sockets = getSocketable(playerCad);

		ItemStack bullet = sockets.getSelectedBullet();
		if (!getComponentInSlot(playerCad, EnumCADComponent.DYE).isEmpty() && ContributorSpellCircleHandler.isContributor(playerIn.getName().getString().toLowerCase(Locale.ROOT))) {
			ItemStack dyeStack = getComponentInSlot(playerCad, EnumCADComponent.DYE);
			if (!((ICADColorizer) dyeStack.getItem()).getContributorName(dyeStack).equalsIgnoreCase(playerIn.getName().getString())) {
				((ICADColorizer) dyeStack.getItem()).setContributorName(dyeStack, playerIn.getName().getString());
				setCADComponent(playerCad, dyeStack);
			}
		}
		boolean did = cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, ctx -> ctx.castFrom = hand).isPresent();

		if (!data.overflowed && bullet.isEmpty() && craft(playerCad, playerIn, null)) {
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), PsiSoundHandler.cadShoot, SoundSource.PLAYERS, 0.5F, (float) (0.5 + Math.random() * 0.5));
			data.deductPsi(100, 60, true);

			if (!data.hasAdvancement(LibPieceGroups.FAKE_LEVEL_PSIDUST)) {
				MinecraftForge.EVENT_BUS.post(new PieceGroupAdvancementComplete(null, playerIn, LibPieceGroups.FAKE_LEVEL_PSIDUST));
			}
			did = true;
		}

		return new InteractionResultHolder<>(did ? InteractionResult.CONSUME : InteractionResult.PASS, itemStackIn);
	}

	public static Optional<ArrayList<Entity>> cast(Level world, Player player, PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate) {
		return cast(world, player, data, bullet, cad, cd, particles, sound, predicate, 0);
	}

	public static Optional<ArrayList<Entity>> cast(Level world, Player player, PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate, int reservoir) {
		if (!data.overflowed && data.getAvailablePsi() > 0 && !cad.isEmpty() && !bullet.isEmpty() && ISpellAcceptor.hasSpell(bullet) && isTruePlayer(player)) {
			ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(bullet);
			Spell spell = spellContainer.getSpell();
			SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
			if (predicate != null) {
				predicate.accept(context);
			}

			if (context.isValid()) {
				if (context.cspell.metadata.evaluateAgainst(cad)) {
					int cost = Math.max(getRealCost(cad, bullet, context.cspell.metadata.getStat(EnumSpellStat.COST)) - reservoir, 0);
					PreSpellCastEvent event = new PreSpellCastEvent(cost, sound, particles, cd, spell, context, player, data, cad, bullet);
					if (MinecraftForge.EVENT_BUS.post(event)) {
						String cancelMessage = event.getCancellationMessage();
						if (cancelMessage != null && !cancelMessage.isEmpty()) {
							player.sendSystemMessage(Component.translatable(cancelMessage).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
						}
						return Optional.empty();
					}

					cd = event.getCooldown();
					particles = event.getParticles();
					sound = event.getSound();
					cost = event.getCost();

					spell = event.getSpell();
					context = event.getContext();

					if (cost > 0) {
						data.deductPsi(cost, cd, true);
					}

					if (cost != 0 && sound > 0) {
						if (!world.isClientSide) {
							world.playSound(null, player.getX(), player.getY(), player.getZ(), PsiSoundHandler.cadShoot, SoundSource.PLAYERS, sound, (float) (0.5 + Math.random() * 0.5));
						} else {
							int color = Psi.proxy.getColorForCAD(cad);
							float r = PsiRenderHelper.r(color) / 255F;
							float g = PsiRenderHelper.g(color) / 255F;
							float b = PsiRenderHelper.b(color) / 255F;
							for (int i = 0; i < particles; i++) {
								double x = player.getX() + (Math.random() - 0.5) * 2.1 * player.getBbWidth();
								double y = player.getY() - player.getMyRidingOffset();
								double z = player.getZ() + (Math.random() - 0.5) * 2.1 * player.getBbWidth();
								float grav = -0.15F - (float) Math.random() * 0.03F;
								Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
							}

							double x = player.getX();
							double y = player.getY() + player.getEyeHeight() - 0.1;
							double z = player.getZ();
							Vector3 lookOrig = new Vector3(player.getLookAngle());
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
					ArrayList<Entity> SpellEntities = new ArrayList<>();
					if (!world.isClientSide) {
						SpellEntities = spellContainer.castSpell(context);
					}
					MinecraftForge.EVENT_BUS.post(new SpellCastEvent(spell, context, player, data, cad, bullet));
					return Optional.of(SpellEntities);
				} else if (!world.isClientSide) {
					player.sendSystemMessage(Component.translatable("psimisc.weak_cad").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
				}
			}
		}

		return Optional.empty();
	}

	@Override
	public boolean craft(ItemStack cad, Player player, PieceCraftingTrick craftingTrick) {
		Level world = player.level;
		if (world.isClientSide) {
			return false;
		}

		List<ItemEntity> items = player.getCommandSenderWorld().getEntitiesOfClass(ItemEntity.class,
				player.getBoundingBox().inflate(8),
				entity -> entity != null && entity.distanceToSqr(player) <= 8 * 8);

		CraftingWrapper inv = new CraftingWrapper();
		boolean did = false;
		for (ItemEntity item : items) {
			ItemStack stack = item.getItem();
			inv.setStack(stack);
			Predicate<ITrickRecipe> predicate = r -> r.getPiece() == null;
			if (craftingTrick != null) {
				predicate = r -> r.getPiece() == null || r.getPiece().canCraft(craftingTrick);
			}

			Optional<ITrickRecipe> recipe = world.getRecipeManager().getRecipeFor(ModCraftingRecipes.TRICK_RECIPE_TYPE, inv, world)
					.filter(predicate);
			if (recipe.isPresent()) {
				ItemStack outCopy = recipe.get().getResultItem().copy();
				int count = stack.getCount() * outCopy.getCount();
				while (count > 64) {
					int dropCount = world.getRandom().nextInt(32) + 32;
					ItemEntity drop = new ItemEntity(world, item.getX(), item.getY(), item.getZ(),
							new ItemStack(outCopy.getItem(), dropCount));
					Vec3 motion = item.getDeltaMovement();
					drop.setDeltaMovement(motion.x() + (world.getRandom().nextFloat() - 0.5D) / 5,
							motion.y() + (world.getRandom().nextFloat()) / 10,
							motion.z() + (world.getRandom().nextFloat() - 0.5D) / 5);
					world.addFreshEntity(drop);
					count -= dropCount;
				}

				outCopy.setCount(count);
				item.setItem(outCopy);
				did = true;
				MessageVisualEffect msg = new MessageVisualEffect(ICADColorizer.DEFAULT_SPELL_COLOR,
						item.getX(), item.getY(), item.getZ(), item.getBbWidth(), item.getBbHeight(), item.getMyRidingOffset(),
						MessageVisualEffect.TYPE_CRAFT);
				MessageRegister.HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> item), msg);
			}
		}

		return did;
	}

	private static class CraftingWrapper extends RecipeWrapper {
		CraftingWrapper() {
			super(new ItemStackHandler(1));
		}

		void setStack(ItemStack stack) {
			inv.setStackInSlot(0, stack);
		}
	}

	public static int getRealCost(ItemStack stack, ItemStack bullet, int cost) {
		if (!stack.isEmpty() && stack.getItem() instanceof ICAD) {
			int eff = ((ICAD) stack.getItem()).getStatValue(stack, EnumCADStat.EFFICIENCY);
			if (eff == -1) {
				return -1;
			}
			if (eff == 0) {
				return cost;
			}

			double effPercentile = (double) eff / 100;
			double procCost = cost / effPercentile;
			if (!bullet.isEmpty() && ISpellAcceptor.isContainer(bullet)) {
				procCost *= ISpellAcceptor.acceptor(bullet).getCostModifier();
			}

			return (int) procCost;
		}

		return cost;
	}

	public static boolean isTruePlayer(Entity e) {
		if (!(e instanceof Player)) {
			return false;
		}

		Player player = (Player) e;

		String name = player.getName().getString();
		return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
	}

	public static void setComponent(ItemStack stack, ItemStack componentStack) {
		if (stack.getItem() instanceof ICAD) {
			((ICAD) stack.getItem()).setCADComponent(stack, componentStack);
		}
	}

	public static ItemStack makeCAD(ItemStack... components) {
		return makeCAD(Arrays.asList(components));
	}

	public static ItemStack makeCADWithAssembly(ItemStack assembly, List<ItemStack> components) {
		ItemStack cad = assembly.getItem() instanceof ICADAssembly ? ((ICADAssembly) assembly.getItem()).createCADStack(assembly, components) : new ItemStack(ModItems.cad);

		return makeCAD(cad, components);
	}

	public static ItemStack makeCAD(List<ItemStack> components) {
		return makeCAD(new ItemStack(ModItems.cad), components);
	}

	public static ItemStack makeCAD(ItemStack base, List<ItemStack> components) {
		ItemStack stack = base.copy();
		for (ItemStack component : components) {
			setComponent(stack, component);
		}
		return stack;
	}

	@Override
	public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
		String name = TAG_COMPONENT_PREFIX + type.name();
		CompoundTag cmp = stack.getOrCreateTag().getCompound(name);

		if (cmp.isEmpty()) {
			return ItemStack.EMPTY;
		}

		return ItemStack.of(cmp);
	}

	@Override
	public int getStatValue(ItemStack stack, EnumCADStat stat) {
		int statValue = 0;
		ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
		if (!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
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
		if (!dye.isEmpty() && dye.getItem() instanceof ICADColorizer) {
			return ((ICADColorizer) dye.getItem()).getColor(dye);
		}
		return ICADColorizer.DEFAULT_SPELL_COLOR;
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
		if (maxPsi == -1) {
			return;
		}

		int currPsi = getStoredPsi(stack);
		int endPsi = Math.min(currPsi + psi, maxPsi);

		if (endPsi != currPsi) {
			ICADData data = getCADData(stack);
			data.setBattery(endPsi);
			data.markDirty(true);
		}
	}

	@Override
	public int consumePsi(ItemStack stack, int psi) {
		if (psi == 0) {
			return 0;
		}

		int currPsi = getStoredPsi(stack);

		if (currPsi == -1) {
			return 0;
		}

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
		int vectors = getStatValue(stack, EnumCADStat.SAVED_VECTORS);
		if (vectors == -1) {
			return 0xFF;
		}
		return vectors;
	}

	@Override
	public void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException {
		int size = getMemorySize(stack);
		if (memorySlot < 0 || memorySlot >= size) {
			throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
		}
		getCADData(stack).setSavedVector(memorySlot, vec);
	}

	@Override
	public Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException {
		int size = getMemorySize(stack);
		if (memorySlot < 0 || memorySlot >= size) {
			throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
		}
		return getCADData(stack).getSavedVector(memorySlot);
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, @Nonnull BlockState state) {
		if (!PieceTrickBreakBlock.doingHarvestCheck.get()) {
			return super.isCorrectToolForDrops(stack, state);
		}
		int level = ConfigHandler.COMMON.cadHarvestLevel.get(); //TODO revisit for better checking of harvestability
		if (level >= 0) {
			return PieceTrickBreakBlock.canHarvest(level, state);
		}
		return false;
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> subItems) {
		if (!allowedIn(tab)) {
			return;
		}

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
	public void appendHoverText(ItemStack stack, @Nullable Level playerin, List<Component> tooltip, TooltipFlag advanced) {
		TooltipHelper.tooltipIfShift(tooltip, () -> {
			Component componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
			tooltip.add(Component.translatable("psimisc.spell_selected", componentName));

			for (EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
				ItemStack componentStack = getComponentInSlot(stack, componentType);
				Component name = Component.translatable("psimisc.none");
				if (!componentStack.isEmpty()) {
					name = componentStack.getHoverName();
				}

				MutableComponent componentTypeName = Component.translatable(componentType.getName()).withStyle(ChatFormatting.GREEN);
				tooltip.add(componentTypeName.append(": ").append(name));

				for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
					if (stat.getSourceType() == componentType) {
						String shrt = stat.getName();
						int statVal = getStatValue(stack, stat);
						String statValStr = statVal == -1 ? "\u221E" : "" + statVal;

						tooltip.add(Component.translatable(shrt).withStyle(ChatFormatting.AQUA).append(": " + statValStr));
					}
				}
			}
		});
	}

	@Nonnull
	@Override
	public Rarity getRarity(ItemStack stack) {
		return Rarity.RARE;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !oldStack.sameItem(newStack);
	}

}
