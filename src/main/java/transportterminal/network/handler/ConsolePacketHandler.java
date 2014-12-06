package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.ButtonMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import transportterminal.utils.TeleportUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ConsolePacketHandler implements
		IMessageHandler<ButtonMessage, IMessage> {

	@Override
	public IMessage onMessage(ButtonMessage message, MessageContext ctx) {
		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				ItemStack stack = player.getCurrentEquippedItem();
				WorldServer world2 = DimensionManager.getWorld(message.newDimension);
				WorldServer worldserver = (WorldServer) world;
				TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(message.tileX, message.tileY, message.tileZ);
				if (tile != null && tile.canTeleport() && TeleportUtils.isValidInterfaceStandardChip(tile, message.buttonID)) {
					int newDim = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipDim");
					int x = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipX");
					int y = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipY");
					int z = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipZ");
					TeleportUtils.dimensionTransfer(worldserver, player, newDim);
					world2 = DimensionManager.getWorld(newDim);
					if (TeleportUtils.freeSpace(world2, x, y, z)) {
						TeleportUtils.teleportPlayer(player, x + 0.5D, y + 1.0D, z + 0.5D, player.rotationYaw, player.rotationPitch);
						TeleportUtils.consumeConsoleEnergy(tile);
					}
				}
				if (tile != null && tile.canTeleport() && TeleportUtils.isValidInterfacePlayerChip(tile, message.buttonID)) {
					if (ConfigHandler.ALLOW_TELEPORT_TO_PLAYER) {
						EntityPlayer playerOnChip = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tile.getStackInSlot(message.buttonID).getDisplayName());
						if (playerOnChip != null && playerOnChip != player)
							TeleportUtils.dimensionTransfer(worldserver, player, playerOnChip.dimension);
						TeleportUtils.teleportPlayer(player, playerOnChip.posX, playerOnChip.posY, playerOnChip.posZ, player.rotationYaw, player.rotationPitch);
						TeleportUtils.consumeConsoleEnergy(tile);
					}
				}
			}
		return null;
	}
}