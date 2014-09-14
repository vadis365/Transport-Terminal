package TransportTerminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TeleportMessage implements IMessage {

	public int dimension, entityID, chipDim, chipX, chipY, chipZ;

	public TeleportMessage() {}

	public TeleportMessage(EntityPlayer player, int x, int y, int z, int newDim) {
		this.dimension = player.dimension;
		this.entityID = player.getEntityId();
		this.chipDim = newDim;
		this.chipX = x;
		this.chipY = y;
		this.chipZ = z;
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
		this.dimension = buf.readInt();
		this.entityID = buf.readInt();
		this.chipDim = buf.readInt();
		this.chipX = buf.readInt();
		this.chipY = buf.readInt();
		this.chipZ = buf.readInt();
	}

}
