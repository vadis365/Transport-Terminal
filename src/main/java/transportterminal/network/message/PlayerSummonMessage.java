package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import transportterminal.utils.PacketUtils;

public class PlayerSummonMessage implements IMessage {

	public int dimension, entityID, buttonID;
	public BlockPos tilePos;

	public PlayerSummonMessage() {
	}

	public PlayerSummonMessage(EntityPlayer player, int button, BlockPos pos) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		buttonID = button;
		tilePos = pos;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		buf.writeInt(buttonID);
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		buttonID = buf.readInt();
		tilePos = PacketUtils.readBlockPos(buf);
	}

}
