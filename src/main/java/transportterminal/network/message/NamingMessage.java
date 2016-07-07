package transportterminal.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import transportterminal.utils.PacketUtils;

public class NamingMessage implements IMessage {

	public int dimension, entityID;
	public String name;
	public BlockPos tilePos;

	public NamingMessage() {
	}

	public NamingMessage(EntityPlayer player, String string) {
		dimension = player.getActiveItemStack().getTagCompound().getInteger("dim");
		entityID = player.getEntityId();
		name = string;
		int x = player.getActiveItemStack().getTagCompound().getInteger("homeX");
		int y = player.getActiveItemStack().getTagCompound().getInteger("homeY");
		int z = player.getActiveItemStack().getTagCompound().getInteger("homeZ");
		tilePos = new BlockPos(x, y, z);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, name);
		PacketUtils.writeBlockPos(buf, tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
		tilePos = PacketUtils.readBlockPos(buf);
	}
}