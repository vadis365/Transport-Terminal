package transportterminal.network.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.TransportTerminal;
import transportterminal.network.message.ItemTransporterMessage;
import transportterminal.tileentites.TileEntityItemTransporter;
import transportterminal.utils.DimensionUtils;
import transportterminal.utils.TeleportUtils;

public class ItemTransporterPacketHandler implements IMessageHandler<ItemTransporterMessage, IMessage> {

	@Override
	public IMessage onMessage(final ItemTransporterMessage message, MessageContext ctx) {

		final World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						TileEntityItemTransporter tile = (TileEntityItemTransporter) world.getTileEntity(message.tilePos);
						if (tile != null && TeleportUtils.isValidInterfaceStandardChip(tile)) {
							WorldServer worldserver = (WorldServer) world;
							if (tile.canTeleport()) {
								ItemStack is = tile.getStackInSlot(1).copy();
								ItemStack chip = tile.getStackInSlot(0);
								if (is != null) {
									int newDim = chip.getTagCompound().getInteger("chipDim");
									int x = chip.getTagCompound().getInteger("chipX");
									int y = chip.getTagCompound().getInteger("chipY");
									int z = chip.getTagCompound().getInteger("chipZ");
									if(newDim == 1) {
										world.playSound(null, player.posX, player.posY, player.posZ, TransportTerminal.ERROR_SOUND, SoundCategory.PLAYERS, 1.0F, 1.0F);
										return;
									}
									DimensionUtils.forceChunkloading((EntityPlayerMP) player, newDim, x, y, z);
									WorldServer world2 = DimensionUtils.getWorldFromDimID(newDim);

									EntityItem entityitem = new EntityItem(world2, x + 0.5D, y + 1.5D, z + 0.5D, is);
									entityitem.setLocationAndAngles(x + 0.5D, y + 1.5D, z + 0.5D, entityitem.rotationYaw, entityitem.rotationPitch);
									world2.spawnEntityInWorld(entityitem);
									tile.setInventorySlotContents(1, null);
									TeleportUtils.consumeItemTransporterEnergy(tile, is);
								}
							}
						}
					}
				});
			}
		return null;
	}
}