/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.common.Psi;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageDataSync;
import vazkii.psi.common.network.message.MessageDeductPsi;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;

import java.lang.ref.WeakReference;
import java.util.*;

public class PlayerDataHandler {

	public static final Set<SpellContext> delayedContexts = new LinkedHashSet<>();
	private static final WeakHashMap<Player, PlayerData> remotePlayerData = new WeakHashMap<>();
	private static final WeakHashMap<Player, PlayerData> playerData = new WeakHashMap<>();
	private static final String DATA_TAG = "PsiData";

	@NotNull
	public static PlayerData get(Player player) {
		if(player == null) {
			return new PlayerData();
		}

		Map<Player, PlayerData> dataMap = player.level().isClientSide ? remotePlayerData : playerData;

		PlayerData data = dataMap.computeIfAbsent(player, PlayerData::new);
		if(data.playerWR != null && data.playerWR.get() != player) {
			CompoundTag cmp = new CompoundTag();
			data.writeToNBT(cmp);
			dataMap.remove(player);
			data = get(player);
			data.readFromNBT(cmp);
		}

		return data;
	}

	public static CompoundTag getDataCompoundForPlayer(Player player) {
		CompoundTag forgeData = player.getPersistentData();
		if(!forgeData.contains(Player.PERSISTED_NBT_TAG)) {
			forgeData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
		}

		CompoundTag persistentData = forgeData.getCompound(Player.PERSISTED_NBT_TAG);
		if(!persistentData.contains(DATA_TAG)) {
			persistentData.put(DATA_TAG, new CompoundTag());
		}

		return persistentData.getCompound(DATA_TAG);
	}

