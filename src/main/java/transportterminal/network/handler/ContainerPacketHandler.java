package transportterminal.network.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import transportterminal.gui.server.ContainerEnergy;
import transportterminal.network.message.ContainerMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ContainerPacketHandler implements IMessageHandler<ContainerMessage, IMessage> {

	@Override
	public IMessage onMessage(ContainerMessage message, MessageContext ctx) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

		if (player.openContainer != null && player.openContainer.windowId == message.windowId) {
			ContainerEnergy container = (ContainerEnergy) player.openContainer;
			container.tile().setEnergy(message.power);
		}

		return null;
	}
}