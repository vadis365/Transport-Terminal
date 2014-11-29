package transportterminal.network.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.network.message.NamingMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class NamingPacketHandler implements IMessageHandler<NamingMessage, IMessage> {

	@Override
	public IMessage onMessage(NamingMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				world = DimensionManager.getWorld(player.getCurrentEquippedItem().getTagCompound().getInteger("dim"));
				BlockPos pos = new BlockPos(message.tileX, message.tileY, message.tileZ);
				TileEntityTransportTerminal console = (TileEntityTransportTerminal) world.getTileEntity(pos);
				if (console != null)
					console.setName(message.name);
			}
		return null;
	}
}
