package TransportTerminal.network;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import TransportTerminal.blocks.BlockTransportTerminal;
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
				World world2 = DimensionManager.getWorld(message.chipDim);
				Block blockType = world2.getBlock(message.chipX, message.chipY, message.chipZ);
				int blockMeta = world2.getBlockMetadata(message.chipX, message.chipY, message.chipZ);
				if(blockType instanceof BlockTransportTerminal) {
					switch (blockMeta) {
					case 2:
						if(world2.isAirBlock(message.chipX, message.chipY, message.chipZ -1) && world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ - 1)) {
							player.playerNetServerHandler.setPlayerLocation(message.chipX + 0.5D, message.chipY, message.chipZ - 0.5D, 0, player.rotationPitch);
							player.worldObj.playSoundEffect(message.chipX, message.chipY, message.chipZ, "transportterminal:teleportsound", 1.0F, 1.0F);
						}
						break;
					case 3:
						if(world2.isAirBlock(message.chipX, message.chipY, message.chipZ +1) && world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ + 1)) {
							player.playerNetServerHandler.setPlayerLocation(message.chipX + 0.5D, message.chipY, message.chipZ + 1.5D, 180, player.rotationPitch);
							player.worldObj.playSoundEffect(message.chipX, message.chipY, message.chipZ, "transportterminal:teleportsound", 1.0F, 1.0F);
						}
						break;
					case 4:
						if(world2.isAirBlock(message.chipX - 1, message.chipY, message.chipZ) && world2.isAirBlock(message.chipX - 1, message.chipY + 1, message.chipZ)) {
							player.playerNetServerHandler.setPlayerLocation(message.chipX - 0.5D, message.chipY, message.chipZ + 0.5D, 270, player.rotationPitch);
							player.worldObj.playSoundEffect(message.chipX, message.chipY, message.chipZ, "transportterminal:teleportsound", 1.0F, 1.0F);
						}
						break;
					case 5:
						if(world2.isAirBlock(message.chipX + 1, message.chipY, message.chipZ) && world2.isAirBlock(message.chipX + 1, message.chipY + 1, message.chipZ)) {
							player.playerNetServerHandler.setPlayerLocation(message.chipX + 1.5D, message.chipY, message.chipZ + 0.5D, 90, player.rotationPitch);
							player.worldObj.playSoundEffect(message.chipX, message.chipY, message.chipZ, "transportterminal:teleportsound", 1.0F, 1.0F);
						}
						break;
					}
				}
				else {
					if(world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ) && world2.isAirBlock(message.chipX, message.chipY + 2, message.chipZ)) {
						player.playerNetServerHandler.setPlayerLocation(message.chipX + 0.5D, message.chipY + 1.0D, message.chipZ + 0.5D, player.rotationYaw, player.rotationPitch);
						player.worldObj.playSoundEffect(message.chipX, message.chipY, message.chipZ, "transportterminal:teleportsound", 1.0F, 1.0F);
					}
				}
			}
		return null;
	}
}