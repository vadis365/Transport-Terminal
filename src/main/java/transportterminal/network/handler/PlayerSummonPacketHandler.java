package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.TransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.tileentites.TileEntitySummoner;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlayerSummonPacketHandler implements IMessageHandler<PlayerSummonMessage, IMessage> {

	@Override
	public IMessage onMessage(PlayerSummonMessage message, MessageContext ctx) {

		EntityPlayerMP playerOnChip = MinecraftServer.getServer().getConfigurationManager().func_152612_a(message.playerOnChip);
		World world = DimensionManager.getWorld(message.dimension);

		if (world == null || playerOnChip == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				WorldServer worldserver = (WorldServer) world;
				if (player != playerOnChip) {
					if (player.dimension != playerOnChip.dimension && playerOnChip.dimension != 1)
						playerOnChip.mcServer.getConfigurationManager().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
					if (player.dimension != playerOnChip.dimension && playerOnChip.dimension == 1) {
						playerOnChip.mcServer.getConfigurationManager().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
						playerOnChip.mcServer.getConfigurationManager().transferPlayerToDimension(playerOnChip, player.dimension, new TransportTerminalTeleporter(worldserver));
					}
					TileEntitySummoner summoner = (TileEntitySummoner) world.getTileEntity(message.tileX, message.tileY, message.tileZ);
					if (summoner != null && summoner.canTeleport())
						if (TransportTerminal.IS_RF_PRESENT)
							summoner.setEnergy(summoner.getEnergyStored(ForgeDirection.UNKNOWN) - ConfigHandler.ENERGY_PER_TELEPORT);
					teleportPlayer(playerOnChip, message.tileX + 0.5D, message.tileY, message.tileZ + 0.5D, playerOnChip.rotationYaw, playerOnChip.rotationPitch);
				}
			}
		return null;
	}

	private void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}
}