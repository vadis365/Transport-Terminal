package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class ButtonMessage implements IMessage {

	public int dimension, newDimension, entityID, buttonID, tileX, tileY, tileZ;

	public ButtonMessage() {
	}

	public ButtonMessage(EntityPlayer player, int button, int x, int y, int z, int newDim) {
		dimension = player.dimension;
		newDimension = newDim;
		entityID = player.getEntityId();
		buttonID = button;
		tileX = x;
		tileY = y;
		tileZ = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(newDimension);
		buf.writeInt(entityID);
		buf.writeInt(buttonID);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		newDimension = buf.readInt();
		entityID = buf.readInt();
		buttonID = buf.readInt();
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}
}