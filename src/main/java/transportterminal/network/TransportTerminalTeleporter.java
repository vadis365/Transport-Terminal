package transportterminal.network;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TransportTerminalTeleporter extends Teleporter {

	public TransportTerminalTeleporter(WorldServer worldServer) {
		super(worldServer);
	}

	@Override
	public void placeInPortal(Entity pEntity, float rotationYaw) {
	}
}