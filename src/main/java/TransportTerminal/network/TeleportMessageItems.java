package TransportTerminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TeleportMessageItems implements IMessage {

	public int dimension, entityID, chipDim, chipX, chipY, chipZ, tileX, tileZ, tileY;
	public TeleportMessageItems() {}

	public TeleportMessageItems(EntityPlayer player, int x, int y, int z, int newDim, int teX, int teY, int teZ) {
		dimension = player.dimension;
		chipDim = newDim;
		chipX = x;
		chipY = y;
		chipZ = z;
		tileX = teX;
		tileY = teY;
		tileZ = teZ;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(chipDim);
		buf.writeInt(chipX);
		buf.writeInt(chipY);
		buf.writeInt(chipZ);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		chipDim = buf.readInt();
		chipX = buf.readInt();
		chipY = buf.readInt();
		chipZ = buf.readInt();
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}

}
