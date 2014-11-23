package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class ButtonMessage implements IMessage {

	public int dimension, entityID, buttonID, chipX, chipY, chipZ;

	public ButtonMessage() {
	}

	public ButtonMessage(EntityPlayer player, int button, int x, int y, int z) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		buttonID = button;
		chipX = x;
		chipY = y;
		chipZ = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(buttonID);
		buf.writeInt(chipX);
		buf.writeInt(chipY);
		buf.writeInt(chipZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		buttonID = buf.readInt();
		chipX = buf.readInt();
		chipY = buf.readInt();
		chipZ = buf.readInt();
	}
}