	@EventBusSubscriber(modid = LibMisc.MOD_ID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onServerTick(ServerTickEvent.Post event) {
			List<SpellContext> delayedContextsCopy = new ArrayList<>(delayedContexts);
			for(SpellContext context : delayedContextsCopy) {
				context.delay--;

				if(context.delay <= 0) {
					delayedContexts.remove(context);
					context.delay = 0; // Just in case it goes under 0
					context.cspell.safeExecute(context);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerTick(PlayerTickEvent.Pre event) {
			if(!event.getEntity().isSpectator()) {
				Player player = event.getEntity();

				ItemStack cadStack = PsiAPI.getPlayerCAD(player);
				if(!cadStack.isEmpty() && cadStack.getItem() instanceof ICAD && PsiAPI.canCADBeUpdated(player)) {
					((ICAD) cadStack.getItem()).incrementTime(cadStack);
				}

				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.TICK));
				PlayerDataHandler.get(player).tick();
			}
		}

		@SubscribeEvent
		public static void onEntityDamage(LivingDamageEvent.Pre event) {
			if(event.getEntity() instanceof Player player) {
				PlayerDataHandler.get(player).damage(event.getNewDamage());

				LivingEntity attacker = null;
				if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity) {
					attacker = (LivingEntity) event.getSource().getEntity();
				}

				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.DAMAGE, event.getNewDamage(), attacker));
				if(event.getSource().is(DamageTypes.ON_FIRE) || event.getSource().is(DamageTypes.IN_FIRE)) {
					PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.ON_FIRE));
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
			if(event.getEntity() instanceof ServerPlayer) {
				MessageDataSync message = new MessageDataSync(get(event.getEntity()));
				MessageRegister.sendToPlayer((ServerPlayer) event.getEntity(), message);
			}
		}

		@SubscribeEvent
		public static void onEntityJump(LivingJumpEvent event) {
			if(event.getEntity() instanceof Player player && event.getEntity().level().isClientSide && !event.getEntity().isSpectator()) {
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.JUMP));
				MessageRegister.sendToServer(new MessageTriggerJumpSpell());
			}
		}

		@SubscribeEvent
		public static void onPsiArmorEvent(PsiArmorEvent event) {
			if(event.getEntity().isSpectator()) {
				return;
			}

			for(int i = 0; i < 4; i++) {
				ItemStack armor = event.getEntity().getInventory().armor.get(i);
				if(!armor.isEmpty() && armor.getItem() instanceof IPsiEventArmor handler) {
					handler.onEvent(armor, event);
				}
			}
		}

		@SubscribeEvent
		public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
			get(event.getEntity()).eidosChangelog.clear();
		}

		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void onRenderWorldLast(RenderLevelStageEvent event) {
			if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
				Minecraft mc = Minecraft.getInstance();
				Entity cameraEntity = mc.getCameraEntity();
				if(cameraEntity != null) {
					float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(false);
					for(Player player : mc.level.players()) {
						PlayerDataHandler.get(player).render(player, partialTicks, event.getPoseStack());
					}
				}
			}
		}

		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void onFOVUpdate(ComputeFovModifierEvent event) {
			PlayerData data = get(Minecraft.getInstance().player);
			if(data.isAnchored) {
				float fov = event.getNewFovModifier();
				if(data.eidosAnchorTime > 0) {
					fov *= Math.min(5, data.eidosAnchorTime - ClientTickHandler.partialTicks) / 5;
				} else {
					fov *= (10 - Math.min(10, data.postAnchorRecallTime + ClientTickHandler.partialTicks)) / 10;
				}
				event.setNewFovModifier(fov);
			}
		}

	}

	public static class PlayerData implements IPlayerData {

		private static final String TAG_AVAILABLE_PSI = "availablePsi";
		private static final String TAG_REGEN_CD = "regenCd";
		private static final String TAG_OVERFLOWED = "overflowed";

		private static final String TAG_EIDOS_ANCHOR_X = "eidosAnchorX";
		private static final String TAG_EIDOS_ANCHOR_Y = "eidosAnchorY";
		private static final String TAG_EIDOS_ANCHOR_Z = "eidosAnchorZ";
		private static final String TAG_EIDOS_ANCHOR_PITCH = "eidosAnchorPitch";
		private static final String TAG_EIDOS_ANCHOR_YAW = "eidosAnchorYaw";
		private static final String TAG_EIDOS_ANCHOR_TIME = "eidosAnchorTime";

		private static final String TAG_CUSTOM_DATA = "customData";
		// Eidos stuff
		public final Stack<Vector3> eidosChangelog = new Stack<>();
		public final List<Deduction> deductions = new ArrayList<>();
		public final WeakReference<Player> playerWR;
		private final boolean client;
		public int totalPsi = 5000;
		public int regen = 25;
		public int availablePsi;
		public int lastAvailablePsi;
		public int regenCooldown;
		public boolean loopcasting = false;
		public InteractionHand loopcastHand = null;
		public ItemStack lastTickLoopcastStack;
		public int loopcastTime = 1;
		public int loopcastAmount = 0;
		public int loopcastFadeTime = 0;
		public boolean overflowed = false;
		public Vector3 eidosAnchor = new Vector3(0, 0, 0);
		public double eidosAnchorPitch, eidosAnchorYaw;
		public boolean isAnchored;
		public boolean isReverting;
		public int eidosAnchorTime;
		public int postAnchorRecallTime;
		public int eidosReversionTime;
		public DimensionType lastDimension;
		public boolean deductTick;
		// Exosuit Event Stuff
		private boolean lowLight, underwater, lowHp;
		// Custom Data
		private CompoundTag customData;

		private PlayerData() {
			playerWR = new WeakReference<>(null);
			client = true;
		}

		public PlayerData(Player player) {
			playerWR = new WeakReference<>(player);
			client = player.getCommandSenderWorld().isClientSide;

			load();
		}

		public void tick() {
			Player player = playerWR.get();
			if(player == null) {
				return;
			}

			DimensionType dimension = player.getCommandSenderWorld().dimensionType();

			if(deductTick) {
				deductTick = false;
			} else {
				lastAvailablePsi = availablePsi;
			}

			int max = getTotalPsi();
			if(availablePsi > max) {
				availablePsi = max;
			}

			ItemStack cadStack = getCAD();

			if(!cadStack.isEmpty()) {
				ICAD cad = (ICAD) cadStack.getItem();
				int overflow = cad.getStatValue(cadStack, EnumCADStat.OVERFLOW);
				if(overflow == -1) {
					availablePsi = max;
				} else {
					applyRegen(player, max, cadStack);
				}
			} else {
				applyRegen(player, max, cadStack);
			}

			int color = ICADColorizer.DEFAULT_SPELL_COLOR;

			if(!cadStack.isEmpty()) {
				color = Psi.proxy.getColorForCAD(cadStack);
			}

			float r = PsiRenderHelper.r(color) / 255F;
			float g = PsiRenderHelper.g(color) / 255F;
			float b = PsiRenderHelper.b(color) / 255F;

			loopcast: {
				if(player.isSpectator()) {
					stopLoopcast();
				}

				if(overflowed) {
					stopLoopcast();
				}

				if(loopcasting && loopcastHand != null) {
					ItemStack stackInHand = player.getItemInHand(loopcastHand);

					if(stackInHand.isEmpty() ||
							!ISocketable.isSocketable(stackInHand) ||
							!ISocketable.socketable(stackInHand).canLoopcast()) {
						stopLoopcast();
						break loopcast;
					}

					if(lastTickLoopcastStack != null) {
						if(!ItemStack.isSameItem(lastTickLoopcastStack, stackInHand) ||
								!ISocketable.isSocketable(lastTickLoopcastStack)) {
							stopLoopcast();
							break loopcast;
						} else {
							ISocketable lastTickItem = ISocketable.socketable(lastTickLoopcastStack);
							ISocketable thisTickItem = ISocketable.socketable(stackInHand);

							int lastSlot = lastTickItem.getSelectedSlot();
							int thisSlot = thisTickItem.getSelectedSlot();
							if(lastSlot != thisSlot) {
								stopLoopcast();
								break loopcast;
							}

							ItemStack lastTick = lastTickItem.getBulletInSocket(lastSlot);
							ItemStack thisTick = thisTickItem.getBulletInSocket(thisSlot);
							if(!ItemStack.matches(lastTick, thisTick)) {
								stopLoopcast();
								break loopcast;
							}
						}
					}

					lastTickLoopcastStack = stackInHand.copy();

					ISocketable socketable = ISocketable.socketable(stackInHand);

					for(int i = 0; i < 5; i++) {
						double x = player.getX() + (Math.random() - 0.5) * 2.1 * player.getBbWidth();
						double y = player.getY() + 0.35D;
						double z = player.getZ() + (Math.random() - 0.5) * 2.1 * player.getBbWidth();
						float grav = -0.15F - (float) Math.random() * 0.03F;
						Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
					}

					if(loopcastTime > 0 && loopcastTime % 5 == 0) {
						ItemStack bullet = socketable.getSelectedBullet();
						if(bullet.isEmpty() || !ISpellAcceptor.hasSpell(bullet)) {
							stopLoopcast();
							break loopcast;
						}

						ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(bullet);
						Spell spell = spellContainer.getSpell();
						SpellContext context = new SpellContext().setPlayer(player).setSpell(spell).setLoopcastIndex(loopcastAmount + 1);
						context.castFrom = loopcastHand;
						if(context.isValid()) {
							if(context.cspell.metadata.evaluateAgainst(cadStack)) {
								int cost = ItemCAD.getRealCost(cadStack, bullet, context.cspell.metadata.getStat(EnumSpellStat.COST));
								if(cost > 0 || cost == -1) {
									if(cost != -1) {
										deductPsi(cost, 0, true);
									}

									if(!player.getCommandSenderWorld().isClientSide && loopcastTime % 10 == 0) {
										player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(), PsiSoundHandler.loopcast, SoundSource.PLAYERS, 0.1F, (float) (0.15 + Math.random() * 0.85));
									}
								}

								if(!player.getCommandSenderWorld().isClientSide) {
									if(!spellContainer.loopcastSpell(context)) {
										stopLoopcast();
										break loopcast;
									}
								}
								loopcastAmount++;
							}
						}
					}

					loopcastTime++;
				} else if(loopcastFadeTime > 0) {
					loopcastFadeTime--;
				}
			}

			if(!player.isAlive() || dimension != lastDimension) {
				eidosAnchorTime = 0;
				eidosReversionTime = 0;
				eidosChangelog.clear();
				isAnchored = false;
				isReverting = false;
			}

			if(eidosAnchorTime > 0) {
				if(eidosAnchorTime == 1) {
					if(player instanceof ServerPlayer pmp) {
						pmp.connection.teleport(eidosAnchor.x, eidosAnchor.y, eidosAnchor.z, (float) eidosAnchorYaw, (float) eidosAnchorPitch);

						Entity riding = player.getVehicle();
						while(riding != null) {
							riding.setPos(eidosAnchor.x, eidosAnchor.y, eidosAnchor.z);
							riding = riding.getVehicle();
						}
					}
					postAnchorRecallTime = 0;
				}
				eidosAnchorTime--;
			} else if(postAnchorRecallTime < 5) {
				postAnchorRecallTime--;
				isAnchored = false;
			}

			if(eidosReversionTime > 0) {
				if(eidosChangelog.isEmpty()) {
					eidosReversionTime = 0;
					isReverting = false;
				} else {
					eidosChangelog.pop();
					if(eidosChangelog.isEmpty()) {
						eidosReversionTime = 0;
						isReverting = false;
					} else {
						Vector3 vec = eidosChangelog.pop();
						if(player instanceof ServerPlayer pmp) {
							pmp.connection.teleport(vec.x, vec.y, vec.z, 0, 0, ImmutableSet.of(RelativeMovement.X_ROT, RelativeMovement.Y_ROT));
							pmp.connection.resetPosition();
						} else {
							player.setPos(vec.x, vec.y, vec.z);
						}

						Entity riding = player.getVehicle();
						while(riding != null) {
							riding.setPos(vec.x, vec.y, vec.z);

							riding = riding.getVehicle();
						}

						if(player.level().isClientSide) {
							for(int i = 0; i < 5; i++) {
								double spread = 0.6;

								double x = player.getX() + (Math.random() - 0.5) * spread;
								double y = player.getY() + (Math.random() - 0.5) * spread;
								double z = player.getZ() + (Math.random() - 0.5) * spread;

								Psi.proxy.sparkleFX(x, y, z, r, g, b, 0, 0, 0, 1.2F, 12);
							}
						}

						player.setDeltaMovement(0, 0, 0);
						player.fallDistance = 0F;
					}
				}

				eidosReversionTime--;
				if(eidosReversionTime == 0 || player.isShiftKeyDown()) {
					eidosChangelog.clear();
					isReverting = false;
				}
			} else {
				if(eidosChangelog.size() > 600) {
					eidosChangelog.removeFirst();
				}
				eidosChangelog.push(Vector3.fromEntity(player));
			}

			BlockPos pos = player.blockPosition();
			int light = player.getCommandSenderWorld().getLightEngine().getRawBrightness(pos, 0);

			boolean lowLight = light == 0;
			if(!this.lowLight && lowLight) {
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.LOW_LIGHT));
			}
			this.lowLight = lowLight;

			boolean underwater = player.isInWater();
			if(!this.underwater && underwater) {
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.UNDERWATER));
			}
			this.underwater = underwater;

			boolean lowHp = player.getHealth() <= 6;
			if(!this.lowHp && lowHp) {
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.LOW_HP));
			}
			this.lowHp = lowHp;

			List<Deduction> remove = new ArrayList<>();
			for(Deduction d : deductions) {
				if(d.invalid) {
					remove.add(d);
				} else {
					d.tick();
				}
			}
			deductions.removeAll(remove);

			lastDimension = dimension;
		}

		private void applyRegen(Player player, int max, ItemStack cadStack) {
			RegenPsiEvent event = new RegenPsiEvent(player, this, cadStack);

			if(!NeoForge.EVENT_BUS.post(event).isCanceled()) {
				if(!cadStack.isEmpty()) {
					ICAD cad = (ICAD) cadStack.getItem();
					cad.regenPsi(cadStack, event.getCadRegen());
				}

				boolean anyChange = availablePsi != max && event.getPlayerRegen() > 0;

				int prevPsi = availablePsi;
				availablePsi = Math.min(max, availablePsi + event.getPlayerRegen());

				if(overflowed && event.willHealOverflow()) {
					anyChange = true;
					overflowed = false;
				}

				if(regenCooldown != event.getRegenCooldown()) {
					anyChange = true;
				}
				regenCooldown = event.getRegenCooldown();

				if(anyChange) {
					if(player instanceof ServerPlayer) {
						MessageDeductPsi message = new MessageDeductPsi(prevPsi, availablePsi, regenCooldown, false);
						MessageRegister.sendToPlayer((ServerPlayer) player, message);
					}

					save();
				}
			}
		}

		public void stopLoopcast() {
			Player player = playerWR.get();

			if(loopcasting) {
				loopcastFadeTime = 5;
				NeoForge.EVENT_BUS.post(new LoopcastEndEvent(player, this, loopcastHand, loopcastAmount));
			}
			loopcasting = false;

			lastTickLoopcastStack = null;
			loopcastHand = null;

			loopcastTime = 1;
			loopcastAmount = 0;

			if(player instanceof ServerPlayer) {
				LoopcastTrackingHandler.syncForTrackersAndSelf((ServerPlayer) player);
			}
		}

		public int calculateDamageDeduction(float amount) {
			return (int) (getTotalPsi() * 0.02 * amount);
		}

		public void damage(float amount) {
			int psi = calculateDamageDeduction(amount);
			if(psi > 0 && availablePsi > 0) {
				psi = Math.min(psi, availablePsi);
				deductPsi(psi, 20, true, true);
			}
		}

		public ItemStack getCAD() {
			return PsiAPI.getPlayerCAD(playerWR.get());
		}

		public void deductPsi(int psi, int cd, boolean sync) {
			deductPsi(psi, cd, sync, false);
		}

		@Override
		public void deductPsi(int psi, int cd, boolean sync, boolean shatter) {
			int currentPsi = availablePsi;

			Player player = playerWR.get();
			if(player == null) {
				return;
			}

			ItemStack cadStack = getCAD();

			if(!cadStack.isEmpty()) {
				ICAD cad = (ICAD) cadStack.getItem();
				int storedPsi = cad.getStoredPsi(cadStack);
				if(storedPsi == -1) {
					return;
				}
			}

			availablePsi -= psi;
			if(regenCooldown < cd) {
				regenCooldown = cd;
			}

			if(availablePsi < 0) {
				int overflow = -availablePsi;
				availablePsi = 0;

				if(!cadStack.isEmpty()) {
					ICAD cad = (ICAD) cadStack.getItem();
					overflow = cad.consumePsi(cadStack, overflow);
				}

				if(!shatter && overflow > 0) {
					float dmg = (float) overflow / (loopcasting ? 50 : 125);
					if(!client) {
						Registry<DamageType> types = player.damageSources().damageTypes;
						DamageSource overloadSource = new DamageSource(types.getHolderOrThrow(LibResources.PSI_OVERLOAD));
						player.hurt(overloadSource, dmg);
					}
					overflowed = true;
				}
			}

			if(sync && player instanceof ServerPlayer) {
				MessageDeductPsi message = new MessageDeductPsi(currentPsi, availablePsi, regenCooldown, shatter);
				MessageRegister.sendToPlayer((ServerPlayer) player, message);
			}

			save();
		}

		public void addDeduction(int current, int deduct, boolean shatter) {
			if(deduct > current) {
				deduct = current;
			}
			if(deduct < 0) {
				deduct = 0;
			}

			if(deduct == 0) {
				return;
			}

			deductions.add(new Deduction(current, deduct, 20, shatter));
		}

		@Override
		public int getAvailablePsi() {
			return availablePsi;
		}

		@Override
		public int getLastAvailablePsi() {
			return lastAvailablePsi;
		}

		@Override
		public int getTotalPsi() {
			return totalPsi;
		}

		@Override
		public int getRegenPerTick() {
			return regen;
		}

		@Override
		public boolean isOverflowed() {
			return overflowed;
		}

		@Override
		public int getRegenCooldown() {
			return regenCooldown;
		}

		public boolean hasAdvancement(ResourceLocation group) {
			Player player = playerWR.get();
			return Psi.proxy.hasAdvancement(group, player);
		}

		@Override
		public boolean isPieceGroupUnlocked(ResourceLocation group, @Nullable ResourceLocation name) {
			Player player = playerWR.get();
			if(player == null) {
				return false;
			}

			if(player.isCreative()) {
				return true;
			}

			boolean hasAdvancement = hasAdvancement(group);
			PieceKnowledgeEvent event = new PieceKnowledgeEvent(group, name, player, this, hasAdvancement);
			NeoForge.EVENT_BUS.post(event);

			return !event.isCanceled();
		}

		@Override
		public void unlockPieceGroup(ResourceLocation resourceLocation) {
			Player player = playerWR.get();
			if(player instanceof ServerPlayer serverPlayer) {
				AdvancementHolder advancement = serverPlayer.getServer().getAdvancements().get(resourceLocation);
				if(advancement != null && !serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone()) {
					for(String s : serverPlayer.getAdvancements().getOrStartProgress(advancement).getRemainingCriteria()) {
						serverPlayer.getAdvancements().getOrStartProgress(advancement).grantProgress(s);
					}
				}
			}
		}

		@Override
		public void markPieceExecuted(SpellPiece piece) {
			PieceExecutedEvent event = new PieceExecutedEvent(piece, playerWR.get());
			NeoForge.EVENT_BUS.post(event);
			ResourceLocation advancement = PsiAPI.getGroupForPiece(piece.getClass());
			if(advancement != null && PsiAPI.getMainPieceForGroup(advancement) == piece.getClass() && !hasAdvancement(advancement)) {
				NeoForge.EVENT_BUS.post(new PieceGroupAdvancementComplete(piece, playerWR.get(), advancement));
			}
		}

		@Override
		public CompoundTag getCustomData() {
			if(customData == null) {
				return customData = new CompoundTag();
			}
			return customData;
		}

		@Override
		public void save() {
			if(!client) {
				Player player = playerWR.get();

				if(player != null) {
					CompoundTag cmp = getDataCompoundForPlayer(player);
					writeToNBT(cmp);
				}
			}
		}

		public void writeToNBT(CompoundTag cmp) {
			cmp.putInt(TAG_AVAILABLE_PSI, availablePsi);
			cmp.putInt(TAG_REGEN_CD, regenCooldown);
			cmp.putBoolean(TAG_OVERFLOWED, overflowed);

			cmp.putDouble(TAG_EIDOS_ANCHOR_X, eidosAnchor.x);
			cmp.putDouble(TAG_EIDOS_ANCHOR_Y, eidosAnchor.y);
			cmp.putDouble(TAG_EIDOS_ANCHOR_Z, eidosAnchor.z);
			cmp.putDouble(TAG_EIDOS_ANCHOR_PITCH, eidosAnchorPitch);
			cmp.putDouble(TAG_EIDOS_ANCHOR_YAW, eidosAnchorYaw);
			cmp.putInt(TAG_EIDOS_ANCHOR_TIME, eidosAnchorTime);

			if(customData != null) {
				cmp.put(TAG_CUSTOM_DATA, customData);
			}
		}

		public void load() {
			if(!client) {
				Player player = playerWR.get();

				if(player != null) {
					CompoundTag cmp = getDataCompoundForPlayer(player);
					readFromNBT(cmp);
				}
			}
		}

		public void readFromNBT(CompoundTag cmp) {
			availablePsi = cmp.getInt(TAG_AVAILABLE_PSI);
			regenCooldown = cmp.getInt(TAG_REGEN_CD);
			overflowed = cmp.getBoolean(TAG_OVERFLOWED);

			double x = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
			double y = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
			double z = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
			eidosAnchor.set(x, y, z);
			eidosAnchorPitch = cmp.getDouble(TAG_EIDOS_ANCHOR_PITCH);
			eidosAnchorYaw = cmp.getDouble(TAG_EIDOS_ANCHOR_YAW);
			eidosAnchorTime = cmp.getInt(TAG_EIDOS_ANCHOR_TIME);

			customData = cmp.getCompound(TAG_CUSTOM_DATA);
		}

		@OnlyIn(Dist.CLIENT)
		public void render(Player player, float partTicks, PoseStack ms) {
			EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
			double x = player.xOld + (player.getX() - player.xOld) * partTicks - renderManager.camera.getPosition().x;
			double y = player.yOld + (player.getY() - player.yOld) * partTicks - renderManager.camera.getPosition().y;
			double z = player.zOld + (player.getZ() - player.zOld) * partTicks - renderManager.camera.getPosition().z;
			float scale = 0.75F;
			if(loopcasting) {
				float mul = Math.min(5F, loopcastTime + partTicks) / 5F;
				scale *= mul;
			} else if(loopcastFadeTime > 0) {
				float mul = Math.min(5F, loopcastFadeTime - partTicks) / 5F;
				scale *= mul;
			} else {
				return;
			}

			int color = ICADColorizer.DEFAULT_SPELL_COLOR;
			ItemStack cad = PsiAPI.getPlayerCAD(playerWR.get());
			if(!cad.isEmpty() && cad.getItem() instanceof ICAD icad) {
				color = icad.getSpellColor(cad);
			}

			ms.pushPose();
			ms.translate(x, y + 0.15, z);
			MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
			RenderSpellCircle.renderSpellCircle(ClientTickHandler.ticksInGame + partTicks, scale, 1, 0, -1, 0, color, ms, buffers);
			buffers.endBatch();
			ms.popPose();
		}

		public static class Deduction {

			public final int current;
			public final int deduct;
			public final int cd;
			public final boolean shatter;

			public int elapsed;

			public boolean invalid;

			public Deduction(int current, int deduct, int cd, boolean shatter) {
				this.current = current;
				this.deduct = deduct;
				this.cd = cd;
				this.shatter = shatter;
			}

			public void tick() {
				elapsed++;

				if(elapsed >= cd) {
					invalid = true;
				}
			}

			public float getPercentile(float partTicks) {
				return 1F - Math.min(1F, (elapsed + partTicks) / cd);
			}
		}

	}
}
