package transportterminal.network.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.blocks.BlockDirectional;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.network.message.TeleportMessage;

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

				BlockPos pos = new BlockPos(message.chipX, message.chipY, message.chipZ);
				IBlockState state = world2.getBlockState(pos);
				if (state.getBlock() instanceof BlockTransportTerminal) {
					EnumFacing facing = (EnumFacing) state.getValue(BlockDirectional.FACING);

					if (world2.isAirBlock(pos.add(facing.getFrontOffsetX(), 0, facing.getFrontOffsetZ())) && world2.isAirBlock(pos.add(0, 1, 0)))
						teleportPlayer(player, message.chipX + 0.5D * facing.getFrontOffsetX(), message.chipY, message.chipZ + 0.5D * facing.getFrontOffsetZ(), 0, player.rotationPitch);
				}
			}
		return null;
	}

	private void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}
}