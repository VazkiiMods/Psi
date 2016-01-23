/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [10/01/2016, 23:21:21 (GMT)]
 */
package vazkii.psi.common.core.handler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.IPlayerData;
import vazkii.psi.api.spell.PieceGroup;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.network.NetworkHandler;
import vazkii.psi.common.network.message.MessageDataSync;
import vazkii.psi.common.network.message.MessageDeductPsi;

public class PlayerDataHandler {

	private static HashMap<Integer, PlayerData> playerData = new HashMap();

	private static final String DATA_TAG = "PsiData";

	public static final DamageSource damageSourceOverload = new DamageSource("psi-overload").setDamageBypassesArmor().setMagicDamage();

	public static PlayerData get(EntityPlayer player) {
		int key = getKey(player);
		if(!playerData.containsKey(key))
			playerData.put(key, new PlayerData(player));

		return playerData.get(key);
	}

	public static void cleanup() {
		List<Integer> remove = new ArrayList();

		for(int i : playerData.keySet()) {
			PlayerData d = playerData.get(i);
			if(d.playerWR.get() == null)
				remove.add(i);
		}

		for(int i : remove)
			playerData.remove(i);
	}

	private static int getKey(EntityPlayer player) {
		return player.hashCode() << 1 + (player.worldObj.isRemote ? 1 : 0);
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

	public static class EventHandler {

		@SubscribeEvent
		public void onServerTick(ServerTickEvent event) {
			if(event.phase == Phase.END)
				PlayerDataHandler.cleanup();
		}

		@SubscribeEvent
		public void onPlayerTick(LivingUpdateEvent event) {
			if(event.entityLiving instanceof EntityPlayer)
				PlayerDataHandler.get((EntityPlayer) event.entityLiving).tick();
		}

		@SubscribeEvent
		public void onEntityDamage(LivingHurtEvent event) {
			if(event.entityLiving instanceof EntityPlayer)
				PlayerDataHandler.get((EntityPlayer) event.entityLiving).damage(event.ammount);
		}

		@SubscribeEvent
		public void onPlayerLogin(PlayerLoggedInEvent event) {
			if(event.player instanceof EntityPlayerMP) {
				MessageDataSync message = new MessageDataSync(get(event.player));
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) event.player);
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
		
		public int level;
		public int availablePsi;
		public int lastAvailablePsi;
		public int regenCooldown;
		public String lastSpellGroup;
		public int levelPoints;

		public boolean deductTick;

		public final List<String> spellGroupsUnlocked = new ArrayList();
		public final List<Deduction> deductions = new ArrayList();
		public final WeakReference<EntityPlayer> playerWR;
		private final boolean client;

		public PlayerData(EntityPlayer player) {
			playerWR = new WeakReference(player);
			client = player.worldObj.isRemote;

			load();
		}

		public void tick() {
			if(deductTick)
				deductTick = false;
			else lastAvailablePsi = availablePsi;

			int max = getTotalPsi();
			if(availablePsi > max)
				availablePsi = max;
			
			if(regenCooldown == 0) {
				boolean doRegen = true;
				ItemStack cadStack = getCAD();
				if(cadStack != null) {
					ICAD cad = (ICAD) cadStack.getItem();
					int maxPsi = cad.getStatValue(cadStack, EnumCADStat.OVERFLOW);
					int currPsi = cad.getStoredPsi(cadStack);
					if(currPsi < maxPsi) {
						cad.regenPsi(cadStack, getRegenPerTick());
						doRegen = false;
					}
				}
				
				if(doRegen && availablePsi < max && regenCooldown == 0) {
						availablePsi = Math.min(max, availablePsi + getRegenPerTick());
						save();
				}
			} else {
				regenCooldown--;
				save();
			}

			List<Deduction> remove = new ArrayList();
			for(Deduction d : deductions) {
				if(d.invalid)
					remove.add(d);
				else d.tick();
			}
			deductions.removeAll(remove);
		}

		public void damage(float amount) {
			int psi = (int) (getTotalPsi() * 0.02 * amount);
			if(psi > 0 && availablePsi > 0) {
				psi = Math.min(psi, availablePsi);
				deductPsi(psi, 20, true, true);
			}
		}
		
		public void levelUp() {
			level++;
			levelPoints++;
			lastSpellGroup = "";
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

			availablePsi -= psi;
			if(regenCooldown < cd)
				regenCooldown = cd;

			if(availablePsi < 0) {
				int overflow = -availablePsi;
				availablePsi = 0;

				ItemStack cadStack = getCAD();
				if(cadStack != null) {
					ICAD cad = (ICAD) cadStack.getItem();
					overflow = cad.consumePsi(cadStack, overflow);
				}

				if(!shatter && overflow > 0) {
					float dmg = (float) overflow / 50;
					if(!client) {
						EntityPlayer player = playerWR.get();
						if(player != null)
							player.attackEntityFrom(damageSourceOverload, dmg); 
					}
				}
			}

			if(sync && playerWR.get() instanceof EntityPlayerMP) {
				MessageDeductPsi message = new MessageDeductPsi(currentPsi, availablePsi, regenCooldown, shatter);
				NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) playerWR.get());
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
		
		public int getTotalPsi() {
			return getLevel() * 200;
		}

		@Override
		public int getRegenPerTick() {
			return getLevel();
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
			if(!isPieceGroupUnlocked(group)) {
				spellGroupsUnlocked.add(group);
				lastSpellGroup = group;
				levelPoints--;
			}
		}
		
		@Override
		public void markPieceExecuted(SpellPiece piece) {
			if(lastSpellGroup.isEmpty())
				return;
			
			PieceGroup group = PsiAPI.groupsForName.get(lastSpellGroup);
			if(group.mainPiece == piece.getClass())
				levelUp();
		}
		
		public void save() {
			if(!client) {
				EntityPlayer player = playerWR.get();

				if(player != null) {
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
			cmp.setString(TAG_LAST_SPELL_GROUP, lastSpellGroup);
			
			NBTTagList list = new NBTTagList();
			for(String s : spellGroupsUnlocked)
				list.appendTag(new NBTTagString(s));
			cmp.setTag(TAG_SPELL_GROUPS_UNLOCKED, list);
		}

		public void load() {
			if(!client) {
				EntityPlayer player = playerWR.get();

				if(player != null) {
					NBTTagCompound cmp = getDataCompoundForPlayer(player);
					readFromNBT(cmp);
				}
			}
		}

		public void readFromNBT(NBTTagCompound cmp) {
			level = cmp.getInteger(TAG_LEVEL);
			availablePsi = cmp.getInteger(TAG_AVAILABLE_PSI);
			regenCooldown = cmp.getInteger(TAG_REGEN_CD);
			levelPoints = cmp.getInteger(TAG_LEVEL_POINTS);
			lastSpellGroup = cmp.getString(TAG_LAST_SPELL_GROUP);
			
			if(cmp.hasKey(TAG_SPELL_GROUPS_UNLOCKED)) {
				spellGroupsUnlocked.clear();
				NBTTagList list = cmp.getTagList(TAG_SPELL_GROUPS_UNLOCKED, 8); // 8 -> String
				int count = list.tagCount();
				for(int i = 0; i < count; i++)
					spellGroupsUnlocked.add(list.getStringTagAt(i));
			}
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