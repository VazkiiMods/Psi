/*
 * This class is distributed as part of the Psi Mod.
 * Get the Source Code in GitHub:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: https://psi.vazkii.net/license.php
 */
package vazkii.psi.common.core.handler;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.dimension.DimensionType;

import org.jetbrains.annotations.Nullable;

import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.event.PsiEvents;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.attribute.base.ModAttributes;
import vazkii.psi.common.client.PsiClientRuntime;
import vazkii.psi.common.lib.LibResources;
import vazkii.psi.common.platform.PsiPlayerDataStorage;
import vazkii.psi.common.platform.PsiPlayerDataSync;
import vazkii.psi.common.spell.PsiSpellCosts;

import java.lang.ref.WeakReference;
import java.util.*;

public class PlayerData implements IPlayerData {
	public static final Codec<PlayerData> CODEC = CompoundTag.CODEC.xmap(PlayerData::fromTag, PlayerData::toTag);

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
	public WeakReference<Player> playerWR;
	private boolean client;
	public int availablePsi;
	public int lastAvailablePsi;
	public int regenCooldown;
	public boolean loopcasting = false;
	public InteractionHand loopcastHand = null;
	public ItemStack lastTickLoopcastStack;
	public int loopcastTime = 1;
	public int loopcastAmount = 0;
	public int loopcastFadeTime = 0;
	public long lastTriggeredDetonation;
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

	public PlayerData() {
		bind(null);
	}

	public PlayerData(Player player) {
		bind(player);
	}

