package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.tileentites.TileEntitySummoner;
import transportterminal.utils.TeleportUtils;

public class PlayerSummonPacketHandler implements IMessageHandler<PlayerSummonMessage, IMessage> {

	@Override
	public IMessage onMessage(PlayerSummonMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				TileEntitySummoner tile = (TileEntitySummoner) world.getTileEntity(message.tilePos);
				if (tile != null && tile.getStackInSlot(message.buttonID) != null && tile.getStackInSlot(message.buttonID).hasDisplayName()) {
					EntityPlayerMP playerOnChip = TeleportUtils.getPlayerByUsername(tile.getStackInSlot(message.buttonID).getDisplayName());
					EntityPlayerMP player = ctx.getServerHandler().playerEntity;
					WorldServer worldserver = (WorldServer) world;
					if (tile.canTeleport() && ConfigHandler.ALLOW_TELEPORT_SUMMON_PLAYER)
						if (player != playerOnChip && playerOnChip != null) {
							TeleportUtils.dimensionTransfer(worldserver, player, playerOnChip);
							TeleportUtils.teleportPlayer(playerOnChip, message.tilePos.getX() + 0.5D, message.tilePos.getY(), message.tilePos.getZ() + 0.5D, playerOnChip.rotationYaw, playerOnChip.rotationPitch);
							TeleportUtils.consumeSummonerEnergy(tile);
						}
				}
			}
		return null;
	}
}