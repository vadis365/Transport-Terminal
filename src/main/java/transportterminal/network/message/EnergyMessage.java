package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class EnergyMessage implements IMessage {

	public int dimension, entityID, tileX, tileY, tileZ;

	public EnergyMessage() {
	}

	public EnergyMessage(EntityPlayer player, int x, int y, int z) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		tileX = x;
		tileY = y;
		tileZ = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}

}
