package vazkii.psi.common.network.message;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import vazkii.arl.network.NetworkMessage;
import vazkii.psi.api.exosuit.PsiArmorEvent;

public class MessageTriggerJumpSpell extends NetworkMessage<MessageTriggerJumpSpell> {

	@Override
	public IMessage handleMessage(MessageContext context) {
		EntityPlayerMP player = context.getServerHandler().player;
		player.getServerWorld().addScheduledTask(() -> PsiArmorEvent.post(new PsiArmorEvent(player, PsiArmorEvent.JUMP)));
		
		return null;
	}
	
}
