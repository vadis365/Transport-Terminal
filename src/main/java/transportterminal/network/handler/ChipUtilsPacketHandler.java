package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.network.message.ChipUtilsMessage;
import transportterminal.tileentites.TileEntityChipUtilities;

public class ChipUtilsPacketHandler implements IMessageHandler<ChipUtilsMessage, IMessage> {

	public final int COPY_CHIP = 0, ERASE_CHIP = 1, ERASE_PLAYER_CHIP = 2, NAME_PLAYER_CHIP = 3;

	@Override
	public IMessage onMessage(final ChipUtilsMessage message, MessageContext ctx) {

		final World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().player.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						TileEntityChipUtilities utilsTile = (TileEntityChipUtilities) world.getTileEntity(message.tilePos);
						if (utilsTile != null) {
							if (message.funcID == COPY_CHIP)
								utilsTile.copyChip();

							if (message.funcID == ERASE_CHIP)
								utilsTile.eraseChip();

							if (message.funcID == ERASE_PLAYER_CHIP)
								utilsTile.erasePlayerChip();

							if (message.funcID == NAME_PLAYER_CHIP) {
								utilsTile.erasePlayerChip();
								utilsTile.setName(message.name);
							}
						}
					}
				});
			}
		return null;
	}
}