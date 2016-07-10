package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.network.message.NamingMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import transportterminal.utils.DimensionUtils;

public class NamingPacketHandler implements IMessageHandler<NamingMessage, IMessage> {

	@Override
	public IMessage onMessage(final NamingMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				final int newDim = player.getHeldItemMainhand().getTagCompound().getInteger("dim");
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						DimensionUtils.loadDimension(newDim);
						DimensionUtils.forceChunkloading(newDim, message.tilePos.getX(), message.tilePos.getY(), message.tilePos.getZ());
						World world = DimensionManager.getWorld(newDim);
						TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world.getTileEntity(message.tilePos);
						if (tile != null)
							tile.setName(message.name);
					}
				});
			}
		return null;
	}
}
