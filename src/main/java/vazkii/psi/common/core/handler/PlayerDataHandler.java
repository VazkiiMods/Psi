/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [10/01/2016, 23:21:21 (GMT)]
 */
package vazkii.psi.common.core.handler;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketPlayerPosLook.EnumFlags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.exosuit.IPsiEventArmor;
import vazkii.psi.api.exosuit.PsiArmorEvent;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.client.render.entity.RenderSpellCircle;
import vazkii.psi.common.Psi;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.lib.LibMisc;
import vazkii.psi.common.lib.LibObfuscation;
import vazkii.psi.common.network.message.MessageDataSync;
import vazkii.psi.common.network.message.MessageDeductPsi;
import vazkii.psi.common.network.message.MessageLevelUp;
import vazkii.psi.common.network.message.MessageTriggerJumpSpell;

import javax.annotation.Nonnull;
import java.awt.*;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.List;

public class PlayerDataHandler {

	private static final WeakHashMap<EntityPlayer, PlayerData> remotePlayerData = new WeakHashMap<>();
	private static final WeakHashMap<EntityPlayer, PlayerData> playerData = new WeakHashMap<>();
	public static final Set<SpellContext> delayedContexts = new HashSet<>();

	private static final String DATA_TAG = "PsiData";

	public static final DamageSource damageSourceOverload = new DamageSource("psi-overload").setDamageBypassesArmor().setMagicDamage();

	@Nonnull
	public static PlayerData get(EntityPlayer player) {
		if (player == null)
			return new PlayerData();

		Map<EntityPlayer, PlayerData> dataMap = player.world.isRemote ? remotePlayerData : playerData;

		PlayerData data = dataMap.computeIfAbsent(player, PlayerData::new);
		if(data.playerWR != null && data.playerWR.get() != player) {
			NBTTagCompound cmp = new NBTTagCompound();
			data.writeToNBT(cmp);
			dataMap.remove(player);
			data = get(player);
			data.readFromNBT(cmp);
		}

		data.validate();

		return data;
	}

	public static NBTTagCompound getDataCompoundForPlayer(EntityPlayer player) {
		NBTTagCompound forgeData = player.getEntityData();
		if(!forgeData.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
			forgeData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());

		NBTTagCompound persistentData = forgeData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if(!persistentData.hasKey(DATA_TAG))
			persistentData.setTag(DATA_TAG, new NBTTagCompound());

		return persistentData.getCompoundTag(DATA_TAG);
	}

