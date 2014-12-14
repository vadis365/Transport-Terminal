package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import transportterminal.utils.PacketUtils;

public class ChipUtilsMessage implements IMessage {

	public int dimension, entityID, funcID;
	public String name;
	public BlockPos tilePos;

	public ChipUtilsMessage() {
	}

	public ChipUtilsMessage(EntityPlayer player, String string, BlockPos pos, int id) {
		dimension = player.dimension;
		entityID = player.getEntityId();
		name = string;
		tilePos = pos;
		funcID = id;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, name);
		PacketUtils.writeBlockPos(buf, tilePos);
		buf.writeInt(funcID);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
		tilePos = PacketUtils.readBlockPos(buf);
		funcID = buf.readInt();
	}
}