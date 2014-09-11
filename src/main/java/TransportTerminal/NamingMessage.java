package TransportTerminal;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class NamingMessage implements IMessage {

	public int dimension, entityID, tileX, tileY, tileZ;
	String name;

	public NamingMessage() {}

	public NamingMessage(EntityPlayer player, String string) {
		dimension = player.getCurrentEquippedItem().getTagCompound().getInteger("dim");
		entityID = player.getEntityId();
		name = string;
		tileX = player.getCurrentEquippedItem().getTagCompound().getInteger("homeX");
		tileY = player.getCurrentEquippedItem().getTagCompound().getInteger("homeY");
		tileZ = player.getCurrentEquippedItem().getTagCompound().getInteger("homeZ");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		buf.writeInt(entityID);
		ByteBufUtils.writeUTF8String(buf, name);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		entityID = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}

}
