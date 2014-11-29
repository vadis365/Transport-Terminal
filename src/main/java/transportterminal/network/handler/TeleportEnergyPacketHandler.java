package transportterminal.network.handler;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.EnergyMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;

public class TeleportEnergyPacketHandler implements IMessageHandler<EnergyMessage, IMessage> {

	@Override
	public IMessage onMessage(EnergyMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				BlockPos pos = new BlockPos(message.tileX, message.tileY, message.tileZ);
				TileEntityTransportTerminal console = (TileEntityTransportTerminal) world.getTileEntity(pos);
			//	if (console != null && console.canTeleport())
			//		console.setEnergy(console.getEnergyStored(ForgeDirection.UNKNOWN) - ConfigHandler.ENERGY_PER_TELEPORT);
			}
		return null;
	}
}
