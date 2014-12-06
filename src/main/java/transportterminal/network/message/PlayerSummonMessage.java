package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PlayerSummonMessage implements IMessage {

	public int dimension, entityID, buttonID, tileX, tileY, tileZ;
	
	public PlayerSummonMessage() {
	}
	
	public PlayerSummonMessage(EntityPlayer player, int button, int x, int y, int z) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		buttonID = button;
		tileX = x;
		tileY = y;
		tileZ = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(buttonID);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		buttonID = buf.readInt();
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}

}