	@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onServerTick(ServerTickEvent event) {
			if(event.phase == Phase.END) {
				List<SpellContext> delayedContextsCopy = new ArrayList<>(delayedContexts);
				for(SpellContext context : delayedContextsCopy) {
					context.delay--;

					if(context.delay <= 0) {
						context.delay = 0; // Just in case it goes under 0
						context.cspell.safeExecute(context);

						if(context.delay == 0)
							delayedContexts.remove(context);
					}
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerTick(LivingUpdateEvent event) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();

				ItemStack cadStack = PsiAPI.getPlayerCAD(player);
				if(!cadStack.isEmpty() && cadStack.getItem() instanceof ICAD && PsiAPI.canCADBeUpdated(player))
					((ICAD) cadStack.getItem()).incrementTime(cadStack);
				
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.TICK));
				PlayerDataHandler.get(player).tick();
			}
		}

		@SubscribeEvent
		public static void onEntityDamage(LivingHurtEvent event) {
			if(event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				PlayerDataHandler.get(player).damage(event.getAmount());

				EntityLivingBase attacker = null;
				if(event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityLivingBase)
					attacker = (EntityLivingBase) event.getSource().getTrueSource();

				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.DAMAGE, event.getAmount(), attacker));
				if(event.getSource().isFireDamage())
					PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.ON_FIRE));
			}
		}

		@SubscribeEvent
		public static void onPlayerLogin(PlayerLoggedInEvent event) {
			if(event.player instanceof EntityPlayerMP) {
				MessageDataSync message = new MessageDataSync(get(event.player));
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) event.player);
			}
		}

		@SubscribeEvent
		public static void onEntityJump(LivingJumpEvent event) {
			if(event.getEntityLiving() instanceof EntityPlayer && event.getEntity().world.isRemote) {
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.JUMP));
				NetworkHandler.INSTANCE.sendToServer(new MessageTriggerJumpSpell());
			}
		}

		@SubscribeEvent
		public static void onPsiArmorEvent(PsiArmorEvent event) {
			for(int i = 0; i < 4; i++) {
				ItemStack armor = event.getEntityPlayer().inventory.armorInventory.get(i);
				if(!armor.isEmpty() && armor.getItem() instanceof IPsiEventArmor) {
					IPsiEventArmor handler = (IPsiEventArmor) armor.getItem();
					handler.onEvent(armor, event);
				}
			}
		}

		@SubscribeEvent
		public static void onChangeDimension(PlayerChangedDimensionEvent event) {
			get(event.player).eidosChangelog.clear();
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void onRenderWorldLast(RenderWorldLastEvent event) {
			Minecraft mc = Minecraft.getMinecraft();
			Entity cameraEntity = mc.getRenderViewEntity();
			if (cameraEntity != null) {
				cameraEntity.getPosition();
				Frustum frustum = new Frustum();

				float partialTicks = event.getPartialTicks();
				double viewX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * partialTicks;
				double viewY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * partialTicks;
				double viewZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * partialTicks;
				frustum.setPosition(viewX, viewY, viewZ);

				for(EntityPlayer player : mc.world.playerEntities)
					PlayerDataHandler.get(player).render(player, partialTicks);
			}


		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void onFOVUpdate(FOVUpdateEvent event) {
			PlayerData data = get(Minecraft.getMinecraft().player);
			if(data.isAnchored) {
				float fov = event.getNewfov();
				if(data.eidosAnchorTime > 0)
					fov *= Math.min(5, data.eidosAnchorTime - ClientTicker.partialTicks) / 5;
				else fov *= (10 - Math.min(10, data.postAnchorRecallTime + ClientTicker.partialTicks)) / 10;
				event.setNewfov(fov);
			}
		}

	}

	public static class PlayerData implements IPlayerData {

		private static final String TAG_LEVEL = "level";
		private static final String TAG_AVAILABLE_PSI = "availablePsi";
		private static final String TAG_REGEN_CD = "regenCd";
		private static final String TAG_SPELL_GROUPS_UNLOCKED = "spellGroupsUnlocked";
		private static final String TAG_LAST_SPELL_GROUP = "lastSpellPoint";
		private static final String TAG_LEVEL_POINTS = "levelPoints";
		private static final String TAG_LEARNING_GROUP = "learning";
		private static final String TAG_OVERFLOWED = "overflowed";

		private static final String TAG_EIDOS_ANCHOR_X = "eidosAnchorX";
		private static final String TAG_EIDOS_ANCHOR_Y = "eidosAnchorY";
		private static final String TAG_EIDOS_ANCHOR_Z = "eidosAnchorZ";
		private static final String TAG_EIDOS_ANCHOR_PITCH = "eidosAnchorPitch";
		private static final String TAG_EIDOS_ANCHOR_YAW = "eidosAnchorYaw";
		private static final String TAG_EIDOS_ANCHOR_TIME = "eidosAnchorTime";

		private static final String TAG_CUSTOM_DATA = "customData";
		
		public final int totalPsi = 5000;
		public final int regen = 25;
		
		public int level;
		public int availablePsi;
		public int lastAvailablePsi;
		public int regenCooldown;

		public String lastSpellGroup;
		public int levelPoints;
		public boolean learning;

		public boolean loopcasting = false;
		public EnumHand loopcastHand = null;
		public ItemStack lastTickLoopcastStack;

		public int loopcastTime = 1;
		public int loopcastAmount = 0;
		public int loopcastFadeTime = 0;
		public boolean overflowed = false;
		
		// Eidos stuff
		public final Stack<Vector3> eidosChangelog = new Stack<>();
		public Vector3 eidosAnchor = new Vector3(0, 0, 0);
		public double eidosAnchorPitch, eidosAnchorYaw;
		public boolean isAnchored;
		public boolean isReverting;
		public int eidosAnchorTime;
		public int postAnchorRecallTime;
		public int eidosReversionTime;
		public int lastDimension;
		
		// Exosuit Event Stuff
		private boolean lowLight, underwater, lowHp;

		public boolean deductTick;

		public final List<String> spellGroupsUnlocked = new ArrayList<>();
		public final List<Deduction> deductions = new ArrayList<>();
		public final WeakReference<EntityPlayer> playerWR;
		private final boolean client;
		
		// Custom Data
		private NBTTagCompound customData;

		private PlayerData() {
			playerWR = new WeakReference<>(null);
			client = true;
		}

		public PlayerData(EntityPlayer player) {
			playerWR = new WeakReference<>(player);
			client = player.getEntityWorld().isRemote;

			load();
		}

		public void tick() {
			EntityPlayer player = playerWR.get();
			if (player == null)
				return;

			int dimension = player.getEntityWorld().provider.getDimension();
			
			if(deductTick)
				deductTick = false;
			else lastAvailablePsi = availablePsi;

			int max = getTotalPsi();
			if(availablePsi > max)
				availablePsi = max;

			ItemStack cadStack = getCAD();
			if(regenCooldown == 0) {
				boolean doRegen = true;
				if(!cadStack.isEmpty()) {
					ICAD cad = (ICAD) cadStack.getItem();
					int maxPsi = cad.getStatValue(cadStack, EnumCADStat.OVERFLOW);
					int currPsi = cad.getStoredPsi(cadStack);
					if(currPsi < maxPsi) {
						cad.regenPsi(cadStack, Math.max(1, getRegenPerTick() / 2));
						doRegen = false;
					}
				}

				if(doRegen && regenCooldown == 0) {
					if(availablePsi < max) {
						availablePsi = Math.min(max, availablePsi + getRegenPerTick());
						save();
					} else {
						boolean was = overflowed;
						overflowed = false;
						if(was)
							save();
					}
					
				}
			} else {
				regenCooldown--;
				save();
			}

			cadStack = getCAD();
			Color color = new Color(ICADColorizer.DEFAULT_SPELL_COLOR);

			if(!cadStack.isEmpty()) color = Psi.proxy.getCADColor(cadStack);

			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;

			loopcast: {
				if(overflowed)
					stopLoopcast();
				
				if(loopcasting && loopcastHand != null) {
					ItemStack stackInHand = player.getHeldItem(loopcastHand);

					if (lastTickLoopcastStack != null && (!ItemStack.areItemsEqual(lastTickLoopcastStack, stackInHand) ||
								!Objects.equals(lastTickLoopcastStack.getTagCompound(), stackInHand.getTagCompound()))) {
						stopLoopcast();
						break loopcast;
					}

					lastTickLoopcastStack = stackInHand.copy();

					if (stackInHand.isEmpty() ||
							!(stackInHand.getItem() instanceof ISocketable) ||
							!((ISocketable) stackInHand.getItem()).canLoopcast(stackInHand)) {
						stopLoopcast();
						break loopcast;
					}

					ISocketable castingItem = (ISocketable) stackInHand.getItem();

					for(int i = 0; i < 5; i++) {
						double x = player.posX + (Math.random() - 0.5) * 2.1 * player.width;
						double y = player.posY - player.getYOffset();
						double z = player.posZ + (Math.random() - 0.5) * 2.1 * player.width;
						float grav = -0.15F - (float) Math.random() * 0.03F;
						Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
					}

					if(loopcastTime > 0 && loopcastTime % 5 == 0) {
						ItemStack bullet = castingItem.getBulletInSocket(stackInHand, castingItem.getSelectedSlot(stackInHand));
						if(bullet.isEmpty()) {
							stopLoopcast();
							break loopcast;
						}

						ISpellContainer spellContainer = (ISpellContainer) bullet.getItem();
						if(spellContainer.containsSpell(bullet)) {
							Spell spell = spellContainer.getSpell(bullet);
							SpellContext context = new SpellContext().setPlayer(player).setSpell(spell).setLoopcastIndex(loopcastAmount + 1);
							if(context.isValid()) {
								if(context.cspell.metadata.evaluateAgainst(cadStack)) {
									int cost = ItemCAD.getRealCost(cadStack, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST));
									if(cost > 0 || cost == -1) {
										if(cost != -1)
											deductPsi(cost, 3, true);

										if(!player.getEntityWorld().isRemote && loopcastTime % 10 == 0)
											player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PsiSoundHandler.loopcast, SoundCategory.PLAYERS, 0.5F, (float) (0.35 + Math.random() * 0.85));
									}

									if (!player.getEntityWorld().isRemote)
										context.cspell.safeExecute(context);
									loopcastAmount++;
								}
							}
						}
					}

					loopcastTime++;
				} else if(loopcastFadeTime > 0)
					loopcastFadeTime--;
			}


			if(player.isDead || dimension != lastDimension) {
				eidosAnchorTime = 0;
				eidosReversionTime = 0;
				eidosChangelog.clear();
				isAnchored = false;
				isReverting = false;
			}

			if(eidosAnchorTime > 0) {
				if(eidosAnchorTime == 1) {
					if(player instanceof EntityPlayerMP) {
						EntityPlayerMP pmp = (EntityPlayerMP) player;
						pmp.connection.setPlayerLocation(eidosAnchor.x, eidosAnchor.y, eidosAnchor.z, (float) eidosAnchorYaw, (float) eidosAnchorPitch);

						Entity riding = player.getRidingEntity();
						while(riding != null) {
							riding.setPosition(eidosAnchor.x, eidosAnchor.y, eidosAnchor.z);
							riding = riding.getRidingEntity();
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
						if(player instanceof EntityPlayerMP) {
							EntityPlayerMP pmp = (EntityPlayerMP) player;
							pmp.connection.setPlayerLocation(vec.x, vec.y, vec.z, 0, 0, ImmutableSet.of(EnumFlags.X_ROT, EnumFlags.Y_ROT));
							LibObfuscation.callMethod(NetHandlerPlayServer.class, pmp.connection,
									LibObfuscation.CAPTURE_CURRENT_POSITION, new Class[0], Void.TYPE);
						} else {
							player.posX = vec.x;
							player.posY = vec.y;
							player.posZ = vec.z;
						}

						Entity riding = player.getRidingEntity();
						while(riding != null) {
							riding.setPosition(vec.x, vec.y, vec.z);

							riding = riding.getRidingEntity();
						}

						if (player.world.isRemote) {
							for (int i = 0; i < 5; i++) {
								double spread = 0.6;

								double x = player.posX + (Math.random() - 0.5) * spread;
								double y = player.posY + (Math.random() - 0.5) * spread;
								double z = player.posZ + (Math.random() - 0.5) * spread;

								Psi.proxy.sparkleFX(x, y, z, r, g, b, 0, 0, 0, 1.2F, 12);
							}
						}

						player.motionX = 0;
						player.motionY = 0;
						player.motionZ = 0;
						player.fallDistance = 0F;
					}
				}

				eidosReversionTime--;
				if(eidosReversionTime == 0 || player.isSneaking()) {
					eidosChangelog.clear();
					isReverting = false;
				}
			} else {
				if(eidosChangelog.size() >= 600)
					eidosChangelog.remove(0);
				eidosChangelog.push(Vector3.fromEntity(player));
			}

			BlockPos pos = player.getPosition();
			int skylight = (int) (player.getEntityWorld().getLightFor(EnumSkyBlock.SKY, pos) * player.getEntityWorld().provider.getSunBrightnessFactor(1F));
			int blocklight = player.getEntityWorld().getLightFor(EnumSkyBlock.BLOCK, pos);
			int light = Math.max(skylight, blocklight);
			
			boolean lowLight = light < 7;
			if(!this.lowLight && lowLight)
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.LOW_LIGHT));
			this.lowLight = lowLight;

			boolean underwater = player.isInsideOfMaterial(Material.WATER);
			if(!this.underwater && underwater)
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.UNDERWATER));
			this.underwater = underwater;

			boolean lowHp = player.getHealth() <= 6;
			if(!this.lowHp && lowHp)
				PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.LOW_HP));
			this.lowHp = lowHp;

			List<Deduction> remove = new ArrayList<>();
			for(Deduction d : deductions) {
				if(d.invalid)
					remove.add(d);
				else d.tick();
			}
			deductions.removeAll(remove);
			
			lastDimension = dimension;
		}

		public void validate() {
			if (!spellGroupsUnlocked.contains(lastSpellGroup)) {
				learning = false;
				lastSpellGroup = "";
			}

			int trueLevel = spellGroupsUnlocked.size();
			if (!learning)
				trueLevel++;

			if (0 > level || level > 1)
				level = trueLevel;

			if (level == 0) {
				levelPoints = 0;
			} else if (learning && levelPoints != 0)
				levelPoints = 0;
			else if (!learning && levelPoints == 0)
				levelPoints = 1;

			if (!learning)
				lastSpellGroup = "";
		}

		public void stopLoopcast() {
			if(loopcasting)
				loopcastFadeTime = 5;
			loopcasting = false;

			lastTickLoopcastStack = null;
			loopcastHand = null;

			loopcastTime = 1;
			loopcastAmount = 0;

			EntityPlayer player = playerWR.get();
			if (player instanceof EntityPlayerMP)
				LoopcastTrackingHandler.syncForTrackers((EntityPlayerMP) player);
		}

		public void damage(float amount) {
			int psi = (int) (getTotalPsi() * 0.02 * amount);
			if(psi > 0 && availablePsi > 0) {
				psi = Math.min(psi, availablePsi);
				deductPsi(psi, 20, true, true);
			}
		}

		public void skipToLevel(int level) {
			int currLevel = this.level;
			int points = level - currLevel;

			this.level = Math.max(currLevel, Math.min(PsiAPI.levelCap, level));
			levelPoints = Math.max(0, Math.max(points, levelPoints));
			save();
		}

		public void levelUp() {
			EntityPlayer player = playerWR.get();
			if(player != null) {
				learning = false;
				level++;
				levelPoints++;
				lastSpellGroup = "";
				save();

				if(player instanceof EntityPlayerMP) {
					MessageLevelUp message = new MessageLevelUp(level);
					MessageDataSync message2 = new MessageDataSync(this);

					NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
					NetworkHandler.INSTANCE.sendTo(message2, (EntityPlayerMP) player);
					if(level == 25)
						player.sendMessage(new TextComponentTranslation("psimisc.softcapIndicator").setStyle(new Style().setColor(TextFormatting.AQUA)));
				}
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

			EntityPlayer player = playerWR.get();
			if (player == null)
				return;

			availablePsi -= psi;
			if(regenCooldown < cd)
				regenCooldown = cd;

			if(availablePsi < 0) {
				int overflow = -availablePsi;
				availablePsi = 0;

				ItemStack cadStack = getCAD();
				if(!cadStack.isEmpty()) {
					ICAD cad = (ICAD) cadStack.getItem();
					overflow = cad.consumePsi(cadStack, overflow);
				}

				if(!shatter && overflow > 0) {
					float dmg = (float) overflow / (loopcasting ? 50 : 125);
					if(!client)
						player.attackEntityFrom(damageSourceOverload, dmg);
					overflowed = true;
				}
			}

			if(sync && player instanceof EntityPlayerMP) {
				MessageDeductPsi message = new MessageDeductPsi(currentPsi, availablePsi, regenCooldown, shatter);
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
			}

			save();
		}

		public void addDeduction(int current, int deduct, boolean shatter) {
			if(deduct > current)
				deduct = current;
			if(deduct < 0)
				deduct = 0;

			if(deduct == 0)
				return;

			deductions.add(new Deduction(current, deduct, 20, shatter));
		}

		@Override
		public int getLevel() {
			EntityPlayer player = playerWR.get();
			if(player != null && player.capabilities.isCreativeMode)
				return PsiAPI.levelCap;
			return level;
		}

		public int getLevelPoints() {
			return levelPoints;
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
		public int getRegenCooldown() {
			return regenCooldown;
		}

		@Override
		public boolean isPieceGroupUnlocked(String group) {
			EntityPlayer player = playerWR.get();
			if(player != null && player.capabilities.isCreativeMode)
				return true;

			return spellGroupsUnlocked.contains(group);
		}

		@Override
		public void unlockPieceGroup(String group) {
			if(!learning && levelPoints > 0 && !isPieceGroupUnlocked(group)) {
				spellGroupsUnlocked.add(group);
				lastSpellGroup = group;
				levelPoints--;
				learning = true;
				save();
			}
		}

		@Override
		public void markPieceExecuted(SpellPiece piece) {
			if(lastSpellGroup == null || lastSpellGroup.isEmpty() || !learning)
				return;

			PieceGroup group = PsiAPI.groupsForName.get(lastSpellGroup);
			if(group != null && group.mainPiece == piece.getClass())
				levelUp();
		}

		@Override
		public NBTTagCompound getCustomData() {
			if (customData == null)
				return customData = new NBTTagCompound();
			return customData;
		}

		@Override
		public void save() {
			if(!client) {
				EntityPlayer player = playerWR.get();

				if(player != null) {
					validate();
					NBTTagCompound cmp = getDataCompoundForPlayer(player);
					writeToNBT(cmp);
				}
			}
		}

		public void writeToNBT(NBTTagCompound cmp) {
			cmp.setInteger(TAG_LEVEL, level);
			cmp.setInteger(TAG_AVAILABLE_PSI, availablePsi);
			cmp.setInteger(TAG_REGEN_CD, regenCooldown);
			cmp.setInteger(TAG_LEVEL_POINTS, levelPoints);
			cmp.setBoolean(TAG_LEARNING_GROUP, learning);
			if(lastSpellGroup != null && !lastSpellGroup.isEmpty())
				cmp.setString(TAG_LAST_SPELL_GROUP, lastSpellGroup);
			cmp.setBoolean(TAG_OVERFLOWED, overflowed);

			NBTTagList list = new NBTTagList();
			for(String s : spellGroupsUnlocked) {
				if(s != null && !s.isEmpty())
					list.appendTag(new NBTTagString(s));
			}
			cmp.setTag(TAG_SPELL_GROUPS_UNLOCKED, list);

			cmp.setDouble(TAG_EIDOS_ANCHOR_X, eidosAnchor.x);
			cmp.setDouble(TAG_EIDOS_ANCHOR_Y, eidosAnchor.y);
			cmp.setDouble(TAG_EIDOS_ANCHOR_Z, eidosAnchor.z);
			cmp.setDouble(TAG_EIDOS_ANCHOR_PITCH, eidosAnchorPitch);
			cmp.setDouble(TAG_EIDOS_ANCHOR_YAW, eidosAnchorYaw);
			cmp.setInteger(TAG_EIDOS_ANCHOR_TIME, eidosAnchorTime);
			
			if(customData != null)
				cmp.setTag(TAG_CUSTOM_DATA, customData);
		}

		public void load() {
			if(!client) {
				EntityPlayer player = playerWR.get();

				if(player != null) {
					NBTTagCompound cmp = getDataCompoundForPlayer(player);
					readFromNBT(cmp);
					validate();
				}
			}
		}

		public void readFromNBT(NBTTagCompound cmp) {
			level = cmp.getInteger(TAG_LEVEL);
			availablePsi = cmp.getInteger(TAG_AVAILABLE_PSI);
			regenCooldown = cmp.getInteger(TAG_REGEN_CD);
			levelPoints = cmp.getInteger(TAG_LEVEL_POINTS);
			lastSpellGroup = cmp.getString(TAG_LAST_SPELL_GROUP);
			overflowed = cmp.getBoolean(TAG_OVERFLOWED);
			learning = cmp.getBoolean(TAG_LEARNING_GROUP);

			if(cmp.hasKey(TAG_SPELL_GROUPS_UNLOCKED, Constants.NBT.TAG_LIST)) {
				spellGroupsUnlocked.clear();
				NBTTagList list = cmp.getTagList(TAG_SPELL_GROUPS_UNLOCKED, Constants.NBT.TAG_STRING); // 8 -> String
				int count = list.tagCount();
				for(int i = 0; i < count; i++)
					spellGroupsUnlocked.add(list.getStringTagAt(i));
			}

			double x = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
			double y = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
			double z = cmp.getDouble(TAG_EIDOS_ANCHOR_X);
			eidosAnchor.set(x, y, z);
			eidosAnchorPitch = cmp.getDouble(TAG_EIDOS_ANCHOR_PITCH);
			eidosAnchorYaw = cmp.getDouble(TAG_EIDOS_ANCHOR_YAW);
			eidosAnchorTime = cmp.getInteger(TAG_EIDOS_ANCHOR_TIME);
			
			customData = cmp.getCompoundTag(TAG_CUSTOM_DATA);
		}

		@SideOnly(Side.CLIENT)
		public void render(EntityPlayer player, float partTicks) {
			RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
			double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partTicks - renderManager.viewerPosX;
			double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partTicks - renderManager.viewerPosY;
			double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partTicks - renderManager.viewerPosZ;

			float scale = 0.75F;
			if(loopcasting) {
				float mul = Math.min(5F, loopcastTime + partTicks) / 5F;
				scale *= mul;
			} else if(loopcastFadeTime > 0) {
				float mul = Math.min(5F, loopcastFadeTime - partTicks) / 5F;
				scale *= mul;
			} else return;

			int color = ICADColorizer.DEFAULT_SPELL_COLOR;
			ItemStack cad = PsiAPI.getPlayerCAD(playerWR.get());
			if(!cad.isEmpty() && cad.getItem() instanceof ICAD) {
				ICAD icad = (ICAD) cad.getItem();
				color = icad.getSpellColor(cad);
			}

			RenderSpellCircle.renderSpellCircle(ClientTicker.ticksInGame + partTicks, scale, x, y, z, color);
			GlStateManager.disableLighting();
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

				if(elapsed >= cd)
					invalid = true;
			}

			public float getPercentile(float partTicks) {
				return 1F - Math.min(1F, (elapsed + partTicks) / cd);
			}
		}

	}
}
