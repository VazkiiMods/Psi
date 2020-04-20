package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.helpers.SpellHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PieceTrickBroadcast extends PieceTrick {

	SpellParam<Number> radius;
	SpellParam<Number> channel;
	SpellParam<Vector3> position;
	SpellParam<Number> signal;

	private static final String SIGNAL_TRACKING_KEY = "psi:BroadcastedSignal";
	private static final String CHANNEL_TRACKING_KEY = "psi:BroadcastedChannel";
	private static final String RECEIVERS_TRACKING_KEY = "psi:BroadcastedToWhom";
	//Prevents broadcasting twice in the same spell
	private static final String DUPLICATE_TRACKING_KEY = "psi:AlreadyBroadcasted";

	public PieceTrickBroadcast(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(channel = new ParamNumber(SpellParam.GENERIC_NAME_CHANNEL, SpellParam.RED, true, true));
		addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
		addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
		addParam(signal = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.YELLOW, false, false));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);

		double radiusVal = SpellHelpers.ensurePositiveAndNonzero(this, radius, SpellContext.MAX_DISTANCE);
		SpellHelpers.ensurePositiveOrZero(this, channel, 0);
		meta.addStat(EnumSpellStat.COST, (int) (radiusVal * 5));
		meta.addStat(EnumSpellStat.POTENCY, 5);
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Vector3 positionVal = SpellHelpers.getVector3(this, context, position, true, false);
		double radiusVal = Math.max(this.getParamValue(context, radius).doubleValue(), SpellContext.MAX_DISTANCE);
		int channelVal = this.getParamValueOrDefault(context, channel, 0).intValue();
		double signalVal = this.getParamValue(context, signal).doubleValue();

		if (context.customData.containsKey(DUPLICATE_TRACKING_KEY)) {
			return null;
		}

		context.customData.put(DUPLICATE_TRACKING_KEY, true);

		List<PlayerEntity> sec = new ArrayList<>();

		String channelKey = "psi_broadcast_channel:" + channelVal;


		AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


		List<PlayerEntity> list = context.caster.world.getEntitiesWithinAABB(PlayerEntity.class, axis,
				(PlayerEntity e) -> e != null && e != context.caster && e != context.focalPoint && context.isInRadius(e));
		if (list.size() > 0) {

			//actually broadcasts it!
			for (Entity ent : list) {
				PlayerEntity pl = (PlayerEntity) ent;
				if (PsiAPI.getPlayerCAD(pl) != null) {
					sec.add(pl);
				}
			}

			writeSecurity(sec, channelVal, signalVal, context.caster, context.caster.world);

			for (Entity ent : list) {
				PlayerEntity pl = (PlayerEntity) ent;
				if (PsiAPI.getPlayerCAD(pl) != null && pl != null) {
					PlayerDataHandler.PlayerData temp = PlayerDataHandler.get(pl);
					temp.getCustomData().putDouble(channelKey, signalVal);
					temp.save();
				}
			}
		}

		return null;
	}

	private void writeSecurity(List<PlayerEntity> list, int secChannel, double secSignal, PlayerEntity player, World world) {
		PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
		if (data.getCustomData().contains(RECEIVERS_TRACKING_KEY) && data.getCustomData().contains(CHANNEL_TRACKING_KEY) && data.getCustomData().contains(SIGNAL_TRACKING_KEY)) {
			ListNBT list1 = (ListNBT) data.getCustomData().get(RECEIVERS_TRACKING_KEY);
			int channel = data.getCustomData().getInt(CHANNEL_TRACKING_KEY);
			double signal = data.getCustomData().getDouble(SIGNAL_TRACKING_KEY);
			String key = "psi_broadcast_channel:" + channel;
			for (INBT cmp : list1) {
				CompoundNBT rcmp = (CompoundNBT) cmp;
				PlayerEntity pl = world.getPlayerByUuid(Objects.requireNonNull(rcmp.getUniqueId(RECEIVERS_TRACKING_KEY)));
				if (pl != null) {
					PlayerDataHandler.PlayerData pldata = PlayerDataHandler.get(pl);
					if (pldata.getCustomData().contains(key) && pldata.getCustomData().getDouble(key) == signal) {
						pldata.getCustomData().remove(key);
						pldata.save();
					}
				}
			}
		}


		ListNBT list1 = new ListNBT();
		for (PlayerEntity pl : list) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putUniqueId(RECEIVERS_TRACKING_KEY, pl.getUniqueID());
			list1.add(nbt);
		}
		data.getCustomData().put(RECEIVERS_TRACKING_KEY, list1);
		data.getCustomData().putInt(CHANNEL_TRACKING_KEY, secChannel);
		data.getCustomData().putDouble(SIGNAL_TRACKING_KEY, secSignal);
		data.save();
	}

}
