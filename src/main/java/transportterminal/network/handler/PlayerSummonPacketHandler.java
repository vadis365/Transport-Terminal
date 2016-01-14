package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.items.ItemTransportTerminalPlayerChip;
import transportterminal.network.message.PlayerSummonMessage;
import transportterminal.tileentites.TileEntitySummoner;
import transportterminal.tileentites.TileEntityTransportTerminal;
import transportterminal.utils.TeleportUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlayerSummonPacketHandler implements IMessageHandler<PlayerSummonMessage, IMessage> {

	@Override
	public IMessage onMessage(PlayerSummonMessage message, MessageContext ctx) {
		
		World world = DimensionManager.getWorld(message.dimension);
		
		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				TileEntitySummoner tile = (TileEntitySummoner) world.getTileEntity(message.tileX, message.tileY, message.tileZ);
				if (tile != null && TeleportUtils.isValidSummonerPlayerChip(tile, message.buttonID)) {
					EntityPlayerMP playerOnChip = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tile.getStackInSlot(message.buttonID).getDisplayName());	
					EntityPlayerMP player = ctx.getServerHandler().playerEntity;
					WorldServer worldserver = (WorldServer) world;
					if (tile.canTeleport() && ConfigHandler.ALLOW_TELEPORT_SUMMON_PLAYER) {
						if (player != playerOnChip && playerOnChip != null) {
							TeleportUtils.dimensionTransfer(worldserver, player, playerOnChip);
							TeleportUtils.teleportPlayer(playerOnChip, message.tileX + 0.5D, message.tileY, message.tileZ + 0.5D, playerOnChip.rotationYaw, playerOnChip.rotationPitch);
							TeleportUtils.consumeSummonerEnergy(tile);
						}
					}
				}
			}
		return null;
	}
}