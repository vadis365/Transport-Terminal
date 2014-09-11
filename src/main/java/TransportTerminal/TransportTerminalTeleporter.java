package TransportTerminal;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TransportTerminalTeleporter extends Teleporter {

	public TransportTerminalTeleporter(WorldServer worldServer) {
		super(worldServer);
	}

	@Override
	public void placeInPortal(Entity pEntity, double posX, double posY, double posZ, float rotationYaw) {}
}