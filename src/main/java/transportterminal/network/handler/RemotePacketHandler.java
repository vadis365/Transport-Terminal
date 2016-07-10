package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.network.message.TeleportMessage;
import transportterminal.utils.TeleportUtils;

public class RemotePacketHandler implements IMessageHandler<TeleportMessage, IMessage> {

	@Override
	public IMessage onMessage(final TeleportMessage message, MessageContext ctx) {
		final World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						WorldServer worldserver = (WorldServer) world;
						TeleportUtils.dimensionTransfer(worldserver, player, message.chipDim);
						WorldServer world2 = DimensionManager.getWorld(message.chipDim);
						if (TeleportUtils.isConsole(world2, message.pos)) {
							TeleportUtils.teleportToConsole(world2, player, message.pos);
						}
					}
				});
			}
		return null;
	}
}