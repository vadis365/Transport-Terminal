package transportterminal.network.handler;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;
import transportterminal.core.confighandler.ConfigHandler;
import transportterminal.network.message.EnergyMessage;
import transportterminal.tileentites.TileEntityTransportTerminal;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TeleportEnergyPacketHandler implements IMessageHandler<EnergyMessage, IMessage> {

	@Override
	public IMessage onMessage(EnergyMessage message, MessageContext ctx) {

		World world = DimensionManager.getWorld(message.dimension);

		if (world == null)
			return null;

		else if (!world.isRemote)
			if (ctx.getServerHandler().playerEntity.getEntityId() == message.entityID) {
				TileEntityTransportTerminal console = (TileEntityTransportTerminal) world.getTileEntity(message.tileX, message.tileY, message.tileZ);
				if (console != null && console.canTeleport())
					console.setEnergy(console.getEnergyStored(ForgeDirection.UNKNOWN) - ConfigHandler.ENERGY_PER_TELEPORT);
			}
		return null;
	}
}
