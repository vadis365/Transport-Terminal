package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.network.message.TeleportMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TeleportPacketHandler implements IMessageHandler<TeleportMessage, IMessage> {

	@Override
	public IMessage onMessage(TeleportMessage message, MessageContext ctx) {
		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				WorldServer worldserver = (WorldServer) world;
				if (message.chipDim != player.dimension && player.dimension != 1)
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, message.chipDim, new TransportTerminalTeleporter(worldserver));
				if (message.chipDim != player.dimension && player.dimension == 1) {
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, message.chipDim, new TransportTerminalTeleporter(worldserver));
					player.mcServer.getConfigurationManager().transferPlayerToDimension(player, message.chipDim, new TransportTerminalTeleporter(worldserver));
				}
				World world2 = DimensionManager.getWorld(message.chipDim);
				if (world2.getBlock(message.chipX, message.chipY, message.chipZ) instanceof BlockTransportTerminal)
					switch (world2.getBlockMetadata(message.chipX, message.chipY, message.chipZ)) {
						case 2:
							if (world2.isAirBlock(message.chipX, message.chipY, message.chipZ - 1) && world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ - 1))
								teleportPlayer(player, message.chipX + 0.5D, message.chipY, message.chipZ - 0.5D, 0, player.rotationPitch);
							break;
						case 3:
							if (world2.isAirBlock(message.chipX, message.chipY, message.chipZ + 1) && world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ + 1))
								teleportPlayer(player, message.chipX + 0.5D, message.chipY, message.chipZ + 1.5D, 180, player.rotationPitch);
							break;
						case 4:
							if (world2.isAirBlock(message.chipX - 1, message.chipY, message.chipZ) && world2.isAirBlock(message.chipX - 1, message.chipY + 1, message.chipZ))
								teleportPlayer(player, message.chipX - 0.5D, message.chipY, message.chipZ + 0.5D, 270, player.rotationPitch);
							break;
						case 5:
							if (world2.isAirBlock(message.chipX + 1, message.chipY, message.chipZ) && world2.isAirBlock(message.chipX + 1, message.chipY + 1, message.chipZ))
								teleportPlayer(player, message.chipX + 1.5D, message.chipY, message.chipZ + 0.5D, 90, player.rotationPitch);
							break;
					}
				else if (world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ) && world2.isAirBlock(message.chipX, message.chipY + 2, message.chipZ))
					teleportPlayer(player, message.chipX + 0.5, message.chipY + 1.0, message.chipZ + 0.5, player.rotationYaw, player.rotationPitch);
			}
		return null;
	}

	private void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}
}