package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.TransportTerminal;
import transportterminal.blocks.BlockTransportTerminal;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.TransportTerminalTeleporter;
import transportterminal.network.message.ButtonMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

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
				TileEntityTransportTerminal tile = (TileEntityTransportTerminal) world2.getTileEntity(message.chipX, message.chipY, message.chipZ); 
				if (tile.getStackInSlot(message.buttonID) != null && tile.getStackInSlot(message.buttonID).stackTagCompound.hasKey("chipX")) {
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
				if (world2.getBlock(message.chipX, message.chipY, message.chipZ) instanceof BlockTransportTerminal)
					switch (world2.getBlockMetadata(message.chipX, message.chipY, message.chipZ)) {
						case 2:
							if (world2.isAirBlock(message.chipX, message.chipY, message.chipZ - 1) && world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ - 1)) {
								teleportPlayer(player, message.chipX + 0.5D, message.chipY, message.chipZ - 0.5D, 0, player.rotationPitch);
								consumeEnergy(tile);
							}
							break;
						case 3:
							if (world2.isAirBlock(message.chipX, message.chipY, message.chipZ + 1) && world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ + 1)) {
								teleportPlayer(player, message.chipX + 0.5D, message.chipY, message.chipZ + 1.5D, 180, player.rotationPitch);
								consumeEnergy(tile);
							}
							break;
						case 4:
							if (world2.isAirBlock(message.chipX - 1, message.chipY, message.chipZ) && world2.isAirBlock(message.chipX - 1, message.chipY + 1, message.chipZ)) {
								teleportPlayer(player, message.chipX - 0.5D, message.chipY, message.chipZ + 0.5D, 270, player.rotationPitch);
								consumeEnergy(tile);
							}
							break;
						case 5:
							if (world2.isAirBlock(message.chipX + 1, message.chipY, message.chipZ) && world2.isAirBlock(message.chipX + 1, message.chipY + 1, message.chipZ)) {
								teleportPlayer(player, message.chipX + 1.5D, message.chipY, message.chipZ + 0.5D, 90, player.rotationPitch);
								consumeEnergy(tile);
							}
							break;
					}
				else if (world2.isAirBlock(message.chipX, message.chipY + 1, message.chipZ) && world2.isAirBlock(message.chipX, message.chipY + 2, message.chipZ)) {
					teleportPlayer(player, message.chipX + 0.5, message.chipY + 1.0, message.chipZ + 0.5, player.rotationYaw, player.rotationPitch);
					consumeEnergy(tile);
					}				
				}
			}
		return null;
	}

	private void consumeEnergy(TileEntityTransportTerminal tile) {
		if (tile.canTeleport())
			if(TransportTerminal.IS_RF_PRESENT)
				tile.setEnergy(tile.getEnergyStored(ForgeDirection.UNKNOWN) - ConfigHandler.ENERGY_PER_TELEPORT);	
	}

	private void teleportPlayer(EntityPlayerMP player, double x, double y, double z, float yaw, float pitch) {
		player.playerNetServerHandler.setPlayerLocation(x, y, z, yaw, pitch);
		player.worldObj.playSoundEffect(x, y, z, "transportterminal:teleportsound", 1.0F, 1.0F);
	}
}