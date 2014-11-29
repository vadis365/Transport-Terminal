package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.network.message.PlayerChipMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class PlayerChipPacketHandler implements IMessageHandler<PlayerChipMessage, IMessage> {

	@Override
	public IMessage onMessage(PlayerChipMessage message, MessageContext ctx) {

		EntityPlayer playerOnChip = MinecraftServer.getServer().getConfigurationManager().func_152612_a(message.playerOnChip);
		World world = DimensionManager.getWorld(message.dimension);

		if (world == null || playerOnChip == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				WorldServer worldserver = (WorldServer) world;
				if (player != playerOnChip) {
					if (playerOnChip.dimension != player.dimension && player.dimension != 1)
						player.mcServer.getConfigurationManager().transferPlayerToDimension(player, playerOnChip.dimension, new TransportTerminalTeleporter(worldserver));
					if (playerOnChip.dimension != player.dimension && player.dimension == 1) {
						player.mcServer.getConfigurationManager().transferPlayerToDimension(player, playerOnChip.dimension, new TransportTerminalTeleporter(worldserver));
						player.mcServer.getConfigurationManager().transferPlayerToDimension(player, playerOnChip.dimension, new TransportTerminalTeleporter(worldserver));
					}
					BlockPos pos = new BlockPos(message.tileX, message.tileY, message.tileZ);
					TileEntityTransportTerminal console = (TileEntityTransportTerminal) world.getTileEntity(pos);
					if (console != null && console.canTeleport())
						if (TransportTerminal.IS_RF_PRESENT)
							console.setEnergy(console.getEnergyStored(null) - ConfigHandler.ENERGY_PER_TELEPORT);
					teleportPlayer(player, playerOnChip.posX, playerOnChip.posY, playerOnChip.posZ, player.rotationYaw, player.rotationPitch);
				}
			}
		return null;
	}

	private void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}
}