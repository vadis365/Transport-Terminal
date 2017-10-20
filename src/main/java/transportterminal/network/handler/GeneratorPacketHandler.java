package transportterminal.network.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.network.message.GeneratorMessage;
import transportterminal.tileentites.TileEntityGenerator;

public class GeneratorPacketHandler implements IMessageHandler<GeneratorMessage, IMessage> {

	@Override
	public IMessage onMessage(final GeneratorMessage message, MessageContext ctx) {

		final World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().player.getEntityId() == message.entityID) {
				final EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServer().addScheduledTask(new Runnable() {
					public void run() {
						TileEntityGenerator generator = (TileEntityGenerator ) world.getTileEntity(message.tilePos);
						if (generator!= null) {
							generator.toggleMode(EnumFacing.getFront(message.buttonID));
							IBlockState state = world.getBlockState(message.tilePos);
							world.notifyBlockUpdate(message.tilePos, state, state, 3);
						}
					}
				});
			}
		return null;
	}
}