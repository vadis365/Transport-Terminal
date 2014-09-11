package TransportTerminal;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class NamingPacketHandler implements
		IMessageHandler<NamingMessage, IMessage> {

	@Override
	public IMessage onMessage(NamingMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote) {
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				world = DimensionManager.getWorld(player.getCurrentEquippedItem().getTagCompound().getInteger("dim"));
				TileEntityTransportTerminal console = (TileEntityTransportTerminal) world.getTileEntity(message.tileX, message.tileY, message.tileZ);
				if (console != null)
					console.setName(message.name);
			}
		}
		return null;
	}
}
