package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import transportterminal.utils.PacketUtils;

public class ButtonMessage implements IMessage {

	public int dimension, newDimension, entityID, buttonID;
	public BlockPos tilePos;

	public ButtonMessage() {
	}

	public ButtonMessage(EntityPlayer player, int button, BlockPos pos, int newDim) {
		dimension = player.dimension;
		newDimension = newDim;
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = pos;
	}

	public ButtonMessage(EntityPlayer player, int button, int x, int y, int z, int newDim) {
		dimension = player.dimension;
		newDimension = newDim;
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(newDimension);
		buf.writeInt(entityID);
		buf.writeInt(buttonID);
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		newDimension = buf.readInt();
		entityID = buf.readInt();
		buttonID = buf.readInt();
		tilePos = PacketUtils.readBlockPos(buf);
	}
}