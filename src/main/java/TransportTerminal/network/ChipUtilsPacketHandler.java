package TransportTerminal.network;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import TransportTerminal.tileentites.TileEntityChipUtilities;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ChipUtilsPacketHandler implements IMessageHandler<ChipUtilsMessage, IMessage> {
	
	public final int COPY_CHIP = 0, ERASE_CHIP = 1, ERASE_PLAYER_CHIP = 2, NAME_PLAYER_CHIP = 3;
	
	@Override
	public IMessage onMessage(ChipUtilsMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote) {
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				TileEntityChipUtilities utilsTile = (TileEntityChipUtilities) world.getTileEntity(message.tileX, message.tileY, message.tileZ);
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
		}
		return null;
	}
}
