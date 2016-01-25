/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 * 
 * Psi is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [12/01/2016, 16:45:17 (GMT)]
 */
package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.psi.client.core.handler.ClientTickHandler;
import vazkii.psi.common.Psi;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;
import vazkii.psi.common.network.Message;

public class MessageDeductPsi extends Message {

	public int prev;
	public int current;
	public int cd;
	public boolean shatter;
	
	public MessageDeductPsi() { }
	
	public MessageDeductPsi(int prev, int current, int cd, boolean shatter) {
		this.prev = prev;
		this.current = current;
		this.cd = cd;
		this.shatter = shatter;
	}
	
	@Override
	public IMessage handleMessage(MessageContext context) {
		ClientTickHandler.scheduledActions.add(() -> {
			PlayerData data = PlayerDataHandler.get(Psi.proxy.getClientPlayer());
			data.availablePsi = current;
			data.regenCooldown = cd;
			data.deductTick = true;
			data.addDeduction(prev, prev - current, shatter);
		});
		
		return null;
	}
	
}
