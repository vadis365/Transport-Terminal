package TransportTerminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TeleportMessage implements IMessage {

	public int dimension, entityID, chipDim, chipX, chipY, chipZ;

	public TeleportMessage() {
	}

	public TeleportMessage(EntityPlayer player, int x, int y, int z, int newDim) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		chipDim = newDim;
		chipX = x;
		chipY = y;
		chipZ = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(chipDim);
		buf.writeInt(chipX);
		buf.writeInt(chipY);
		buf.writeInt(chipZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		chipDim = buf.readInt();
		chipX = buf.readInt();
		chipY = buf.readInt();
		chipZ = buf.readInt();
	}
}