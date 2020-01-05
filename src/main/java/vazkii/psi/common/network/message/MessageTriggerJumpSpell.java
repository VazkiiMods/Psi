package vazkii.psi.common.network.message;

import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.arl.network.IMessage;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class MessageTriggerJumpSpell implements IMessage {

	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> PsiArmorEvent.post(new PsiArmorEvent(context.getSender(), PsiArmorEvent.JUMP)));
		return true;
	}
	
}