	public void bind(@Nullable Player player) {
		playerWR = new WeakReference<>(player);
		client = player == null || player.getCommandSenderWorld().isClientSide;
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
			color = ((ICAD) cadStack.getItem()).getSpellColor(cadStack);
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

				if(player.level().isClientSide) {
					for(int i = 0; i < 5; i++) {
						double x = player.getX() + (Math.random() - 0.5) * 2.1 * player.getBbWidth();
						double y = player.getY() + 0.35D;
						double z = player.getZ() + (Math.random() - 0.5) * 2.1 * player.getBbWidth();
						float grav = -0.15F - (float) Math.random() * 0.03F;
						PsiClientRuntime.sparkle(player.level(), x, y, z, r, g, b, 0, -grav, 0, 0.25F, 15);
					}
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
							int cost = PsiSpellCosts.realCost(cadStack, bullet, context.cspell.metadata.getStat(EnumSpellStat.COST));
							if(cost > 0 || cost == -1) {
								if(cost != -1) {
									deductPsi(cost, 0, true);
								}

								if(!player.getCommandSenderWorld().isClientSide && loopcastTime % 10 == 0) {
									player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(), PsiSoundHandler.loopcast.get(), SoundSource.PLAYERS, 0.1F, (float) (0.15 + Math.random() * 0.85));
								}
							}

							if(player.getCommandSenderWorld().isClientSide) {
								if(!spellContainer.predictLoopcastSpell(context)) {
									stopLoopcast();
									break loopcast;
								}
							} else if(!spellContainer.loopcastSpell(context)) {
								stopLoopcast();
								break loopcast;
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

							PsiClientRuntime.sparkle(player.level(), x, y, z, r, g, b, 0, 0, 0, 1.2F, 12);
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

		if(!PsiEvents.post(event).isCanceled()) {
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
					PsiPlayerDataSync.sendDeduction((ServerPlayer) player, prevPsi, availablePsi, regenCooldown, false);
				}

				save();
			}
		}
	}

	public void stopLoopcast() {
		Player player = playerWR.get();

		if(loopcasting) {
			loopcastFadeTime = 5;
			PsiEvents.post(new LoopcastEndEvent(player, this, loopcastHand, loopcastAmount));
		}
		loopcasting = false;

		lastTickLoopcastStack = null;
		loopcastHand = null;

		loopcastTime = 1;
		loopcastAmount = 0;

		if(player instanceof ServerPlayer) {
			PsiPlayerDataSync.sendLoopcast((ServerPlayer) player, loopcasting, loopcastHand);
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
					Registry<DamageType> types = player.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
					DamageSource overloadSource = new DamageSource(types.getHolderOrThrow(LibResources.PSI_OVERLOAD));
					player.hurt(overloadSource, dmg);
				}
				overflowed = true;
				if(sync && player instanceof ServerPlayer) {
					PsiPlayerDataSync.sendOverflow((ServerPlayer) player, true);
				}
			}
		}

		if(sync && player instanceof ServerPlayer) {
			PsiPlayerDataSync.sendDeduction((ServerPlayer) player, currentPsi, availablePsi, regenCooldown, shatter);
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
		Player player = playerWR.get();
		if(player != null) {
			return (int) player.getAttributeValue(ModAttributes.TOTAL_PSI.holder());
		}
		return (int) ModAttributes.TOTAL_PSI.get().getDefaultValue();
	}

	@Override
	public int getRegenPerTick() {
		Player player = playerWR.get();
		if(player != null) {
			return (int) player.getAttributeValue(ModAttributes.REGEN.holder());
		}
		return (int) ModAttributes.REGEN.get().getDefaultValue();
	}

	@Override
	public boolean isOverflowed() {
		return overflowed;
	}

	@Override
	public int getRegenCooldown() {
		return regenCooldown;
	}

	@Override
	public boolean hasAdvancement(ResourceLocation advancement) {
		Player player = playerWR.get();
		if(player instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
			AdvancementHolder holder = serverPlayer.getServer().getAdvancements().get(advancement);
			return holder != null && serverPlayer.getAdvancements().getOrStartProgress(holder).isDone();
		}
		return player != null && PsiClientRuntime.hasAdvancement(player, advancement);
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
		PsiEvents.post(event);

		return !event.isCanceled();
	}

	@Override
	public void unlockPieceGroup(ResourceLocation resourceLocation) {
		Player player = playerWR.get();
		if(player instanceof ServerPlayer serverPlayer) {
			if(serverPlayer.getServer() == null) {
				return;
			}

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
		Player player = playerWR.get();
		if(player == null) {
			return;
		}

		PieceExecutedEvent event = new PieceExecutedEvent(piece, player);
		PsiEvents.post(event);
		Optional<Holder.Reference<SpellPieceGroup>> group = PsiAPI.getPieceGroup(player.registryAccess(), piece.getRegistryKey());
		if(group.isEmpty() || !(group.get().value().unlock() instanceof SpellPieceGroup.Unlock.ExecuteMain)) {
			return;
		}

		ResourceLocation advancement = group.get().key().location();
		if(group.get().value().main().equals(piece.getRegistryKey()) && !hasAdvancement(advancement)) {
			PsiEvents.post(new PieceGroupAdvancementComplete(piece, player, advancement));
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
				PsiPlayerDataStorage.save(player, this);
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

	public void readFromNBT(CompoundTag cmp) {
		availablePsi = cmp.getInt(TAG_AVAILABLE_PSI);
		regenCooldown = cmp.getInt(TAG_REGEN_CD);
		overflowed = cmp.getBoolean(TAG_OVERFLOWED);

		double x = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
		double y = cmp.getDouble(TAG_EIDOS_ANCHOR_Y);
		double z = cmp.getDouble(TAG_EIDOS_ANCHOR_Z);
		eidosAnchor.set(x, y, z);
		eidosAnchorPitch = cmp.getDouble(TAG_EIDOS_ANCHOR_PITCH);
		eidosAnchorYaw = cmp.getDouble(TAG_EIDOS_ANCHOR_YAW);
		eidosAnchorTime = cmp.getInt(TAG_EIDOS_ANCHOR_TIME);

		customData = cmp.getCompound(TAG_CUSTOM_DATA);
	}

	private static PlayerData fromTag(CompoundTag tag) {
		PlayerData data = new PlayerData();
		data.readFromNBT(tag);
		return data;
	}

	private static CompoundTag toTag(PlayerData data) {
		CompoundTag tag = new CompoundTag();
		data.writeToNBT(tag);
		return tag;
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
