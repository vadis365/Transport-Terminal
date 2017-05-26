package transportterminal.network.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.gui.server.ContainerEnergy;
import transportterminal.network.message.ContainerMessage;

public class ContainerPacketHandler implements IMessageHandler<ContainerMessage, IMessage> {

	@Override
	public IMessage onMessage(ContainerMessage message, MessageContext ctx) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;

		if (player.openContainer != null && player.openContainer.windowId == message.windowId) {
			ContainerEnergy container = (ContainerEnergy) player.openContainer;
			container.tile().setEnergy(message.power);
		}

		return null;
	}
}