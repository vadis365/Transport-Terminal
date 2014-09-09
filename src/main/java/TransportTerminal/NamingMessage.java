package TransportTerminal;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class NamingMessage implements IMessage {

	public int dimension, tileX, tileY, tileZ;
	String name;

	public NamingMessage() {
	}

	public NamingMessage(EntityPlayer player, String string) {
		dimension = player.getCurrentEquippedItem().getTagCompound().getInteger("dim");
		name = string;
		tileX = player.getCurrentEquippedItem().getTagCompound().getInteger("homeX");
		tileY = player.getCurrentEquippedItem().getTagCompound().getInteger("homeY");
		tileZ = player.getCurrentEquippedItem().getTagCompound().getInteger("homeZ");
	}

	/** enconding */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimension);
		ByteBufUtils.writeUTF8String(buf, name);
		buf.writeInt(tileX);
		buf.writeInt(tileY);
		buf.writeInt(tileZ);
	}

	/** decoding */
	@Override
	public void fromBytes(ByteBuf buf) {
		dimension = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);;
		tileX = buf.readInt();
		tileY = buf.readInt();
		tileZ = buf.readInt();
	}

	@Override
	public String toString() {
		return "Message: " + dimension + " - " + tileX + ", " + tileY + ", " + tileZ + " - " + name;
	}
}