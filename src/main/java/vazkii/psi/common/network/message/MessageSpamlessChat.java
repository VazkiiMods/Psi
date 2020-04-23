package vazkii.psi.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.arl.network.IMessage;

public class MessageSpamlessChat implements IMessage {

	public ITextComponent message;
	public static final int BASE_MAGIC = 696969;
	public int magic;

	public MessageSpamlessChat(){
		//NO-OP
	}

	public MessageSpamlessChat(ITextComponent message, int magic){
		this.message = message;
		this.magic = BASE_MAGIC + magic;
	}


	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean receive(NetworkEvent.Context context) {
		context.enqueueWork(() -> {
			NewChatGui chatGui = Minecraft.getInstance().ingameGUI.getChatGUI();
			chatGui.deleteChatLine(magic);
			chatGui.printChatMessageWithOptionalDeletion(message, magic);
		});
		return true;
	}
}
