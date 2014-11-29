package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
import transportterminal.items.ItemTransportTerminalChip;
import transportterminal.items.ItemTransportTerminalPlayerChip;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.network.message.ButtonMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class ShadowTeleportPacketHandler implements IMessageHandler<ButtonMessage, IMessage> {

	@Override
	public IMessage onMessage(ButtonMessage message, MessageContext ctx) {
		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				ItemStack stack = player.getCurrentEquippedItem();
				WorldServer world2 = DimensionManager.getWorld(stack.getTagCompound().getInteger("dim"));
				WorldServer worldserver = (WorldServer) world;
				BlockPos pos = new BlockPos(message.tileX, message.tileY, message.tileZ);
				TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(pos);
				if (tile != null && tile.canTeleport() && tile.getStackInSlot(message.buttonID) != null && tile.getStackInSlot(message.buttonID).getTagCompound().hasKey("chipX") && tile.getStackInSlot(message.buttonID).getItem() instanceof ItemTransportTerminalChip) {
					int newDim = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipDim");
					int x = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipX");
					int y = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipY");
					int z = tile.getStackInSlot(message.buttonID).getTagCompound().getInteger("chipZ");
					if (newDim != player.dimension && player.dimension != 1)
						player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
					if (newDim != player.dimension && player.dimension == 1) {
						player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
						player.mcServer.getConfigurationManager().transferPlayerToDimension(player, newDim, new TransportTerminalTeleporter(worldserver));
					}
					world2 = DimensionManager.getWorld(newDim);
					BlockPos pos1 = new BlockPos(x, y + 1, z);
					BlockPos pos2 = new BlockPos(x, y + 2, z);
					if (world2.isAirBlock(pos1) && world2.isAirBlock(pos2)) {
						teleportPlayer(player, x + 0.5D, y + 1.0D, z + 0.5D, player.rotationYaw, player.rotationPitch);
						consumeEnergy(tile);
					}
				}
				if (tile != null && tile.canTeleport() && tile.getStackInSlot(message.buttonID) != null && tile.getStackInSlot(message.buttonID).hasDisplayName() && tile.getStackInSlot(message.buttonID).getItem() instanceof ItemTransportTerminalPlayerChip)
					if (ConfigHandler.ALLOW_TELEPORT_TO_PLAYER) {
						EntityPlayer playerOnChip = MinecraftServer.getServer().getConfigurationManager().func_152612_a(tile.getStackInSlot(message.buttonID).getDisplayName());
						if (playerOnChip != null && playerOnChip != player) {
							if (playerOnChip.dimension != player.dimension && player.dimension != 1)
								player.mcServer.getConfigurationManager().transferPlayerToDimension(player, playerOnChip.dimension, new TransportTerminalTeleporter(worldserver));
							if (playerOnChip.dimension != player.dimension && player.dimension == 1) {
								player.mcServer.getConfigurationManager().transferPlayerToDimension(player, playerOnChip.dimension, new TransportTerminalTeleporter(worldserver));
								player.mcServer.getConfigurationManager().transferPlayerToDimension(player, playerOnChip.dimension, new TransportTerminalTeleporter(worldserver));
							}
							teleportPlayer(player, playerOnChip.posX, playerOnChip.posY, playerOnChip.posZ, player.rotationYaw, player.rotationPitch);
							consumeEnergy(tile);
						}
					}
			}
		return null;
	}

	private void consumeEnergy(TileEntityTransportTerminal tile) {
	//	if (tile.canTeleport())
	//		if (TransportTerminal.IS_RF_PRESENT)
	//			tile.setEnergy(tile.getEnergyStored(ForgeDirection.UNKNOWN) - ConfigHandler.ENERGY_PER_TELEPORT);
	}

	private void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}
}