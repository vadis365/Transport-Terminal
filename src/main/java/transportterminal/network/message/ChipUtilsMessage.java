package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ChipUtilsMessage implements IMessage {

	public int dimension, entityID, tileX, tileY, tileZ, funcID;
	public String name;

	public ChipUtilsMessage() {
	}

	public ChipUtilsMessage(EntityPlayer player, String string, int x, int y, int z, int id) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		name = string;
		tileX = x;
		tileY = y;
		tileZ = z;
		funcID = id;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, name);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
		buf.writeInt(funcID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
		funcID = buf.readInt();
	}
}