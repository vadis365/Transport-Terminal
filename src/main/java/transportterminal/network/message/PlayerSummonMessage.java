package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerSummonMessage implements IMessage {

	public int dimension, entityID, tileX, tileY, tileZ;
	public String playerOnChip;

	public PlayerSummonMessage() {
	}

	public PlayerSummonMessage(EntityPlayer player, String string, int x, int y, int z) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		playerOnChip = string;
		tileX = x;
		tileY = y;
		tileZ = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, playerOnChip);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		playerOnChip = ByteBufUtils.readUTF8String(buf);
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}

}
