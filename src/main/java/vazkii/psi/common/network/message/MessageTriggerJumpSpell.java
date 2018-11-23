package vazkii.psi.common.network.message;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vazkii.arl.network.NetworkMessage;
import vazkii.arl.util.ClientTicker;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class MessageTriggerJumpSpell extends NetworkMessage<MessageTriggerJumpSpell> {

	@Override
	public IMessage handleMessage(MessageContext context) {
		ClientTicker.addAction(() -> {
			PsiArmorEvent.post(new PsiArmorEvent(context.getServerHandler().player, PsiArmorEvent.JUMP));
		});
		
		return null;
	}
	
}
