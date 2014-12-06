package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import transportterminal.network.message.TeleportMessage;
import transportterminal.utils.TeleportUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RemotePacketHandler implements IMessageHandler<TeleportMessage, IMessage> {

	@Override
	public IMessage onMessage(TeleportMessage message, MessageContext ctx) {
		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				WorldServer worldserver = (WorldServer) world;
				TeleportUtils.dimensionTransfer(worldserver, player, message.chipDim);
				World world2 = DimensionManager.getWorld(message.chipDim);
				if (TeleportUtils.isConsole(world2, message.chipX, message.chipY, message.chipZ))
					TeleportUtils.teleportToConsole(world2, player, message.chipX, message.chipY, message.chipZ);
			}
		return null;
	}
}