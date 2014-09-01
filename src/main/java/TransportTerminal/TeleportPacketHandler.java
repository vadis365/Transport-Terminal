package TransportTerminal;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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
				player.playerNetServerHandler.setPlayerLocation(message.chipX + 0.5D, message.chipY + 1.5D, message.chipZ + 0.5D, player.rotationYaw, player.rotationPitch);
				player.worldObj.playSoundEffect(message.chipX, message.chipY, message.chipZ, "mob.endermen.portal", 1.0F, 1.0F);
			}
		return null;
	}
}