package TransportTerminal.network;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import TransportTerminal.TransportTerminal;
import TransportTerminal.tileentites.TileEntityTransportItems;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TeleportPacketHandlerItems implements IMessageHandler<TeleportMessageItems, IMessage> {
	private Ticket ticket;
	@Override
	public IMessage onMessage(TeleportMessageItems message, MessageContext ctx) {
		WorldServer world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;
		
		else if (!world.isRemote) {
			WorldServer world2 = DimensionManager.getWorld(message.chipDim);
			TileEntityTransportItems postBox = (TileEntityTransportItems) world.getTileEntity(message.tileX, message.tileY, message.tileZ);
			if (postBox != null) {
				ItemStack is = postBox.getStackInSlot(1);
				if (is != null) {
					if (ticket == null)
						ticket = ForgeChunkManager.requestTicket(TransportTerminal.instance, world2, ForgeChunkManager.Type.NORMAL);

					if (ticket != null)
						ForgeChunkManager.forceChunk(ticket, new ChunkCoordIntPair(message.chipX, message.chipZ));
						
					MinecraftServer mcServer = MinecraftServer.getServer();
					EntityItem entityitem = new EntityItem(world, message.tileX + 0.5D, message.tileY + 1.5D, message.tileZ + 0.5D, is);
					entityitem.delayBeforeCanPickup = 20;
					entityitem.worldObj.removeEntity(entityitem);
					mcServer.getConfigurationManager().transferEntityToWorld(entityitem, message.chipDim, world, world2, new TransportTerminalTeleporter(world2));
					entityitem.isDead = false;
					entityitem.setLocationAndAngles(message.chipX + 0.5D, message.chipY + 1D, message.chipZ + 0.5D, entityitem.rotationYaw, entityitem.rotationPitch);
					world2.spawnEntityInWorld(entityitem);
					entityitem.worldObj.updateEntity(entityitem);
					world.playSoundEffect(message.tileX, message.tileY, message.tileZ, "transportterminal:teleportsound", 1.0F, 1.0F);
					world2.playSoundEffect(message.chipX, message.chipY, message.chipZ, "transportterminal:teleportsound", 1.0F, 1.0F);
					postBox.setInventorySlotContents(1, null);
				}
			}
		}
		return null;
	}
}