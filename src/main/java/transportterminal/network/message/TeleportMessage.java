package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import transportterminal.utils.PacketUtils;

public class TeleportMessage implements IMessage {

	public int dimension, entityID, chipDim;
	public BlockPos pos;

	public TeleportMessage() {
	}

	public TeleportMessage(EntityPlayer player, int x, int y, int z, int newDim) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		chipDim = newDim;
		pos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(chipDim);
		PacketUtils.writeBlockPos(buf, pos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		chipDim = buf.readInt();
		pos = PacketUtils.readBlockPos(buf);
	}
}